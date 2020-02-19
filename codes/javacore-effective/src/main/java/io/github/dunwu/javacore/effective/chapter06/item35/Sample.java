// Program containing marker annotations - Page 170
package io.github.dunwu.javacore.effective.chapter06.item35;

public class Sample {

    @Test
    public static void m1() {
    } // Test should pass

    public static void m2() {
    }

    @Test
    public static void m3() { // Test Should fail
        throw new RuntimeException("Boom");
    }

    public static void m4() {
    }

    public static void m6() {
    }

    @Test
    public static void m7() { // Test should fail
        throw new RuntimeException("Crash");
    }

    public static void m8() {
    }

    @Test
    public void m5() {
    } // INVALID USE: nonstatic method

}
