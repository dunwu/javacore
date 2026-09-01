package io.github.dunwu.javacore.method;

/**
 * 演示 return 语句：提前结束方法执行，返回调用处。
 */
public class MethodDemo05 {

    public static void demo() {
        System.out.println("1、调用fun()方法之前。");
        fun(10);
        System.out.println("2、调用fun()方法之后。");
    }

    public static void main(String[] args) {
        demo();
    }

    private static void fun(int x) {
        System.out.println("3、进入fun()方法。");
        if (x == 10) {
            return; // 结束方法，返回被调用处
        }
        System.out.println("4、正常执行完fun()方法。");
    }

}
