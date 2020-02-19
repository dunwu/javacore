package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class MethodOverrideDemo {

    public static void main(String[] args) {
        Animal dog = new Dog();
        dog.move();
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
