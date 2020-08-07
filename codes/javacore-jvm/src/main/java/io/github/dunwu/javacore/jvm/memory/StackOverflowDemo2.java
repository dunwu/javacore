package io.github.dunwu.javacore.jvm.memory;

/**
 * 类成员循环依赖，导致 StackOverflowError。
 * <p>
 * VM 参数：
 * <ul>
 * <li>-Xss228k - 设置栈大小为 228k</li>
 * </ul>
 * <p>
 * Linux Test Cli: nohup java -verbose:gc -Xss228k -cp target/javacore-jvm-1.0.1.jar io.github.dunwu.javacore.jvm.memory.StackOverflowDemo2 >> output.log 2>&1 &
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class StackOverflowDemo2 {

    public static void main(String[] args) {
        A obj = new A();
        System.out.println(obj.toString());
    }

    static class A {

        private int value;

        private B instance;

        public A() {
            value = 0;
            instance = new B();
        }

        @Override
        public String toString() {
            return "<" + value + ", " + instance + ">";
        }

    }

    static class B {

        private int value;

        private A instance;

        public B() {
            value = 10;
            instance = new A();
        }

        @Override
        public String toString() {
            return "<" + value + ", " + instance + ">";
        }

    }

}
