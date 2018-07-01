package io.github.dunwu.javacore.string;

import org.junit.Test;

/**
 * @author Zhang Peng
 */
public class StringBuilderDemoTest {

    @Test
    public void test() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("\tNo.").append(i);
        }
        System.out.println(sb.toString());
    }

    @Test
    public void test2() {
        String str = "";
        for (int i = 0; i < 10000; i++) {
            str += "\tNo." + "i";
        }
        System.out.println(str);
    }
}
