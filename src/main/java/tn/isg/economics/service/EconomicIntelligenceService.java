package tn.isg.economics.service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tn.isg.economics.ai.BaseAIModel;
import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.PricePrediction;
import tn.isg.economics.exception.ModelException;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EconomicIntelligenceService {
    
    @NonNull
    private BaseAIModel predictionModel;
    
    @NonNull
    private ReportGenerator reportGenerator;
    
    @Builder.Default
    private List<PricePrediction> predictionHistory = new ArrayList<>();
    
    /**
     * Analyze exports and generate predictions
     */
    public List<PricePrediction> analyzeExports(List<ExportData> exports) {
        log.info("Analyzing {} export records", exports.size());
        
        // Filter valid exports using Predicate (Functional Interface)
        Predicate<ExportData> isValidExport = export -> 
            export != null && 
            export.pricePerTon() > 0 && 
            export.volume() > 0;
        
        List<ExportData> validExports = exports.stream()
            .filter(isValidExport)
            .collect(Collectors.toList());
        
        log.debug("Valid exports after filtering: {}", validExports.size());
        
        // Generate predictions
        List<PricePrediction> predictions = predictionModel.predictBatch(validExports);
        
        // Store in history
        predictionHistory.addAll(predictions);
        
        // Filter high-confidence predictions
        return predictions.stream()
            .filter(p -> p.confidence() > 0.7)
            .collect(Collectors.toList());
    }
    
    /**
     * Generate market intelligence report
     */
    public String generateMarketReport(List<PricePrediction> predictions) {
        if (predictions == null || predictions.isEmpty()) {
            return "No predictions available for report generation.";
        }
        
        log.info("Generating market report for {} predictions", predictions.size());
        return reportGenerator.generateMarketReport(predictions);
    }
    
    /**
     * Generate executive summary
     */
    public String generateExecutiveSummary(List<PricePrediction> predictions) {
        log.info("Generating executive summary");
        return reportGenerator.generateSummaryReport(predictions);
    }
    
    /**
     * Get statistics by product type (JCF Map usage)
     */
    public Map<String, Double> getAveragePriceByProduct(List<PricePrediction> predictions) {
        return predictions.stream()
            .collect(Collectors.groupingBy(
                p -> p.productType().name(),
                Collectors.averagingDouble(PricePrediction::predictedPrice)
            ));
    }
    
    /**
     * Get confidence distribution (JCF usage)
     */
    public Map<String, Long> getConfidenceDistribution(List<PricePrediction> predictions) {
        return predictions.stream()
            .collect(Collectors.groupingBy(
                p -> {
                    double conf = p.confidence();
                    if (conf >= 0.8) return "High (80-100%)";
                    else if (conf >= 0.6) return "Medium (60-79%)";
                    else return "Low (0-59%)";
                },
                Collectors.counting()
            ));
    }
    
    /**
     * Filter predictions by multiple criteria (Complex JCF usage)
     */
    public List<PricePrediction> filterPredictions(
            List<PricePrediction> predictions,
            Predicate<PricePrediction> filter,
            Comparator<PricePrediction> sorter,
            int limit) {
        
        return predictions.stream()
            .filter(filter)
            .sorted(sorter)
            .limit(limit)
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Get prediction statistics
     */
    public Map<String, Object> getPredictionStatistics(List<PricePrediction> predictions) {
        Map<String, Object> stats = new HashMap<>();
        
        if (predictions.isEmpty()) {
            return stats;
        }
        
        DoubleSummaryStatistics priceStats = predictions.stream()
            .mapToDouble(PricePrediction::predictedPrice)
            .summaryStatistics();
        
        DoubleSummaryStatistics confidenceStats = predictions.stream()
            .mapToDouble(PricePrediction::confidence)
            .summaryStatistics();
        
        stats.put("totalPredictions", predictions.size());
        stats.put("avgPrice", priceStats.getAverage());
        stats.put("minPrice", priceStats.getMin());
        stats.put("maxPrice", priceStats.getMax());
        stats.put("avgConfidence", confidenceStats.getAverage());
        stats.put("highConfidenceCount", predictions.stream()
            .filter(p -> p.confidence() > 0.7).count());
        
        return stats;
    }
    
    /**
     * Process predictions asynchronously
     */
    public void processPredictionsAsync(List<ExportData> exports, 
                                        java.util.function.Consumer<PricePrediction> callback) {
        new Thread(() -> {
            exports.forEach(export -> {
                try {
                    PricePrediction prediction = predictionModel.predictPrice(export);
                    callback.accept(prediction);
                } catch (Exception e) {
                    log.error("Error processing export: {}", export, e);
                }
            });
        }).start();
    }
    
    /**
     * Get all prediction history
     */
    public List<PricePrediction> getPredictionHistory() {
        return Collections.unmodifiableList(predictionHistory);
    }
    
    /**
     * Clear prediction history
     */
    public void clearHistory() {
        predictionHistory.clear();
        log.info("Prediction history cleared");
    }
    
    /**
     * Export predictions to CSV format
     */
    public String exportToCSV(List<PricePrediction> predictions) {
        StringBuilder csv = new StringBuilder();
        csv.append("Product,Predicted Price,Confidence,Date,Model,Status\n");
        
        predictions.forEach(p -> {
            csv.append(String.format("%s,%.2f,%.2f,%s,%s,%s\n",
                p.productType().name(),
                p.predictedPrice(),
                p.confidence(),
                p.predictionDate(),
                p.modelName(),
                p.status()
            ));
        });
        
        return csv.toString();
    }
}
