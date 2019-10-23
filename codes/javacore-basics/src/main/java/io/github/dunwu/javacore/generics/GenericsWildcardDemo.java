package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhang Peng
 * @date 2019-03-21
 */
public class GenericsWildcardDemo {

	public static void main(String[] args) {
		List<Integer> intList = new ArrayList<>();
		// List<Number> numList = intList; // Error

		List<? extends Integer> intList2 = new ArrayList<>();
		List<? extends Number> numList2 = intList2; // OK
	}

}
