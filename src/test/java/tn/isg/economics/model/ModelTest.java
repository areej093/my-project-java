package tn.isg.economics.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class ModelTest {
    
    @Test
    @DisplayName("Test ExportData record creation and validation")
    void testExportDataCreation() {
        ExportData exportData = new ExportData(
            LocalDate.of(2024, 12, 24),
            ProductType.OLIVE_OIL,
            3500.0,
            100.0,
            "France",
            MarketIndicator.RISING
        );
        
        assertEquals(ProductType.OLIVE_OIL, exportData.productType());
        assertEquals(3500.0, exportData.pricePerTon(), 0.001);
        assertEquals("France", exportData.destinationCountry());
        assertEquals(MarketIndicator.RISING, exportData.indicator());
    }
    
    @Test
    @DisplayName("Test ExportData validation - negative price")
    void testExportDataNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ExportData(
                LocalDate.now(),
                ProductType.OLIVE_OIL,
                -100.0,  // Negative price should throw exception
                100.0,
                "France",
                MarketIndicator.STABLE
            );
        });
    }
    
    @Test
    @DisplayName("Test ProductType French names")
    void testProductTypeFrenchNames() {
        assertEquals("Huile d'olive", ProductType.OLIVE_OIL.getFrenchName());
        assertEquals("Dattes", ProductType.DATES.getFrenchName());
        assertEquals("Agrumes", ProductType.CITRUS_FRUITS.getFrenchName());
    }
    
    @Test
    @DisplayName("Test MarketIndicator values")
    void testMarketIndicator() {
        assertEquals(5, MarketIndicator.values().length, "Should have 5 market indicators");
        assertArrayEquals(new MarketIndicator[]{
            MarketIndicator.STABLE,
            MarketIndicator.VOLATILE,
            MarketIndicator.RISING,
            MarketIndicator.FALLING,
            MarketIndicator.UNPREDICTABLE
        }, MarketIndicator.values());
    }
    
    @Test
    @DisplayName("Test PricePrediction record")
    void testPricePrediction() {
        PricePrediction prediction = new PricePrediction(
            LocalDate.of(2025, 1, 24),
            ProductType.DATES,
            2500.0,
            0.85,
            "DJL-Price-Predictor",
            PredictionStatus.COMPLETED
        );
        
        assertEquals(ProductType.DATES, prediction.productType());
        assertEquals(2500.0, prediction.predictedPrice(), 0.001);
        assertEquals(0.85, prediction.confidence(), 0.001);
        assertEquals("DJL-Price-Predictor", prediction.modelName());
        assertEquals(PredictionStatus.COMPLETED, prediction.status());
    }
    
    @Test
    @DisplayName("Test PredictionStatus enum")
    void testPredictionStatus() {
        assertEquals(4, PredictionStatus.values().length, "Should have 4 prediction statuses");
        assertEquals(PredictionStatus.PENDING, PredictionStatus.valueOf("PENDING"));
        assertEquals(PredictionStatus.COMPLETED, PredictionStatus.valueOf("COMPLETED"));
        assertEquals(PredictionStatus.FAILED, PredictionStatus.valueOf("FAILED"));
        assertEquals(PredictionStatus.LOW_CONFIDENCE, PredictionStatus.valueOf("LOW_CONFIDENCE"));
    }
}
