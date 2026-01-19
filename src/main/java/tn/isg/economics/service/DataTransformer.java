package tn.isg.economics.service;

import java.util.function.Function;

/**
 * Functional interface for data transformation
 */
@FunctionalInterface
public interface DataTransformer<T, R> {
    R transform(T input);
    
    default <V> DataTransformer<T, V> andThen(DataTransformer<R, V> after) {
        return (T t) -> after.transform(transform(t));
    }
    
    default <V> DataTransformer<V, R> compose(DataTransformer<V, T> before) {
        return (V v) -> transform(before.transform(v));
    }
    
    // Static factory methods
    static <T> DataTransformer<T, T> identity() {
        return t -> t;
    }
    
    static <T, R> DataTransformer<T, R> fromFunction(Function<T, R> function) {
        return function::apply;
    }
}
