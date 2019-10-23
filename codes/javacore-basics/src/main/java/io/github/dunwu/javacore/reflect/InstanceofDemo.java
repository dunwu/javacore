package io.github.dunwu.javacore.reflect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhang Peng
 * @date 2019-03-26
 */
public class InstanceofDemo {

	public static void main(String[] args) {
		ArrayList arrayList = new ArrayList();
		if (arrayList instanceof List) {
			System.out.println("ArrayList is List");
		}
		if (List.class.isInstance(arrayList)) {
			System.out.println("ArrayList is List");
		}
	}

}
// Output:
// ArrayList is List
// ArrayList is List
