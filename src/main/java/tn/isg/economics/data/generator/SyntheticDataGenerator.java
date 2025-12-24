package tn.isg.economics.data.generator;

import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.ProductType;
import tn.isg.economics.model.MarketIndicator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.FileWriter;
import java.io.IOException;

public class SyntheticDataGenerator {
    private final Random random = new Random(42);
    
    public List<ExportData> generateExportData(int numRecords, LocalDate startDate, LocalDate endDate) {
        List<ExportData> exportData = new ArrayList<>();
        ProductType[] productTypes = ProductType.values();
        
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        
        for (int i = 0; i < numRecords; i++) {
            LocalDate randomDate = startDate.plusDays(random.nextInt((int) daysBetween + 1));
            ProductType productType = productTypes[random.nextInt(productTypes.length)];
            
            double pricePerTon = 0;
            switch (productType) {
                case OLIVE_OIL: pricePerTon = 3000 + random.nextDouble() * 1000; break;
                case DATES: pricePerTon = 2000 + random.nextDouble() * 1000; break;
                case CITRUS_FRUITS: pricePerTon = 1000 + random.nextDouble() * 500; break;
                case WHEAT: pricePerTon = 700 + random.nextDouble() * 200; break;
                default: pricePerTon = 1000 + random.nextDouble() * 500;
            }
            
            double volume = 50 + random.nextDouble() * 150;
            String destinationCountry = random.nextBoolean() ? "France" : "Germany";
            MarketIndicator indicator = MarketIndicator.values()[random.nextInt(MarketIndicator.values().length)];
            
            exportData.add(new ExportData(randomDate, productType, pricePerTon, volume, destinationCountry, indicator));
        }
        
        System.out.println("Generated " + numRecords + " synthetic export records");
        return exportData;
    }    
    /**
     * Export generated data to CSV file
     */
    public void exportToCSV(List<ExportData> exportData, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("date", "product_type", "price_per_ton", "volume", 
                                 "destination_country", "market_indicator"))) {
            
            for (ExportData data : exportData) {
                csvPrinter.printRecord(
                    data.date().toString(),
                    data.productType().name(),
                    String.format("%.2f", data.pricePerTon()),
                    String.format("%.2f", data.volume()),
                    data.destinationCountry(),
                    data.indicator().name()
                );
            }
            
            System.out.println("Exported " + exportData.size() + " records to CSV: " + filePath);
        }
    }
    
    /**
     * Generate and save a complete synthetic dataset
     */
    public void generateCompleteDataset(String outputPath, int numRecords) throws IOException {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(3);
        
        List<ExportData> exportData = generateExportData(numRecords, startDate, endDate);
        exportToCSV(exportData, outputPath);
    }
}
