package io.github.dunwu.javacore.exception;

/**
 * 演示 assert 断言：默认不开启（需 java -ea 启动参数），断言失败时抛出 AssertionError。
 */
public class AssertDemo {

    /**
     * 断言数组长度为 0（实际为 3）：开启断言时会失败，关闭时正常执行。
     */
    public static void demo() {
        int[] x = { 1, 2, 3 }; // 定义数组，长度为3
        assert x.length == 0 : "数组长度不为0"; // 此处断言数组的长度为0
        System.out.println("x.length = [" + x.length + "]");
    }

    public static void main(String[] args) {
        demo();
    }

}
