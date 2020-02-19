// Avoid float and double if exact answers are required!! - Page 48
package io.github.dunwu.javacore.effective.chapter08.item48;

import java.math.BigDecimal;

public class Arithmetic {

    public static void main(String[] args) {
        System.out.println(1.03 - .42);
        System.out.println();

        System.out.println(1.00 - 9 * .10);
        System.out.println();

        howManyCandies1();
        System.out.println();

        howManyCandies2();
        System.out.println();

        howManyCandies3();
    }

    // Broken - uses floating point for monetary calculation!
    public static void howManyCandies1() {
        double funds = 1.00;
        int itemsBought = 0;
        for (double price = .10; funds >= price; price += .10) {
            funds -= price;
            itemsBought++;
        }
        System.out.println(itemsBought + " items bought.");
        System.out.println("Change: $" + funds);
    }

    public static void howManyCandies2() {
        final BigDecimal TEN_CENTS = new BigDecimal(".10");

        int itemsBought = 0;
        BigDecimal funds = new BigDecimal("1.00");
        for (BigDecimal price = TEN_CENTS; funds.compareTo(price) >= 0; price = price.add(TEN_CENTS)) {
            itemsBought++;
            funds = funds.subtract(price);
        }
        System.out.println(itemsBought + " items bought.");
        System.out.println("Money left over: $" + funds);
    }

    public static void howManyCandies3() {
        int itemsBought = 0;
        int funds = 100;
        for (int price = 10; funds >= price; price += 10) {
            itemsBought++;
            funds -= price;
        }
        System.out.println(itemsBought + " items bought.");
        System.out.println("Money left over: " + funds + " cents");
    }

}
