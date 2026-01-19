package tn.isg.economics.data.transformer;

import tn.isg.economics.model.ExportData;
import tn.isg.economics.model.ProductType;
import tn.isg.economics.model.MarketIndicator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

/**
 * Transforms FAOSTAT dataset to our format with Tunisian Dinar conversion
 */
public class FAOSTATDataTransformer {
    // Map FAOSTAT product codes to ProductType
    private static final Map<String, ProductType> PRODUCT_MAPPING = Map.of(
            "01314", ProductType.DATES,
            "2167", ProductType.OLIVE_OIL,
            "01450", ProductType.OLIVE_OIL,
            "01323", ProductType.CITRUS_FRUITS,
            "01329", ProductType.CITRUS_FRUITS
    );

    // Common Tunisian export destinations
    private static final String[] DESTINATION_COUNTRIES = {
            "France", "Germany", "Italy", "Spain", "Libya",
            "United Kingdom", "Netherlands", "Belgium", "Algeria", "Morocco"
    };

    // USD to Tunisian Dinar exchange rate (approximate)
    private static final double USD_TO_TND = 3.1;

    private final Random random = new Random();

    /**
     * Transform FAOSTAT CSV to ExportData objects with TND prices
     */
    public List<ExportData> transformFAOSTATData(String filePath) {
        List<ExportData> exportDataList = new ArrayList<>();

        if (!Files.exists(Path.of(filePath))) {
            System.out.println("FAOSTAT file not found, using synthetic data: " + filePath);
            return generateSyntheticFAOData();
        }

        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            System.out.println("Processing FAOSTAT data from: " + filePath);
            Map<String, Map<Integer, Double>> dataCache = new HashMap<>();

            // Parse CSV data
            for (CSVRecord record : csvParser) {
                try {
                    String itemCode = record.get("Item Code (CPC)");
                    String element = record.get("Element");
                    int year = Integer.parseInt(record.get("Year"));
                    double value = parseDouble(record.get("Value"));

                    if (value == 0) continue;

                    String key = itemCode + "_" + year;
                    dataCache.putIfAbsent(key, new HashMap<>());

                    if ("Export quantity".equals(element)) {
                        dataCache.get(key).put(5910, value);
                    } else if ("Export value".equals(element)) {
                        dataCache.get(key).put(5922, value);
                    }
                } catch (Exception e) {
                    // Skip invalid records
                }
            }

            System.out.println("Found " + dataCache.size() + " product-year combinations");

            // Create ExportData objects
            for (Map.Entry<String, Map<Integer, Double>> entry : dataCache.entrySet()) {
                String[] parts = entry.getKey().split("_");
                String itemCode = parts[0];
                int year = Integer.parseInt(parts[1]);

                Map<Integer, Double> values = entry.getValue();

                if (values.containsKey(5910) && values.containsKey(5922)) {
                    double quantity = values.get(5910);
                    double valueUsd = values.get(5922); // in 1000 USD

                    if (quantity > 0) {
                        // Calculate USD price per ton
                        double pricePerTonUsd = (valueUsd * 1000) / quantity;

                        // Convert to Tunisian Dinar
                        double pricePerTonTnd = pricePerTonUsd * USD_TO_TND;

                        ProductType productType = PRODUCT_MAPPING.getOrDefault(itemCode, ProductType.OLIVE_OIL);

                        // Generate monthly variations
                        for (int month = 1; month <= 12; month++) {
                            double monthlyPrice = pricePerTonTnd * (0.9 + random.nextDouble() * 0.2);
                            double monthlyVolume = quantity / 12 * (0.8 + random.nextDouble() * 0.4);

                            ExportData exportData = new ExportData(
                                    LocalDate.of(year, month, 15), // Middle of month
                                    productType,
                                    Math.round(monthlyPrice * 100.0) / 100.0, // Round to 2 decimals
                                    Math.round(monthlyVolume * 100.0) / 100.0,
                                    DESTINATION_COUNTRIES[random.nextInt(DESTINATION_COUNTRIES.length)],
                                    calculateMarketIndicator(monthlyPrice, year, productType)
                            );

                            exportDataList.add(exportData);
                        }
                    }
                }
            }

            System.out.println("Transformed " + exportDataList.size() + " FAOSTAT records to TND");

        } catch (Exception e) {
            System.err.println("Error processing FAOSTAT data: " + e.getMessage());
            System.out.println("Falling back to synthetic data...");
            exportDataList = generateSyntheticFAOData();
        }

        return exportDataList;
    }

    /**
     * Generate synthetic FAO-like data for testing
     */
    private List<ExportData> generateSyntheticFAOData() {
        List<ExportData> data = new ArrayList<>();
        LocalDate startDate = LocalDate.now().minusYears(5);

        // Generate data for each year and product
        for (int year = 0; year < 5; year++) {
            LocalDate date = startDate.plusYears(year).withMonth(6).withDayOfMonth(15);

            // Olive Oil (in TND)
            data.add(new ExportData(
                    date,
                    ProductType.OLIVE_OIL,
                    Math.round((10500 + random.nextDouble() * 5000) * 100.0) / 100.0,
                    Math.round((50000 + random.nextDouble() * 150000) * 100.0) / 100.0,
                    "France",
                    MarketIndicator.RISING
            ));

            // Dates (in TND)
            data.add(new ExportData(
                    date,
                    ProductType.DATES,
                    Math.round((8000 + random.nextDouble() * 4000) * 100.0) / 100.0,
                    Math.round((30000 + random.nextDouble() * 70000) * 100.0) / 100.0,
                    "Germany",
                    MarketIndicator.STABLE
            ));

            // Citrus (in TND)
            data.add(new ExportData(
                    date,
                    ProductType.CITRUS_FRUITS,
                    Math.round((4000 + random.nextDouble() * 2000) * 100.0) / 100.0,
                    Math.round((80000 + random.nextDouble() * 120000) * 100.0) / 100.0,
                    "Italy",
                    MarketIndicator.VOLATILE
            ));
        }

        System.out.println("Generated " + data.size() + " synthetic FAO-like records in TND");
        return data;
    }

    private double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private MarketIndicator calculateMarketIndicator(double priceTnd, int year, ProductType productType) {
        if (year >= 2020) {
            return MarketIndicator.RISING;
        } else if (year >= 2015) {
            return MarketIndicator.VOLATILE;
        } else {
            return MarketIndicator.STABLE;
        }
    }

    /**
     * Get USD to TND exchange rate
     */
    public double getExchangeRate() {
        return USD_TO_TND;
    }

    /**
     * Convert USD to TND
     */
    public double convertUsdToTnd(double usdAmount) {
        return Math.round(usdAmount * USD_TO_TND * 100.0) / 100.0;
    }

    /**
     * Convert TND to USD
     */
    public double convertTndToUsd(double tndAmount) {
        return Math.round((tndAmount / USD_TO_TND) * 100.0) / 100.0;
    }
}