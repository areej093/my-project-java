package tn.isg.economics.model;

import java.time.LocalDate;

public record PricePrediction(
    LocalDate predictionDate,
    ProductType productType,
    double predictedPrice,
    double confidence,
    String modelName,
    PredictionStatus status
) {}
