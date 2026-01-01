package tn.isg.economics.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.ProductType;
import tn.isg.economics.model.MarketIndicator;
import tn.isg.economics.model.PricePrediction;
import tn.isg.economics.exception.ModelException;
import java.time.LocalDate;
import java.util.List;

public class AIServiceTest {
    
    private DJLPredictionService djlService;
    private ONNXRuntimeService onnxService;
    
    @BeforeEach
    void setUp() {
        djlService = new DJLPredictionService();
        onnxService = new ONNXRuntimeService();
    }
    
    @Test
    @DisplayName("Test DJL model loading")
    void testDJLModelLoading() {
        try {
            djlService.loadModel();
            assertTrue(djlService.getModelAccuracy() > 0, "Model accuracy should be positive");
            assertTrue(djlService.getModelAccuracy() <= 1, "Model accuracy should be <= 1");
        } catch (ModelException e) {
            fail("Model loading should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test ONNX model loading")
    void testONNXModelLoading() {
        try {
            onnxService.loadModel();
            assertTrue(onnxService.getModelAccuracy() > 0, "Model accuracy should be positive");
            assertTrue(onnxService.getModelAccuracy() <= 1, "Model accuracy should be <= 1");
        } catch (ModelException e) {
            fail("Model loading should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test price prediction with valid input")
    void testPricePrediction() {
        try {
            djlService.loadModel();
            
            ExportData testData = new ExportData(
                LocalDate.now(),
                ProductType.OLIVE_OIL,
                3500.0,
                100.0,
                "France",
                MarketIndicator.RISING
            );
            
            PricePrediction prediction = djlService.predictPrice(testData);
            
            assertNotNull(prediction, "Prediction should not be null");
            assertEquals(ProductType.OLIVE_OIL, prediction.productType());
            assertTrue(prediction.predictedPrice() > 0, "Predicted price should be positive");
            assertTrue(prediction.confidence() >= 0 && prediction.confidence() <= 1, 
                      "Confidence should be between 0 and 1");
            assertNotNull(prediction.modelName(), "Model name should not be null");
            assertFalse(prediction.modelName().isEmpty(), "Model name should not be empty");
            
        } catch (ModelException e) {
            fail("Prediction should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test batch prediction")
    void testBatchPrediction() {
        try {
            djlService.loadModel();
            
            List<ExportData> testData = List.of(
                new ExportData(LocalDate.now(), ProductType.OLIVE_OIL, 3500.0, 100.0, "France", MarketIndicator.RISING),
                new ExportData(LocalDate.now(), ProductType.DATES, 2500.0, 50.0, "Germany", MarketIndicator.STABLE),
                new ExportData(LocalDate.now(), ProductType.CITRUS_FRUITS, 1200.0, 200.0, "Italy", MarketIndicator.VOLATILE)
            );
            
            List<PricePrediction> predictions = djlService.predictBatch(testData);
            
            assertEquals(3, predictions.size(), "Should have 3 predictions");
            assertTrue(predictions.stream().allMatch(p -> p != null), 
                      "All predictions should be non-null");
            
            // Check each prediction has valid data
            for (PricePrediction p : predictions) {
                assertNotNull(p.productType());
                assertTrue(p.predictedPrice() > 0);
                assertTrue(p.confidence() >= 0);
            }
            
        } catch (ModelException e) {
            fail("Batch prediction should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test model unloading")
    void testModelUnloading() {
        try {
            djlService.loadModel();
            assertTrue(true, "Model should load successfully");
            
            djlService.unloadModel();
            assertTrue(true, "Model should unload without exception");
            
        } catch (ModelException e) {
            fail("Model operations should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test prediction with null input")
    void testPredictionWithNullInput() {
        try {
            djlService.loadModel();
            
            assertThrows(IllegalArgumentException.class, () -> {
                djlService.predictPrice(null);
            }, "Should throw IllegalArgumentException for null input");
            
        } catch (ModelException e) {
            fail("Model loading should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test prediction without loading model")
    void testPredictionWithoutModelLoaded() {
        DJLPredictionService unloadedService = new DJLPredictionService();
        
        ExportData testData = new ExportData(
            LocalDate.now(),
            ProductType.OLIVE_OIL,
            3500.0,
            100.0,
            "France",
            MarketIndicator.RISING
        );
        
        assertThrows(IllegalStateException.class, () -> {
            unloadedService.predictPrice(testData);
        }, "Should throw IllegalStateException if model not loaded");
    }
    
    @Test
    @DisplayName("Compare DJL and ONNX model accuracies")
    void testModelAccuracies() {
        try {
            djlService.loadModel();
            onnxService.loadModel();
            
            double djlAccuracy = djlService.getModelAccuracy();
            double onnxAccuracy = onnxService.getModelAccuracy();
            
            assertTrue(djlAccuracy > 0.7, "DJL accuracy should be reasonable (>0.7)");
            assertTrue(onnxAccuracy > 0.7, "ONNX accuracy should be reasonable (>0.7)");
            
            System.out.println("DJL Accuracy: " + djlAccuracy);
            System.out.println("ONNX Accuracy: " + onnxAccuracy);
            
        } catch (ModelException e) {
            fail("Model loading should not throw exception: " + e.getMessage());
        }
    }
}
