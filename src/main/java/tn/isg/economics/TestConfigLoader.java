package tn.isg.economics;

import tn.isg.economics.util.ConfigLoader;

public class TestConfigLoader {
    public static void main(String[] args) {
        System.out.println("=== Testing ConfigLoader ===");
        
        System.out.println("\n=== Basic Configuration ===");
        System.out.println("App Name: " + ConfigLoader.getProperty("app.name", "Tunisian Agricultural Export AI"));
        System.out.println("Environment: " + ConfigLoader.getProperty("app.environment", "development"));
        
        System.out.println("\n=== Data Configuration ===");
        System.out.println("Export CSV Path: " + ConfigLoader.getExportDataPath());
        System.out.println("Synthetic Data Enabled: " + ConfigLoader.isSyntheticDataEnabled());
        
        System.out.println("\n=== LLM Configuration ===");
        System.out.println("LLM Provider: " + ConfigLoader.getLLMProvider());
        System.out.println("Ollama URL: " + ConfigLoader.getProperty("llm.ollama.base.url", "http://localhost:11434"));
        
        System.out.println("\n=== AI Configuration ===");
        System.out.println("Default Model: " + ConfigLoader.getProperty("ai.model.default", "djl-lstm"));
        
        System.out.println("\n=== ConfigLoader Test Complete ===");
    }
}
