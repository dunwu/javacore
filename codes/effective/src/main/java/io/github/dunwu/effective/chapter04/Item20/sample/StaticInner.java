/**
 * The Apache License 2.0
 * Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.effective.chapter04.Item20.sample;

/**
 * 静态内部类
 * 只能访问外部类的静态成员变量与静态方法。
 *
 * @author Zhang Peng
 * @date 2016/11/22.
 */
public class StaticInner {
    private static int a = 1;

    /**
     * 静态内部类
     * 生成的类名：StaticInner$InnerClass
     */
    public static class InnerClass {
        //静态内部类可以声明静态的成员变量，其他的内部类不可以
        private static int b = 1;

        public void execute() {
            //静态内部类只能访问静态程序
            System.out.println(a + b);
        }
    }

    public static void main(String[] args) {
        //创建静态内部类
        InnerClass innerClass = new InnerClass();
        innerClass.execute();
    }
}
