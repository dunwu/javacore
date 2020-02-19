/**
 * The Apache License 2.0 Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.javacore.effective.chapter04.Item20.sample;

/**
 * 成员内部类 成员内部类可以访问外部类的静态与非静态的方法和成员变量。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016/11/22.
 */
public class MemberInner {

    private int a = 1;

    public static void main(String[] args) {
        // 创建成员内部类
        InnerClass innerClass = new MemberInner().new InnerClass();
        innerClass.execute();
    }

    public void execute() {
        // 在外部类中创建成员内部类
        InnerClass innerClass = this.new InnerClass();
    }

    /**
     * 成员内部类
     */
    public class InnerClass {

        // 内部类可以创建与外部类同名的成员变量
        private int a = 2;

        public void execute() {
            // this引用的是内部类
            System.out.println(this.a);
            // 在内部了中使用外部类的成员变量的方法
            System.out.println(MemberInner.this.a);
        }

    }

}
