package tn.isg.economics.data.loader;

import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.ProductType;
import tn.isg.economics.model.MarketIndicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Load export data from CSV file
     */
    public List<ExportData> loadExportDataFromCSV(String filePath) {
        List<ExportData> exportDataList = new ArrayList<>();
        
        if (!Files.exists(Path.of(filePath))) {
            log.error("CSV file not found: {}", filePath);
            return exportDataList;
        }
        
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {
            
            log.info("Loading export data from: {}", filePath);
            int recordCount = 0;
            
            for (CSVRecord record : csvParser) {
                try {
                    ExportData exportData = parseExportRecord(record);
                    exportDataList.add(exportData);
                    recordCount++;
                } catch (Exception e) {
                    log.warn("Skipping invalid record at line {}: {}", 
                            record.getRecordNumber(), e.getMessage());
                }
            }
            
            log.info("Successfully loaded {} export records from {}", recordCount, filePath);
            
        } catch (Exception e) {
            log.error("Failed to load CSV file: {}", filePath, e);
        }
        
        return exportDataList;
    }
    
    /**
     * Parse a CSV record into ExportData object
     */
//    private ExportData parseExportRecord(CSVRecord record) {
//        LocalDate date = LocalDate.parse(record.get("date"), DATE_FORMATTER);
//        ProductType productType = ProductType.valueOf(record.get("product_type").toUpperCase());
//        double pricePerTon = Double.parseDouble(record.get("price_per_ton"));
//        double volume = Double.parseDouble(record.get("volume"));
//        String destinationCountry = record.get("destination_country");
//        MarketIndicator indicator = MarketIndicator.valueOf(record.get("market_indicator").toUpperCase());
//
//        return new ExportData(date, productType, pricePerTon, volume, destinationCountry, indicator);
//    }
    private ExportData parseExportRecord(CSVRecord record) {
        LocalDate date = LocalDate.parse(record.get("date"), DATE_FORMATTER);
        ProductType productType = ProductType.valueOf(record.get("product_type").toUpperCase());

        // FIX: Handle both comma and dot decimal separators
        String priceStr = record.get("price_per_ton").replace(',', '.');
        String volumeStr = record.get("volume").replace(',', '.');

        double pricePerTon = Double.parseDouble(priceStr);
        double volume = Double.parseDouble(volumeStr);
        String destinationCountry = record.get("destination_country");
        MarketIndicator indicator = MarketIndicator.valueOf(record.get("market_indicator").toUpperCase());

        return new ExportData(date, productType, pricePerTon, volume, destinationCountry, indicator);
    }
    /**
     * Load sample data for quick testing
     */
    public List<ExportData> loadSampleData() {
        List<ExportData> sampleData = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        sampleData.add(new ExportData(
            today.minusDays(30),
            ProductType.OLIVE_OIL,
            3500.0,
            100.0,
            "France",
            MarketIndicator.RISING
        ));
        
        sampleData.add(new ExportData(
            today.minusDays(15),
            ProductType.DATES,
            2500.0,
            50.0,
            "Germany",
            MarketIndicator.STABLE
        ));
        
        sampleData.add(new ExportData(
            today.minusDays(7),
            ProductType.CITRUS_FRUITS,
            1200.0,
            200.0,
            "Italy",
            MarketIndicator.VOLATILE
        ));
        
        log.info("Loaded {} sample export records", sampleData.size());
        return sampleData;
    }
}
