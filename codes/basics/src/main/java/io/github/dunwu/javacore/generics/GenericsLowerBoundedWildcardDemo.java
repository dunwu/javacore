package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类型边界之下限通配符
 *
 * @author Zhang Peng
 * @date 2019-03-21
 * @see GenericsUpperBoundedWildcardDemo
 * @see GenericsLowerBoundedWildcardDemo
 * @see GenericsUnboundedWildcardDemo
 */
public class GenericsLowerBoundedWildcardDemo {

	public static void addNumbers(List<? super Integer> list) {
		for (int i = 1; i <= 5; i++) {
			list.add(i);
		}
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>();
		addNumbers(list);
		System.out.println(Arrays.deepToString(list.toArray()));
	}

}
// Output:
// [1, 2, 3, 4, 5]
