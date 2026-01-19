package tn.isg.economics.dashboard.service;

import lombok.extern.slf4j.Slf4j;
import tn.isg.economics.dashboard.model.DashboardStatistics;
import tn.isg.economics.model.PricePrediction;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementation of StatisticsService interface.
 * Uses Java Stream API extensively for data processing.
 * Demonstrates comprehensive use of Java Collections Framework.
 */
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Override
    public DashboardStatistics calculateStatistics(List<PricePrediction> predictions) {
        log.debug("Calculating statistics for {} predictions",
                predictions != null ? predictions.size() : 0);

        if (predictions == null || predictions.isEmpty()) {
            return DashboardStatistics.empty();
        }

        // Calculate basic statistics using Stream API
        DoubleSummaryStatistics priceStats = predictions.stream()
                .mapToDouble(PricePrediction::predictedPrice)
                .summaryStatistics();

        DoubleSummaryStatistics confidenceStats = predictions.stream()
                .mapToDouble(PricePrediction::confidence)
                .summaryStatistics();

        // Calculate distributions
        Map<String, Double> productDistribution = calculateProductAverages(predictions);
        Map<String, Long> confidenceDistribution = getConfidenceDistribution(predictions);
        Map<String, Double> monthlyTrends = getMonthlyTrends(predictions);
        Map<String, Double> quarterlyTrends = getQuarterlyTrends(predictions);
        Map<String, Integer> countryDistribution = getCountryDistribution(predictions);

        // Calculate performance metrics
        double stdDev = calculateStandardDeviation(predictions);
        double totalExportValue = calculateTotalExportValue(predictions);
        double modelAccuracy = calculateModelAccuracy(predictions);

        // Count confidence levels
        long highConfidence = confidenceDistribution.getOrDefault("High", 0L);
        long mediumConfidence = confidenceDistribution.getOrDefault("Medium", 0L);
        long lowConfidence = confidenceDistribution.getOrDefault("Low", 0L);

        // Calculate average volume (if available in data)
        double averageVolume = 100.0; // Default value, would come from ExportData

        return DashboardStatistics.builder()
                .averagePrice(priceStats.getAverage())
                .minPrice(priceStats.getMin())
                .maxPrice(priceStats.getMax())
                .standardDeviation(stdDev)
                .totalPredictions(predictions.size())
                .averageConfidence(confidenceStats.getAverage())
                .productDistribution(productDistribution)
                .countryDistribution(countryDistribution)
                .confidenceDistribution(confidenceDistribution)
                .monthlyTrends(monthlyTrends)
                .quarterlyTrends(quarterlyTrends)
                .modelAccuracy(modelAccuracy)
                .highConfidenceCount((int) highConfidence)
                .mediumConfidenceCount((int) mediumConfidence)
                .lowConfidenceCount((int) lowConfidence)
                .totalExportValue(totalExportValue)
                .averageVolume(averageVolume)
                .exportByDestination(new HashMap<>()) // Would be calculated from ExportData
                .build();
    }

    @Override
    public DashboardStatistics calculateFilteredStatistics(
            List<PricePrediction> predictions,
            Predicate<PricePrediction> filter) {

        List<PricePrediction> filtered = predictions.stream()
                .filter(filter)
                .collect(Collectors.toList());

        log.debug("Filter applied: {} → {} predictions",
                predictions.size(), filtered.size());

        return calculateStatistics(filtered);
    }

    @Override
    public Map<String, Double> getTimeSeriesData(
            List<PricePrediction> predictions,
            String groupingKey) {

        if ("MONTH".equalsIgnoreCase(groupingKey)) {
            return getMonthlyTrends(predictions);
        } else if ("QUARTER".equalsIgnoreCase(groupingKey)) {
            return getQuarterlyTrends(predictions);
        } else if ("YEAR".equalsIgnoreCase(groupingKey)) {
            return predictions.stream()
                    .collect(Collectors.groupingBy(
                            p -> String.valueOf(p.predictionDate().getYear()),
                            Collectors.averagingDouble(PricePrediction::predictedPrice)
                    ));
        }

        throw new IllegalArgumentException("Invalid grouping key: " + groupingKey);
    }

    @Override
    public Map<String, Double> calculateProductAverages(List<PricePrediction> predictions) {
        return predictions.stream()
                .collect(Collectors.groupingBy(
                        p -> p.productType().name(),
                        Collectors.averagingDouble(PricePrediction::predictedPrice)
                ));
    }

    @Override
    public Map<String, Long> getConfidenceDistribution(List<PricePrediction> predictions) {
        return predictions.stream()
                .collect(Collectors.groupingBy(
                        p -> {
                            double conf = p.confidence();
                            if (conf >= 0.8) return "High";
                            else if (conf >= 0.6) return "Medium";
                            else return "Low";
                        },
                        Collectors.counting()
                ));
    }

    @Override
    public double calculateStandardDeviation(List<PricePrediction> predictions) {
        if (predictions.size() <= 1) return 0;

        double mean = predictions.stream()
                .mapToDouble(PricePrediction::predictedPrice)
                .average()
                .orElse(0);

        double variance = predictions.stream()
                .mapToDouble(p -> Math.pow(p.predictedPrice() - mean, 2))
                .sum() / (predictions.size() - 1);

        return Math.sqrt(variance);
    }

    @Override
    public double calculateTotalExportValue(List<PricePrediction> predictions) {
        // In real implementation, this would use volume from ExportData
        // For now, estimate based on average volume
        double averageVolume = 100.0; // Default average volume in tons
        return predictions.stream()
                .mapToDouble(p -> p.predictedPrice() * averageVolume)
                .sum();
    }

    @Override
    public Map<String, Integer> getCountryDistribution(List<PricePrediction> predictions) {
        // This would require country data from ExportData
        // For demo, return empty map
        return new HashMap<>();
    }

    @Override
    public Map<String, Double> getMonthlyTrends(List<PricePrediction> predictions) {
        return predictions.stream()
                .collect(Collectors.groupingBy(
                        p -> {
                            String month = p.predictionDate().getMonth().toString();
                            int year = p.predictionDate().getYear();
                            return month.substring(0, 3) + " " + year;
                        },
                        Collectors.averagingDouble(PricePrediction::predictedPrice)
                ));
    }

    @Override
    public Map<String, Double> getQuarterlyTrends(List<PricePrediction> predictions) {
        return predictions.stream()
                .collect(Collectors.groupingBy(
                        p -> {
                            int month = p.predictionDate().getMonthValue();
                            int quarter = (month - 1) / 3 + 1;
                            int year = p.predictionDate().getYear();
                            return "Q" + quarter + " " + year;
                        },
                        Collectors.averagingDouble(PricePrediction::predictedPrice)
                ));
    }

    @Override
    public double calculateModelAccuracy(List<PricePrediction> predictions) {
        // In real implementation, this would compare predictions with actual values
        // For demo, return average confidence as proxy for accuracy
        return predictions.stream()
                .mapToDouble(PricePrediction::confidence)
                .average()
                .orElse(0.75);
    }
}