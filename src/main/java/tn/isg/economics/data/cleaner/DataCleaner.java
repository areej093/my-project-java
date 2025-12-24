package tn.isg.economics.data.cleaner;

import tn.isg.economics.model.ExportData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class DataCleaner {
    private static final Logger log = LoggerFactory.getLogger(DataCleaner.class);
    
    /**
     * Clean export data by removing invalid records
     */
    public List<ExportData> cleanExportData(List<ExportData> rawData) {
        log.info("Cleaning {} export records", rawData.size());
        
        List<ExportData> cleanedData = new ArrayList<>();
        int invalidCount = 0;
        
        for (ExportData data : rawData) {
            if (isValidExportData(data)) {
                cleanedData.add(data);
            } else {
                invalidCount++;
            }
        }
        
        if (invalidCount > 0) {
            log.warn("Removed {} invalid records during cleaning", invalidCount);
        }
        
        log.info("Returning {} cleaned records", cleanedData.size());
        return cleanedData;
    }
    
    /**
     * Validate export data
     */
    private boolean isValidExportData(ExportData data) {
        if (data == null) {
            log.debug("Skipping null data");
            return false;
        }
        
        // Check date is not in the future
        if (data.date().isAfter(LocalDate.now())) {
            log.debug("Skipping record with future date: {}", data.date());
            return false;
        }
        
        // Check price is positive
        if (data.pricePerTon() <= 0) {
            log.debug("Skipping record with non-positive price: {}", data.pricePerTon());
            return false;
        }
        
        // Check volume is positive
        if (data.volume() <= 0) {
            log.debug("Skipping record with non-positive volume: {}", data.volume());
            return false;
        }
        
        // Check destination country is not empty
        if (data.destinationCountry() == null || data.destinationCountry().trim().isEmpty()) {
            log.debug("Skipping record with empty destination country");
            return false;
        }
        
        return true;
    }
    
    /**
     * Normalize data (future enhancement: normalize prices, etc.)
     */
    public List<ExportData> normalizeData(List<ExportData> data) {
        // For now, just return the data as-is
        // Future: Implement normalization (scaling, encoding, etc.)
        log.info("Normalizing {} records (placeholder)", data.size());
        return data;
    }
}
