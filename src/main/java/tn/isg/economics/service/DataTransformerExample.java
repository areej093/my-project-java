package tn.isg.economics.service;

import tn.isg.economics.model.PricePrediction;

import java.util.function.Function;

public class DataTransformerExample {
    
    public static void main(String[] args) {
        // Example 1: Simple string transformation
        DataTransformer<String, String> toUpperCase = String::toUpperCase;
        DataTransformer<String, Integer> stringLength = String::length;
        
        // Chain transformations
        DataTransformer<String, Integer> chain = toUpperCase.andThen(stringLength);
        int result = chain.transform("hello");
        System.out.println("Length of 'HELLO': " + result);
        
        // Example 2: Transform PricePrediction to formatted string
        DataTransformer<PricePrediction, String> formatPrediction = 
            p -> String.format("%s: $%.2f (%.1f%%)", 
                p.productType(), p.predictedPrice(), p.confidence() * 100);
        
        // Example 3: Using Function
        Function<Double, String> formatCurrency = 
            amount -> String.format("$%.2f", amount);
        
        DataTransformer<Double, String> currencyTransformer = 
            DataTransformer.fromFunction(formatCurrency);
            
        System.out.println("Formatted: " + currencyTransformer.transform(1234.56));
        
        // Example 4: Identity transformer
        DataTransformer<String, String> identity = DataTransformer.identity();
        System.out.println("Identity: " + identity.transform("test"));
    }
}
