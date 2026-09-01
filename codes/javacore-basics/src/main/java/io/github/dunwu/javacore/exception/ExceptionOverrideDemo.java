package io.github.dunwu.javacore.exception;

import java.io.IOException;

/**
 * 因为 Son 类抛出异常的实质是 SQLException，而 IOException 无法处理它。 那么这里的 try catch 就不能处理 Son 中的异常了。多态就不能实现了。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-11
 */
public class ExceptionOverrideDemo {

    /**
     * 演示重写方法时异常声明的约束：子类不能抛出比父类更宽的受检异常。
     */
    public static void demo() {
        Father obj1 = new Father();
        Father obj2 = new Son();
        try {
            obj1.start();
            obj2.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        demo();
    }

    static class Father {

        public void start() throws IOException {
            throw new IOException();
        }

    }

    static class Son extends Father {

        // 放开下面的注释会编译报错：
        // @Override
        // public void start() throws SQLException {
        // throw new SQLException();
        // }
    }

}
