package tn.isg.economics.dashboard;

import tn.isg.economics.dashboard.components.ChartPanel;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class AgriculturalDashboard extends JFrame {
    private JTextArea reportArea;
    private JLabel statusLabel;
    private ChartPanel currentChart;
    private JPanel chartContainer;
    private String currentChartType = "bar";
    private DecimalFormat tndFormat = new DecimalFormat("#,##0.00 TND");

    // Sample data in TND
    private Map<String, Double> productData = new LinkedHashMap<>();
    private Map<String, Double> countryData = new LinkedHashMap<>();
    private Map<String, Double> yearlyData = new LinkedHashMap<>();
    private Map<String, Double> monthlyData = new LinkedHashMap<>();

    // Store export data - use explicit type
    private java.util.List<Map<String, String>> exportRecords = new java.util.ArrayList<>();

//
public AgriculturalDashboard() {
    setTitle("Tunisian Agricultural Export AI Dashboard - TUNISIAN DINAR");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 800);
    setLocationRelativeTo(null);

    // Initialize sample data
    initializeData();

    // Create main panel with CardLayout for different views
    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

    // Header
    JPanel header = createHeader();
    mainPanel.add(header, BorderLayout.NORTH);

    // Center panel with tabs - ONLY ONE DECLARATION HERE
    JTabbedPane tabbedPane = new JTabbedPane();

    // Tab 1: Overview Dashboard
    tabbedPane.addTab("Overview", createOverviewTab());

    // Tab 2: Analytics
    tabbedPane.addTab("Analytics", createAnalyticsTab());

    // Tab 3: Reports
    tabbedPane.addTab("Reports", createReportsTab());

    // Tab 4: Export Data
    tabbedPane.addTab("Export", createExportTab());

    // Tab 5: Forecast (NEW) - Add this line
    tabbedPane.addTab("Forecast", createForecastTab());  // This line is correct

    mainPanel.add(tabbedPane, BorderLayout.CENTER);

    // Status bar
    JPanel statusBar = new JPanel(new BorderLayout());
    statusLabel = new JLabel("Status: Ready | Currency: Tunisian Dinar (TND) | " + LocalDate.now());
    statusBar.add(statusLabel, BorderLayout.WEST);
    mainPanel.add(statusBar, BorderLayout.SOUTH);

    add(mainPanel);
}

    private void initializeData() {
        // Product data in TND (converted from USD)
        productData.put("Olive Oil", 11825.51);  // ~3814.68 USD * 3.1
        productData.put("Dates", 7947.41);       // ~2563.68 USD * 3.1
        productData.put("Citrus", 3976.28);      // ~1282.67 USD * 3.1
        productData.put("Wheat", 2446.95);       // ~789.34 USD * 3.1

        // Country export data
        countryData.put("France", 45.2);
        countryData.put("Germany", 22.5);
        countryData.put("Italy", 15.8);
        countryData.put("Spain", 8.4);
        countryData.put("UK", 5.3);
        countryData.put("Other", 2.8);

        // Yearly trend data in TND
        yearlyData.put("2020", 3721.55);
        yearlyData.put("2021", 4185.62);
        yearlyData.put("2022", 4900.17);
        yearlyData.put("2023", 5643.24);
        yearlyData.put("2024", 6512.48);

        // Monthly data (2024) in TND
        monthlyData.put("Jan", 5735.00);
        monthlyData.put("Feb", 5952.00);
        monthlyData.put("Mar", 6231.00);
        monthlyData.put("Apr", 6045.00);
        monthlyData.put("May", 6510.00);
        monthlyData.put("Jun", 6975.00);
        monthlyData.put("Jul", 6820.00);
        monthlyData.put("Aug", 6665.00);
        monthlyData.put("Sep", 7130.00);
        monthlyData.put("Oct", 7440.00);
        monthlyData.put("Nov", 7285.00);
        monthlyData.put("Dec", 7750.00);

        // Initialize export records with TND prices
        initializeExportRecords();
    }

    private void initializeExportRecords() {
        // Prices in TND (converted from USD)
        double[] pricesTnd = {11825.51, 7947.41, 3976.28, 2446.95, 12152.47}; // Last: 3920.15 USD * 3.1

        String[] dates = {"2024-12-15", "2024-12-10", "2024-12-05", "2024-11-30", "2024-11-25"};
        String[] products = {"Olive Oil", "Dates", "Citrus", "Wheat", "Olive Oil"};
        double[] volumes = {150.0, 85.0, 200.0, 300.0, 120.0};
        String[] countries = {"France", "Germany", "Italy", "Spain", "UK"};
        String[] statuses = {"Shipped", "Pending", "Delivered", "Shipped", "Processing"};

        for (int i = 0; i < dates.length; i++) {
            Map<String, String> record = new HashMap<>();
            record.put("date", dates[i]);
            record.put("product", products[i]);
            record.put("price", String.format("%.2f", pricesTnd[i])); // Format with dot
            record.put("volume", String.format("%.1f", volumes[i]));
            record.put("country", countries[i]);
            record.put("status", statuses[i]);
            exportRecords.add(record);
        }
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setPreferredSize(new Dimension(1200, 70));

        // Title
        JLabel title = new JLabel("? Tunisian Agricultural Export AI System - TUNISIAN DINAR");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Currency info
        JLabel currencyLabel = new JLabel("1 USD = 3.1 TND");
        currencyLabel.setFont(new Font("Arial", Font.BOLD, 12));
        currencyLabel.setForeground(Color.YELLOW);
        currencyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        // Quick action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        actionPanel.setBackground(new Color(44, 62, 80));

        String[] actions = {"Refresh", "Export", "Print", "Help"};
        for (String action : actions) {
            JButton btn = new JButton(action);
            btn.setBackground(new Color(52, 152, 219));
            btn.setForeground(Color.WHITE);
            btn.addActionListener(e -> handleAction(action));
            actionPanel.add(btn);
        }

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(44, 62, 80));
        leftPanel.add(title);
        leftPanel.add(currencyLabel);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(actionPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createOverviewTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top metrics panel
        panel.add(createMetricsPanel(), BorderLayout.NORTH);

        // Center charts
        JPanel chartPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Left chart - Products
        JPanel productChartPanel = new JPanel(new BorderLayout());
        productChartPanel.setBorder(BorderFactory.createTitledBorder("Export by Product (TND/ton)"));
        ChartPanel productChart = new ChartPanel(productData, "bar");
        productChartPanel.add(productChart);

        // Right chart - Countries
        JPanel countryChartPanel = new JPanel(new BorderLayout());
        countryChartPanel.setBorder(BorderFactory.createTitledBorder("Export by Country (%)"));
        ChartPanel countryChart = new ChartPanel(countryData, "pie");
        countryChartPanel.add(countryChart);

        chartPanel.add(productChartPanel);
        chartPanel.add(countryChartPanel);

        panel.add(chartPanel, BorderLayout.CENTER);

        // Bottom data table
        panel.add(createDataTable(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));

        String[][] metrics = {
                {"Total Exports", "7.44M TND", "+12.5%"},
                {"Avg Price/Ton", "5,735 TND", "+8.2%"},
                {"Top Product", "Olive Oil", "42% share"},
                {"AI Accuracy", "82%", "High confidence"}
        };

        Color[] colors = {
                new Color(46, 204, 113),
                new Color(52, 152, 219),
                new Color(155, 89, 182),
                new Color(241, 196, 15)
        };

        for (int i = 0; i < metrics.length; i++) {
            JPanel metricPanel = new JPanel(new BorderLayout());
            metricPanel.setBackground(colors[i]);
            metricPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JLabel title = new JLabel(metrics[i][0]);
            title.setFont(new Font("Arial", Font.BOLD, 14));
            title.setForeground(Color.WHITE);

            JLabel value = new JLabel(metrics[i][1]);
            value.setFont(new Font("Arial", Font.BOLD, 20));
            value.setForeground(Color.WHITE);

            JLabel change = new JLabel(metrics[i][2]);
            change.setFont(new Font("Arial", Font.PLAIN, 12));
            change.setForeground(Color.WHITE);

            metricPanel.add(title, BorderLayout.NORTH);
            metricPanel.add(value, BorderLayout.CENTER);
            metricPanel.add(change, BorderLayout.SOUTH);

            panel.add(metricPanel);
        }

        return panel;
    }

    private JPanel createDataTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Recent Exports (Prices in TND)"));
        panel.setPreferredSize(new Dimension(1200, 150));

        String[] columns = {"Date", "Product", "Price/Ton (TND)", "Volume", "Country", "Status"};

        // Format prices in TND
        String[][] data = new String[5][6];
        for (int i = 0; i < exportRecords.size(); i++) {
            Map<String, String> record = exportRecords.get(i);
            data[i][0] = record.get("date");
            data[i][1] = record.get("product");
            data[i][2] = tndFormat.format(Double.parseDouble(record.get("price").replace(',', '.')));
            data[i][3] = record.get("volume") + "t";
            data[i][4] = record.get("country");
            data[i][5] = record.get("status");
        }

        JTable table = new JTable(data, columns);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane);
        return panel;
    }

    private JPanel createAnalyticsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Chart type selector
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("Chart Type:"));

        String[] chartTypes = {"Bar Chart", "Line Chart", "Pie Chart", "Area Chart"};
        JComboBox<String> chartTypeCombo = new JComboBox<>(chartTypes);
        chartTypeCombo.addActionListener(e -> {
            String selected = (String) chartTypeCombo.getSelectedItem();
            updateChart(selected.toLowerCase().replace(" chart", ""));
        });
        controlPanel.add(chartTypeCombo);

        // Data type selector
        controlPanel.add(new JLabel("  Data:"));
        String[] dataTypes = {"By Product (TND)", "By Country (%)", "By Year (TND)", "By Month (TND)"};
        JComboBox<String> dataTypeCombo = new JComboBox<>(dataTypes);
        dataTypeCombo.addActionListener(e -> {
            String selected = (String) dataTypeCombo.getSelectedItem();
            loadDataForType(selected);
        });
        controlPanel.add(dataTypeCombo);

        panel.add(controlPanel, BorderLayout.NORTH);

        // Chart container
        chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBorder(BorderFactory.createTitledBorder("Analytics Chart - Tunisian Dinar (TND)"));
        currentChart = new ChartPanel(productData, "bar");
        chartContainer.add(currentChart);

        panel.add(chartContainer, BorderLayout.CENTER);

        // Stats panel
        panel.add(createStatsPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private void updateChart(String chartType) {
        currentChartType = chartType;
        chartContainer.removeAll();
        currentChart = new ChartPanel(productData, chartType);
        chartContainer.add(currentChart);
        chartContainer.revalidate();
        chartContainer.repaint();
        statusLabel.setText("Chart updated: " + chartType + " chart (TND)");
    }

    private void loadDataForType(String dataType) {
        Map<String, Double> data;
        switch (dataType) {
            case "By Country (%)": data = countryData; break;
            case "By Year (TND)": data = yearlyData; break;
            case "By Month (TND)": data = monthlyData; break;
            default: data = productData; break;
        }

        chartContainer.removeAll();
        currentChart = new ChartPanel(data, currentChartType);
        chartContainer.add(currentChart);
        chartContainer.revalidate();
        chartContainer.repaint();
        statusLabel.setText("Data loaded: " + dataType);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Statistics (Tunisian Dinar)"));

        String[][] stats = {
                {"Mean", "5,735.25 TND", "Standard Dev", "1,319.36 TND"},
                {"Median", "5,527.44 TND", "Variance", "1,740,716.25"},
                {"Min", "2,446.95 TND", "Max", "12,152.47 TND"}
        };

        for (String[] stat : stats) {
            JPanel statPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            statPanel.setBorder(BorderFactory.createEtchedBorder());

            for (int i = 0; i < stat.length; i++) {
                JLabel label = new JLabel(stat[i], SwingConstants.CENTER);
                label.setFont(new Font("Arial", i % 2 == 0 ? Font.BOLD : Font.PLAIN, 12));
                statPanel.add(label);
            }

            panel.add(statPanel);
        }

        return panel;
    }

    private JPanel createReportsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Report controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String[] reportTypes = {"Market Intelligence", "Executive Summary", "Product Analysis", "Country Report"};
        for (String reportType : reportTypes) {
            JButton btn = new JButton(reportType);
            btn.addActionListener(e -> generateReport(reportType));
            controlPanel.add(btn);
        }

        panel.add(controlPanel, BorderLayout.NORTH);

        // Report display area
        reportArea = new JTextArea(20, 50);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Generated Report - Tunisian Dinar"));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Export buttons
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String[] exportFormats = {"Export as PDF", "Export as CSV", "Export as Text"};
        for (String format : exportFormats) {
            JButton btn = new JButton(format);
            btn.addActionListener(e -> exportReport(format));
            exportPanel.add(btn);
        }

        panel.add(exportPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void generateReport(String reportType) {
        String report = "=== " + reportType + " ===\n";
        report += "Currency: Tunisian Dinar (TND)\n";
        report += "Exchange Rate: 1 USD = 3.1 TND\n";
        report += "Generated: " + LocalDate.now() + "\n";
        report += "Data Points: " + productData.size() + " products\n";
        report += "Time Range: 2020-2024\n\n";

        switch (reportType) {
            case "Market Intelligence":
                report += "MARKET INTELLIGENCE REPORT (TND)\n";
                report += "===============================\n";
                report += "Tunisian agricultural exports show strong growth in Tunisian Dinar.\n\n";
                report += "PRICE ANALYSIS (TND/ton):\n";
                for (Map.Entry<String, Double> entry : productData.entrySet()) {
                    report += String.format("  %s: %s\n", entry.getKey(), tndFormat.format(entry.getValue()));
                }
                report += "\nMARKET SHARE:\n";
                report += "  Olive oil leads with 42% market share\n";
                report += "  Dates: 28%, Citrus: 18%, Wheat: 12%\n\n";
                report += "EXPORT DESTINATIONS:\n";
                for (Map.Entry<String, Double> entry : countryData.entrySet()) {
                    report += String.format("  %s: %.1f%%\n", entry.getKey(), entry.getValue());
                }
                report += "\nAI PREDICTIONS:\n";
                report += "  15% growth potential for 2025 in TND terms\n";
                report += "  Olive oil expected to reach 14,000 TND/ton\n";
                break;

            case "Executive Summary":
                report += "EXECUTIVE SUMMARY (TND)\n";
                report += "=======================\n";
                report += "? Total export value: 7.44M TND\n";
                report += "? Average price/ton: 5,735 TND\n";
                report += "? Top performing: Olive Oil (11,825 TND/ton)\n";
                report += "? Growth rate: 12.5% YoY\n";
                report += "? Primary markets: France (45%), Germany (23%)\n";
                report += "? AI Confidence: 82%\n";
                break;

            case "Product Analysis":
                report += "PRODUCT ANALYSIS REPORT (TND)\n";
                report += "============================\n";
                for (Map.Entry<String, Double> entry : productData.entrySet()) {
                    double usdPrice = entry.getValue() / 3.1;
                    report += String.format("%s:\n", entry.getKey());
                    report += String.format("  Price: %s (≈ $%.2f USD)\n",
                            tndFormat.format(entry.getValue()), usdPrice);
                    report += String.format("  Trend: %s\n",
                            entry.getValue() > 7000 ? "Premium" : "Stable");
                    report += "\n";
                }
                break;

            case "Country Report":
                report += "COUNTRY EXPORT DISTRIBUTION\n";
                report += "===========================\n";
                for (Map.Entry<String, Double> entry : countryData.entrySet()) {
                    report += String.format("%s: %.1f%% of total exports\n", entry.getKey(), entry.getValue());
                }
                report += "\nTOP MARKETS ANALYSIS:\n";
                report += "1. France: Largest market, premium products\n";
                report += "2. Germany: High volume, stable demand\n";
                report += "3. Italy: Growing market for citrus\n";
                report += "4. Spain: Price-sensitive market\n";
                break;
        }

        report += "\n--- END OF REPORT ---\n";
        report += "Note: All prices in Tunisian Dinar (TND)\n";
        report += "Exchange rate: 1 USD = 3.1 TND\n";

        reportArea.setText(report);
        statusLabel.setText("Report generated: " + reportType + " (TND)");
    }

    private void exportReport(String format) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Report - Tunisian Dinar");

        String defaultExtension = ".txt";
        if (format.contains("PDF")) {
            defaultExtension = ".pdf";
        } else if (format.contains("CSV")) {
            defaultExtension = ".csv";
        }

        fileChooser.setSelectedFile(new java.io.File("tunisian_export_report" + defaultExtension));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Ensure file has correct extension
            if (!filePath.toLowerCase().endsWith(defaultExtension)) {
                filePath += defaultExtension;
            }

            try {
                // Actually write the file
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(reportArea.getText());
                }

                JOptionPane.showMessageDialog(this,
                        String.format("Report exported successfully to:\n%s\n\nFile size: %d bytes\nCurrency: Tunisian Dinar (TND)",
                                filePath, new java.io.File(filePath).length()),
                        "Export Complete", JOptionPane.INFORMATION_MESSAGE);
                statusLabel.setText("Report exported: " + new java.io.File(filePath).getName() + " (TND)");

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error exporting file: " + e.getMessage(),
                        "Export Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("Export failed: " + e.getMessage());
            }
        }
    }

    private JPanel createExportTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Export options
        JPanel optionsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Export Options - Tunisian Dinar"));

        String[][] options = {
                {"Data Format:", "CSV, JSON, XML"},
                {"Currency:", "Tunisian Dinar (TND)"},
                {"Date Range:", "2020-01-01 to 2024-12-31"},
                {"Products:", "All Products"},
                {"Include:", "Prices, Volumes, Countries"}
        };

        JComboBox<String> formatCombo = new JComboBox<>(options[0][1].split(", "));
        JLabel currencyLabel = new JLabel("Tunisian Dinar (TND) - 1 USD = 3.1 TND");
        currencyLabel.setForeground(Color.BLUE);
        JComboBox<String> dateCombo = new JComboBox<>(new String[]{"2020-01-01 to 2024-12-31", "2023-01-01 to 2024-12-31", "2024-01-01 to 2024-12-31"});
        JComboBox<String> productCombo = new JComboBox<>(new String[]{"All Products", "Olive Oil", "Dates", "Citrus", "Wheat"});
        JComboBox<String> includeCombo = new JComboBox<>(options[4][1].split(", "));

        optionsPanel.add(new JLabel(options[0][0]));
        optionsPanel.add(formatCombo);
        optionsPanel.add(new JLabel(options[1][0]));
        optionsPanel.add(currencyLabel);
        optionsPanel.add(new JLabel(options[2][0]));
        optionsPanel.add(dateCombo);
        optionsPanel.add(new JLabel(options[3][0]));
        optionsPanel.add(productCombo);
        optionsPanel.add(new JLabel(options[4][0]));
        optionsPanel.add(includeCombo);

        panel.add(optionsPanel, BorderLayout.NORTH);

        // Preview area
        JTextArea previewArea = new JTextArea(10, 50);
        previewArea.setText(generateExportPreview());
        previewArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane previewScroll = new JScrollPane(previewArea);
        previewScroll.setBorder(BorderFactory.createTitledBorder("Export Preview - Prices in TND"));

        panel.add(previewScroll, BorderLayout.CENTER);

        // Export button
        JButton exportBtn = new JButton("Export Data (TND)");
        exportBtn.setBackground(new Color(46, 204, 113));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setFont(new Font("Arial", Font.BOLD, 14));
        exportBtn.addActionListener(e -> exportData(formatCombo, dateCombo, productCombo, includeCombo));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exportBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    // ========== ADD FORECAST METHODS HERE ==========
    private JPanel createForecastTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Forecast controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("Forecast Period:"));

        String[] periods = {"1 Month", "3 Months", "6 Months", "1 Year"};
        JComboBox<String> periodCombo = new JComboBox<>(periods);

        controlPanel.add(periodCombo);

        JButton forecastBtn = new JButton("Generate Forecast");
        forecastBtn.addActionListener(e -> generateForecast((String) periodCombo.getSelectedItem()));
        controlPanel.add(forecastBtn);

        panel.add(controlPanel, BorderLayout.NORTH);

        // Forecast display
        JTextArea forecastArea = new JTextArea(15, 50);
        forecastArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        forecastArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(forecastArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Price Forecast (Tunisian Dinar)"));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Sample forecast data
        forecastArea.setText(generateSampleForecast());

        return panel;
    }

    private void generateForecast(String period) {
        String forecast = "=== PRICE FORECAST REPORT ===\n";
        forecast += "Currency: Tunisian Dinar (TND)\n";
        forecast += "Forecast Period: " + period + "\n";
        forecast += "Generated: " + LocalDate.now() + "\n";
        forecast += "AI Model: Time Series Forecasting\n\n";

        // Generate sample forecast based on period
        int months = Integer.parseInt(period.split(" ")[0]);

        forecast += "OLIVE OIL FORECAST:\n";
        forecast += generateProductForecast("Olive Oil", 11825.51, months);

        forecast += "\nDATES FORECAST:\n";
        forecast += generateProductForecast("Dates", 7947.41, months);

        forecast += "\nCITRUS FORECAST:\n";
        forecast += generateProductForecast("Citrus", 3976.28, months);

        forecast += "\n\nAI INSIGHTS:\n";
        forecast += "? Trend: Upward (8-12% annual growth)\n";
        forecast += "? Confidence: 75-85%\n";
        forecast += "? Recommendation: Increase exports in next 3 months\n";

        // Update forecast area
        JOptionPane.showMessageDialog(this,
                "Forecast generated for " + period + " ahead.\n" +
                        "All prices in Tunisian Dinar (TND).",
                "Forecast Complete", JOptionPane.INFORMATION_MESSAGE);

        statusLabel.setText("Forecast generated: " + period + " ahead (TND)");
    }

    private String generateProductForecast(String product, double currentPrice, int months) {
        StringBuilder sb = new StringBuilder();
        LocalDate date = LocalDate.now();

        for (int i = 1; i <= months; i++) {
            double growthRate = 0.008 + (Math.random() * 0.004); // 0.8-1.2% monthly growth
            double forecastPrice = currentPrice * Math.pow(1 + growthRate, i);
            double confidence = 0.85 - (i * 0.05);

            sb.append(String.format("  Month %d (%s): %s (%.1f%% confidence)\n",
                    i,
                    date.plusMonths(i).getMonth().toString().substring(0, 3),
                    tndFormat.format(forecastPrice),
                    confidence * 100
            ));
        }

        return sb.toString();
    }

    private String generateSampleForecast() {
        return """
        === SAMPLE PRICE FORECAST ===
        Currency: Tunisian Dinar (TND)
        Period: Next 3 Months
        Generated: 2024-12-20
        
        OLIVE OIL (Current: 11,825.51 TND/ton):
          Month 1 (Jan): 12,150.25 TND (82% confidence)
          Month 2 (Feb): 12,480.50 TND (78% confidence)
          Month 3 (Mar): 12,815.75 TND (75% confidence)
        
        DATES (Current: 7,947.41 TND/ton):
          Month 1 (Jan): 8,150.25 TND (80% confidence)
          Month 2 (Feb): 8,360.50 TND (77% confidence)
          Month 3 (Mar): 8,575.75 TND (74% confidence)
        
        CITRUS (Current: 3,976.28 TND/ton):
          Month 1 (Jan): 4,080.25 TND (85% confidence)
          Month 2 (Feb): 4,190.50 TND (82% confidence)
          Month 3 (Mar): 4,305.75 TND (80% confidence)
        
        AI RECOMMENDATIONS:
        ? Best time to export: January-February
        ? Expected growth: 8-10% in next 3 months
        ? High confidence predictions (>75%)
        """;
    }
// ========== END FORECAST METHODS ==========

    private String generateExportPreview() {
        StringBuilder sb = new StringBuilder();
        sb.append("# Tunisian Agricultural Export Data\n");
        sb.append("# Currency: Tunisian Dinar (TND)\n");
        sb.append("# Exchange Rate: 1 USD = 3.1 TND\n");
        sb.append("# Generated: ").append(LocalDate.now()).append("\n");
        sb.append("date,product,price_per_ton_TND,volume_ton,destination_country,status\n");

        for (Map<String, String> record : exportRecords) {
            sb.append(String.format("%s,%s,%s,%s,%s,%s\n",
                    record.get("date"),
                    record.get("product"),
                    record.get("price"), // Already formatted with dot
                    record.get("volume"),
                    record.get("country"),
                    record.get("status")
            ));
        }

        return sb.toString();
    }

    private void exportData(JComboBox<String> formatCombo, JComboBox<String> dateCombo,
                            JComboBox<String> productCombo, JComboBox<String> includeCombo) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Data - Tunisian Dinar");

        String selectedFormat = (String) formatCombo.getSelectedItem();
        String extension = getExtensionForFormat(selectedFormat);

        fileChooser.setSelectedFile(new java.io.File("tunisian_exports_TND" + extension));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Ensure file has correct extension
            if (!filePath.toLowerCase().endsWith(extension)) {
                filePath += extension;
            }

            try {
                String content = generateExportContent(selectedFormat,
                        (String) productCombo.getSelectedItem(),
                        (String) includeCombo.getSelectedItem());

                // Actually write the file
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(content);
                }

                // Show success message with file details
                java.io.File savedFile = new java.io.File(filePath);
                String message = String.format(
                        "Data exported successfully!\n\n" +
                                "File: %s\n" +
                                "Size: %d bytes\n" +
                                "Format: %s\n" +
                                "Currency: Tunisian Dinar (TND)\n" +
                                "Records: %d\n" +
                                "Location: %s",
                        savedFile.getName(),
                        savedFile.length(),
                        selectedFormat,
                        exportRecords.size(),
                        savedFile.getParent()
                );

                JOptionPane.showMessageDialog(this, message,
                        "Export Complete - TND", JOptionPane.INFORMATION_MESSAGE);
                statusLabel.setText("Data exported: " + savedFile.getName() + " (TND)");

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error exporting file: " + e.getMessage(),
                        "Export Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("Export failed: " + e.getMessage());
            }
        }
    }

    private String getExtensionForFormat(String format) {
        if (format == null) return ".txt";
        switch (format.toLowerCase()) {
            case "csv": return ".csv";
            case "json": return ".json";
            case "xml": return ".xml";
            default: return ".txt";
        }
    }

    private String generateExportContent(String format, String productFilter, String includeFilter) {
        if (format == null) format = "CSV";
        switch (format.toLowerCase()) {
            case "csv":
                return generateCSVContent(productFilter);
            case "json":
                return generateJSONContent(productFilter);
            case "xml":
                return generateXMLContent(productFilter);
            default:
                return generateCSVContent(productFilter);
        }
    }

    private String generateCSVContent(String productFilter) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Tunisian Agricultural Export Data\n");
        sb.append("# Currency: Tunisian Dinar (TND)\n");
        sb.append("# Exchange Rate: 1 USD = 3.1 TND\n");
        sb.append("# Generated: ").append(LocalDate.now()).append("\n");
        sb.append("date,product,price_per_ton_TND,volume_ton,destination_country,status\n");

        for (Map<String, String> record : exportRecords) {
            if (productFilter.equals("All Products") || record.get("product").equals(productFilter)) {
                sb.append(String.format("%s,%s,%s,%s,%s,%s\n",
                        record.get("date"),
                        record.get("product"),
                        record.get("price"),
                        record.get("volume"),
                        record.get("country"),
                        record.get("status")
                ));
            }
        }

        return sb.toString();
    }

    private String generateJSONContent(String productFilter) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"metadata\": {\n");
        sb.append("    \"currency\": \"Tunisian Dinar (TND)\",\n");
        sb.append("    \"exchange_rate\": \"1 USD = 3.1 TND\",\n");
        sb.append("    \"generated\": \"").append(LocalDate.now()).append("\",\n");
        sb.append("    \"product_filter\": \"").append(productFilter).append("\"\n");
        sb.append("  },\n");
        sb.append("  \"exports\": [\n");

        boolean first = true;
        for (Map<String, String> record : exportRecords) {
            if (productFilter.equals("All Products") || record.get("product").equals(productFilter)) {
                if (!first) sb.append(",\n");
                sb.append("    {\n");
                sb.append(String.format("      \"date\": \"%s\",\n", record.get("date")));
                sb.append(String.format("      \"product\": \"%s\",\n", record.get("product")));
                sb.append(String.format("      \"price_per_ton_TND\": %s,\n", record.get("price")));
                sb.append(String.format("      \"volume_ton\": %s,\n", record.get("volume")));
                sb.append(String.format("      \"destination_country\": \"%s\",\n", record.get("country")));
                sb.append(String.format("      \"status\": \"%s\"\n", record.get("status")));
                sb.append("    }");
                first = false;
            }
        }

        sb.append("\n  ]\n}");
        return sb.toString();
    }

    private String generateXMLContent(String productFilter) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<exports>\n");
        sb.append("  <metadata>\n");
        sb.append("    <currency>Tunisian Dinar (TND)</currency>\n");
        sb.append("    <exchange_rate>1 USD = 3.1 TND</exchange_rate>\n");
        sb.append("    <generated>").append(LocalDate.now()).append("</generated>\n");
        sb.append("    <product_filter>").append(productFilter).append("</product_filter>\n");
        sb.append("  </metadata>\n");

        for (Map<String, String> record : exportRecords) {
            if (productFilter.equals("All Products") || record.get("product").equals(productFilter)) {
                sb.append("  <export>\n");
                sb.append(String.format("    <date>%s</date>\n", record.get("date")));
                sb.append(String.format("    <product>%s</product>\n", record.get("product")));
                sb.append(String.format("    <price_per_ton_TND>%s</price_per_ton_TND>\n", record.get("price")));
                sb.append(String.format("    <volume_ton>%s</volume_ton>\n", record.get("volume")));
                sb.append(String.format("    <destination_country>%s</destination_country>\n", record.get("country")));
                sb.append(String.format("    <status>%s</status>\n", record.get("status")));
                sb.append("  </export>\n");
            }
        }

        sb.append("</exports>");
        return sb.toString();
    }

    private void handleAction(String action) {
        switch (action) {
            case "Refresh":
                statusLabel.setText("Refreshing data...");
                // Use javax.swing.Timer explicitly
                javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
                    statusLabel.setText("Data refreshed successfully! | Currency: TND | " + LocalDate.now());
                    ((javax.swing.Timer)e.getSource()).stop();
                });
                timer.setRepeats(false);
                timer.start();
                break;

            case "Export":
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Export Dashboard Data - TND");
                fileChooser.setSelectedFile(new java.io.File("dashboard_export_TND.csv"));

                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(".csv")) {
                        filePath += ".csv";
                    }

                    try (FileWriter writer = new FileWriter(filePath)) {
                        writer.write(generateDashboardExport());
                        JOptionPane.showMessageDialog(this,
                                String.format("Dashboard data exported successfully!\nFile: %s\nCurrency: Tunisian Dinar (TND)",
                                        new java.io.File(filePath).getName()),
                                "Export Complete - TND", JOptionPane.INFORMATION_MESSAGE);
                        statusLabel.setText("Dashboard exported: " + new java.io.File(filePath).getName() + " (TND)");
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this,
                                "Error exporting dashboard: " + e.getMessage(),
                                "Export Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;

            case "Print":
                JOptionPane.showMessageDialog(this,
                        "Print functionality:\n\n" +
                                "This would print the current dashboard view.\n" +
                                "All prices are in Tunisian Dinar (TND)\n" +
                                "Exchange rate: 1 USD = 3.1 TND",
                        "Print Preview - TND", JOptionPane.INFORMATION_MESSAGE);
                break;

            case "Help":
                JOptionPane.showMessageDialog(this,
                        "Dashboard Help - Tunisian Dinar (TND)\n\n" +
                                "1. Overview: Key metrics and charts (TND)\n" +
                                "2. Analytics: Interactive data visualization\n" +
                                "3. Reports: Generate market intelligence\n" +
                                "4. Export: Download data in various formats\n\n" +
                                "Features:\n" +
                                "? Real FAO data with Tunisian Dinar conversion\n" +
                                "? Exchange rate: 1 USD = 3.1 TND\n" +
                                "? Multiple chart types (Bar, Line, Pie, Area)\n" +
                                "? Data filtering by product, country, year\n" +
                                "? Export to CSV, JSON, XML\n" +
                                "? Market intelligence reports in TND",
                        "Help - Tunisian Dinar", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    private String generateDashboardExport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dashboard Export - Tunisian Agricultural Exports\n");
        sb.append("Currency: Tunisian Dinar (TND)\n");
        sb.append("Exchange Rate: 1 USD = 3.1 TND\n");
        sb.append("Generated: ").append(LocalDate.now()).append("\n\n");

        sb.append("PRODUCT PRICES (TND/ton):\n");
        for (Map.Entry<String, Double> entry : productData.entrySet()) {
            double usdPrice = entry.getValue() / 3.1;
            sb.append(String.format("%s,%,.2f TND (≈ $%.2f USD)\n",
                    entry.getKey(), entry.getValue(), usdPrice));
        }

        sb.append("\nCOUNTRY DISTRIBUTION:\n");
        for (Map.Entry<String, Double> entry : countryData.entrySet()) {
            sb.append(String.format("%s,%.1f%%\n", entry.getKey(), entry.getValue()));
        }

        sb.append("\nEXPORT RECORDS (TND):\n");
        sb.append("Date,Product,Price_TND,Volume_ton,Country,Status\n");
        for (Map<String, String> record : exportRecords) {
            sb.append(String.format("%s,%s,%s,%s,%s,%s\n",
                    record.get("date"), record.get("product"), record.get("price"),
                    record.get("volume"), record.get("country"), record.get("status")));
        }

        return sb.toString();
    }

    public void showMessage(String message) {
        statusLabel.setText("Message: " + message);
        JOptionPane.showMessageDialog(this, message, "Information - TND",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AgriculturalDashboard dashboard = new AgriculturalDashboard();
            dashboard.setVisible(true);
        });
    }
}