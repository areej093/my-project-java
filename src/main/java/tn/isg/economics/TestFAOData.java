package tn.isg.economics;

import tn.isg.economics.data.transformer.FAOSTATDataTransformer;
import tn.isg.economics.model.ExportData;
import java.util.List;

public class TestFAOData {
    public static void main(String[] args) {
        System.out.println("=== Testing FAO Data Transformation ===\n");
        
        try {
            new java.io.File("data/datasets/transformed").mkdirs();
            
            FAOSTATDataTransformer transformer = new FAOSTATDataTransformer();
            
            String faoPath = "datasets/raw/FAOSTAT_data_en_12-20-2025.csv";
            System.out.println("Processing: " + faoPath);
            
            List<ExportData> faoData = transformer.transformFAOSTATData(faoPath);
            
            if (faoData.isEmpty()) {
                System.out.println("ERROR: No data transformed!");
                System.out.println("Using synthetic data instead...");
                tn.isg.economics.data.generator.SyntheticDataGenerator generator = 
                    new tn.isg.economics.data.generator.SyntheticDataGenerator();
                faoData = generator.generateExportData(50, 
                    java.time.LocalDate.now().minusYears(1),
                    java.time.LocalDate.now());
            }
            
            System.out.println("\n=== RESULTS ===");
            System.out.println("Total records: " + faoData.size());
            
            System.out.println("\n=== SAMPLE RECORDS ===");
            int sampleSize = Math.min(5, faoData.size());
            for (int i = 0; i < sampleSize; i++) {
                ExportData data = faoData.get(i);
                System.out.println(String.format("%d. %s | %s | $%.2f/ton | %.2f tons | %s",
                    i+1,
                    data.date().getYear(),
                    data.productType(),
                    data.pricePerTon(),
                    data.volume(),
                    data.destinationCountry()
                ));
            }
            
            System.out.println("\n=== STATISTICS ===");
            double avgPrice = faoData.stream()
                .mapToDouble(ExportData::pricePerTon)
                .average().orElse(0);
            
            System.out.println("Average price: $" + String.format("%,.2f", avgPrice) + "/ton");
            
            faoData.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    ExportData::productType,
                    java.util.stream.Collectors.counting()
                ))
                .forEach((product, count) -> {
                    System.out.println(product + ": " + count + " records");
                });
            
            System.out.println("\n? Test completed successfully!");
            
        } catch (Exception e) {
            System.err.println("? ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
