package io.github.dunwu.javacore.container;

import java.util.Stack;

public class StackDemo {

    public static void main(String[] args) {
        Stack<String> s = new Stack<String>();
        s.push("A");    // 入栈
        s.push("B");    // 入栈
        s.push("C");    // 入栈
        System.out.print(s.pop() + "、");
        System.out.print(s.pop() + "、");
        System.out.println(s.pop() + "、");
        System.out.println(s.pop());
    }

}
