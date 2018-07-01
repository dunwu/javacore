package io.github.dunwu.javacore.generics;

public class FibonacciGenerator implements Generator<Integer> {
    private int count = 0;

    @Override
    public Integer next() { return fib(count++); }

    private int fib(int n) {
        if (n < 2) {
            return 1;
        }
        return fib(n - 2) + fib(n - 1);
    }

    public static void main(String[] args) {
        FibonacciGenerator gen = new FibonacciGenerator();
        for (int i = 0; i < 18; i++) {
            System.out.print(gen.next() + " ");
        }
    }
}
/*
Output:
1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597 2584
*/
