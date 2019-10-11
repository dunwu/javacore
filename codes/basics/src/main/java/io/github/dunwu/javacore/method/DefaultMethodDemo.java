package io.github.dunwu.javacore.method;

/**
 * @author Zhang Peng
 * @date 2019-03-16
 */
public class DefaultMethodDemo {

	public static void main(String[] args) {
		MyInterface obj = new MyClass();
		obj.print();
	}

	interface MyInterface {

		default void print() {
			System.out.println("Hello World");
		}

	}


	static class MyClass implements MyInterface {

	}

}
// Output:
// Hello World
