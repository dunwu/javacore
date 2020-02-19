// Can you spot the "memory leak"?
package io.github.dunwu.javacore.effective.chapter02.item06;

import java.util.Arrays;

public class Stack {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private Object[] elements;

    private int size = 0;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    /**
     * Ensure space for at least one more element, roughly doubling the capacity each time the array needs to grow.
     */
    private void ensureCapacity() {
        if (elements.length == size) { elements = Arrays.copyOf(elements, 2 * size + 1); }
    }

    public Object pop() {
        if (size == 0) { throw new EmptyStackException(); }
        return elements[--size];
    }

}
