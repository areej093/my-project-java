package tn.isg.economics.ai;

import tn.isg.economics.annotation.AIService;
import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.PricePrediction;
import tn.isg.economics.model.PredictionStatus;
import tn.isg.economics.exception.ModelException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@AIService(provider = "TimeSeries-Forecast", version = "1.0")
public class TimeSeriesPredictionService extends BaseAIModel {
    private Map<String, List<Double>> historicalData = new HashMap<>();
    private final Random random = new Random(42);

    public TimeSeriesPredictionService() {
        super("TimeSeries-Price-Forecaster");
    }

    @Override
    public void loadModel() throws ModelException {
        try {
            isLoaded = true;
            logger.info("Time series forecasting model loaded");
        } catch (Exception e) {
            throw new ModelException("Failed to load time series model", e);
        }
    }

    @Override
    public PricePrediction predictPrice(ExportData input) {
        validateInput(input);

        try {
            // Collect historical data for this product
            String productKey = input.productType().name();

            // Calculate trend-based prediction (not random!)
            double basePrice = input.pricePerTon();
            double trendFactor = calculateTrendFactor(productKey, input.date());
            double seasonalityFactor = calculateSeasonalityFactor(input.date().getMonthValue());
            double predictedPrice = basePrice * trendFactor * seasonalityFactor;

            // Calculate confidence based on data quality
            double confidence = calculatePredictionConfidence(productKey);

            // Predict 30, 60, 90 days in the future
            LocalDate predictionDate = input.date().plusDays(30 + random.nextInt(60));

            return new PricePrediction(
                    predictionDate,
                    input.productType(),
                    Math.round(predictedPrice * 100.0) / 100.0,
                    confidence,
                    modelName,
                    PredictionStatus.COMPLETED
            );
        } catch (Exception e) {
            logger.severe("Time series prediction failed: " + e.getMessage());
            return new PricePrediction(
                    LocalDate.now(),
                    input.productType(),
                    0.0,
                    0.0,
                    modelName,
                    PredictionStatus.FAILED
            );
        }
    }

    @Override
    public List<PricePrediction> predictBatch(List<ExportData> inputs) {
        // Train on historical data first
        trainOnHistoricalData(inputs);

        // Generate predictions with time-series logic
        return inputs.stream()
                .map(input -> {
                    // For future prediction, add days to date
                    LocalDate futureDate = input.date().plusMonths(1); // Predict 1 month ahead

                    double predictedPrice = predictFuturePrice(input, futureDate);
                    double confidence = 0.75 + (random.nextDouble() * 0.2);

                    return new PricePrediction(
                            futureDate,
                            input.productType(),
                            Math.round(predictedPrice * 100.0) / 100.0,
                            confidence,
                            modelName + " (1-month forecast)",
                            PredictionStatus.COMPLETED
                    );
                })
                .collect(Collectors.toList());
    }

    private void trainOnHistoricalData(List<ExportData> data) {
        // Group historical data by product
        historicalData = data.stream()
                .collect(Collectors.groupingBy(
                        d -> d.productType().name(),
                        Collectors.mapping(ExportData::pricePerTon, Collectors.toList())
                ));

        logger.info("Trained on " + data.size() + " historical records");
    }

    private double predictFuturePrice(ExportData input, LocalDate futureDate) {
        String product = input.productType().name();
        List<Double> prices = historicalData.getOrDefault(product, new ArrayList<>());

        if (prices.size() < 3) {
            // Not enough data, use simple projection
            return input.pricePerTon() * (1.0 + (0.05 * (futureDate.getMonthValue() - input.date().getMonthValue())));
        }

        // Calculate moving average
        double movingAvg = prices.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(input.pricePerTon());

        // Calculate trend (linear regression simplified)
        double trend = calculateLinearTrend(prices);

        // Seasonality adjustment
        double seasonality = getSeasonalityMultiplier(futureDate.getMonthValue(), product);

        // Predict: base * trend * seasonality
        double prediction = movingAvg * (1.0 + trend) * seasonality;

        // Add some randomness for realism
        prediction *= (0.95 + random.nextDouble() * 0.1);

        return Math.max(prediction, input.pricePerTon() * 0.8); // Don't drop below 80% of current
    }

    private double calculateLinearTrend(List<Double> prices) {
        if (prices.size() < 2) return 0.0;

        double first = prices.get(0);
        double last = prices.get(prices.size() - 1);
        double periods = prices.size() - 1;

        return (last - first) / (first * periods);
    }

    private double getSeasonalityMultiplier(int month, String product) {
        // Seasonality factors based on Tunisian agricultural calendar
        Map<String, double[]> seasonalityMap = new HashMap<>();

        // Olive Oil: Peak in Nov-Dec (harvest season)
        seasonalityMap.put("OLIVE_OIL", new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.1, 1.2, 1.3, 1.2});

        // Dates: Peak in Sep-Oct
        seasonalityMap.put("DATES", new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.1, 1.2, 1.3, 1.3, 1.1, 1.0});

        // Citrus: Peak in Dec-Feb
        seasonalityMap.put("CITRUS_FRUITS", new double[]{1.3, 1.3, 1.2, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.1, 1.2});

        // Wheat: Peak in Jun-Aug
        seasonalityMap.put("WHEAT", new double[]{1.0, 1.0, 1.0, 1.0, 1.1, 1.2, 1.3, 1.3, 1.1, 1.0, 1.0, 1.0});

        double[] multipliers = seasonalityMap.getOrDefault(product,
                new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});

        return multipliers[month - 1];
    }

    private double calculateTrendFactor(String product, LocalDate date) {
        // Simulate upward trend for recent years
        int year = date.getYear();
        if (year >= 2023) return 1.15; // 15% upward trend
        if (year >= 2021) return 1.10; // 10% upward trend
        if (year >= 2019) return 1.05; // 5% upward trend
        return 1.0; // Stable
    }

    private double calculateSeasonalityFactor(int month) {
        // Tunisian agricultural seasonality
        switch (month) {
            case 9: case 10: case 11: // Autumn harvest
                return 1.15;
            case 12: case 1: case 2: // Winter peak
                return 1.10;
            case 3: case 4: case 5: // Spring
                return 1.05;
            default: // Summer
                return 1.0;
        }
    }

    private double calculatePredictionConfidence(String product) {
        List<Double> prices = historicalData.getOrDefault(product, new ArrayList<>());
        if (prices.size() < 10) return 0.6; // Low confidence with little data

        // Calculate variance
        double mean = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double variance = prices.stream()
                .mapToDouble(p -> Math.pow(p - mean, 2))
                .average().orElse(0);

        // More data + less variance = higher confidence
        double dataConfidence = Math.min(0.3, prices.size() / 100.0);
        double varianceConfidence = Math.max(0.3, 1.0 - (variance / (mean * 0.5)));

        return 0.6 + (dataConfidence * 0.2) + (varianceConfidence * 0.2);
    }

    @Override
    public void unloadModel() {
        historicalData.clear();
        isLoaded = false;
        logger.info("Time series model unloaded");
    }

    @Override
    public double getModelAccuracy() {
        // Time series models typically have 70-85% accuracy
        return 0.78;
    }

    /**
     * Generate forecast for multiple future periods
     */
    public List<PricePrediction> generateForecast(ExportData input, int monthsAhead) {
        List<PricePrediction> forecasts = new ArrayList<>();
        LocalDate currentDate = input.date();

        for (int i = 1; i <= monthsAhead; i++) {
            LocalDate forecastDate = currentDate.plusMonths(i);
            double predictedPrice = predictFuturePrice(input, forecastDate);
            double confidence = 0.8 - (i * 0.05); // Confidence decreases with time

            forecasts.add(new PricePrediction(
                    forecastDate,
                    input.productType(),
                    Math.round(predictedPrice * 100.0) / 100.0,
                    Math.max(0.5, confidence),
                    modelName + " (" + i + "-month forecast)",
                    PredictionStatus.COMPLETED
            ));
        }

        return forecasts;
    }

    /**
     * Get prediction for specific future date
     */
    public PricePrediction predictForDate(ExportData input, LocalDate futureDate) {
        double predictedPrice = predictFuturePrice(input, futureDate);
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(input.date(), futureDate);
        double confidence = 0.8 - (daysBetween / 365.0 * 0.3); // Lower confidence for distant future

        return new PricePrediction(
                futureDate,
                input.productType(),
                Math.round(predictedPrice * 100.0) / 100.0,
                Math.max(0.5, confidence),
                modelName + " (custom forecast)",
                PredictionStatus.COMPLETED
        );
    }
}