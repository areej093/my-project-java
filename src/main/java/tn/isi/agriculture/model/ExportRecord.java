package tn.isi.agriculture.model;

public record ExportRecord(
        int year,
        ProductType productType,  // Changed from String to ProductType
        double quantityTons,
        double valueUSD,
        double unitPriceUSDPerKg
) {
    @Override
    public String toString() {
        return String.format("%d | %-20s | %8.1f tons | $%11.0f | $%.2f/kg",
                year, productType.getEnglishName(), quantityTons, valueUSD, unitPriceUSDPerKg);
    }

    // Helper method to get product name
    public String getProductName() {
        return productType.getEnglishName();
    }
}