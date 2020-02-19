package io.github.dunwu.javacore.util;

import io.github.dunwu.javacore.util.tuple.FiveTuple;
import io.github.dunwu.javacore.util.tuple.FourTuple;
import io.github.dunwu.javacore.util.tuple.ThreeTuple;
import io.github.dunwu.javacore.util.tuple.TwoTuple;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class TupleUtilTest {

    @Test
    public void test() {
        TwoTuple<String, Integer> ttsi = f();
        System.out.println(ttsi);
        // ttsi.first = "there"; // Compile error: final
        System.out.println(g());
        System.out.println(h());
        System.out.println(k());
    }

    static TwoTuple<String, Integer> f() {
        return TupleUtil.tuple("hi", 47);
    }

    static ThreeTuple<Amphibian, String, Integer> g() {
        return TupleUtil.tuple(new Amphibian(), "hi", 47);
    }

    static FourTuple<Vehicle, Amphibian, String, Integer> h() {
        return TupleUtil.tuple(new Vehicle(), new Amphibian(), "hi", 47);
    }

    static FiveTuple<Vehicle, Amphibian, String, Integer, Double> k() {
        return TupleUtil.tuple(new Vehicle(), new Amphibian(), "hi", 47, 11.1);
    }

    @Test
    public void testList() {
        TupleList<Vehicle, Amphibian, String, Integer> tl = new TupleList<>();
        tl.add(h());
        tl.add(h());
        for (FourTuple<Vehicle, Amphibian, String, Integer> i : tl)
            System.out.println(i);
    }

    static class TupleList<A, B, C, D> extends ArrayList<FourTuple<A, B, C, D>> {

    }

    static class Amphibian {

    }

    static class Vehicle {

    }

}
