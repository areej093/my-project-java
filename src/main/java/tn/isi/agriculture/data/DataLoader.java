package tn.isi.agriculture.data;

import tn.isi.agriculture.model.ExportRecord;
import tn.isi.agriculture.model.ProductType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DataLoader {

    public List<ExportRecord> loadFromCSV() throws IOException {
        System.out.println("🔍 Attempting to read CSV file...");

        Path csvPath = Paths.get("datasets/raw/FAOSTAT_data_en_12-20-2025.csv");
        System.out.println("📁 CSV path: " + csvPath.toAbsolutePath());

        if (!csvPath.toFile().exists()) {
            System.out.println("❌ CSV file not found!");
            return loadSampleData();
        }

        List<ExportRecord> records = new ArrayList<>();
        Map<String, Double> quantityMap = new HashMap<>();
        Map<String, Double> valueMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toFile()))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                // Skip header line
                if (lineNumber == 1) {
                    System.out.println("📋 CSV Header: " + line.substring(0, Math.min(100, line.length())) + "...");
                    continue;
                }

                // Simple CSV parsing - split by comma but handle quotes
                String[] parts = parseCSVLine(line);

                if (parts.length >= 13) {
                    try {
                        String item = clean(parts[7]);      // Item column
                        String yearStr = clean(parts[9]);   // Year column
                        String element = clean(parts[5]);   // Element column
                        String valueStr = clean(parts[11]); // Value column

                        // Skip if not export data
                        if (!element.equals("Export quantity") && !element.equals("Export value")) {
                            continue;
                        }

                        int year = Integer.parseInt(yearStr);
                        double value = Double.parseDouble(valueStr);

                        // Create unique key
                        String key = item + "_" + year;

                        if (element.equals("Export quantity")) {
                            quantityMap.put(key, value);
                        } else if (element.equals("Export value")) {
                            // Convert from "1000 USD" to actual USD
                            valueMap.put(key, value * 1000);
                        }

                    } catch (NumberFormatException e) {
                        // Skip invalid number rows
                        continue;
                    }
                }
            }

            System.out.println("📊 Found " + quantityMap.size() + " quantity records");
            System.out.println("💰 Found " + valueMap.size() + " value records");

        } catch (Exception e) {
            System.out.println("⚠️  Error reading CSV: " + e.getMessage());
            return loadSampleData();
        }

        // Combine data into ExportRecord objects
        int successfulRecords = 0;
        for (Map.Entry<String, Double> entry : quantityMap.entrySet()) {
            String key = entry.getKey();
            double quantity = entry.getValue();
            Double value = valueMap.get(key);

            if (value != null && quantity > 0) {
                String[] keyParts = key.split("_");
                String productName = keyParts[0];
                int year = Integer.parseInt(keyParts[1]);

                // Convert product name to ProductType enum
                ProductType productType = ProductType.fromString(productName);

                // Calculate price per kg
                double pricePerKg = value / (quantity * 1000);

                records.add(new ExportRecord(year, productType, quantity, value, pricePerKg));
                successfulRecords++;
            }
        }

        System.out.println("✅ Successfully created " + successfulRecords + " export records");

        // Sort by year and product
        records.sort(Comparator
                .comparing(ExportRecord::year)
                .thenComparing(ExportRecord::productType));

        return records;
    }

    // Helper method to parse CSV line with quotes
    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }

    // Helper to clean string
    private String clean(String str) {
        return str.replace("\"", "").trim();
    }

    // Sample data fallback
    public List<ExportRecord> loadSampleData() {
        System.out.println("🔄 Using sample data (fallback)");
        List<ExportRecord> records = new ArrayList<>();

        // Dates data (2000-2004)
        records.add(new ExportRecord(2000, ProductType.DATES, 22411.0, 38590000, 1.72));
        records.add(new ExportRecord(2001, ProductType.DATES, 47043.0, 73412000, 1.56));
        records.add(new ExportRecord(2002, ProductType.DATES, 41890.0, 68621000, 1.64));
        records.add(new ExportRecord(2003, ProductType.DATES, 37079.0, 73921000, 1.99));
        records.add(new ExportRecord(2004, ProductType.DATES, 40432.0, 84382000, 2.09));
        records.add(new ExportRecord(2005, ProductType.DATES, 50163.0, 100771000, 2.01));
        records.add(new ExportRecord(2006, ProductType.DATES, 42764.0, 91563000, 2.14));
        records.add(new ExportRecord(2007, ProductType.DATES, 68856.0, 164759000, 2.39));

        // Olive oil data (2000-2007)
        records.add(new ExportRecord(2000, ProductType.OLIVE_OIL, 113863.0, 193023000, 1.70));
        records.add(new ExportRecord(2001, ProductType.OLIVE_OIL, 94529.0, 139201000, 1.47));
        records.add(new ExportRecord(2002, ProductType.OLIVE_OIL, 22502.0, 39268000, 1.75));
        records.add(new ExportRecord(2003, ProductType.OLIVE_OIL, 39876.0, 88793000, 2.23));
        records.add(new ExportRecord(2004, ProductType.OLIVE_OIL, 211175.0, 568625000, 2.69));
        records.add(new ExportRecord(2005, ProductType.OLIVE_OIL, 109371.0, 367929000, 3.36));
        records.add(new ExportRecord(2006, ProductType.OLIVE_OIL, 272805.0, 937854000, 3.44));
        records.add(new ExportRecord(2007, ProductType.OLIVE_OIL, 172613.0, 543502000, 3.15));

        // Oranges data (2000-2007)
        records.add(new ExportRecord(2000, ProductType.ORANGES, 22214.0, 7183000, 0.32));
        records.add(new ExportRecord(2001, ProductType.ORANGES, 24808.0, 8874000, 0.36));
        records.add(new ExportRecord(2002, ProductType.ORANGES, 22667.0, 8402000, 0.37));
        records.add(new ExportRecord(2003, ProductType.ORANGES, 17059.0, 9148000, 0.54));
        records.add(new ExportRecord(2004, ProductType.ORANGES, 17722.0, 11225000, 0.63));
        records.add(new ExportRecord(2005, ProductType.ORANGES, 18706.0, 11565000, 0.62));
        records.add(new ExportRecord(2006, ProductType.ORANGES, 18943.0, 10265000, 0.54));
        records.add(new ExportRecord(2007, ProductType.ORANGES, 15980.0, 10444000, 0.65));

        return records;
    }

    // New method: Get data for a specific product
    public List<ExportRecord> getProductData(List<ExportRecord> allRecords, ProductType productType) {
        List<ExportRecord> productRecords = new ArrayList<>();
        for (ExportRecord record : allRecords) {
            if (record.productType() == productType) {
                productRecords.add(record);
            }
        }
        // Sort by year
        productRecords.sort(Comparator.comparing(ExportRecord::year));
        return productRecords;
    }

    // New method: Get data for a specific year range
    public List<ExportRecord> getYearRangeData(List<ExportRecord> allRecords, int startYear, int endYear) {
        List<ExportRecord> yearRangeRecords = new ArrayList<>();
        for (ExportRecord record : allRecords) {
            if (record.year() >= startYear && record.year() <= endYear) {
                yearRangeRecords.add(record);
            }
        }
        return yearRangeRecords;
    }

    // New method: Get unique years in dataset
    public List<Integer> getUniqueYears(List<ExportRecord> records) {
        Set<Integer> years = new TreeSet<>();
        for (ExportRecord record : records) {
            years.add(record.year());
        }
        return new ArrayList<>(years);
    }

    // New method: Get price history for a product
    public Map<Integer, Double> getPriceHistory(List<ExportRecord> records, ProductType productType) {
        Map<Integer, Double> priceHistory = new TreeMap<>();
        for (ExportRecord record : records) {
            if (record.productType() == productType) {
                priceHistory.put(record.year(), record.unitPriceUSDPerKg());
            }
        }
        return priceHistory;
    }
}