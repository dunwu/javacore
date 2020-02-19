/**
 * The Apache License 2.0 Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.javacore.effective.chapter04.Item20.sample;

/**
 * 局部内部类 类似于局部变量，不能定义为public，protected，private或者static类型。 定义方法中，只能方法中声明为final类型的变量。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016/11/22.
 */
public class LocalInner {

    public static void main(String[] args) {
        // 外部不能直接创建局部内部类
        LocalInner localInner = new LocalInner();
        localInner.execute();
    }

    public void execute() {
        final int a = 1;
        /**
         * 创建局部内部类
         */
        class InnerClass {

            public void execute() {
                System.out.println("LocalInner Class");

                // 局部内部类只能访问final类型的变量
                System.out.println(a);
            }

        }
        // 只能在所在方法区域创建
        new InnerClass().execute();
    }

}
