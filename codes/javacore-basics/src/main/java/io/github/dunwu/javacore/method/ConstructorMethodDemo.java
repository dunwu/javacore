package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class ConstructorMethodDemo {

    /**
     * 演示构造方法：创建对象时自动调用，完成成员变量初始化。
     */
    public static void demo() {
        Person person = new Person("jack");
        System.out.println("person name is " + person.getName());
    }

    public static void main(String[] args) {
        demo();
    }

    static class Person {

        private String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
