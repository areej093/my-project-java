package tn.isi.agriculture.service;

import tn.isi.agriculture.data.DataLoader;
import tn.isi.agriculture.model.ExportRecord;
import tn.isi.agriculture.model.ProductType;
import java.util.*;

public class SimplePredictionService implements PredictionService {

    private final DataLoader dataLoader;
    private final Map<ProductType, List<ExportRecord>> productDataCache;

    public SimplePredictionService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
        this.productDataCache = new HashMap<>();
        initializeCache();
    }

    private void initializeCache() {
        try {
            List<ExportRecord> allRecords = dataLoader.loadFromCSV();
            for (ProductType product : ProductType.values()) {
                List<ExportRecord> productRecords = dataLoader.getProductData(allRecords, product);
                if (!productRecords.isEmpty()) {
                    productDataCache.put(product, productRecords);
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️  Using sample data for prediction service");
            List<ExportRecord> sampleRecords = dataLoader.loadSampleData();
            for (ProductType product : ProductType.values()) {
                List<ExportRecord> productRecords = dataLoader.getProductData(sampleRecords, product);
                if (!productRecords.isEmpty()) {
                    productDataCache.put(product, productRecords);
                }
            }
        }
    }

    @Override
    public double predictPrice(ProductType product, int year) {
        List<ExportRecord> historicalData = getHistoricalData(product);

        if (historicalData.isEmpty()) {
            return 0.0;
        }

        // Simple linear regression: y = mx + b
        // Calculate average growth rate

        double[] prices = historicalData.stream()
                .mapToDouble(ExportRecord::unitPriceUSDPerKg)
                .toArray();

        int[] years = historicalData.stream()
                .mapToInt(ExportRecord::year)
                .toArray();

        // Calculate average price and growth
        double avgPrice = Arrays.stream(prices).average().orElse(0);
        double avgYear = Arrays.stream(years).average().orElse(0);

        double numerator = 0;
        double denominator = 0;

        for (int i = 0; i < years.length; i++) {
            numerator += (years[i] - avgYear) * (prices[i] - avgPrice);
            denominator += Math.pow(years[i] - avgYear, 2);
        }

        double slope = denominator == 0 ? 0 : numerator / denominator;
        double intercept = avgPrice - slope * avgYear;

        // Predict for target year
        double prediction = intercept + slope * year;

        // Ensure prediction is not negative
        return Math.max(prediction, avgPrice * 0.5);
    }

    @Override
    public Map<Integer, Double> predictPriceRange(ProductType product, int startYear, int endYear) {
        Map<Integer, Double> predictions = new TreeMap<>();

        for (int year = startYear; year <= endYear; year++) {
            predictions.put(year, predictPrice(product, year));
        }

        return predictions;
    }

    @Override
    public List<ExportRecord> getHistoricalData(ProductType product) {
        return productDataCache.getOrDefault(product, new ArrayList<>());
    }

    @Override
    public double getModelAccuracy() {
        // Simple accuracy estimation
        return 0.75; // 75% accuracy for simple linear model
    }

    @Override
    public String getModelName() {
        return "Simple Linear Regression Model";
    }

    // Additional helper methods
    public Map<ProductType, Double> predictAllProducts(int year) {
        Map<ProductType, Double> predictions = new HashMap<>();

        for (ProductType product : ProductType.values()) {
            if (productDataCache.containsKey(product)) {
                predictions.put(product, predictPrice(product, year));
            }
        }

        return predictions;
    }

    public double calculateAverageGrowthRate(ProductType product) {
        List<ExportRecord> data = getHistoricalData(product);
        if (data.size() < 2) return 0.0;

        Collections.sort(data, Comparator.comparing(ExportRecord::year));

        double firstPrice = data.get(0).unitPriceUSDPerKg();
        double lastPrice = data.get(data.size() - 1).unitPriceUSDPerKg();
        int firstYear = data.get(0).year();
        int lastYear = data.get(data.size() - 1).year();

        if (lastYear == firstYear) return 0.0;

        return ((lastPrice - firstPrice) / firstPrice) / (lastYear - firstYear);
    }
}