// Enum type with constant-specific class bodies and data - Page 153
package io.github.dunwu.javacore.effective.chapter06.item30;

import java.util.HashMap;
import java.util.Map;

public enum Operation {

    PLUS("+") {
        @Override
        double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS("-") {
        @Override
        double apply(double x, double y) {
            return x - y;
        }
    },
    TIMES("*") {
        @Override
        double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE("/") {
        @Override
        double apply(double x, double y) {
            return x / y;
        }
    };

    // Implementing a fromString method on an enum type - Page 154
    private static final Map<String, Operation> stringToEnum = new HashMap<String, Operation>();

    static { // Initialize map from constant name to enum constant
        for (Operation op : values()) {
            stringToEnum.put(op.toString(), op);
        }
    }

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    // Returns Operation for string, or null if string is invalid
    public static Operation fromString(String symbol) {
        return stringToEnum.get(symbol);
    }

    // Test program to perform all operations on given operands
    public static void main(String[] args) {
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        for (Operation op : Operation.values()) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }

    abstract double apply(double x, double y);

    @Override
    public String toString() {
        return symbol;
    }
}
