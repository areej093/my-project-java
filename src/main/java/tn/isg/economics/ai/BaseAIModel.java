package tn.isg.economics.ai;

import tn.isg.economics.annotation.AIService;
import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.PricePrediction;
import tn.isg.economics.service.PredictionService;
import tn.isg.economics.exception.ModelException;
import java.util.List;
import java.util.logging.Logger;

@AIService(provider = "Base", version = "1.0")
public abstract class BaseAIModel implements PredictionService {
    protected static final Logger logger = Logger.getLogger(BaseAIModel.class.getName());
    protected String modelName;
    protected boolean isLoaded = false;
    
    public BaseAIModel(String modelName) {
        this.modelName = modelName;
    }
    
    public abstract void loadModel() throws ModelException;
    public abstract void unloadModel();
    
    protected void validateInput(ExportData input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        if (!isLoaded) {
            throw new IllegalStateException("Model not loaded");
        }
    }
    
    @Override
    public double getModelAccuracy() {
        return 0.75;
    }
}
