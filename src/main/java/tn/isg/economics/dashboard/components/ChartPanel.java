package tn.isg.economics.dashboard.components;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class ChartPanel extends JPanel {
    private Map<String, Double> data;
    private String chartType;
    private String title = "Chart";
    private Color[] colors = {
        new Color(70, 130, 180),    // Steel blue
        new Color(46, 204, 113),    // Emerald
        new Color(155, 89, 182),    // Amethyst
        new Color(241, 196, 15),    // Sunflower
        new Color(230, 126, 34),    // Carrot
        new Color(231, 76, 60)      // Alizarin
    };
    
    public ChartPanel(Map<String, Double> data, String chartType) {
        this.data = data;
        this.chartType = chartType;
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.WHITE);
    }
    
    public void setTitle(String title) {
        this.title = title;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (data == null || data.isEmpty()) {
            drawNoData(g2d);
            return;
        }
        
        // Draw background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw border
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        
        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, getWidth()/2 - titleWidth/2, 20);
        
        // Draw chart based on type
        switch (chartType.toLowerCase()) {
            case "bar":
                drawBarChart(g2d);
                break;
            case "line":
                drawLineChart(g2d);
                break;
            case "pie":
                drawPieChart(g2d);
                break;
            case "area":
                drawAreaChart(g2d);
                break;
            default:
                drawBarChart(g2d);
        }
    }
    
    private void drawBarChart(Graphics2D g2d) {
        int padding = 60;
        int chartWidth = getWidth() - 2 * padding;
        int chartHeight = getHeight() - 2 * padding;
        
        List<Map.Entry<String, Double>> entries = new ArrayList<>(data.entrySet());
        int barCount = entries.size();
        int barWidth = Math.max(20, chartWidth / (barCount * 2));
        int barSpacing = barWidth;
        
        // Find max value
        double maxValue = entries.stream()
            .mapToDouble(Map.Entry::getValue)
            .max()
            .orElse(100.0);
        maxValue *= 1.1; // Add 10% headroom
        
        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding);
        g2d.drawLine(padding, padding, padding, getHeight() - padding);
        
        // Draw Y-axis labels
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        DecimalFormat df = new DecimalFormat("#,##0");
        
        int numTicks = 5;
        for (int i = 0; i <= numTicks; i++) {
            double value = maxValue * i / numTicks;
            int y = getHeight() - padding - (int)((value / maxValue) * chartHeight);
            
            // Tick mark
            g2d.drawLine(padding - 5, y, padding, y);
            
            // Label
            String label = df.format(value);
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g2d.drawString(label, padding - labelWidth - 8, y + 4);
        }
        
        // Draw bars
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Double> entry = entries.get(i);
            double value = entry.getValue();
            int barHeight = (int)((value / maxValue) * chartHeight);
            int x = padding + i * (barWidth + barSpacing);
            int y = getHeight() - padding - barHeight;
            
            // Draw bar with gradient
            Color barColor = colors[i % colors.length];
            GradientPaint gradient = new GradientPaint(
                x, y, barColor.darker(),
                x, y + barHeight, barColor.brighter()
            );
            g2d.setPaint(gradient);
            g2d.fillRect(x, y, barWidth, barHeight);
            
            // Draw bar border
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(x, y, barWidth, barHeight);
            
            // Draw value on top
            g2d.setColor(Color.BLACK);
            String valueStr = df.format(value);
            int valueWidth = g2d.getFontMetrics().stringWidth(valueStr);
            g2d.drawString(valueStr, x + barWidth/2 - valueWidth/2, y - 5);
            
            // Draw label
            String label = entry.getKey();
            if (label.length() > 8) label = label.substring(0, 7) + ".";
            int labelWidth = g2d.getFontMetrics().stringWidth(label);
            g2d.drawString(label, x + barWidth/2 - labelWidth/2, getHeight() - padding + 15);
        }
    }
    
    private void drawLineChart(Graphics2D g2d) {
        int padding = 60;
        int chartWidth = getWidth() - 2 * padding;
        int chartHeight = getHeight() - 2 * padding;
        
        List<Map.Entry<String, Double>> entries = new ArrayList<>(data.entrySet());
        if (entries.size() < 2) return;
        
        // Find min and max values
        double minValue = entries.stream()
            .mapToDouble(Map.Entry::getValue)
            .min()
            .orElse(0);
        double maxValue = entries.stream()
            .mapToDouble(Map.Entry::getValue)
            .max()
            .orElse(100.0);
        double range = maxValue - minValue;
        minValue = Math.max(0, minValue - range * 0.1);
        maxValue = maxValue + range * 0.1;
        
        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding);
        g2d.drawLine(padding, padding, padding, getHeight() - padding);
        
        // Calculate points
        java.awt.Point[] points = new java.awt.Point[entries.size()];
        for (int i = 0; i < entries.size(); i++) {
            double value = entries.get(i).getValue();
            int x = padding + (i * chartWidth / (entries.size() - 1));
            int y = getHeight() - padding - (int)(((value - minValue) / (maxValue - minValue)) * chartHeight);
            points[i] = new java.awt.Point(x, y);
        }
        
        // Draw line
        g2d.setColor(new Color(52, 152, 219));
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < points.length - 1; i++) {
            g2d.drawLine(points[i].x, points[i].y, points[i+1].x, points[i+1].y);
        }
        
        // Draw points
        g2d.setColor(new Color(231, 76, 60));
        g2d.setStroke(new BasicStroke(1));
        for (java.awt.Point point : points) {
            g2d.fillOval(point.x - 4, point.y - 4, 8, 8);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawOval(point.x - 4, point.y - 4, 8, 8);
            g2d.setColor(new Color(231, 76, 60));
        }
        
        // Draw labels
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i < entries.size(); i++) {
            String label = entries.get(i).getKey();
            if (label.length() > 6) label = label.substring(0, 5) + ".";
            int labelWidth = g2d.getFontMetrics().stringWidth(label);
            g2d.drawString(label, points[i].x - labelWidth/2, getHeight() - padding + 15);
        }
    }
    
    private void drawPieChart(Graphics2D g2d) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 3;
        
        List<Map.Entry<String, Double>> entries = new ArrayList<>(data.entrySet());
        double total = entries.stream().mapToDouble(Map.Entry::getValue).sum();
        
        // Draw pie slices
        int startAngle = 0;
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Double> entry = entries.get(i);
            double value = entry.getValue();
            int arcAngle = (int)Math.round((value / total) * 360);
            
            // Draw slice
            g2d.setColor(colors[i % colors.length]);
            g2d.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 
                       startAngle, arcAngle);
            
            // Draw slice border
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 
                       startAngle, arcAngle);
            
            startAngle += arcAngle;
        }
        
        // Draw legend
        drawLegend(g2d, entries, total);
    }
    
    private void drawAreaChart(Graphics2D g2d) {
        int padding = 60;
        int chartWidth = getWidth() - 2 * padding;
        int chartHeight = getHeight() - 2 * padding;
        
        List<Map.Entry<String, Double>> entries = new ArrayList<>(data.entrySet());
        if (entries.size() < 2) return;
        
        // Find max value
        double maxValue = entries.stream()
            .mapToDouble(Map.Entry::getValue)
            .max()
            .orElse(100.0);
        maxValue *= 1.1;
        
        // Calculate polygon points
        java.awt.Polygon polygon = new java.awt.Polygon();
        polygon.addPoint(padding, getHeight() - padding); // Start at bottom-left
        
        for (int i = 0; i < entries.size(); i++) {
            double value = entries.get(i).getValue();
            int x = padding + (i * chartWidth / (entries.size() - 1));
            int y = getHeight() - padding - (int)((value / maxValue) * chartHeight);
            polygon.addPoint(x, y);
        }
        
        polygon.addPoint(getWidth() - padding, getHeight() - padding); // End at bottom-right
        
        // Draw area with gradient
        Color areaColor = new Color(52, 152, 219, 150);
        GradientPaint gradient = new GradientPaint(
            padding, getHeight() - padding, areaColor.darker(),
            padding, padding, areaColor.brighter()
        );
        g2d.setPaint(gradient);
        g2d.fillPolygon(polygon);
        
        // Draw line on top
        g2d.setColor(new Color(52, 152, 219));
        g2d.setStroke(new BasicStroke(2));
        for (int i = 1; i < polygon.npoints - 1; i++) {
            if (i > 1) {
                g2d.drawLine(polygon.xpoints[i-1], polygon.ypoints[i-1], 
                            polygon.xpoints[i], polygon.ypoints[i]);
            }
        }
    }
    
    private void drawLegend(Graphics2D g2d, List<Map.Entry<String, Double>> entries, double total) {
        int legendX = getWidth() - 150;
        int legendY = 50;
        
        // Legend background
        g2d.setColor(new Color(240, 240, 240, 200));
        g2d.fillRoundRect(legendX - 10, legendY - 20, 140, entries.size() * 20 + 30, 10, 10);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRoundRect(legendX - 10, legendY - 20, 140, entries.size() * 20 + 30, 10, 10);
        
        // Legend title
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Legend", legendX, legendY);
        
        // Legend items
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Double> entry = entries.get(i);
            double percentage = (entry.getValue() / total) * 100;
            
            // Color box
            g2d.setColor(colors[i % colors.length]);
            g2d.fillRect(legendX, legendY + 15 + i * 20, 12, 12);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(legendX, legendY + 15 + i * 20, 12, 12);
            
            // Text
            String text = String.format("%s: %.1f%%", entry.getKey(), percentage);
            g2d.setColor(Color.BLACK);
            g2d.drawString(text, legendX + 20, legendY + 25 + i * 20);
        }
    }
    
    private void drawNoData(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String message = "No data available";
        FontMetrics fm = g2d.getFontMetrics();
        int messageWidth = fm.stringWidth(message);
        g2d.drawString(message, getWidth()/2 - messageWidth/2, getHeight()/2);
    }
}
