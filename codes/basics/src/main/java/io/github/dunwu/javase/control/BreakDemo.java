package io.github.dunwu.javase.control;

/**
 * @author Zhang Peng
 */
public class BreakDemo {
    public static void main(String args[]) {
        int[] numbers = { 10, 20, 30, 40, 50 };

        for (int x : numbers) {
            if (x == 30) {
                break;
            }
            System.out.print(x);
            System.out.print("\n");
        }
    }
}
// output:
// 10
// 20
