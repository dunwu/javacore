package io.github.dunwu.javacore.jdk8.stream;

import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * @author Benjamin Winterberg
 */
public class Streams4 {

    public static void main(String[] args) {
        System.out.println("[demo1]");
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 1) {
                System.out.println(i);
            }
        }

        System.out.println("[demo2]");
        IntStream.range(0, 10).forEach(i -> {
            if (i % 2 == 1) {
                System.out.println(i);
            }
        });

        System.out.println("[demo3]");
        IntStream.range(0, 10).filter(i -> i % 2 == 1).forEach(System.out::println);

        System.out.println("[demo4]");
        OptionalInt reduced1 = IntStream.range(0, 10).reduce((a, b) -> a + b);
        System.out.println(reduced1.getAsInt());

        System.out.println("[demo5]");
        int reduced2 = IntStream.range(0, 10).reduce(7, (a, b) -> a + b);
        System.out.println(reduced2);
    }

}
