// Takes earth-weight and prints table of weights on all planets - Page 150
package io.github.dunwu.javacore.effective.chapter06.item30;

public class WeightTable {

    public static void main(String[] args) {
        double earthWeight = Double.parseDouble(args[0]);
        double mass = earthWeight / Planet.EARTH.surfaceGravity();
        for (Planet p : Planet.values()) {
            System.out.printf("Weight on %s is %f%n", p, p.surfaceWeight(mass));
        }
    }

}
