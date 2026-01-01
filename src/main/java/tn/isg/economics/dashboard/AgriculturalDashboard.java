package tn.isg.economics.dashboard;

import tn.isg.economics.dashboard.components.ChartPanel;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class AgriculturalDashboard extends JFrame {
    
    public AgriculturalDashboard() {
        setTitle("Tunisian Agricultural Export AI Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // Header
        JPanel header = createHeader();
        mainPanel.add(header, BorderLayout.NORTH);
        
        // Center with simple content
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Left panel - Stats
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        JTextArea statsText = new JTextArea();
        statsText.setEditable(false);
        statsText.append("Tunisian Agricultural Exports\n");
        statsText.append("=============================\n\n");
        statsText.append("? Olive Oil: 3,814.68 USD/ton\n");
        statsText.append("? Dates: 2,563.68 USD/ton\n");
        statsText.append("? Wheat: 789.34 USD/ton\n");
        statsText.append("? Citrus: 1,282.67 USD/ton\n\n");
        statsText.append("AI Model Accuracy: 82.0%\n");
        statsText.append("Total Predictions: 50\n");
        statsPanel.add(new JScrollPane(statsText));
        
        // Right panel - Chart
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Price Analysis"));
        Map<String, Double> sampleData = new LinkedHashMap<>();
        sampleData.put("Olive Oil", 3814.68);
        sampleData.put("Dates", 2563.68);
        sampleData.put("Wheat", 789.34);
        sampleData.put("Citrus", 1282.67);
        
        ChartPanel chart = new ChartPanel(sampleData, "bar");
        chartPanel.add(chart);
        
        centerPanel.add(statsPanel);
        centerPanel.add(chartPanel);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.add(new JLabel("Status: Ready | " + LocalDate.now()));
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setPreferredSize(new Dimension(800, 60));
        
        JLabel title = new JLabel("Tunisian Agricultural Export AI System");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JButton runBtn = new JButton("Run AI Analysis");
        runBtn.setBackground(new Color(46, 204, 113));
        runBtn.setForeground(Color.WHITE);
        
        header.add(title, BorderLayout.WEST);
        header.add(runBtn, BorderLayout.EAST);
        
        return header;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AgriculturalDashboard dashboard = new AgriculturalDashboard();
            dashboard.setVisible(true);
        });
    }
}
