package io.github.dunwu.javacore.generics;

import java.util.Iterator;

@SuppressWarnings("all")
public class IterableFibonacciGenerator extends FibonacciGenerator implements Iterable<Integer> {
    private int n;

    public IterableFibonacciGenerator(int count) { n = count; }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            @Override
            public boolean hasNext() { return n > 0; }

            @Override
            public Integer next() {
                n--;
                return IterableFibonacciGenerator.this.next();
            }

            @Override
            public void remove() { // Not implemented
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {
        for (int i : new IterableFibonacciGenerator(18)) {
            System.out.print(i + " ");
        }
    }
}
/*
Output:
1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597 2584
*/
