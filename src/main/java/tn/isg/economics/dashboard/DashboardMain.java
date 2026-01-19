package tn.isg.economics.dashboard;

import tn.isg.economics.data.transformer.FAOSTATDataTransformer;
import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.PricePrediction;
import tn.isg.economics.ai.TimeSeriesPredictionService;  // Add this line
import java.util.ArrayList;
import tn.isg.economics.ai.DJLPredictionService;

import javax.swing.*;
import java.util.List;

public class DashboardMain {

    public static void main(String[] args) {
        System.out.println("=== Tunisian Agricultural Export Dashboard ===");
        System.out.println("Using real FAO data with Tunisian Dinar conversion\n");

        try {
            // 1. Load and transform FAO data
            System.out.println("1. Loading and transforming FAO data...");
            FAOSTATDataTransformer transformer = new FAOSTATDataTransformer();

            String faoDataPath = "datasets/raw/FAOSTAT_data_en_12-20-2025.csv";
            List<ExportData> faoData = transformer.transformFAOSTATData(faoDataPath);

            if (faoData.isEmpty()) {
                System.out.println("Warning: No FAO data loaded. Using synthetic data.");
                tn.isg.economics.data.generator.SyntheticDataGenerator generator =
                        new tn.isg.economics.data.generator.SyntheticDataGenerator();
                faoData = generator.generateExportData(100,
                        java.time.LocalDate.now().minusYears(1),
                        java.time.LocalDate.now());
            }

            // Convert USD to TND (Tunisian Dinar)
            System.out.println("2. Converting prices to Tunisian Dinar (TND)...");
            double usdToTndRate = 3.1; // Approximate exchange rate
            List<ExportData> tndData = faoData.stream()
                    .map(export -> new ExportData(
                            export.date(),
                            export.productType(),
                            Math.round(export.pricePerTon() * usdToTndRate * 100.0) / 100.0, // Convert to TND, rounded
                            export.volume(),
                            export.destinationCountry(),
                            export.indicator()
                    ))
                    .toList();

            System.out.println("Converted " + tndData.size() + " records to TND");

//            // 3. Generate predictions
//            System.out.println("3. Generating AI predictions in TND...");
//            var predictionService = new DJLPredictionService();
//            predictionService.loadModel();
//            List<PricePrediction> predictions = predictionService.predictBatch(tndData);
//            predictionService.unloadModel();
//
//            // Convert predictions to TND
//            List<PricePrediction> tndPredictions = predictions.stream()
//                    .map(pred -> new PricePrediction(
//                            pred.predictionDate(),
//                            pred.productType(),
//                            Math.round(pred.predictedPrice() * usdToTndRate * 100.0) / 100.0,
//                            pred.confidence(),
//                            pred.modelName() + " (TND)",
//                            pred.status()
//                    ))
//                    .toList();
//
//            System.out.println("Generated " + tndPredictions.size() + " price predictions in TND");

            // 3. Generate REAL predictions with time series
            System.out.println("3. Generating AI time-series predictions in TND...");
            var predictionService = new TimeSeriesPredictionService(); // CHANGED: Use TimeSeries
            predictionService.loadModel();

// Generate forecasts for next 3 months
            List<PricePrediction> predictions = new ArrayList<>();
            for (ExportData export : tndData) {
                // Get 3-month forecast for each product
                List<PricePrediction> forecast = predictionService.generateForecast(export, 3);
                predictions.addAll(forecast);
            }

            predictionService.unloadModel();

// Convert predictions to TND (already in TND from FAOSTATDataTransformer)
            List<PricePrediction> tndPredictions = predictions.stream()
                    .map(pred -> new PricePrediction(
                            pred.predictionDate(),
                            pred.productType(),
                            pred.predictedPrice(), // Already in TND
                            pred.confidence(),
                            pred.modelName() + " (TND Forecast)",
                            pred.status()
                    ))
                    .toList();

            System.out.println("Generated " + tndPredictions.size() + " FUTURE price predictions in TND");
            System.out.println("Forecast period: Next 3 months");
            // 5. Create and show dashboard
            System.out.println("4. Launching dashboard GUI...");
            SwingUtilities.invokeLater(() -> {
                try {
                    // Use AgriculturalDashboard (the working one)
                    AgriculturalDashboard dashboard = new AgriculturalDashboard();
                    dashboard.setVisible(true);

                    // Show success message
                    dashboard.showMessage("Dashboard loaded with " + tndPredictions.size() +
                            " FAO predictions in Tunisian Dinar");

                    System.out.println("? Dashboard launched successfully!");
                    System.out.println("\n=== PREDICTION SUMMARY ===");
                    System.out.println("Currency: Tunisian Dinar (TND)");
                    System.out.println("Exchange rate: 1 USD = " + usdToTndRate + " TND");

                    // Show summary by product
                    tndPredictions.stream()
                            .collect(java.util.stream.Collectors.groupingBy(
                                    p -> p.productType().name(),
                                    java.util.stream.Collectors.counting()
                            ))
                            .forEach((product, count) -> {
                                double avgPrice = tndPredictions.stream()
                                        .filter(p -> p.productType().name().equals(product))
                                        .mapToDouble(PricePrediction::predictedPrice)
                                        .average()
                                        .orElse(0);
                                System.out.printf("  %s: %d predictions, Average: %,.2f TND/ton%n",
                                        product, count, avgPrice);
                            });

                } catch (Exception e) {
                    System.err.println("Error creating dashboard: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            System.err.println("Error in DashboardMain: " + e.getMessage());
            e.printStackTrace();

            // Fallback: Launch simple dashboard
            SwingUtilities.invokeLater(() -> {
                var fallbackDashboard = new AgriculturalDashboard();
                fallbackDashboard.setVisible(true);
                fallbackDashboard.showMessage("Using fallback dashboard. Error: " + e.getMessage());
            });
        }

        System.out.println("\n=== Dashboard System Ready ===");
    }
}