package tn.isg.economics;

import tn.isg.economics.ai.DJLPredictionService;
import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.PricePrediction;
import tn.isg.economics.model.ProductType;
import tn.isg.economics.model.MarketIndicator;
import java.time.LocalDate;
import java.util.List;

public class SimpleMain {
    public static void main(String[] args) {
        System.out.println("=== Tunisian Agricultural Export AI (Simple Version) ===");
        System.out.println("Currency: Tunisian Dinar (TND) - 1 USD = 3.1 TND\n");
        
        try {
            // Create sample data in TND
            double usdToTnd = 3.1;
            List<ExportData> exports = List.of(
                new ExportData(
                    LocalDate.now().minusDays(30),
                    ProductType.OLIVE_OIL,
                    3500.0 * usdToTnd, // Convert to TND
                    100.0,
                    "France",
                    MarketIndicator.RISING
                ),
                new ExportData(
                    LocalDate.now().minusDays(15),
                    ProductType.DATES,
                    2500.0 * usdToTnd, // Convert to TND
                    50.0,
                    "Germany",
                    MarketIndicator.STABLE
                ),
                new ExportData(
                    LocalDate.now().minusDays(7),
                    ProductType.CITRUS_FRUITS,
                    1200.0 * usdToTnd, // Convert to TND
                    200.0,
                    "Italy",
                    MarketIndicator.VOLATILE
                )
            );
            
            System.out.println("? Created " + exports.size() + " export records in TND");
            
            // Initialize AI service
            System.out.println("Initializing AI prediction model...");
            var predictionService = new DJLPredictionService();
            predictionService.loadModel();
            
            // Generate predictions
            System.out.println("Generating price predictions...");
            var predictions = predictionService.predictBatch(exports);
            
            // Show results
            System.out.println("\n=== PRICE PREDICTIONS (TND) ===");
            predictions.forEach(p -> {
                double priceUsd = p.predictedPrice() / usdToTnd;
                System.out.printf("%s: %,.0f TND/ton (?%,.0f USD/ton) - Confidence: %.1f%%%n",
                    p.productType().getFrenchName(),
                    p.predictedPrice(),
                    priceUsd,
                    p.confidence() * 100);
            });
            
            predictionService.unloadModel();
            
            System.out.println("\n? System completed successfully!");
            System.out.println("Total predictions: " + predictions.size());
            System.out.println("Model accuracy: " + 
                String.format("%.1f%%", predictionService.getModelAccuracy() * 100));
            
        } catch (Exception e) {
            System.err.println("? Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
