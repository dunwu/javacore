// Two attacks on the internals of an "immutable" period
package io.github.dunwu.javacore.effective.chapter07.item39;

import java.util.Date;

public class Attack {

    public static void main(String[] args) {
        // Attack the internals of a Period instance - Page 185
        Date start = new Date();
        Date end = new Date();
        Period p = new Period(start, end);
        end.setYear(78); // Modifies internals of p!
        System.out.println(p);

        // Second attack on the internals of a Period instance - Page 186
        start = new Date();
        end = new Date();
        p = new Period(start, end);
        p.end().setYear(78); // Modifies internals of p!
        System.out.println(p);
    }

}
