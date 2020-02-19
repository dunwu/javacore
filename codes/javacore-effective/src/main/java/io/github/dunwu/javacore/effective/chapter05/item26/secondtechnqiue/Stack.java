// Generic stack using Object[] - Pages 125-127
package io.github.dunwu.javacore.effective.chapter05.item26.secondtechnqiue;

import java.util.Arrays;

public class Stack<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private Object[] elements;

    private int size = 0;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    // Little program to exercise our generic Stack
    public static void main(String[] args) {
        Stack<String> stack = new Stack<String>();
        for (String arg : args) {
            stack.push(arg);
        }
        while (!stack.isEmpty()) { System.out.println(stack.pop().toUpperCase()); }
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    private void ensureCapacity() {
        if (elements.length == size) { elements = Arrays.copyOf(elements, 2 * size + 1); }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Appropriate suppression of unchecked warning
    public E pop() {
        if (size == 0) { throw new EmptyStackException(); }

        // push requires elements to be of type E, so cast is correct
        @SuppressWarnings("unchecked")
        E result = (E) elements[--size];

        elements[size] = null; // Eliminate obsolete reference
        return result;
    }

}
