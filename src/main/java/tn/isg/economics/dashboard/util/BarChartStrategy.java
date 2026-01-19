package tn.isg.economics.dashboard.util;

import java.awt.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Concrete strategy for drawing bar charts.
 * Demonstrates Strategy pattern implementation.
 */
public class BarChartStrategy implements ChartStrategy {

    // Color palette for bars
    private static final Color[] BAR_COLORS = {
            new Color(70, 130, 180),    // Steel blue
            new Color(46, 204, 113),    // Emerald
            new Color(155, 89, 182),    // Amethyst
            new Color(241, 196, 15),    // Sunflower
            new Color(230, 126, 34),    // Carrot
            new Color(231, 76, 60)      // Alizarin
    };

    @Override
    public void drawChart(Graphics2D g2d, Map<String, Double> data,
                          int width, int height, String title) {
        // Enable anti-aliasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Validate data
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

        // Calculate chart dimensions
        int padding = 80;
        int chartWidth = width - 2 * padding;
        int chartHeight = height - 2 * padding;
        int barCount = data.size();

        // Sort data by value for better visualization
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(data.entrySet());
        sortedEntries.sort(Map.Entry.<String, Double>comparingByValue().reversed());

        // Calculate bar dimensions
        int barWidth = Math.max(30, chartWidth / (barCount * 2));
        int barSpacing = barWidth;

        // Find max value for scaling
        double maxValue = sortedEntries.stream()
                .map(Map.Entry::getValue)
                .max(Double::compare)
                .orElse(100.0);

        // Add 10% headroom
        maxValue *= 1.1;

        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics titleMetrics = g2d.getFontMetrics();
        int titleWidth = titleMetrics.stringWidth(title);
        g2d.drawString(title, width/2 - titleWidth/2, 30);

        // Draw axes
        drawAxes(g2d, padding, width, height, chartHeight, maxValue);

        // Draw bars
        for (int i = 0; i < sortedEntries.size(); i++) {
            Map.Entry<String, Double> entry = sortedEntries.get(i);
            drawBar(g2d, entry, i, barCount, barWidth, barSpacing,
                    padding, height, chartHeight, maxValue);
        }

        // Draw legend
        drawLegend(g2d, sortedEntries, width, height);
    }

    private void drawAxes(Graphics2D g2d, int padding, int width, int height,
                          int chartHeight, double maxValue) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        // X-axis
        g2d.drawLine(padding, height - padding, width - padding, height - padding);
        // Y-axis
        g2d.drawLine(padding, padding, padding, height - padding);

        // Y-axis labels
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        int numTicks = 5;
        for (int i = 0; i <= numTicks; i++) {
            double value = maxValue * i / numTicks;
            int y = height - padding - (int)((value / maxValue) * chartHeight);

            // Tick mark
            g2d.drawLine(padding - 5, y, padding, y);

            // Label
            String label = String.format("$%,.0f", value);
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g2d.drawString(label, padding - labelWidth - 8, y + 4);
        }

        // Axis titles
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Price ($/ton)", 10, height / 2);
        g2d.drawString("Products", width / 2 - 30, height - 20);
    }

    private void drawBar(Graphics2D g2d, Map.Entry<String, Double> entry, int index,
                         int barCount, int barWidth, int barSpacing, int padding,
                         int height, int chartHeight, double maxValue) {
        double value = entry.getValue();
        int barHeight = (int) ((value / maxValue) * chartHeight);
        int x = padding + index * (barWidth + barSpacing);
        int y = height - padding - barHeight;

        // Create gradient fill
        Color barColor = BAR_COLORS[index % BAR_COLORS.length];
        GradientPaint gradient = new GradientPaint(
                x, y, barColor.darker(),
                x, y + barHeight, barColor.brighter()
        );

        // Draw bar
        g2d.setPaint(gradient);
        g2d.fillRect(x, y, barWidth, barHeight);

        // Draw bar border
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect(x, y, barWidth, barHeight);

        // Draw value label on top
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        String valueLabel = String.format("$%,.0f", value);
        FontMetrics fm = g2d.getFontMetrics();
        int valueWidth = fm.stringWidth(valueLabel);
        g2d.drawString(valueLabel, x + barWidth/2 - valueWidth/2, y - 5);

        // Draw product label
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        String productLabel = entry.getKey();
        if (productLabel.length() > 10) {
            productLabel = productLabel.substring(0, 8) + "..";
        }
        int labelWidth = fm.stringWidth(productLabel);
        g2d.drawString(productLabel, x + barWidth/2 - labelWidth/2, height - padding + 15);
    }

    private void drawLegend(Graphics2D g2d, List<Map.Entry<String, Double>> entries,
                            int width, int height) {
        int legendX = width - 150;
        int legendY = 50;

        g2d.setColor(new Color(240, 240, 240, 200));
        g2d.fillRoundRect(legendX - 10, legendY - 20, 140,
                entries.size() * 20 + 30, 10, 10);

        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRoundRect(legendX - 10, legendY - 20, 140,
                entries.size() * 20 + 30, 10, 10);

        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Legend", legendX, legendY);

        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Double> entry = entries.get(i);
            Color barColor = BAR_COLORS[i % BAR_COLORS.length];

            // Legend color box
            g2d.setColor(barColor);
            g2d.fillRect(legendX, legendY + 15 + i * 20, 12, 12);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(legendX, legendY + 15 + i * 20, 12, 12);

            // Legend text
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            String legendText = String.format("%s: $%,.0f",
                    entry.getKey(), entry.getValue());
            g2d.drawString(legendText, legendX + 20, legendY + 25 + i * 20);
        }
    }

    private void drawNoDataMessage(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String message = "No data available for chart";
        FontMetrics fm = g2d.getFontMetrics();
        int messageWidth = fm.stringWidth(message);
        g2d.drawString(message, width/2 - messageWidth/2, height/2);
    }
}