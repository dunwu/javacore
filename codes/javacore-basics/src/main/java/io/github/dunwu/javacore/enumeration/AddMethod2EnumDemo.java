/**
 * The Apache License 2.0 Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.javacore.enumeration;

/**
 * 本例展示在枚举类型中添加普通方法、静态方法、抽象方法、构造方法
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see org.zp.javase.enumeration.ErrorCodeEn
 * @since 2016/11/24.
 */
public enum AddMethod2EnumDemo {

    OK(0) {
        @Override
        public String getDescription() {
            return "成功";
        }
    },
    ERROR_A(100) {
        @Override
        public String getDescription() {
            return "错误A";
        }
    },
    ERROR_B(200) {
        @Override
        public String getDescription() {
            return "错误B";
        }
    };

    private int code;

    // 构造方法：enum的构造方法只能被声明为private权限或不声明权限
    AddMethod2EnumDemo(int number) { // 构造方法
        this.code = number;
    }

    public static void main(String[] args) { // 静态方法
        for (AddMethod2EnumDemo item : AddMethod2EnumDemo.values()) {
            System.out.println("code: " + item.getCode() + ", description: " + item.getDescription());
        }
    }

    public int getCode() { // 普通方法
        return code;
    } // 普通方法

    public abstract String getDescription(); // 抽象方法
}
