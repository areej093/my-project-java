package tn.isg.economics.dashboard.service;

import tn.isg.economics.dashboard.model.DashboardStatistics;
import tn.isg.economics.model.PricePrediction;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Service interface for calculating dashboard statistics.
 * Follows Strategy pattern for different statistical calculations.
 */
public interface StatisticsService {

    /**
     * Calculate comprehensive statistics from predictions.
     * @param predictions List of price predictions
     * @return Dashboard statistics object
     */
    DashboardStatistics calculateStatistics(List<PricePrediction> predictions);

    /**
     * Calculate statistics for filtered predictions.
     * Demonstrates functional programming with Predicate.
     * @param predictions List of price predictions
     * @param filter Filter predicate to apply
     * @return Filtered dashboard statistics
     */
    DashboardStatistics calculateFilteredStatistics(
            List<PricePrediction> predictions,
            Predicate<PricePrediction> filter
    );

    /**
     * Get time series data for trend visualization.
     * @param predictions List of price predictions
     * @param groupingKey How to group data (MONTH, QUARTER, YEAR)
     * @return Time series data map
     */
    Map<String, Double> getTimeSeriesData(
            List<PricePrediction> predictions,
            String groupingKey
    );

    /**
     * Calculate average price by product type.
     * @param predictions List of price predictions
     * @return Map of product type to average price
     */
    Map<String, Double> calculateProductAverages(List<PricePrediction> predictions);

    /**
     * Get confidence level distribution.
     * @param predictions List of price predictions
     * @return Map of confidence level to count
     */
    Map<String, Long> getConfidenceDistribution(List<PricePrediction> predictions);

    /**
     * Calculate standard deviation of prices.
     * @param predictions List of price predictions
     * @return Standard deviation value
     */
    double calculateStandardDeviation(List<PricePrediction> predictions);

    /**
     * Calculate total export value.
     * @param predictions List of price predictions
     * @return Total export value in USD
     */
    double calculateTotalExportValue(List<PricePrediction> predictions);

    /**
     * Get country-wise export distribution.
     * @param predictions List of price predictions
     * @return Map of country to export count
     */
    Map<String, Integer> getCountryDistribution(List<PricePrediction> predictions);

    /**
     * Get monthly trends for time series visualization.
     * @param predictions List of price predictions
     * @return Map of month to average price
     */
    Map<String, Double> getMonthlyTrends(List<PricePrediction> predictions);

    /**
     * Get quarterly trends for time series visualization.
     * @param predictions List of price predictions
     * @return Map of quarter to average price
     */
    Map<String, Double> getQuarterlyTrends(List<PricePrediction> predictions);

    /**
     * Calculate model performance metrics.
     * @param predictions List of price predictions
     * @return Model accuracy percentage
     */
    double calculateModelAccuracy(List<PricePrediction> predictions);
}