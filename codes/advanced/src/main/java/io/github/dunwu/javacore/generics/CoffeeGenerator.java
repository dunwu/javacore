package io.github.dunwu.javacore.generics;

import java.util.Iterator;
import java.util.Random;

public class CoffeeGenerator implements Generator<CoffeeGenerator.Coffee>, Iterable<CoffeeGenerator.Coffee> {
    private Class[] types = {Latte.class, Mocha.class, Cappuccino.class, Americano.class, Breve.class,};
    private static Random rand = new Random(47);

    public CoffeeGenerator() {}

    private int size = 0;

    private CoffeeGenerator(int sz) { size = sz; }

    @Override
    public Coffee next() {
        try {
            return (Coffee) types[rand.nextInt(types.length)].newInstance();
            // Report programmer errors at run time:
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class CoffeeIterator implements Iterator<Coffee> {
        int count = size;

        @Override
        public boolean hasNext() { return count > 0; }

        @Override
        public Coffee next() {
            count--;
            return CoffeeGenerator.this.next();
        }

        @Override
        public void remove() { // Not implemented
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Coffee> iterator() {
        return new CoffeeIterator();
    }

    public static void main(String[] args) {
        CoffeeGenerator gen = new CoffeeGenerator();
        for (int i = 0; i < 5; i++) {
            System.out.println(gen.next());
        }
        for (Coffee c : new CoffeeGenerator(5)) {
            System.out.println(c);
        }
    }

    static class Coffee {
        private static long counter = 0;
        private final long id = counter++;

        @Override
        public String toString() {
            return getClass().getSimpleName() + " " + id;
        }
    }

    static class Americano extends Coffee {}

    static class Breve extends Coffee {}

    static class Cappuccino extends Coffee {}

    static class Latte extends Coffee {}

    static class Mocha extends Coffee {}
}
