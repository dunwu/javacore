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

    public static void main(String[] args) {
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

}
// Output:
// obj1 = [abc]
// obj2 = [18]
// obj3 = [[D@47089e5f]
// Exception in thread "main" java.lang.ClassCastException: java.lang.String cannot be
// cast to java.lang.Integer
// at io.github.dunwu.javacore.generics.NoGenericsDemo.main(NoGenericsDemo.java:23)
