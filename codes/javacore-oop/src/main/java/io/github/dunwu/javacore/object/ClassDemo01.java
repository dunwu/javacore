package io.github.dunwu.javacore.object;

/**
 * 示例：对象的两种实例化写法——直接实例化、先声明再实例化。
 */
public class ClassDemo01 {

    /**
     * 演示对象的声明与实例化。
     */
    public static void demo() {
        // 创建并实例化对象
        Person person1 = new Person();

        // 先声明再实例化对象（声明时 person2 不指向任何堆内存空间）
        Person person2 = null;
        person2 = new Person();

        System.out.println("person1 已实例化：" + (person1 != null));
        System.out.println("person2 已实例化：" + (person2 != null));
    }

    public static void main(String[] args) {
        demo();
    }

}
