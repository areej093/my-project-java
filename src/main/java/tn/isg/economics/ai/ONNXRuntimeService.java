package tn.isg.economics.ai;

import tn.isg.economics.annotation.AIService;
import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.PricePrediction;
import tn.isg.economics.model.PredictionStatus;
import tn.isg.economics.exception.ModelException;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@AIService(provider = "ONNX Runtime", version = "1.16.0")
public class ONNXRuntimeService extends BaseAIModel {
    private static final Logger logger = Logger.getLogger(ONNXRuntimeService.class.getName());
    
    public ONNXRuntimeService() {
        super("ONNX-Price-Predictor");
    }
    
    @Override
    public void loadModel() throws ModelException {
        try {
            isLoaded = true;
            logger.info("ONNX model loaded successfully (demo mode)");
        } catch (Exception e) {
            throw new ModelException("Failed to load ONNX model", e);
        }
    }
    
    @Override
    public PricePrediction predictPrice(ExportData input) {
        validateInput(input);
        
        try {
            double basePrice = input.pricePerTon();
            double predictedPrice = basePrice * (1.0 + (Math.random() * 0.15 - 0.075));
            double confidence = 0.75 + (Math.random() * 0.2);
            
            return new PricePrediction(
                LocalDate.now().plusDays(30),
                input.productType(),
                predictedPrice,
                confidence,
                modelName,
                PredictionStatus.COMPLETED
            );
        } catch (Exception e) {
            logger.severe("ONNX prediction failed: " + e.getMessage());
            return new PricePrediction(
                LocalDate.now(),
                input.productType(),
                0.0,
                0.0,
                modelName,
                PredictionStatus.FAILED
            );
        }
    }
    
    @Override
    public List<PricePrediction> predictBatch(List<ExportData> inputs) {
        return inputs.stream()
            .map(this::predictPrice)
            .toList();
    }
    
    @Override
    public void unloadModel() {
        isLoaded = false;
        logger.info("ONNX model unloaded");
    }
    
    @Override
    public double getModelAccuracy() {
        return 0.78;
    }
}
