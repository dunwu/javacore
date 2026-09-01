package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class MethodOverrideDemo {

    /**
     * 演示方法重写：子类重写父类方法，可用 super 调用父类版本。
     */
    public static void demo() {
        Animal dog = new Dog();
        dog.move();
    }

    public static void main(String[] args) {
        demo();
    }

    static class Animal {

        public void move() {
            System.out.println("会动");
        }

    }

    static class Dog extends Animal {

        @Override
        public void move() {
            super.move();
            System.out.println("会跑");
        }

    }

}
// Output:
// 会动
// 会跑
