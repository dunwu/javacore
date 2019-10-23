package io.github.dunwu.javacore.method;

/**
 * @author Zhang Peng
 * @date 2019-03-16
 */
public class MethodOverloadDemo {

	public static void main(String[] args) {
		add(10, 20);
		add(1.0, 2.0);
	}

	public static void add(int x, int y) {
		System.out.println("x + y = " + (x + y));
	}

	public static void add(double x, double y) {
		System.out.println("x + y = " + (x + y));
	}

}
// Output:
// x + y = 30
// x + y = 3.0
