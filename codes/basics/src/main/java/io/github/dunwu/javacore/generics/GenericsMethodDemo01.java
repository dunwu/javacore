package io.github.dunwu.javacore.generics;

/**
 * @author Zhang Peng
 * @date 2019-03-20
 */
public class GenericsMethodDemo01 {

	public static <T> void printClass(T obj) {
		System.out.println(obj.getClass().toString());
	}

	public static void main(String[] args) {
		printClass("abc");
		printClass(10);
	}

}
// Output:
// class java.lang.String
// class java.lang.Integer
