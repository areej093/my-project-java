package tn.isi.agriculture.dashboard;

import tn.isi.agriculture.model.ExportRecord;
import tn.isi.agriculture.model.ProductType;
import tn.isi.agriculture.service.PredictionService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DashboardService {

    private final PredictionService predictionService;
    private final List<ExportRecord> allRecords;

    public DashboardService(PredictionService predictionService, List<ExportRecord> allRecords) {
        this.predictionService = predictionService;
        this.allRecords = allRecords;
    }

    // Complex Functionality 1: Display comprehensive statistics
    public void displayStatistics() {
        System.out.println("\n📊 === DASHBOARD: COMPREHENSIVE STATISTICS ===");

        // 1. Basic counts
        System.out.println("\n1️⃣  DATA OVERVIEW:");
        System.out.println("   • Total records: " + allRecords.size());
        System.out.println("   • Years covered: " + getUniqueYears().size());
        System.out.println("   • Products tracked: " + getProductsWithData().size());

        // 2. Financial summary
        System.out.println("\n2️⃣  FINANCIAL SUMMARY:");
        double totalValue = allRecords.stream().mapToDouble(ExportRecord::valueUSD).sum();
        double avgPrice = allRecords.stream().mapToDouble(ExportRecord::unitPriceUSDPerKg).average().orElse(0);
        System.out.printf("   • Total export value: $%,.0f USD\n", totalValue);
        System.out.printf("   • Average price: $%.2f/kg\n", avgPrice);

        // 3. Product performance
        System.out.println("\n3️⃣  PRODUCT PERFORMANCE RANKING:");
        Map<ProductType, Double> productValues = new HashMap<>();
        for (ExportRecord record : allRecords) {
            productValues.put(record.productType(),
                    productValues.getOrDefault(record.productType(), 0.0) + record.valueUSD());
        }

        productValues.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .forEach(entry -> {
                    System.out.printf("   • %-15s: $%,12.0f USD\n",
                            entry.getKey().getEnglishName(), entry.getValue());
                });
    }

    // Complex Functionality 2: Price trend analysis
    public void analyzePriceTrends() {
        System.out.println("\n📈 === DASHBOARD: PRICE TREND ANALYSIS ===");

        for (ProductType product : getProductsWithData()) {
            List<ExportRecord> productData = getProductData(product);
            if (productData.size() >= 2) {
                // Create mutable copy for sorting
                List<ExportRecord> sortedData = new ArrayList<>(productData);
                sortedData.sort(Comparator.comparing(ExportRecord::year));

                double firstPrice = sortedData.get(0).unitPriceUSDPerKg();
                double lastPrice = sortedData.get(sortedData.size() - 1).unitPriceUSDPerKg();
                double growth = ((lastPrice - firstPrice) / firstPrice) * 100;

                System.out.printf("\n🔍 %s:\n", product.getEnglishName());
                System.out.printf("   • Price range: $%.2f to $%.2f/kg\n",
                        getMinPrice(product), getMaxPrice(product));
                System.out.printf("   • Total growth: %.1f%% over %d years\n",
                        growth, sortedData.size());
                System.out.printf("   • Avg annual growth: %.1f%%\n",
                        growth / sortedData.size());

                // Volatility analysis
                double volatility = calculateVolatility(sortedData);
                System.out.printf("   • Price volatility: %.2f (%.1f%%)\n",
                        volatility, volatility * 100);
            }
        }
    }

    // Complex Functionality 3: Predictive analytics
    public void showPredictiveAnalytics() {
        System.out.println("\n🔮 === DASHBOARD: PREDICTIVE ANALYTICS ===");

        int nextYear = 2025;
        System.out.println("\n🎯 PREDICTIONS FOR " + nextYear + ":");
        System.out.println("┌─────────────────┬────────────┬──────────────┬────────────────┐");
        System.out.println("│ Product         │ Price      │ Change       │ Recommendation │");
        System.out.println("├─────────────────┼────────────┼──────────────┼────────────────┤");

        for (ProductType product : getProductsWithData()) {
            double predictedPrice = predictionService.predictPrice(product, nextYear);
            List<ExportRecord> productData = getProductData(product);
            if (!productData.isEmpty()) {
                // Get most recent price
                List<ExportRecord> sortedData = new ArrayList<>(productData);
                sortedData.sort(Comparator.comparing(ExportRecord::year).reversed());
                double currentPrice = sortedData.get(0).unitPriceUSDPerKg();
                double changePercent = ((predictedPrice - currentPrice) / currentPrice) * 100;

                String recommendation = getRecommendation(changePercent);

                System.out.printf("│ %-15s │ $%7.2f/kg │ %+6.1f%%      │ %-14s │\n",
                        product.getEnglishName(), predictedPrice, changePercent, recommendation);
            }
        }

        System.out.println("└─────────────────┴────────────┴──────────────┴────────────────┘");
    }

    // Complex Functionality 4: Export optimization
    public void showExportOptimization() {
        System.out.println("\n⚡ === DASHBOARD: EXPORT OPTIMIZATION ===");

        System.out.println("\n📅 BEST TIME TO EXPORT (Based on historical patterns):");
        for (ProductType product : getProductsWithData()) {
            List<ExportRecord> productData = getProductData(product);

            // Simple seasonality: assume prices are higher in later years
            double avgEarly = productData.stream()
                    .filter(r -> r.year() <= 2010)
                    .mapToDouble(ExportRecord::unitPriceUSDPerKg)
                    .average().orElse(0);

            double avgLate = productData.stream()
                    .filter(r -> r.year() > 2010)
                    .mapToDouble(ExportRecord::unitPriceUSDPerKg)
                    .average().orElse(0);

            String bestPeriod = avgLate > avgEarly ? "Recent years" : "Earlier years";

            System.out.printf("\n📦 %s:\n", product.getEnglishName());
            System.out.printf("   • Best period: %s\n", bestPeriod);
            if (avgEarly > 0) {
                System.out.printf("   • Price difference: %.1f%%\n",
                        Math.abs(avgLate - avgEarly) / avgEarly * 100);
            }
            System.out.printf("   • Current trend: %s\n",
                    avgLate > avgEarly ? "↗️  Increasing" : "↘️  Decreasing");

            // Export strategy
            double volatility = calculateVolatility(productData);
            String strategy = volatility > 0.15 ? "Hedge with contracts" : "Direct export";
            System.out.printf("   • Recommended strategy: %s\n", strategy);
        }
    }

    // Helper methods
    private List<Integer> getUniqueYears() {
        return allRecords.stream()
                .map(ExportRecord::year)
                .distinct()
                .sorted()
                .toList();
    }

    private List<ProductType> getProductsWithData() {
        return allRecords.stream()
                .map(ExportRecord::productType)
                .distinct()
                .toList();
    }

    private List<ExportRecord> getProductData(ProductType product) {
        return allRecords.stream()
                .filter(r -> r.productType() == product)
                .sorted(Comparator.comparing(ExportRecord::year))
                .toList();
    }

    private double getMinPrice(ProductType product) {
        return getProductData(product).stream()
                .mapToDouble(ExportRecord::unitPriceUSDPerKg)
                .min()
                .orElse(0);
    }

    private double getMaxPrice(ProductType product) {
        return getProductData(product).stream()
                .mapToDouble(ExportRecord::unitPriceUSDPerKg)
                .max()
                .orElse(0);
    }

    private double calculateVolatility(List<ExportRecord> data) {
        if (data.size() < 2) return 0;

        double[] prices = data.stream()
                .mapToDouble(ExportRecord::unitPriceUSDPerKg)
                .toArray();

        double mean = 0;
        for (double price : prices) {
            mean += price;
        }
        mean /= prices.length;

        double variance = 0;
        for (double price : prices) {
            variance += Math.pow(price - mean, 2);
        }
        variance /= prices.length;

        double stdDev = Math.sqrt(variance);
        return mean > 0 ? stdDev / mean : 0; // Coefficient of variation
    }

    private String getRecommendation(double changePercent) {
        if (changePercent > 15) return "🟢 BUY";
        else if (changePercent > 5) return "🟡 HOLD";
        else if (changePercent > -5) return "⚪ WAIT";
        else if (changePercent > -15) return "🟠 REDUCE";
        else return "🔴 SELL";
    }

    // SIMPLIFIED Quick Summary without sorting issues
    public void showQuickSummary() {
        System.out.println("\n⚡ QUICK SUMMARY:");
        System.out.println("----------------");

        System.out.printf("💰 Highest value product: %s\n", getHighestValueProduct());
        System.out.printf("🎯 Model accuracy: %.1f%%\n", predictionService.getModelAccuracy() * 100);
        System.out.printf("📊 Records analyzed: %d\n", allRecords.size());
        System.out.printf("📅 Years covered: %d to %d\n",
                getUniqueYears().stream().min(Integer::compare).orElse(0),
                getUniqueYears().stream().max(Integer::compare).orElse(0));
    }

    private String getHighestValueProduct() {
        Map<ProductType, Double> productValues = new HashMap<>();
        for (ExportRecord record : allRecords) {
            productValues.put(record.productType(),
                    productValues.getOrDefault(record.productType(), 0.0) + record.valueUSD());
        }

        return productValues.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> String.format("%s ($%,.0f)",
                        entry.getKey().getEnglishName(), entry.getValue()))
                .orElse("N/A");
    }
}