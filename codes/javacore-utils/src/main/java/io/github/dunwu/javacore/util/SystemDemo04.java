package io.github.dunwu.javacore.util;

class Person {

    private String name;

    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() { // 覆写toString()方法
        return "姓名：" + this.name + "，年龄：" + this.age;
    }

    @Deprecated
    @Override
    protected void finalize() throws Throwable { // 对象被回收前调用（finalize 已废弃，此处仅作教学演示）
        System.out.println("对象被释放 --> " + this);
    }

}

/**
 * 示例：断开引用后调用 System.gc() 建议 JVM 回收对象（被回收前会触发 finalize，输出时机不保证）。
 */
public class SystemDemo04 {

    /**
     * 演示断开引用并请求垃圾回收。
     */
    public static void demo() {
        Person per = new Person("张三", 30);
        System.out.println("对象信息：" + per); // 覆写的 toString() 生效
        per = null; // 断开引用，对象变为可回收状态
        System.gc(); // 建议 JVM 进行垃圾回收（不保证立即执行）
    }

    public static void main(String[] args) {
        demo();
    }

}
