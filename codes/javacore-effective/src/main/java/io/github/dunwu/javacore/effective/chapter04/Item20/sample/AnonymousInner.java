/**
 * The Apache License 2.0 Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.javacore.effective.chapter04.Item20.sample;

import java.util.Date;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016/11/22.
 */
public class AnonymousInner {

    public static void main(String[] args) {
        AnonymousInner test = new AnonymousInner();

        /**
         * 创建匿名内部类 生成的类名：AnonymousInnerClassTest$1
         */
        test.print(new Date() {
            // 重写toString()方法
            @Override
            public String toString() {
                return "Hello world. Date: " + new Date();
            }
        });
    }

    public void print(Date date) {
        System.out.println(date);
    }

}
