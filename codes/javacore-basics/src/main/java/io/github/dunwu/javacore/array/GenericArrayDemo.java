package io.github.dunwu.javacore.array;

import java.util.Arrays;

public class GenericArrayDemo<T> {

    public static void main(String[] args) {
        GenericArray<Integer> genericArray = new GenericArray<Integer>(4);
        genericArray.put(0, 0);
        genericArray.put(1, 1);
        Object[] array = genericArray.array();
        System.out.println(Arrays.deepToString(array));
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
