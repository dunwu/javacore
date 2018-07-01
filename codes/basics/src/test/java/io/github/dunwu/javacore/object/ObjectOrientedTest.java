package io.github.dunwu.javacore.object;

import org.junit.Test;

/**
 * @author Zhang Peng
 */
public class ObjectOrientedTest {
    @Test
    public void test() {
        Salary s = new Salary("Mohd Mohtashim", "Ambehta, UP", 3, 3600.00);
        Employee e = new Salary("John Adams", "Boston, MA", 2, 2400.00);
        System.out.println("Call mailCheck using Salary reference --");
        s.mailCheck();
        System.out.println("Call mailCheck using Employee reference--");
        e.mailCheck();
    }
}
