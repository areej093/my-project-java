package tn.isg.economics;

import tn.isg.economics.data.loader.DataLoader;
import tn.isg.economics.data.cleaner.DataCleaner;
import tn.isg.economics.data.generator.SyntheticDataGenerator;
import tn.isg.economics.util.ConfigLoader;
import tn.isg.economics.model.ExportData;

import java.util.List;

public class DataPipelineTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Complete Data Pipeline ===");
        
        try {
            // 1. Check configuration
            System.out.println("1. Checking configuration...");
            System.out.println("   Synthetic Data Enabled: " + ConfigLoader.isSyntheticDataEnabled());
            System.out.println("   Export Data Path: " + ConfigLoader.getExportDataPath());
            
            // 2. Generate synthetic data
            System.out.println("2. Generating synthetic data...");
            SyntheticDataGenerator generator = new SyntheticDataGenerator();
            
            // Create directory
            new java.io.File("data/datasets/synthetic").mkdirs();
            new java.io.File("data/datasets/raw").mkdirs();
            new java.io.File("data/datasets/processed").mkdirs();
            
            // Generate and save dataset
            String syntheticPath = "data/datasets/synthetic/tunisia_exports_full.csv";
            generator.generateCompleteDataset(syntheticPath, 1000);
            
            // 3. Load data
            System.out.println("3. Loading data...");
            DataLoader loader = new DataLoader();
            
            // Try to load synthetic data
            List<ExportData> loadedData = loader.loadExportDataFromCSV(syntheticPath);
            System.out.println("   Loaded " + loadedData.size() + " records from CSV");
            
            // Also load sample data
            List<ExportData> sampleData = loader.loadSampleData();
            System.out.println("   Loaded " + sampleData.size() + " sample records");
            
            // 4. Clean data
            System.out.println("4. Cleaning data...");
            DataCleaner cleaner = new DataCleaner();
            List<ExportData> cleanedData = cleaner.cleanExportData(loadedData);
            System.out.println("   Cleaned data: " + cleanedData.size() + " valid records");
            
            // 5. Display sample of cleaned data
            System.out.println("5. Sample of cleaned data:");
            int sampleSize = Math.min(5, cleanedData.size());
            for (int i = 0; i < sampleSize; i++) {
                ExportData data = cleanedData.get(i);
                System.out.println("   - " + data.date() + " | " + 
                    data.productType() + " | " + 
                    String.format("%.2f", data.pricePerTon()) + " USD/ton | " +
                    data.destinationCountry());
            }
            
            // 6. Statistics
            System.out.println("6. Data pipeline statistics:");
            System.out.println("   - Total records generated: 1000");
            System.out.println("   - Records loaded: " + loadedData.size());
            System.out.println("   - Valid records after cleaning: " + cleanedData.size());
            System.out.println("   - Invalid records removed: " + (loadedData.size() - cleanedData.size()));
            
            System.out.println("? Data pipeline test completed successfully!");
            
        } catch (Exception e) {
            System.err.println("? Error in data pipeline test: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== Test Complete ===");
    }
}
