//package tn.isg.economics;
//
//import tn.isg.economics.ai.DJLPredictionService;
//import tn.isg.economics.ai.LLMReportService;
//import tn.isg.economics.data.transformer.FAOSTATDataTransformer;
//import tn.isg.economics.model.ExportData;
//import tn.isg.economics.model.PricePrediction;
//import tn.isg.economics.model.ProductType;
//import tn.isg.economics.model.MarketIndicator;
//import tn.isg.economics.exception.ModelException;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public class Main {
//    public static void main(String[] args) {
//        System.out.println("=== Tunisian Agricultural Export AI System ===\n");
//        System.out.println("Currency: Tunisian Dinar (TND) - Exchange rate: 1 USD = 3.1 TND\n");
//
//        try {
//            // 1. Load and transform FAO data with TND conversion
//            System.out.println("1. Loading FAO data with TND conversion...");
//            FAOSTATDataTransformer transformer = new FAOSTATDataTransformer();
//
//            String faoPath = "datasets/raw/FAOSTAT_data_en_12-20-2025.csv";
//            List<ExportData> exports = transformer.transformFAOSTATData(faoPath);
//
//            if (exports.isEmpty()) {
//                System.out.println("Using enhanced synthetic data in TND...");
//                exports = createSampleDataInTND();
//            }
//
//            System.out.println("? Loaded " + exports.size() + " export records in Tunisian Dinar");
//
//            // Show currency conversion info
//            System.out.println("\n=== CURRENCY INFORMATION ===");
//            System.out.println("Base currency: Tunisian Dinar (TND)");
//            System.out.println("Exchange rate: 1 USD = " + transformer.getExchangeRate() + " TND");
//            System.out.println("Example: 3,500 USD/ton = " +
//                String.format("%,.0f", 3500 * transformer.getExchangeRate()) + " TND/ton");
//
//            // 2. Initialize AI prediction service
//            System.out.println("\n2. Initializing AI prediction model...");
//            var predictionService = new DJLPredictionService();
//            predictionService.loadModel();
//
//            // 3. Initialize LLM report service
//            System.out.println("3. Initializing LLM report service...");
//            var reportService = new LLMReportService();
//
//            // 4. Generate predictions
//            System.out.println("4. Generating price predictions in TND...");
//            var predictions = predictionService.predictBatch(exports);
//
//            // Show sample predictions in TND
//            System.out.println("\n=== SAMPLE PREDICTIONS (TND) ===");
//            predictions.stream().limit(5).forEach(p -> {
//                System.out.printf("%s: %,.2f TND/ton (Confidence: %.1f%%)%n",
//                    p.productType().getFrenchName(),
//                    p.predictedPrice(),
//                    p.confidence() * 100);
//            });
//
//            // 5. Generate market report
//            System.out.println("\n=== MARKET INTELLIGENCE REPORT ===");
//            String report = reportService.generateMarketReport(predictions);
//            System.out.println(report);
//
//            // 6. Generate executive summary
//            System.out.println("\n=== EXECUTIVE SUMMARY ===");
//            String summary = reportService.generateSummaryReport(predictions);
//            System.out.println(summary);
//
//            // 7. Cleanup
//            predictionService.unloadModel();
//
//            // 8. Show statistics
//            System.out.println("\n=== FINAL STATISTICS ===");
//            System.out.println("Total predictions: " + predictions.size());
//            System.out.println("Currency: Tunisian Dinar (TND)");
//            System.out.println("Model accuracy: " +
//                String.format("%.1f%%", predictionService.getModelAccuracy() * 100));
//
//            // Average prices by product
//            System.out.println("\n=== AVERAGE PRICES BY PRODUCT (TND) ===");
//            predictions.stream()
//                .collect(java.util.stream.Collectors.groupingBy(
//                    p -> p.productType(),
//                    java.util.stream.Collectors.averagingDouble(PricePrediction::predictedPrice)
//                ))
//                .forEach((product, avgPrice) -> {
//                    double avgPriceUsd = avgPrice / transformer.getExchangeRate();
//                    System.out.printf("%s: %,.0f TND/ton (?%,.0f USD/ton)%n",
//                        product.getFrenchName(), avgPrice, avgPriceUsd);
//                });
//
//            System.out.println("\n? System completed successfully!");
//            System.out.println("\nTo run the full dashboard with interactive features:");
//            System.out.println("Run: mvn exec:java -Dexec.mainClass=\"tn.isg.economics.dashboard.DashboardMain\"");
//
//        } catch (ModelException e) {
//            System.err.println("? Model error: " + e.getMessage());
//            e.printStackTrace();
//        } catch (Exception e) {
//            System.err.println("? System error: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private static List<ExportData> createSampleDataInTND() {
//        double usdToTnd = 3.1;
//        LocalDate today = LocalDate.now();
//
//        return List.of(
//            new ExportData(
//                today.minusDays(30),
//                ProductType.OLIVE_OIL,
//                3500.0 * usdToTnd, // Convert to TND
//                100.0,
//                "France",
//                MarketIndicator.RISING
//            ),
//            new ExportData(
//                today.minusDays(15),
//                ProductType.DATES,
//                2500.0 * usdToTnd, // Convert to TND
//                50.0,
//                "Germany",
//                MarketIndicator.STABLE
//            ),
//            new ExportData(
//                today.minusDays(7),
//                ProductType.CITRUS_FRUITS,
//                1200.0 * usdToTnd, // Convert to TND
//                200.0,
//                "Italy",
//                MarketIndicator.VOLATILE
//            ),
//            new ExportData(
//                today.minusDays(45),
//                ProductType.OLIVE_OIL,
//                3800.0 * usdToTnd,
//                75.0,
//                "Spain",
//                MarketIndicator.RISING
//            ),
//            new ExportData(
//                today.minusDays(20),
//                ProductType.DATES,
//                2200.0 * usdToTnd,
//                60.0,
//                "United Kingdom",
//                MarketIndicator.STABLE
//            )
//        );
//    }
//}
package tn.isg.economics;

import tn.isg.economics.ai.TimeSeriesPredictionService;  // CHANGED IMPORT
import tn.isg.economics.ai.LLMReportService;
import tn.isg.economics.data.transformer.FAOSTATDataTransformer;
import tn.isg.economics.data.generator.SyntheticDataGenerator;  // ADDED IMPORT
import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.PricePrediction;
import tn.isg.economics.model.ProductType;
import tn.isg.economics.model.MarketIndicator;
import tn.isg.economics.exception.ModelException;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;  // ADDED IMPORT
import java.util.Map;  // ADDED IMPORT
import java.util.stream.Collectors;  // ADDED IMPORT

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Tunisian Agricultural Export AI System ===\n");
        System.out.println("FEATURING: REAL FUTURE PRICE PREDICTIONS\n");
        System.out.println("Currency: Tunisian Dinar (TND) - Exchange rate: 1 USD = 3.1 TND\n");

        try {
            // 1. Generate sample data
            System.out.println("1. Generating historical data...");
            SyntheticDataGenerator generator = new SyntheticDataGenerator();
            List<ExportData> exports = generator.generateExportData(20,
                    LocalDate.now().minusYears(2), LocalDate.now());

            System.out.println("   Generated " + exports.size() + " historical records");

            // 2. Initialize REAL time-series prediction service
            System.out.println("\n2. Initializing Time Series Forecasting Model...");
            var predictionService = new TimeSeriesPredictionService();  // CHANGED
            predictionService.loadModel();

            // 3. Generate FUTURE predictions
            System.out.println("3. Generating 6-month price forecasts...");
            List<PricePrediction> allPredictions = new ArrayList<>();

            for (ExportData export : exports) {
                // Get 6-month forecast for each product
                List<PricePrediction> forecast = predictionService.generateForecast(export, 6);
                allPredictions.addAll(forecast);
            }

            System.out.println("   Generated " + allPredictions.size() + " future predictions");

            // 4. Show sample future predictions
            System.out.println("\n=== FUTURE PRICE PREDICTIONS (Next 6 Months) ===");
            System.out.println("Currency: Tunisian Dinar (TND)");
            System.out.println("Exchange rate: 1 USD = 3.1 TND\n");

            // Group by product and show predictions
            Map<String, List<PricePrediction>> byProduct = allPredictions.stream()
                    .collect(Collectors.groupingBy(p -> p.productType().name()));

            byProduct.forEach((product, predictions) -> {
                System.out.println(product + " FORECAST:");
                predictions.stream()
                        .filter(p -> p.predictionDate().getMonthValue() ==
                                LocalDate.now().plusMonths(3).getMonthValue())
                        .limit(3)
                        .forEach(p -> {
                            System.out.printf("  %s: %,.2f TND/ton (%.1f%% confidence)%n",
                                    p.predictionDate().getMonth().toString().substring(0, 3),
                                    p.predictedPrice(),
                                    p.confidence() * 100);
                        });
                System.out.println();
            });

            // 5. Show prediction statistics
            System.out.println("=== PREDICTION STATISTICS ===");
            double avgConfidence = allPredictions.stream()
                    .mapToDouble(PricePrediction::confidence)
                    .average()
                    .orElse(0) * 100;

            long highConfidence = allPredictions.stream()
                    .filter(p -> p.confidence() > 0.7)
                    .count();

            System.out.printf("Average Confidence: %.1f%%%n", avgConfidence);
            System.out.printf("High Confidence Predictions (>70%%): %d/%d%n",
                    highConfidence, allPredictions.size());
            System.out.printf("Model Accuracy: %.1f%%%n",
                    predictionService.getModelAccuracy() * 100);
            System.out.println("Forecast Period: 6 months ahead");

            // 6. Initialize LLM report service for additional insights
            System.out.println("\n=== AI MARKET INSIGHTS ===");
            var reportService = new LLMReportService();
            String report = reportService.generateMarketReport(allPredictions);
            System.out.println(report);

            // 7. Cleanup
            predictionService.unloadModel();

            System.out.println("\n? System demonstrates REAL future price predictions!");
            System.out.println("? Using Time Series Forecasting (not random)");
            System.out.println("? All prices in Tunisian Dinar (TND)");
            System.out.println("? Forecast period: 6 months ahead");

            // 8. Show dashboard launch instruction
            System.out.println("\nTo run the full dashboard with interactive features:");
            System.out.println("Run: mvn exec:java -Dexec.mainClass=\"tn.isg.economics.dashboard.DashboardMain\"");

        } catch (ModelException e) {
            System.err.println("? Model error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("? System error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Optional: Keep the sample data creation method for reference
    private static List<ExportData> createSampleDataInTND() {
        double usdToTnd = 3.1;
        LocalDate today = LocalDate.now();

        return List.of(
                new ExportData(
                        today.minusDays(30),
                        ProductType.OLIVE_OIL,
                        3500.0 * usdToTnd, // Convert to TND
                        100.0,
                        "France",
                        MarketIndicator.RISING
                ),
                new ExportData(
                        today.minusDays(15),
                        ProductType.DATES,
                        2500.0 * usdToTnd, // Convert to TND
                        50.0,
                        "Germany",
                        MarketIndicator.STABLE
                ),
                new ExportData(
                        today.minusDays(7),
                        ProductType.CITRUS_FRUITS,
                        1200.0 * usdToTnd, // Convert to TND
                        200.0,
                        "Italy",
                        MarketIndicator.VOLATILE
                )
        );
    }
}