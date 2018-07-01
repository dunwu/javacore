package io.github.dunwu.javacore.generics;

import java.util.ArrayList;

/**
 * 泛型的类型擦除
 * @see LostInformation
 */
public class ErasedTypeEquivalence {
    public static void main(String[] args) {
        Class c1 = new ArrayList<String>().getClass();
        Class c2 = new ArrayList<Integer>().getClass();
        System.out.println(c1 == c2);
    }
}
/*
Output:
true
*/
