package tn.isg.economics.dashboard.util;

import java.awt.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Concrete strategy for drawing pie charts.
 * Demonstrates percentage-based visualization.
 */
public class PieChartStrategy implements ChartStrategy {

    private static final Color[] PIE_COLORS = {
            new Color(70, 130, 180),    // Steel blue
            new Color(46, 204, 113),    // Emerald
            new Color(155, 89, 182),    // Amethyst
            new Color(241, 196, 15),    // Sunflower
            new Color(230, 126, 34),    // Carrot
            new Color(231, 76, 60),     // Alizarin
            new Color(52, 152, 219),    // Peter River
            new Color(149, 165, 166)    // Concrete
    };

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

        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics titleMetrics = g2d.getFontMetrics();
        int titleWidth = titleMetrics.stringWidth(title);
        g2d.drawString(title, width/2 - titleWidth/2, 30);

        // Calculate pie chart dimensions
        int centerX = width / 2;
        int centerY = height / 2 + 20;
        int radius = Math.min(width, height) / 3;

        // Calculate total value
        double total = data.values().stream().mapToDouble(Double::doubleValue).sum();

        // Sort data by value
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(data.entrySet());
        sortedEntries.sort(Map.Entry.<String, Double>comparingByValue().reversed());

        // Draw pie slices
        int startAngle = 0;
        for (int i = 0; i < sortedEntries.size(); i++) {
            Map.Entry<String, Double> entry = sortedEntries.get(i);
            double value = entry.getValue();
            double percentage = (value / total) * 100;

            // Calculate arc angle
            int arcAngle = (int) Math.round((value / total) * 360);

            // Draw pie slice
            Color sliceColor = PIE_COLORS[i % PIE_COLORS.length];
            g2d.setColor(sliceColor);
            g2d.fillArc(centerX - radius, centerY - radius,
                    radius * 2, radius * 2,
                    startAngle, arcAngle);

            // Draw slice border
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawArc(centerX - radius, centerY - radius,
                    radius * 2, radius * 2,
                    startAngle, arcAngle);

            // Draw percentage label
            if (arcAngle > 15) { // Only draw label if slice is large enough
                drawPercentageLabel(g2d, centerX, centerY, radius,
                        startAngle, arcAngle, percentage);
            }

            startAngle += arcAngle;
        }

        // Draw center circle (donut effect)
        g2d.setColor(Color.WHITE);
        g2d.fillOval(centerX - radius/3, centerY - radius/3,
                radius * 2/3, radius * 2/3);

        // Draw total value in center
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String totalText = String.format("Total:\n$%,.0f", total);
        drawCenteredString(g2d, totalText, centerX, centerY);

        // Draw legend
        drawLegend(g2d, sortedEntries, total, width, height);
    }

    private void drawPercentageLabel(Graphics2D g2d, int centerX, int centerY,
                                     int radius, int startAngle, int arcAngle,
                                     double percentage) {
        // Calculate label position
        double midAngle = Math.toRadians(startAngle + arcAngle / 2.0);
        int labelRadius = radius * 3 / 4;

        int labelX = centerX + (int)(labelRadius * Math.cos(midAngle));
        int labelY = centerY - (int)(labelRadius * Math.sin(midAngle));

        // Draw percentage
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        String label = String.format("%.1f%%", percentage);
        FontMetrics fm = g2d.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        g2d.drawString(label, labelX - labelWidth/2, labelY + 4);
    }

    private void drawLegend(Graphics2D g2d, List<Map.Entry<String, Double>> entries,
                            double total, int width, int height) {
        int legendX = width - 200;
        int legendY = 60;
        int entryHeight = 20;

        // Draw legend background
        g2d.setColor(new Color(240, 240, 240, 200));
        g2d.fillRoundRect(legendX - 10, legendY - 20, 190,
                entries.size() * entryHeight + 30, 10, 10);

        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRoundRect(legendX - 10, legendY - 20, 190,
                entries.size() * entryHeight + 30, 10, 10);

        // Draw legend title
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Product Distribution", legendX, legendY);

        // Draw legend entries
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Double> entry = entries.get(i);
            double value = entry.getValue();
            double percentage = (value / total) * 100;
            Color sliceColor = PIE_COLORS[i % PIE_COLORS.length];

            // Color box
            g2d.setColor(sliceColor);
            g2d.fillRect(legendX, legendY + 15 + i * entryHeight, 12, 12);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(legendX, legendY + 15 + i * entryHeight, 12, 12);

            // Entry text
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            String legendText = String.format("%s: $%,.0f (%.1f%%)",
                    entry.getKey(), value, percentage);
            g2d.drawString(legendText, legendX + 20, legendY + 25 + i * entryHeight);
        }
    }

    private void drawCenteredString(Graphics2D g2d, String text, int centerX, int centerY) {
        FontMetrics fm = g2d.getFontMetrics();
        String[] lines = text.split("\n");

        int totalHeight = fm.getHeight() * lines.length;
        int startY = centerY - totalHeight / 2 + fm.getAscent();

        for (String line : lines) {
            int lineWidth = fm.stringWidth(line);
            g2d.drawString(line, centerX - lineWidth / 2, startY);
            startY += fm.getHeight();
        }
    }

    private void drawNoDataMessage(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String message = "No distribution data available";
        FontMetrics fm = g2d.getFontMetrics();
        int messageWidth = fm.stringWidth(message);
        g2d.drawString(message, width/2 - messageWidth/2, height/2);
    }
}