package tn.isg.economics;

import tn.isg.economics.data.generator.SyntheticDataGenerator;
import tn.isg.economics.util.ConfigLoader;
import java.time.LocalDate;

public class TestDataGenerator {
    public static void main(String[] args) {
        System.out.println("=== Testing Data Generator ===");
        
        try {
            SyntheticDataGenerator generator = new SyntheticDataGenerator();
            
            if (ConfigLoader.isSyntheticDataEnabled()) {
                System.out.println("Synthetic data is enabled");
                
                // Create directory
                new java.io.File("data/datasets/synthetic").mkdirs();
                
                // Generate test data
                LocalDate endDate = LocalDate.now();
                LocalDate startDate = endDate.minusYears(1);
                
                var data = generator.generateExportData(10, startDate, endDate);
                System.out.println("Generated " + data.size() + " records");
                
                System.out.println("? Test passed!");
            } else {
                System.out.println("Synthetic data is disabled");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== Test Complete ===");
    }
}
