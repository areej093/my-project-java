package tn.isg.economics.dashboard.util;

import java.awt.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Concrete strategy for drawing line charts.
 * Demonstrates time-series visualization.
 */
public class LineChartStrategy implements ChartStrategy {

    private static final Color LINE_COLOR = new Color(46, 204, 113);
    private static final Color GRID_COLOR = new Color(240, 240, 240);
    private static final Color POINT_COLOR = new Color(231, 76, 60);

    @Override
    public void drawChart(Graphics2D g2d, Map<String, Double> data,
                          int width, int height, String title) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (!validateData(data)) {
            drawNoDataMessage(g2d, width, height);
            return;
        }

        // Set background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Draw chart border
        g2d.setColor(new Color(220, 220, 220));
        g2d.drawRect(0, 0, width - 1, height - 1);

        // Calculate dimensions
        int padding = 80;
        int chartWidth = width - 2 * padding;
        int chartHeight = height - 2 * padding;

        // Sort data by key for time series
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(data.entrySet());
        sortedEntries.sort(Map.Entry.comparingByKey());

        // Find min and max values
        double minValue = sortedEntries.stream()
                .mapToDouble(Map.Entry::getValue)
                .min().orElse(0);
        double maxValue = sortedEntries.stream()
                .mapToDouble(Map.Entry::getValue)
                .max().orElse(100.0);

        // Add margins
        double range = maxValue - minValue;
        minValue = Math.max(0, minValue - range * 0.1);
        maxValue = maxValue + range * 0.1;

        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics titleMetrics = g2d.getFontMetrics();
        int titleWidth = titleMetrics.stringWidth(title);
        g2d.drawString(title, width/2 - titleWidth/2, 30);

        // Draw grid
        drawGrid(g2d, padding, width, height, chartWidth, chartHeight,
                minValue, maxValue, sortedEntries.size());

        // Draw line and points
        if (sortedEntries.size() > 1) {
            drawLine(g2d, sortedEntries, padding, chartWidth, chartHeight,
                    height, minValue, maxValue);
        }

        // Draw axis labels
        drawAxisLabels(g2d, sortedEntries, padding, width, height, chartHeight);
    }

    private void drawGrid(Graphics2D g2d, int padding, int width, int height,
                          int chartWidth, int chartHeight, double minValue,
                          double maxValue, int numPoints) {
        // Draw vertical grid lines
        g2d.setColor(GRID_COLOR);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{5}, 0));

        if (numPoints > 1) {
            int pointSpacing = chartWidth / (numPoints - 1);
            for (int i = 0; i < numPoints; i++) {
                int x = padding + i * pointSpacing;
                g2d.drawLine(x, padding, x, height - padding);
            }
        }

        // Draw horizontal grid lines
        int numHLines = 6;
        for (int i = 0; i <= numHLines; i++) {
            int y = height - padding - (i * chartHeight / numHLines);
            g2d.drawLine(padding, y, width - padding, y);
        }

        // Reset stroke
        g2d.setStroke(new BasicStroke(2));
    }

    private void drawLine(Graphics2D g2d, List<Map.Entry<String, Double>> entries,
                          int padding, int chartWidth, int chartHeight,
                          int height, double minValue, double maxValue) {
        // Calculate point coordinates
        List<Point> points = new ArrayList<>();
        double valueRange = maxValue - minValue;
        int numPoints = entries.size();

        for (int i = 0; i < numPoints; i++) {
            Map.Entry<String, Double> entry = entries.get(i);
            double value = entry.getValue();

            int x = padding + (i * chartWidth / (numPoints - 1));
            int y = height - padding -
                    (int)(((value - minValue) / valueRange) * chartHeight);

            points.add(new Point(x, y));
        }

        // Draw connecting lines
        g2d.setColor(LINE_COLOR);
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // Draw data points
        g2d.setColor(POINT_COLOR);
        g2d.setStroke(new BasicStroke(1));

        for (Point p : points) {
            g2d.fillOval(p.x - 4, p.y - 4, 8, 8);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawOval(p.x - 4, p.y - 4, 8, 8);
            g2d.setColor(POINT_COLOR);
        }
    }

    private void drawAxisLabels(Graphics2D g2d, List<Map.Entry<String, Double>> entries,
                                int padding, int width, int height, int chartHeight) {
        // Y-axis labels
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));

        // Find max value for Y-axis labels
        double maxValue = entries.stream()
                .mapToDouble(Map.Entry::getValue)
                .max().orElse(100.0);

        int numTicks = 5;
        for (int i = 0; i <= numTicks; i++) {
            double value = maxValue * i / numTicks;
            int y = height - padding - (int)((value / maxValue) * chartHeight);

            String label = String.format("$%,.0f", value);
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g2d.drawString(label, padding - labelWidth - 8, y + 4);
        }

        // X-axis labels (time points)
        int numPoints = entries.size();
        if (numPoints > 0) {
            int labelSpacing = Math.max(1, (width - 2 * padding) / numPoints);

            for (int i = 0; i < numPoints; i++) {
                String label = entries.get(i).getKey();
                if (label.length() > 8) {
                    label = label.substring(0, 6) + "..";
                }

                int x = padding + i * labelSpacing;
                FontMetrics fm = g2d.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g2d.drawString(label, x - labelWidth/2, height - padding + 20);
            }
        }

        // Axis titles
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Price ($/ton)", 10, height / 2);
        g2d.drawString("Time Period", width / 2 - 30, height - 20);
    }

    private void drawNoDataMessage(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String message = "No time-series data available";
        FontMetrics fm = g2d.getFontMetrics();
        int messageWidth = fm.stringWidth(message);
        g2d.drawString(message, width/2 - messageWidth/2, height/2);
    }
}