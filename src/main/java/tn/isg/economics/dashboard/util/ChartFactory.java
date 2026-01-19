package tn.isg.economics.dashboard.util;

/**
 * Factory class for creating chart strategies.
 * Demonstrates Factory pattern for object creation.
 */
public class ChartFactory {

    /**
     * Create a chart strategy based on the specified type.
     *
     * @param chartType Type of chart (BAR, LINE, PIE)
     * @return ChartStrategy implementation
     * @throws IllegalArgumentException if chart type is not supported
     */
    public static ChartStrategy createChartStrategy(String chartType) {
        if (chartType == null) {
            throw new IllegalArgumentException("Chart type cannot be null");
        }

        switch (chartType.toUpperCase()) {
            case "BAR":
                return new BarChartStrategy();
            case "LINE":
                return new LineChartStrategy();
            case "PIE":
                return new PieChartStrategy();
            default:
                throw new IllegalArgumentException(
                        String.format("Unsupported chart type: %s. Supported types: BAR, LINE, PIE",
                                chartType)
                );
        }
    }

    /**
     * Get list of supported chart types.
     *
     * @return Array of supported chart type names
     */
    public static String[] getSupportedChartTypes() {
        return new String[]{"BAR", "LINE", "PIE"};
    }

    /**
     * Check if a chart type is supported.
     *
     * @param chartType Chart type to check
     * @return true if supported, false otherwise
     */
    public static boolean isChartTypeSupported(String chartType) {
        if (chartType == null) return false;

        String typeUpper = chartType.toUpperCase();
        for (String supportedType : getSupportedChartTypes()) {
            if (supportedType.equals(typeUpper)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get default chart strategy.
     *
     * @return Default chart strategy (BarChartStrategy)
     */
    public static ChartStrategy getDefaultChartStrategy() {
        return new BarChartStrategy();
    }
}