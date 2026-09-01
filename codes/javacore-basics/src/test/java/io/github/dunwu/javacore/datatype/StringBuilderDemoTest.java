package io.github.dunwu.javacore.datatype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class StringBuilderDemoTest {

    @Test
    @DisplayName("使用 StringBuilder 高效拼接字符串")
    public void test() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("\tNo.").append(i);
        }
        System.out.println(sb.toString());
    }

    @Test
    @DisplayName("使用 String += 拼接字符串（低效写法）")
    public void test2() {
        String str = "";
        for (int i = 0; i < 10000; i++) {
            str += "\tNo." + "i";
        }
        System.out.println(str);
    }

}
