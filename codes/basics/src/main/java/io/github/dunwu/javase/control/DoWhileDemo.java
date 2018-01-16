package io.github.dunwu.javase.control;

/**
 * @author Zhang Peng
 */
public class DoWhileDemo {
    public static void main(String args[]) {
        int x = 10;

        do {
            System.out.print("value of x : " + x);
            x++;
            System.out.print("\n");
        } while (x < 20);
    }
}
// output:
// value of x:10
// value of x:11
// value of x:12
// value of x:13
// value of x:14
// value of x:15
// value of x:16
// value of x:17
// value of x:18
// value of x:19
