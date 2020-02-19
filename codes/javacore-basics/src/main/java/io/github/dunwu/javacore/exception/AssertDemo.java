package io.github.dunwu.javacore.exception;

public class AssertDemo {

    public static void main(String[] args) {
        int[] x = { 1, 2, 3 }; // 定义数组，长度为3
        assert x.length == 0 : "数组长度不为0"; // 此处断言数组的长度为0
        System.out.println("x.length = [" + x.length + "]");
    }

}
