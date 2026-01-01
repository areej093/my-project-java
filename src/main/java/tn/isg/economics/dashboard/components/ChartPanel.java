package tn.isg.economics.dashboard.components;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ChartPanel extends JPanel {
    private Map<String, Double> data;
    private String chartType;
    
    public ChartPanel(Map<String, Double> data, String chartType) {
        this.data = data;
        this.chartType = chartType;
        setPreferredSize(new Dimension(400, 300));
        setBorder(BorderFactory.createTitledBorder("Price Analysis"));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (data == null || data.isEmpty()) return;
        
        int width = getWidth();
        int height = getHeight();
        int padding = 40;
        int chartWidth = width - 2 * padding;
        int chartHeight = height - 2 * padding;
        
        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.drawLine(padding, height - padding, width - padding, height - padding);
        g2d.drawLine(padding, padding, padding, height - padding);
        
        // Draw bars for bar chart
        if ("bar".equals(chartType)) {
            int barCount = data.size();
            int barWidth = chartWidth / (barCount * 2);
            int barSpacing = barWidth;
            
            double maxValue = data.values().stream().max(Double::compare).orElse(100.0);
            
            int i = 0;
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                int barHeight = (int) ((entry.getValue() / maxValue) * chartHeight);
                int x = padding + i * (barWidth + barSpacing);
                int y = height - padding - barHeight;
                
                // Draw bar
                g2d.setColor(new Color(70, 130, 180));
                g2d.fillRect(x, y, barWidth, barHeight);
                
                // Draw label
                g2d.setColor(Color.BLACK);
                String label = entry.getKey();
                FontMetrics fm = g2d.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g2d.drawString(label, x + barWidth/2 - labelWidth/2, height - padding + 20);
                
                // Draw value
                String value = String.format("%.0f", entry.getValue());
                int valueWidth = fm.stringWidth(value);
                g2d.drawString(value, x + barWidth/2 - valueWidth/2, y - 5);
                
                i++;
            }
        }
    }
}
