package io.github.dunwu.javacore.array;

/**
 * 数组示例
 * @author Zhang Peng
 */
@SuppressWarnings("all")
public class ArrayDemo2 {
    static class User {}

    public static void main(String[] args) {
        User[] array1 = new User[2]; // 指定数组维度
        User[] array2 = new User[] {new User(), new User()}; // 不指定数组维度

        System.out.println("array1: ");
        for (User item : array1) {
            System.out.println(item);
        }

        System.out.println("array2: ");
        for (User item : array2) {
            System.out.println(item);
        }
    }
}
// Output:
// array1:
// null
// null
// array2:
// io.github.dunwu.javacore.array.ArrayDemo2$User@4141d797
// io.github.dunwu.javacore.array.ArrayDemo2$User@68f7aae2
