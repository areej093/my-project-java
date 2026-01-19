package tn.isg.economics.dashboard.util;

import java.awt.Graphics2D;
import java.util.Map;

/**
 * Functional interface for chart drawing strategies.
 * Demonstrates Strategy pattern for different visualization types.
 *
 * @FunctionalInterface ensures only one abstract method.
 */
@FunctionalInterface
public interface ChartStrategy {

    /**
     * Draw a chart using the specified strategy.
     *
     * @param g2d Graphics2D context for drawing
     * @param data Data to visualize (key-value pairs)
     * @param width Chart width in pixels
     * @param height Chart height in pixels
     * @param title Chart title
     */
    void drawChart(Graphics2D g2d, Map<String, Double> data,
                   int width, int height, String title);

    /**
     * Default method to validate data before drawing.
     * Demonstrates default methods in functional interfaces.
     */
    default boolean validateData(Map<String, Double> data) {
        return data != null && !data.isEmpty() &&
                data.values().stream().allMatch(value -> !value.isNaN() && !value.isInfinite());
    }

    /**
     * Default method to get chart type name.
     */
    default String getChartType() {
        return this.getClass().getSimpleName().replace("ChartStrategy", "");
    }
}