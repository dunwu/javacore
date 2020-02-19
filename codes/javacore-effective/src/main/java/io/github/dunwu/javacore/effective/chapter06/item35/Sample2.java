// Program containing annotations with a parameter - Page 172
package io.github.dunwu.javacore.effective.chapter06.item35;

import java.util.ArrayList;
import java.util.List;

public class Sample2 {

    // Code containing an annotation with an array parameter - Page 174
    @ExceptionTest({ IndexOutOfBoundsException.class, NullPointerException.class })
    public static void doublyBad() {
        List<String> list = new ArrayList<String>();

        // The spec permits this method to throw either
        // IndexOutOfBoundsException or NullPointerException
        list.addAll(5, null);
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m1() { // Test should pass
        int i = 0;
        i = i / i;
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m2() { // Should fail (wrong exception)
        int[] a = new int[0];
        int i = a[1];
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m3() {
    } // Should fail (no exception)

}
