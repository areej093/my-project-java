package tn.isg.economics.dashboard.view;

import tn.isg.economics.dashboard.model.DashboardStatistics;
import tn.isg.economics.dashboard.util.ChartFactory;
import tn.isg.economics.dashboard.util.ChartStrategy;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Map;

public class SwingDashboardView extends JFrame implements DashboardView {
    private JTextArea statisticsArea;
    private JTextArea reportArea;
    private JLabel statusLabel;
    private JLabel predictionCountLabel;
    private JLabel filterStatusLabel;
    private JProgressBar loadingBar;
    private JPanel chartPanel;
    private ChartStrategy currentChartStrategy;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private final DecimalFormat percentFormat = new DecimalFormat("0.0%");

    public SwingDashboardView() {
        setTitle("Tunisian Agricultural Export AI Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initializeComponents();
        setupLayout();
        currentChartStrategy = ChartFactory.createChartStrategy("BAR");
    }

    private void initializeComponents() {
        statisticsArea = new JTextArea(15, 50);
        statisticsArea.setEditable(false);
        statisticsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statisticsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        reportArea = new JTextArea(20, 50);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);

        statusLabel = new JLabel("Status: Ready");
        predictionCountLabel = new JLabel("Predictions: 0");
        filterStatusLabel = new JLabel("Filters: 0 active");

        loadingBar = new JProgressBar();
        loadingBar.setVisible(false);
        loadingBar.setStringPainted(true);

        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (currentChartStrategy != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    Map<String, Double> sampleData = Map.of(
                            "Olive Oil", 3814.68,
                            "Dates", 2563.68,
                            "Wheat", 789.34,
                            "Citrus", 1282.67
                    );
                    currentChartStrategy.drawChart(g2d, sampleData,
                            getWidth(), getHeight(), "Product Price Distribution");
                }
            }
        };
        chartPanel.setPreferredSize(new Dimension(400, 300));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Price Analysis Chart"));
    }

    private void setupLayout() {
        JPanel leftPanel = createLeftPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel rightPanel = createRightPanel();
        JPanel bottomPanel = createBottomPanel();

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftPanel, centerPanel);
        mainSplitPane.setResizeWeight(0.3);

        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                chartPanel, rightPanel);
        rightSplitPane.setResizeWeight(0.6);

        add(mainSplitPane, BorderLayout.CENTER);
        add(rightSplitPane, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dashboard Statistics"));
        JScrollPane scrollPane = new JScrollPane(statisticsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Market Intelligence Report"));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Quick Actions"));
        panel.setPreferredSize(new Dimension(300, 200));

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JButton refreshBtn = new JButton("Refresh Statistics");
        JButton exportBtn = new JButton("Export Data");
        JButton filterBtn = new JButton("Apply Filter");
        JButton clearBtn = new JButton("Clear Filters");
        JButton reportBtn = new JButton("Generate Report");
        JButton chartBtn = new JButton("Change Chart Type");

        buttonPanel.add(refreshBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(filterBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(reportBtn);
        buttonPanel.add(chartBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createEtchedBorder());

        panel.add(statusLabel);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(predictionCountLabel);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(filterStatusLabel);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(loadingBar);

        return panel;
    }

    @Override
    public void displayStatistics(DashboardStatistics statistics) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DASHBOARD STATISTICS ===\n\n");

        sb.append("PREDICTION OVERVIEW:\n");
        sb.append(String.format("  Total Predictions: %,d\n", statistics.totalPredictions()));
        sb.append(String.format("  Model Accuracy: %s\n",
                percentFormat.format(statistics.modelAccuracy())));
        sb.append(String.format("  Average Confidence: %s\n\n",
                percentFormat.format(statistics.averageConfidence())));

        sb.append("PRICE STATISTICS:\n");
        sb.append(String.format("  Average Price: $%s/ton\n",
                df.format(statistics.averagePrice())));
        sb.append(String.format("  Price Range: $%s - $%s\n",
                df.format(statistics.minPrice()), df.format(statistics.maxPrice())));
        sb.append(String.format("  Standard Deviation: $%s\n\n",
                df.format(statistics.standardDeviation())));

        Map<String, Long> confDist = statistics.confidenceDistribution();
        confDist.forEach((level, count) ->
                sb.append(String.format("  %s: %,d predictions (%.1f%%)\n",
                        level, count,
                        (statistics.totalPredictions() > 0 ?
                                (count * 100.0 / statistics.totalPredictions()) : 0)
                ))
        );
        sb.append("\n");

        sb.append("PRODUCT AVERAGE PRICES:\n");
        statistics.productDistribution().forEach((product, avgPrice) ->
                sb.append(String.format("  %s: $%s/ton\n", product, df.format(avgPrice)))
        );

        sb.append("\nEXPORT STATISTICS:\n");
        sb.append(String.format("  Total Export Value: $%s\n",
                df.format(statistics.totalExportValue())));
        sb.append(String.format("  Average Volume: %s tons\n",
                df.format(statistics.averageVolume())));

        statisticsArea.setText(sb.toString());
        predictionCountLabel.setText("Predictions: " + statistics.totalPredictions());
        updateChart(statistics.productDistribution());
    }

    @Override
    public void displayReport(String report) {
        reportArea.setText(report);
        statusLabel.setText("Status: Report generated at " +
                java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    @Override
    public void exportAsCSV(String csvData) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Data as CSV");
        fileChooser.setSelectedFile(new java.io.File("tunisian_export_data.csv"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "CSV Files", "csv"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (java.io.FileWriter writer = new java.io.FileWriter(
                    fileChooser.getSelectedFile())) {
                writer.write(csvData);
                showMessage("Data exported successfully to: " +
                        fileChooser.getSelectedFile().getAbsolutePath());
            } catch (Exception e) {
                showError("Failed to export: " + e.getMessage());
            }
        }
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information",
                JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText("Status: " + message);
    }

    @Override
    public void showError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error",
                JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Status: Error - " + error);
    }

    @Override
    public void updateChart(Object chartData) {
        if (chartData instanceof Map) {
            chartPanel.repaint();
            statusLabel.setText("Status: Chart updated at " +
                    java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    }

    @Override
    public void updatePredictionCount(int count) {
        if (predictionCountLabel != null) {
            predictionCountLabel.setText("Predictions: " + count);
        }
    }
    
    @Override
    public void updateFilterStatus(int filterCount) {
        if (filterStatusLabel != null) {
            filterStatusLabel.setText("Filters: " + filterCount + " active");
        }
    }
    
    @Override
    public void showLoading(boolean isLoading) {
        if (loadingBar != null) {
            loadingBar.setVisible(isLoading);
            if (isLoading) {
                loadingBar.setIndeterminate(true);
                statusLabel.setText("Status: Processing...");
            } else {
                loadingBar.setIndeterminate(false);
            }
        }
    }
    
    @Override
    public void clearDisplay() {
        if (statisticsArea != null) {
            statisticsArea.setText("");
        }
        if (reportArea != null) {
            reportArea.setText("");
        }
        if (statusLabel != null) {
            statusLabel.setText("Status: Ready");
        }
        updatePredictionCount(0);
        updateFilterStatus(0);
        showLoading(false);
        if (chartPanel != null) {
            chartPanel.repaint();
        }
    }

    public void changeChartStrategy(String chartType) {
        try {
            currentChartStrategy = ChartFactory.createChartStrategy(chartType);
            chartPanel.repaint();
            showMessage("Chart type changed to: " + chartType);
        } catch (IllegalArgumentException e) {
            showError("Invalid chart type: " + chartType);
        }
    }
}
