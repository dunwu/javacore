package io.github.dunwu.javacore.object;

/**
 * 示例：字符串传参——String 是不可变对象，方法内重新赋值不会影响外部字符串。
 */
public class ObjectRefDemo02 {

    /**
     * 演示 String 传参的不可变性。
     */
    public static void demo() {
        String str1 = "hello";
        System.out.println("fun()方法调用之前：" + str1);
        fun(str1);
        System.out.println("fun()方法调用之后：" + str1);
    }

    private static void fun(String str2) {
        // 只是让形参 str2 指向新字符串，不会改变实参 str1 的指向
        str2 = "javase";
    }

    public static void main(String[] args) {
        demo();
    }

}
