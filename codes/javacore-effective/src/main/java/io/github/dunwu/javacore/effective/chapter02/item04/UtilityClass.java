// Noninstantiable utility class
package io.github.dunwu.javacore.effective.chapter02.item04;

public class UtilityClass {

    // Suppress default constructor for noninstantiability
    private UtilityClass() {
        throw new AssertionError();
    }

}
