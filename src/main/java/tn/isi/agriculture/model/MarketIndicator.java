package tn.isi.agriculture.model;

public enum MarketIndicator {
    STABLE("Stable", "Stable"),
    VOLATILE("Volatile", "Volatile"),
    RISING("Rising", "En hausse"),
    FALLING("Falling", "En baisse"),
    UNPREDICTABLE("Unpredictable", "Imprévisible");

    private final String englishName;
    private final String frenchName;

    MarketIndicator(String englishName, String frenchName) {
        this.englishName = englishName;
        this.frenchName = frenchName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getFrenchName() {
        return frenchName;
    }

    public static MarketIndicator fromPriceTrend(double trendPercentage) {
        if (Math.abs(trendPercentage) < 0.05) { // Less than 5% change
            return STABLE;
        } else if (trendPercentage > 0.15) { // More than 15% increase
            return RISING;
        } else if (trendPercentage < -0.15) { // More than 15% decrease
            return FALLING;
        } else if (Math.abs(trendPercentage) > 0.05) { // 5-15% change
            return VOLATILE;
        } else {
            return UNPREDICTABLE;
        }
    }
}