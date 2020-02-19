// Broken! - What does this program print?
package io.github.dunwu.javacore.effective.chapter07.item41;

import java.math.BigInteger;
import java.util.*;

public class CollectionClassifier {

    public static String classify(Set<?> s) {
        return "Set";
    }

    public static String classify(List<?> lst) {
        return "List";
    }

    public static void main(String[] args) {
        Collection<?>[] collections = { new HashSet<String>(), new ArrayList<BigInteger>(),
            new HashMap<String, String>().values() };

        for (Collection<?> c : collections) {
            System.out.println(classify(c));
        }
    }

    public static String classify(Collection<?> c) {
        return "Unknown Collection";
    }

}
