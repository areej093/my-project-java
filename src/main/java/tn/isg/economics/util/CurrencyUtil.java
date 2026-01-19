package tn.isg.economics.util;

/**
 * Utility class for currency conversion between USD and Tunisian Dinar (TND)
 */
public class CurrencyUtil {
    // Current exchange rate (approximate)
    private static final double USD_TO_TND_RATE = 3.1;
    private static final String CURRENCY_SYMBOL = "TND";
    
    /**
     * Convert USD to Tunisian Dinar
     */
    public static double convertUsdToTnd(double usdAmount) {
        return usdAmount * USD_TO_TND_RATE;
    }
    
    /**
     * Convert Tunisian Dinar to USD
     */
    public static double convertTndToUsd(double tndAmount) {
        return tndAmount / USD_TO_TND_RATE;
    }
    
    /**
     * Format amount in TND with currency symbol
     */
    public static String formatTnd(double amount) {
        return String.format("%,.2f %s", amount, CURRENCY_SYMBOL);
    }
    
    /**
     * Format amount in USD with currency symbol
     */
    public static String formatUsd(double amount) {
        return String.format("%,.2f USD", amount);
    }
    
    /**
     * Format amount in both currencies
     */
    public static String formatBothCurrencies(double tndAmount) {
        double usdAmount = convertTndToUsd(tndAmount);
        return String.format("%,.2f TND (?%,.2f USD)", tndAmount, usdAmount);
    }
    
    /**
     * Get current exchange rate
     */
    public static double getExchangeRate() {
        return USD_TO_TND_RATE;
    }
    
    /**
     * Get currency symbol
     */
    public static String getCurrencySymbol() {
        return CURRENCY_SYMBOL;
    }
}
