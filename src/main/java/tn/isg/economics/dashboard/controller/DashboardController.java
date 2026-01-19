package tn.isg.economics.dashboard.controller;

import lombok.extern.slf4j.Slf4j;
import tn.isg.economics.dashboard.model.DashboardStatistics;
import tn.isg.economics.dashboard.service.StatisticsService;
import tn.isg.economics.dashboard.view.DashboardView;
import tn.isg.economics.model.PricePrediction;
import tn.isg.economics.service.EconomicIntelligenceService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * MVC Controller for the Agricultural Dashboard.
 * Manages business logic, data flow, and user interactions.
 * Uses the Observer pattern for real-time updates.
 */
@Slf4j
public class DashboardController {
    private final StatisticsService statisticsService;
    private final DashboardView view;
    private final EconomicIntelligenceService intelligenceService;

    private List<PricePrediction> currentPredictions;
    private List<Predicate<PricePrediction>> activeFilters;

    public DashboardController(StatisticsService statisticsService,
                               DashboardView view,
                               EconomicIntelligenceService intelligenceService) {
        this.statisticsService = statisticsService;
        this.view = view;
        this.intelligenceService = intelligenceService;
        this.currentPredictions = new ArrayList<>();
        this.activeFilters = new ArrayList<>();
    }

    /**
     * Initialize dashboard with initial predictions.
     * @param initialPredictions Initial price predictions to display
     */
    public void initializeDashboard(List<PricePrediction> initialPredictions) {
        this.currentPredictions = new ArrayList<>(initialPredictions);
        updateStatistics();
        log.info("Dashboard initialized with {} predictions",
                currentPredictions.size());
        view.showMessage("Dashboard ready with " + currentPredictions.size() + " predictions");
    }

    /**
     * Update and display current statistics.
     * Demonstrates real-time data updates (Observer pattern).
     */
    public void updateStatistics() {
        DashboardStatistics stats =
                statisticsService.calculateStatistics(currentPredictions);
        view.displayStatistics(stats);
        log.debug("Statistics updated for {} predictions",
                currentPredictions.size());
    }

    /**
     * Apply filter to current predictions.
     * Demonstrates functional programming with Predicate.
     * @param filter Filter predicate to apply
     */
    public void applyFilter(Predicate<PricePrediction> filter) {
        activeFilters.add(filter);
        DashboardStatistics filteredStats =
                statisticsService.calculateFilteredStatistics(currentPredictions, filter);
        view.displayStatistics(filteredStats);
        log.info("Filter applied, showing {} filtered predictions",
                filteredStats.totalPredictions());
        view.showMessage("Filter applied: " +
                (currentPredictions.size() - filteredStats.totalPredictions()) +
                " records filtered out");
    }

    /**
     * Remove all active filters and show all data.
     * Demonstrates Command pattern for undo functionality.
     */
    public void clearFilters() {
        activeFilters.clear();
        updateStatistics();
        log.info("All filters cleared");
        view.showMessage("All filters cleared");
    }

    /**
     * Generate market intelligence report using LLM integration.
     * Demonstrates complex functionality 4: Report Generation.
     */
    public void generateReport() {
        log.info("Generating comprehensive market intelligence report...");
        try {
            String report = intelligenceService.generateMarketReport(currentPredictions);
            view.displayReport(report);
            log.info("LLM report generated successfully");
            view.showMessage("Market intelligence report generated successfully");
        } catch (Exception e) {
            log.error("Failed to generate report", e);
            view.showError("Failed to generate report: " + e.getMessage());
        }
    }

    /**
     * Generate executive summary report.
     */
    public void generateSummaryReport() {
        log.info("Generating executive summary...");
        try {
            String summary = intelligenceService.generateExecutiveSummary(currentPredictions);
            view.displayReport(summary);
            log.info("Executive summary generated");
        } catch (Exception e) {
            log.error("Failed to generate summary", e);
            view.showError("Failed to generate summary: " + e.getMessage());
        }
    }

    /**
     * Export data in specified format.
     * Demonstrates complex functionality 1: Data Export.
     * @param format Export format (CSV, JSON, etc.)
     */
    public void exportData(String format) {
        log.info("Exporting data in {} format", format);
        try {
            String exportedData;
            if ("CSV".equalsIgnoreCase(format)) {
                exportedData = intelligenceService.exportToCSV(currentPredictions);
                view.exportAsCSV(exportedData);
            } else if ("SUMMARY".equalsIgnoreCase(format)) {
                exportedData = intelligenceService.generateExecutiveSummary(currentPredictions);
                view.exportAsCSV(exportedData);
            } else {
                view.showError("Unsupported format: " + format);
                return;
            }
            log.info("Data exported successfully in {} format", format);
        } catch (Exception e) {
            log.error("Export failed", e);
            view.showError("Export failed: " + e.getMessage());
        }
    }

    /**
     * Add new predictions to dashboard.
     * @param newPredictions New predictions to add
     */
    public void addPredictions(List<PricePrediction> newPredictions) {
        currentPredictions.addAll(newPredictions);
        updateStatistics();
        log.info("Added {} new predictions", newPredictions.size());
        view.showMessage("Added " + newPredictions.size() + " new predictions");
    }

    /**
     * Clear all predictions from dashboard.
     */
    public void clearPredictions() {
        currentPredictions.clear();
        activeFilters.clear();
        updateStatistics();
        log.info("All predictions cleared");
        view.showMessage("All predictions cleared");
    }

    /**
     * Get current dashboard statistics.
     * @return Current statistics
     */
    public DashboardStatistics getCurrentStatistics() {
        return statisticsService.calculateStatistics(currentPredictions);
    }

    /**
     * Get filtered predictions based on active filters.
     * @return Filtered predictions list
     */
    public List<PricePrediction> getFilteredPredictions() {
        List<PricePrediction> filtered = new ArrayList<>(currentPredictions);
        for (Predicate<PricePrediction> filter : activeFilters) {
            filtered = filtered.stream().filter(filter).toList();
        }
        return filtered;
    }

    /**
     * Get count of active filters.
     * @return Number of active filters
     */
    public int getActiveFilterCount() {
        return activeFilters.size();
    }

    /**
     * Get total prediction count.
     * @return Total number of predictions
     */
    public int getTotalPredictionCount() {
        return currentPredictions.size();
    }
}