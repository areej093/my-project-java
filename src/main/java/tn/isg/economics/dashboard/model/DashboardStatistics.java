package tn.isg.economics.dashboard.model;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

public record DashboardStatistics(
        double averagePrice,
        double minPrice,
        double maxPrice,
        double standardDeviation,
        int totalPredictions,
        double averageConfidence,
        Map<String, Double> productDistribution,
        Map<String, Integer> countryDistribution,
        Map<String, Long> confidenceDistribution,
        Map<String, Double> monthlyTrends,
        Map<String, Double> quarterlyTrends,
        double modelAccuracy,
        int highConfidenceCount,
        int mediumConfidenceCount,
        int lowConfidenceCount,
        double totalExportValue,
        double averageVolume,
        Map<String, Double> exportByDestination
) {

    public DashboardStatistics {
        productDistribution = productDistribution != null ?
                Collections.unmodifiableMap(productDistribution) :
                Collections.emptyMap();
        countryDistribution = countryDistribution != null ?
                Collections.unmodifiableMap(countryDistribution) :
                Collections.emptyMap();
        confidenceDistribution = confidenceDistribution != null ?
                Collections.unmodifiableMap(confidenceDistribution) :
                Collections.emptyMap();
        monthlyTrends = monthlyTrends != null ?
                Collections.unmodifiableMap(monthlyTrends) :
                Collections.emptyMap();
        quarterlyTrends = quarterlyTrends != null ?
                Collections.unmodifiableMap(quarterlyTrends) :
                Collections.emptyMap();
        exportByDestination = exportByDestination != null ?
                Collections.unmodifiableMap(exportByDestination) :
                Collections.emptyMap();
    }

    public static DashboardStatistics empty() {
        return new DashboardStatistics(
                0, 0, 0, 0, 0, 0,
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                0, 0, 0, 0,
                0, 0,
                Collections.emptyMap()
        );
    }

    public static class Builder {
        private double averagePrice;
        private double minPrice;
        private double maxPrice;
        private double standardDeviation;
        private int totalPredictions;
        private double averageConfidence;
        private Map<String, Double> productDistribution = new HashMap<>();
        private Map<String, Integer> countryDistribution = new HashMap<>();
        private Map<String, Long> confidenceDistribution = new HashMap<>();
        private Map<String, Double> monthlyTrends = new HashMap<>();
        private Map<String, Double> quarterlyTrends = new HashMap<>();
        private double modelAccuracy;
        private int highConfidenceCount;
        private int mediumConfidenceCount;
        private int lowConfidenceCount;
        private double totalExportValue;
        private double averageVolume;
        private Map<String, Double> exportByDestination = new HashMap<>();

        public Builder averagePrice(double averagePrice) {
            this.averagePrice = averagePrice;
            return this;
        }

        public Builder minPrice(double minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public Builder maxPrice(double maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public Builder standardDeviation(double standardDeviation) {
            this.standardDeviation = standardDeviation;
            return this;
        }

        public Builder totalPredictions(int totalPredictions) {
            this.totalPredictions = totalPredictions;
            return this;
        }

        public Builder averageConfidence(double averageConfidence) {
            this.averageConfidence = averageConfidence;
            return this;
        }

        public Builder productDistribution(Map<String, Double> productDistribution) {
            this.productDistribution = productDistribution;
            return this;
        }

        public Builder countryDistribution(Map<String, Integer> countryDistribution) {
            this.countryDistribution = countryDistribution;
            return this;
        }

        public Builder confidenceDistribution(Map<String, Long> confidenceDistribution) {
            this.confidenceDistribution = confidenceDistribution;
            return this;
        }

        public Builder monthlyTrends(Map<String, Double> monthlyTrends) {
            this.monthlyTrends = monthlyTrends;
            return this;
        }

        public Builder quarterlyTrends(Map<String, Double> quarterlyTrends) {
            this.quarterlyTrends = quarterlyTrends;
            return this;
        }

        public Builder modelAccuracy(double modelAccuracy) {
            this.modelAccuracy = modelAccuracy;
            return this;
        }

        public Builder highConfidenceCount(int highConfidenceCount) {
            this.highConfidenceCount = highConfidenceCount;
            return this;
        }

        public Builder mediumConfidenceCount(int mediumConfidenceCount) {
            this.mediumConfidenceCount = mediumConfidenceCount;
            return this;
        }

        public Builder lowConfidenceCount(int lowConfidenceCount) {
            this.lowConfidenceCount = lowConfidenceCount;
            return this;
        }

        public Builder totalExportValue(double totalExportValue) {
            this.totalExportValue = totalExportValue;
            return this;
        }

        public Builder averageVolume(double averageVolume) {
            this.averageVolume = averageVolume;
            return this;
        }

        public Builder exportByDestination(Map<String, Double> exportByDestination) {
            this.exportByDestination = exportByDestination;
            return this;
        }

        public DashboardStatistics build() {
            return new DashboardStatistics(
                    averagePrice, minPrice, maxPrice, standardDeviation,
                    totalPredictions, averageConfidence,
                    productDistribution, countryDistribution, confidenceDistribution,
                    monthlyTrends, quarterlyTrends,
                    modelAccuracy, highConfidenceCount, mediumConfidenceCount, lowConfidenceCount,
                    totalExportValue, averageVolume, exportByDestination
            );
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toFormattedString() {
        return String.format(
                "Dashboard Statistics:\n" +
                "====================\n" +
                "Total Predictions: %,d\n" +
                "Average Price: $%,.2f/ton\n" +
                "Price Range: $%,.2f - $%,.2f\n" +
                "Standard Deviation: $%,.2f\n" +
                "Average Confidence: %.1f%%\n" +
                "High Confidence Predictions: %,d (%.1f%%)\n" +
                "Model Accuracy: %.1f%%\n" +
                "Total Export Value: $%,.2f\n" +
                "Average Volume: %.2f tons",
                totalPredictions(),
                averagePrice(),
                minPrice(),
                maxPrice(),
                standardDeviation(),
                averageConfidence() * 100,
                highConfidenceCount(),
                (totalPredictions() > 0 ? (highConfidenceCount() * 100.0 / totalPredictions()) : 0),
                modelAccuracy() * 100,
                totalExportValue(),
                averageVolume()
        );
    }
}
