// A cloneable version of Stack - Pages 56-57
package io.github.dunwu.javacore.effective.chapter03.item11;

import java.util.Arrays;

public class Stack implements Cloneable {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private Object[] elements;

    private int size = 0;

    public Stack() {
        this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    // To see that clone works, call with several command line arguments
    public static void main(String[] args) {
        Stack stack = new Stack();
        for (String arg : args) {
            stack.push(arg);
        }
        Stack copy = stack.clone();
        while (!stack.isEmpty()) { System.out.print(stack.pop() + " "); }
        System.out.println();
        while (!copy.isEmpty()) { System.out.print(copy.pop() + " "); }
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    // Ensure space for at least one more element.
    private void ensureCapacity() {
        if (elements.length == size) { elements = Arrays.copyOf(elements, 2 * size + 1); }
    }

    @Override
    public Stack clone() {
        try {
            Stack result = (Stack) super.clone();
            result.elements = elements.clone();
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object pop() {
        if (size == 0) { throw new EmptyStackException(); }
        Object result = elements[--size];
        elements[size] = null; // Eliminate obsolete reference
        return result;
    }

}
