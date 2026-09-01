package io.github.dunwu.javacore.container.queue;

import java.util.Stack;

/**
 * {@link Stack} 使用示例。
 * <p>注意：本例故意演示错误用法 —— 栈只有 3 个元素却 pop 4 次，
 * 第 4 次会抛出 {@link java.util.EmptyStackException}；实际使用前应先判 {@code empty()}。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-02-21
 */
public class StackDemo {

    public static void main(String[] args) {
        Stack<String> s = new Stack<>();
        s.push("A"); // 入栈
        s.push("B"); // 入栈
        s.push("C"); // 入栈
        System.out.println(s.pop()); // 出栈
        System.out.println(s.pop()); // 出栈
        System.out.println(s.pop()); // 出栈
        System.out.println(s.pop()); // 出栈
    }

}
// 先输出：C、B、A、
// 执行最后一个打印语句时，由于 Stack 已空，试图执行 pop 方法会抛出异常 java.util.EmptyStackException
