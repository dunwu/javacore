package io.github.dunwu.javacore.datatype;

/**
 * 字符串常量拼接
 * <p>
 * 请比对本 java 文件和由它编译出的 class 文件
 *
 * @author peng.zhang
 * @date 2020/12/1
 */
public class String拼接 {

    public static void main(String[] args) {
        // 本行代码在 class 文件中，会被编译器直接优化为：
        // String str = "abc";
        String str = "a" + "b" + "c";
        System.out.println("str = " + str);
    }

}
