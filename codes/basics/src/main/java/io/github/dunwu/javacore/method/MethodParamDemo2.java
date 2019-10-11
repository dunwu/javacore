package io.github.dunwu.javacore.method;

/**
 * @author Zhang Peng
 * @date 2019-03-16
 */
public class MethodParamDemo2 {

	public static void method(StringBuilder sb) {
		sb = new StringBuilder("B");
	}

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder("A");
		System.out.println("sb = [" + sb.toString() + "]");
		method(sb);
		System.out.println("sb = [" + sb.toString() + "]");
		sb = new StringBuilder("C");
		System.out.println("sb = [" + sb.toString() + "]");
	}

}
// Output:
// sb = [A]
// sb = [A]
// sb = [C]
