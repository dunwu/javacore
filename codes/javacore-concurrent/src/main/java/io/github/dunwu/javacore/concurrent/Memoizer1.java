package io.github.dunwu.javacore.concurrent;

import io.github.dunwu.javacore.concurrent.annotation.GuardedBy;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

interface Computable<A, V> {

    V compute(A arg) throws InterruptedException;

}

/**
 * Memoizer1
 * <p>
 * Initial cache attempt using HashMap and synchronization
 *
 * @author Brian Goetz and Tim Peierls
 */
public class Memoizer1<A, V> implements Computable<A, V> {

    @GuardedBy("this")
    private final Map<A, V> cache = new HashMap<A, V>();

    private final Computable<A, V> c;

    public Memoizer1(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }

}

class ExpensiveFunction implements Computable<String, BigInteger> {

    @Override
    public BigInteger compute(String arg) {
        // after deep thought...
        return new BigInteger(arg);
    }

}
