package io.github.dunwu.javase.control;

/**
 * @author Zhang Peng
 */
public class IfElseifElseDemo {
    public static void main(String args[]) {
        int x = 3;

        if (x == 1) {
            System.out.print("Value of X is 1");
        } else if (x == 2) {
            System.out.print("Value of X is 2");
        } else if (x == 3) {
            System.out.print("Value of X is 3");
        } else {
            System.out.print("This is else statement");
        }
    }
}
// output:
// Value of X is 3
