/**
 * Main application class for Tunisian Agricultural Export AI System.
 * <p>
 * This system integrates multiple AI libraries (DJL, ONNX Runtime) for price prediction
 * and demonstrates LLM integration for market intelligence report generation.
 * </p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>AI-powered price prediction using historical export data</li>
 *   <li>Multiple AI model integration (DJL + ONNX Runtime)</li>
 *   <li>LLM-generated market intelligence reports</li>
 *   <li>Complete data pipeline with ETL processing</li>
 *   <li>Interactive dashboard with real-time analytics</li>
 * </ul>
 * 
 * @author Student Name
 * @version 1.0.0
 * @since 2025-2026 Academic Year
 * @see tn.isg.economics.ai.DJLPredictionService
 * @see tn.isg.economics.ai.ONNXRuntimeService
 * @see tn.isg.economics.ai.LLMReportService
 */
package tn.isg.economics;

import tn.isg.economics.ai.DJLPredictionService;
import tn.isg.economics.ai.LLMReportService;
import tn.isg.economics.data.generator.SyntheticDataGenerator;
import tn.isg.economics.data.loader.DataLoader;
import tn.isg.economics.data.cleaner.DataCleaner;
import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.PricePrediction;
import tn.isg.economics.model.ProductType;
import tn.isg.economics.model.MarketIndicator;
import tn.isg.economics.exception.ModelException;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Tunisian Agricultural Export AI System ===\n");
        
        try {
            // 1. Generate sample data
            SyntheticDataGenerator generator = new SyntheticDataGenerator();
            List<ExportData> exports = generator.generateExportData(50, 
                LocalDate.now().minusYears(1), LocalDate.now());
            
            System.out.println("? Generated " + exports.size() + " export records");
            
            // 2. Initialize AI prediction service
            System.out.println("Initializing AI prediction model...");
            var predictionService = new DJLPredictionService();
            predictionService.loadModel();
            
            // 3. Initialize LLM report service
            System.out.println("Initializing LLM report service...");
            var reportService = new LLMReportService();
            System.out.println("LLM Report Service: " + reportService.demonstrateLLMIntegration("test"));
            
            // 4. Generate predictions
            System.out.println("Generating price predictions...");
            var predictions = predictionService.predictBatch(exports);
            
            // Show sample predictions
            System.out.println("\n=== SAMPLE PREDICTIONS ===");
            predictions.stream().limit(5).forEach(p -> {
                System.out.printf("%s: $%.2f/ton (Confidence: %.1f%%)%n",
                    p.productType().getFrenchName(),
                    p.predictedPrice(),
                    p.confidence() * 100);
            });
            
            // 5. Generate market report
            System.out.println("\n=== MARKET INTELLIGENCE REPORT ===");
            String report = reportService.generateMarketReport(predictions);
            System.out.println(report);
            
            // 6. Generate executive summary
            System.out.println("\n=== EXECUTIVE SUMMARY ===");
            String summary = reportService.generateSummaryReport(predictions);
            System.out.println(summary);
            
            // 7. Cleanup
            predictionService.unloadModel();
            
            System.out.println("\n? System completed successfully!");
            System.out.println("Total predictions: " + predictions.size());
            System.out.println("Model accuracy: " + 
                String.format("%.1f%%", predictionService.getModelAccuracy() * 100));
            
        } catch (ModelException e) {
            System.err.println("? Model error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("? System error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

