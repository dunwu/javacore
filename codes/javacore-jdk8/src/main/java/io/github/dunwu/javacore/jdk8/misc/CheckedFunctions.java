package io.github.dunwu.javacore.jdk8.misc;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utilities for hassle-free usage of lambda expressions who throw checked exceptions.
 *
 * @author Benjamin Winterberg
 */
public final class CheckedFunctions {

    /**
     * Return a consumer which rethrows possible checked exceptions as runtime exception.
     */
    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer) {
        return input -> {
            try {
                consumer.accept(input);
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Return a function which rethrows possible checked exceptions as runtime exception.
     */
    public static <F, T> Function<F, T> function(CheckedFunction<F, T> function) {
        return input -> {
            try {
                return function.apply(input);
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Return a predicate which rethrows possible checked exceptions as runtime exception.
     */
    public static <T> Predicate<T> predicate(CheckedPredicate<T> predicate) {
        return input -> {
            try {
                return predicate.test(input);
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException(e);
            }
        };
    }

    @FunctionalInterface
    public interface CheckedConsumer<T> {

        void accept(T input) throws Exception;

    }

    @FunctionalInterface
    public interface CheckedPredicate<T> {

        boolean test(T input) throws Exception;

    }

    @FunctionalInterface
    public interface CheckedFunction<F, T> {

        T apply(F input) throws Exception;

    }

}
