package io.github.dunwu.javacore.array;

import java.util.Arrays;

/**
 * 示例：泛型数组 —— 因为不能直接创建 T[]，通常用 (T[]) new Object[num] 变通。
 */
public class GenericArrayDemo<T> {

    /**
     * 演示泛型数组的创建与读写。
     */
    public static void demo() {
        GenericArray<Integer> genericArray = new GenericArray<Integer>(4);
        genericArray.put(0, 0);
        genericArray.put(1, 1);
        Object[] array = genericArray.array();
        System.out.println(Arrays.deepToString(array));
    }

    public static void main(String[] args) {
        demo();
    }

    static class GenericArray<T> {

        private T[] array;

        public GenericArray(int num) {
            array = (T[]) new Object[num];
        }

        public void put(int index, T item) {
            array[index] = item;
        }

        public T get(int index) {
            return array[index];
        }

        public T[] array() {
            return array;
        }

    }

}
// Output:
// [0, 1, null, null]
