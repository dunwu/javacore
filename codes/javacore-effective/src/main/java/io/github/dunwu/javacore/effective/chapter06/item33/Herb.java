// Using an EnumMap to associate data with an enum - Pages 161-162
package io.github.dunwu.javacore.effective.chapter06.item33;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Simplistic class representing a culinary herb - Page 161
public class Herb {

    private final String name;

    private final Type type;

    Herb(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public static void main(String[] args) {
        Herb[] garden = { new Herb("Basil", Type.ANNUAL), new Herb("Carroway", Type.BIENNIAL),
            new Herb("Dill", Type.ANNUAL), new Herb("Lavendar", Type.PERENNIAL), new Herb("Parsley", Type.BIENNIAL),
            new Herb("Rosemary", Type.PERENNIAL) };

        // Using an EnumMap to associate data with an enum - Page 162
        Map<Type, Set<Herb>> herbsByType = new EnumMap<Type, Set<Herb>>(Type.class);
        for (Type t : Type.values()) {
            herbsByType.put(t, new HashSet<Herb>());
        }
        for (Herb h : garden) {
            herbsByType.get(h.type).add(h);
        }
        System.out.println(herbsByType);
    }

    @Override
    public String toString() {
        return name;
    }

    public enum Type {

        ANNUAL,
        PERENNIAL,
        BIENNIAL
    }

}
