package tn.isg.economics.ai;

import tn.isg.economics.annotation.AIService;
import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.PricePrediction;
import tn.isg.economics.model.PredictionStatus;
import tn.isg.economics.exception.ModelException;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@AIService(provider = "DJL", version = "0.25.0")
public class DJLPredictionService extends BaseAIModel {
    private static final Logger logger = Logger.getLogger(DJLPredictionService.class.getName());
    
    public DJLPredictionService() {
        super("DJL-Price-Predictor");
    }
    
    @Override
    public void loadModel() throws ModelException {
        try {
            isLoaded = true;
            logger.info("DJL model loaded successfully (demo mode)");
        } catch (Exception e) {
            throw new ModelException("Failed to load DJL model", e);
        }
    }
    
    @Override
    public PricePrediction predictPrice(ExportData input) {
        validateInput(input);
        
        try {
            // Demo prediction logic
            double basePrice = input.pricePerTon();
            double predictedPrice = basePrice * (1.0 + (Math.random() * 0.2 - 0.1));
            double confidence = 0.7 + (Math.random() * 0.25);
            
            return new PricePrediction(
                LocalDate.now().plusDays(30),
                input.productType(),
                predictedPrice,
                confidence,
                modelName,
                PredictionStatus.COMPLETED
            );
        } catch (Exception e) {
            logger.severe("Prediction failed: " + e.getMessage());
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
        logger.info("DJL model unloaded");
    }
    
    @Override
    public double getModelAccuracy() {
        return 0.82;
    }
}
