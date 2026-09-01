package io.github.dunwu.javacore.util;

import io.github.dunwu.javacore.util.tuple.FiveTuple;
import io.github.dunwu.javacore.util.tuple.FourTuple;
import io.github.dunwu.javacore.util.tuple.ThreeTuple;
import io.github.dunwu.javacore.util.tuple.TwoTuple;

/**
 * 元祖工具类
 */
public class TupleUtil {

    public static <A, B> TwoTuple<A, B> tuple(A a, B b) {
        return new TwoTuple<A, B>(a, b);
    }

    public static <A, B, C> ThreeTuple<A, B, C> tuple(A a, B b, C c) {
        return new ThreeTuple<A, B, C>(a, b, c);
    }

    public static <A, B, C, D> FourTuple<A, B, C, D> tuple(A a, B b, C c, D d) {
        return new FourTuple<A, B, C, D>(a, b, c, d);
    }

    public static <A, B, C, D, E> FiveTuple<A, B, C, D, E> tuple(A a, B b, C c, D d, E e) {
        return new FiveTuple<A, B, C, D, E>(a, b, c, d, e);
    }

}
