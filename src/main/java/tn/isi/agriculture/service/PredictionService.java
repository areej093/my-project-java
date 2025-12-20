package tn.isi.agriculture.service;

import tn.isi.agriculture.model.ExportRecord;
import tn.isi.agriculture.model.ProductType;
import java.util.List;
import java.util.Map;

public interface PredictionService {

    // Predict price for a product in a specific year
    double predictPrice(ProductType product, int year);

    // Predict prices for multiple years
    Map<Integer, Double> predictPriceRange(ProductType product, int startYear, int endYear);

    // Get historical data needed for prediction
    List<ExportRecord> getHistoricalData(ProductType product);

    // Get model accuracy (for evaluation)
    double getModelAccuracy();

    // Get model name/type
    String getModelName();
}