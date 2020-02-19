package io.github.dunwu.javacore.jvm.memory;

/**
 * VM Args: -Xmx30m
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class JvmXmxArgs {

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("参数为" + arg);
        }
        System.out.println("-Xmx:" + Runtime.getRuntime().maxMemory() / 1000 / 1000 + "M");
    }

}
