package tn.isg.economics.util;

import java.util.Properties;

public class ConfigLoader {
    private static final Properties props = new Properties();
    
    static {
        // Set default values
        props.setProperty("app.name", "Tunisian Agricultural Export AI");
        props.setProperty("app.version", "1.0.0");
        props.setProperty("app.environment", "development");
        props.setProperty("data.synthetic.enabled", "true");
        props.setProperty("data.export.csv.path", "data/datasets/raw/exports.csv");
        props.setProperty("llm.provider", "ollama");
        props.setProperty("llm.ollama.base.url", "http://localhost:11434");
        props.setProperty("llm.ollama.model", "llama2");
        props.setProperty("ai.model.default", "djl-lstm");
        props.setProperty("ai.model.djl.path", "resources/models/djl/price_predictor.zip");
    }
    
    public static String getProperty(String key) {
        return props.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
    
    public static int getIntProperty(String key, int defaultValue) {
        try {
            String value = getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public static double getDoubleProperty(String key, double defaultValue) {
        try {
            String value = getProperty(key);
            return value != null ? Double.parseDouble(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.toLowerCase());
    }
    
    // NEW: Add the missing methods
    public static String getModelPath(String modelType) {
        String key = "ai.model." + modelType + ".path";
        return getProperty(key, "resources/models/" + modelType + "/default.model");
    }
    
    public static Object getModelConfig() {
        // Return a simple default config
        return "{\"models\":{\"default\":{\"type\":\"LSTM\"}}}";
    }
    
    public static boolean isSyntheticDataEnabled() {
        return getBooleanProperty("data.synthetic.enabled", true);
    }
    
    public static String getExportDataPath() {
        return getProperty("data.export.csv.path", "data/datasets/raw/exports.csv");
    }
    
    public static String getLLMProvider() {
        return getProperty("llm.provider", "ollama");
    }
    
    public static String getOllamaBaseUrl() {
        return getProperty("llm.ollama.base.url", "http://localhost:11434");
    }
    
    public static String getDefaultModelName() {
        return getProperty("ai.model.default", "djl-lstm");
    }
}
