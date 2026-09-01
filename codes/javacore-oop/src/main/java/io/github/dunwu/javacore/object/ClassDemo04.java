package io.github.dunwu.javacore.object;

/**
 * 示例：对象引用赋值——两个引用指向同一块堆内存，修改任一方都会影响另一方。
 */
public class ClassDemo04 {

    /**
     * 演示引用共享同一块堆内存。
     */
    public static void demo() {
        // 创建对象
        Person person1 = new Person();
        // person2 和 person1 指向同一块堆内存（并非创建新对象）
        Person person2 = person1;

        // 给类的属性赋值
        person1.name = "张三";
        person1.age = 30;
        // 设置person2对象的内容，实际上就是设置person1对象的内容
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
