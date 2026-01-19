package tn.isg.economics.dashboard.service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tn.isg.economics.model.PricePrediction;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatisticsService {
    
    private List<PricePrediction> currentPredictions;
    
    public Map<String, Double> calculateProductAverages() {
        if (currentPredictions == null || currentPredictions.isEmpty()) {
            log.warn("No predictions available for statistics");
            return Collections.emptyMap();
        }
        
        return currentPredictions.stream()
            .collect(Collectors.groupingBy(
                p -> p.productType().name(),
                Collectors.averagingDouble(PricePrediction::predictedPrice)
            ));
    }
    
    @Getter(lazy = true)
    private final Map<String, Long> confidenceDistribution = calculateConfidenceDistribution();
    
    private Map<String, Long> calculateConfidenceDistribution() {
        return currentPredictions.stream()
            .collect(Collectors.groupingBy(
                p -> {
                    double conf = p.confidence();
                    return conf >= 0.8 ? "High" : conf >= 0.6 ? "Medium" : "Low";
                },
                Collectors.counting()
            ));
    }
    
    @SneakyThrows
    public void exportStatistics(String format) {
        log.info("Exporting statistics in {} format", format);
        // Simulate export logic
        Thread.sleep(500);
        log.info("Export completed successfully");
    }
}
