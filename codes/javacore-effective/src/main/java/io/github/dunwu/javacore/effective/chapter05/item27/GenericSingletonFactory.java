// Generic singleton factory method - Pages 131-132
package io.github.dunwu.javacore.effective.chapter05.item27;

public class GenericSingletonFactory {

    // Generic singleton factory pattern
    private static UnaryFunction<Object> IDENTITY_FUNCTION = new UnaryFunction<Object>() {
        @Override
        public Object apply(Object arg) {
            return arg;
        }
    };

    // Sample program to exercise generic singleton
    public static void main(String[] args) {
        String[] strings = { "jute", "hemp", "nylon" };
        UnaryFunction<String> sameString = identityFunction();
        for (String s : strings) {
            System.out.println(sameString.apply(s));
        }

        Number[] numbers = { 1, 2.0, 3L };
        UnaryFunction<Number> sameNumber = identityFunction();
        for (Number n : numbers) {
            System.out.println(sameNumber.apply(n));
        }
    }

    // IDENTITY_FUNCTION is stateless and its type parameter is
    // unbounded so it's safe to share one instance across all types.
    @SuppressWarnings("unchecked")
    public static <T> UnaryFunction<T> identityFunction() {
        return (UnaryFunction<T>) IDENTITY_FUNCTION;
    }

}
