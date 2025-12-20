package tn.isi.agriculture.model;

public enum ProductType {
    DATES("Dates", "Dattes"),
    OLIVE_OIL("Olive oil", "Huile d'olive"),
    ORANGES("Oranges", "Oranges"),
    OLIVES("Olives", "Olives"),
    OTHER_CITRUS("Other citrus fruit", "Autres agrumes");

    private final String englishName;
    private final String frenchName;

    ProductType(String englishName, String frenchName) {
        this.englishName = englishName;
        this.frenchName = frenchName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getFrenchName() {
        return frenchName;
    }

    public static ProductType fromString(String name) {
        for (ProductType type : ProductType.values()) {
            if (type.englishName.equalsIgnoreCase(name) ||
                    type.frenchName.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return DATES; // Default
    }

    @Override
    public String toString() {
        return englishName;
    }
}