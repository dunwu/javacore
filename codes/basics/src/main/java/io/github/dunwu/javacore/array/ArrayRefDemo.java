package io.github.dunwu.javacore.array;

/**
 * 数组示例
 *
 * @author Zhang Peng
 */
public class ArrayRefDemo {

	private static void fun(int[] array) {
		for (int i : array) {
			System.out.print(i + "\t");
		}
	}

	public static void main(String[] args) {
		int[] array = new int[] { 1, 3, 5 };
		fun(array);
	}

}
// Output:
// 1 3 5
