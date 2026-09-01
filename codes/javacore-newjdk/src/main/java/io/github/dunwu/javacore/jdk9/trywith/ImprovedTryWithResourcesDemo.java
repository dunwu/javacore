package io.github.dunwu.javacore.jdk9.trywith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Java 9 改进的 try-with-resources 示例。
 * <p>
 * Java 7 引入的 try-with-resources 要求在 try 小括号内声明资源，
 * 如果资源已经在外部声明（且是 effectively final 变量），则无法直接复用。
 * Java 9 放宽了这一限制：try 小括号中可以直接使用已初始化的 effectively final 变量。
 */
public class ImprovedTryWithResourcesDemo {

    /**
     * 示例 1：Java 7 写法——资源必须在 try 小括号内声明
     */
    public static void java7Style() throws IOException {
        try (BufferedReader reader1 = new BufferedReader(new StringReader("Java 7 写法"))) {
            System.out.println(reader1.readLine());
        }
    }

    /**
     * 示例 2：Java 9 写法——资源可以在 try 外部声明，只要它是 effectively final 变量
     */
    public static void java9Style() throws IOException {
        BufferedReader reader2 = new BufferedReader(new StringReader("Java 9 写法"));
        try (reader2) {
            System.out.println(reader2.readLine());
        }
        // try 结束后 reader2 已自动关闭
        System.out.println("reader2 是否已关闭（无法读取已关闭的流，此处仅演示语法）");
    }

    /**
     * 示例 3：可以同时混用新声明的资源和外部已声明的资源
     */
    public static void mixedResources() throws IOException {
        BufferedReader outer = new BufferedReader(new StringReader("外部资源"));
        try (outer; BufferedReader inner = new BufferedReader(new StringReader("内部资源"))) {
            System.out.println(outer.readLine() + " + " + inner.readLine());
        }
    }

    public static void main(String[] args) throws IOException {
        java7Style();
        java9Style();
        mixedResources();
    }

}
// Output:
// Java 7 写法
// Java 9 写法
// reader2 是否已关闭（无法读取已关闭的流，此处仅演示语法）
// 外部资源 + 内部资源
