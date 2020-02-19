package io.github.dunwu.javacore.method;

/**
 * 展示错误的例子：参数完全相同，仅返回值不同，编译器仍视为一个方法
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class MethodDemo04 {

    public static int add(int x, int y) {
        int temp = 0; // 方法中的参数，是局部变量
        temp = x + y; // 执行加法计算
        return temp; // 返回计算结果
    }

    /**
     * 重载要求方法中的参数不同。如果参数完全相同，仅返回值不同，编译器仍视为一个方法。 这里暂且注释，否则编译器会告警
     */
    // public static float add(int x, int y) {
    // float temp = 0; // 方法中的参数，是局部变量
    // temp = x + y; // 执行加法操作
    // return temp; // 返回计算结果
    // }
}
