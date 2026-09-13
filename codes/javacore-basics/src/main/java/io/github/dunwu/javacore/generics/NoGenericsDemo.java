package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * 不使用泛型的隐患示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-20
 */
public class NoGenericsDemo {

    /**
     * 反例：不使用泛型时，取出元素需强转，类型不匹配会抛 ClassCastException。
     */
    public static void demo() {
        List list = new ArrayList<>();
        list.add("abc");
        list.add(18);
        list.add(new double[] { 1.0, 2.0 });
        Object obj1 = list.get(0);
        Object obj2 = list.get(1);
        Object obj3 = list.get(2);
        System.out.println("obj1 = [" + obj1 + "]");
        System.out.println("obj2 = [" + obj2 + "]");
        System.out.println("obj3 = [" + obj3 + "]");

        int num1 = (int) list.get(0);
        int num2 = (int) list.get(1);
        int num3 = (int) list.get(2);
        System.out.println("num1 = [" + num1 + "]");
        System.out.println("num2 = [" + num2 + "]");
        System.out.println("num3 = [" + num3 + "]");
    }

    public static void main(String[] args) {
        demo();
    }

}
// Output:（JDK 21 实测。前 3 行走 stdout，异常与栈帧走 stderr，两者的交错顺序不作保证）
// obj1 = [abc]
// obj2 = [18]
// obj3 = [[D@2f2c9b19]
// Exception in thread "main" java.lang.ClassCastException: class java.lang.String cannot be cast to class
// java.lang.Integer (java.lang.String and java.lang.Integer are in module java.base of loader 'bootstrap')
//     at io.github.dunwu.javacore.generics.NoGenericsDemo.demo(NoGenericsDemo.java:29)
//     at io.github.dunwu.javacore.generics.NoGenericsDemo.main(NoGenericsDemo.java:38)
//
// 说明：
// 1. obj3 中 [D 是 double[] 的 JVM 类型签名，@2f2c9b19 是身份哈希，每次运行都不同。
// 2. 异常消息是 JDK 9+ 格式；JDK 8 为 "java.lang.String cannot be cast to java.lang.Integer"。
// 3. 抛异常的是 demo() 里 L29 的 (int) list.get(0)，L30、L31 因此不会执行；栈帧行号随源码改动而变。
// 4. 真实输出中每行 at 之前是一个制表符，此处按项目缩进规范写作空格。
