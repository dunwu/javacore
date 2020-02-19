// Initialization styles - Pages 282-284
package io.github.dunwu.javacore.effective.chapter10.item71;

public class Initialization {

    // Normal initialization of an instance field - Page 282
    private final FieldType field1 = computeFieldValue();

    // Lazy initialization of instance field - synchronized accessor - Page 282
    private FieldType field2;

    // Double-check idiom for lazy initialization of instance fields - Page 283
    private volatile FieldType field4;

    // Single-check idiom - can cause repeated initialization! - Page 284
    private volatile FieldType field5;

    static FieldType getField3() {
        return FieldHolder.field;
    }

    synchronized FieldType getField2() {
        if (field2 == null) { field2 = computeFieldValue(); }
        return field2;
    }

    private static FieldType computeFieldValue() {
        return new FieldType();
    }

    FieldType getField4() {
        FieldType result = field4;
        if (result == null) { // First check (no locking)
            synchronized (this) {
                result = field4;
                if (result == null) // Second check (with locking)
                { field4 = result = computeFieldValue(); }
            }
        }
        return result;
    }

    private FieldType getField5() {
        FieldType result = field5;
        if (result == null) { field5 = result = computeFieldValue(); }
        return result;
    }

    // Lazy initialization holder class idiom for static fields - Page 283
    private static class FieldHolder {

        static final FieldType field = computeFieldValue();

    }

}

class FieldType {

}
