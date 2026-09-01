package io.github.dunwu.javacore.object;

/**
 * 示例：匿名对象——实例化后不保留引用，直接调用方法。
 */
public class ConstructorDemo03 {

    /**
     * 演示匿名对象的创建与使用。
     */
    public static void demo() {
        // 匿名对象：new 出的对象没有被任何引用保存，方法调用后即可被 GC 回收
        new Person2("张三", 30).tell();
    }

    public static void main(String[] args) {
        demo();
    }

}
