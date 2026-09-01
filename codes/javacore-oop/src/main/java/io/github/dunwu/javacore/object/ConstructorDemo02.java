package io.github.dunwu.javacore.object;

/**
 * 示例：使用带参构造方法在实例化的同时完成属性初始化。
 */
public class ConstructorDemo02 {

    /**
     * 演示带参构造方法初始化对象。
     */
    public static void demo() {
        Person2 per = new Person2("张三", 30);
        per.tell();
    }

    public static void main(String[] args) {
        demo();
    }

}
