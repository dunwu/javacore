package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 泛型的类型擦除
 * @see ErasedTypeEquivalence
 */
public class LostInformation {
    class Frob {}

    class Fnorkle {}

    static class Quark<Q> {}

    static class Particle<POSITION, MOMENTUM> {}

    public static void main(String[] args) {
        List<Frob> list = new ArrayList<>();
        Map<Frob, Fnorkle> map = new HashMap<>();
        Quark<Fnorkle> quark = new Quark<>();
        Particle<Long, Double> p = new Particle<>();
        System.out.println(Arrays.toString(list.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(map.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(quark.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(p.getClass().getTypeParameters()));
    }
}
/*
Output:
[E]
[K, V]
[Q]
[POSITION, MOMENTUM]
*/
