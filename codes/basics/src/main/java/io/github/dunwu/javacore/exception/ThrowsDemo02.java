package io.github.dunwu.javacore.exception;

public class ThrowsDemo02 {
    /**
     * 在主方法中的所有异常都可以不使用try...catch进行处理
     */
    public static void main(String[] args) throws Exception {
        Math m = new Math(); // 实例化Math类对象
        System.out.println("除法操作：" + m.div(10, 0));
    }
};
