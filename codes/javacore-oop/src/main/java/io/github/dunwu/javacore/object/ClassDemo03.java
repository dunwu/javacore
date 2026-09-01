package io.github.dunwu.javacore.object;

/**
 * 示例：两个对象各自拥有独立的堆内存空间，属性互不影响。
 */
public class ClassDemo03 {

    /**
     * 演示两个独立对象的属性互不影响。
     */
    public static void demo() {
        // 创建对象
        Person person1 = new Person();
        Person person2 = new Person();

        // 给类的属性赋值
        person1.name = "张三";
        person1.age = 30;
        person2.name = "李四";
        person2.age = 33;

        // 调用类的方法
        System.out.print("person1对象中的内容 --> ");
        person1.tell();
        System.out.print("person2对象中的内容 --> ");
        person2.tell();
    }

    public static void main(String[] args) {
        demo();
    }

}
