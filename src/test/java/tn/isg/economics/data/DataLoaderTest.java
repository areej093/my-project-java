package tn.isg.economics.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import tn.isg.economics.data.loader.DataLoader;
import tn.isg.economics.model.ExportData;
import java.util.List;

public class DataLoaderTest {
    
    private DataLoader dataLoader;
    
    @BeforeEach
    void setUp() {
        dataLoader = new DataLoader();
    }
    
    @Test
    @DisplayName("Test loading sample data")
    void testLoadSampleData() {
        List<ExportData> sampleData = dataLoader.loadSampleData();
        
        assertNotNull(sampleData, "Sample data should not be null");
        assertFalse(sampleData.isEmpty(), "Sample data should not be empty");
        assertTrue(sampleData.size() >= 3, "Should have at least 3 sample records");
        
        ExportData firstRecord = sampleData.get(0);
        assertNotNull(firstRecord.productType(), "Product type should not be null");
        assertTrue(firstRecord.pricePerTon() > 0, "Price should be positive");
        assertTrue(firstRecord.volume() > 0, "Volume should be positive");
    }
    
    @Test
    @DisplayName("Test data validation in sample")
    void testSampleDataValidation() {
        List<ExportData> sampleData = dataLoader.loadSampleData();
        
        // All records should have valid data
        for (ExportData data : sampleData) {
            assertNotNull(data.date(), "Date should not be null");
            assertNotNull(data.productType(), "Product type should not be null");
            assertTrue(data.pricePerTon() > 0, "Price should be positive: " + data.pricePerTon());
            assertTrue(data.volume() > 0, "Volume should be positive: " + data.volume());
            assertNotNull(data.destinationCountry(), "Destination country should not be null");
            assertFalse(data.destinationCountry().trim().isEmpty(), "Destination country should not be empty");
        }
    }
    
    @Test
    @DisplayName("Test CSV loading (if file exists)")
    void testCSVLoading() {
        // Test that data loader doesn't crash even if CSV doesn't exist
        DataLoader loader = new DataLoader();
        List<ExportData> data = loader.loadExportDataFromCSV("nonexistent.csv");
        
        assertNotNull(data, "Should return empty list, not null");
        assertTrue(data.isEmpty(), "Should return empty list for non-existent file");
    }
}
