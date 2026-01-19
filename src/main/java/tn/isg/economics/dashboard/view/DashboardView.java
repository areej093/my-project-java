package tn.isg.economics.dashboard.view;

import tn.isg.economics.dashboard.model.DashboardStatistics;

public interface DashboardView {
    void displayStatistics(DashboardStatistics statistics);
    void displayReport(String report);
    void exportAsCSV(String csvData);
    void showMessage(String message);
    void showError(String error);
    void updateChart(Object chartData);
    void updatePredictionCount(int count);
    void updateFilterStatus(int filterCount);
    void showLoading(boolean isLoading);
    void clearDisplay();
}
