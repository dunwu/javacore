package io.github.dunwu.javacore.jvm.classloader.exception;

/**
 * ClassCastException 示例
 * <p>
 * 强制类型转换只在编译期检查语法，运行期会校验对象的真实类型；
 * 两个没有继承关系的类型之间转型，运行期抛 {@code ClassCastException}
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-03-07
 */
public class ClassCastExceptionDemo {

    public static void main(String[] args) {
        Object obj = new Object();
        try {
            EmptyClass newObj = (EmptyClass) obj;
            System.out.println("转型成功：" + newObj);
        } catch (ClassCastException e) {
            // Object 与 EmptyClass 之间没有继承关系，向下转型在运行期失败
            System.out.println("捕获到 ClassCastException：" + e.getMessage());
        }
    }

    static class EmptyClass {}

}
