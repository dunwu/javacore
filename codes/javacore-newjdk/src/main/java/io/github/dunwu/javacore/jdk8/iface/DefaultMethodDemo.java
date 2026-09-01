package io.github.dunwu.javacore.jdk8.iface;

/**
 * Java 8 接口默认方法与静态方法示例。
 * <p>
 * Java 8 允许接口包含具体实现，解决了"接口演进"问题
 * （给接口加方法不再会破坏所有实现类）：
 * <ul>
 * <li>{@code default} 方法：实现类可直接继承，也可重写</li>
 * <li>{@code static} 方法：只能通过接口名调用，不能被实现类继承</li>
 * </ul>
 * 多接口默认方法冲突（菱形继承）时，实现类必须显式重写，
 * 并用 {@code 接口名.super.方法()} 指定调用哪一个。
 */
public class DefaultMethodDemo {

    /**
     * 示例 1：默认方法可直接继承使用，也可被实现类重写
     */
    public static void defaultMethodInheritAndOverride() {
        // 默认方法可直接继承使用
        Greeting zh = new ChineseGreeting();
        zh.sayHello();
        // 默认方法可以被重写
        Greeting en = new EnglishGreeting();
        en.sayHello();
    }

    /**
     * 示例 2：接口静态方法只能通过接口名调用
     */
    public static void staticMethod() {
        Greeting.printVersion();
    }

    /**
     * 示例 3：菱形冲突，必须显式重写选择调用哪一个默认方法
     */
    public static void diamondConflict() {
        Greeting mixed = new MixedGreeting();
        mixed.sayHello();
    }

    public static void main(String[] args) {
        defaultMethodInheritAndOverride();
        staticMethod();
        diamondConflict();
    }

    interface Greeting {

        /**
         * 默认方法
         */
        default void sayHello() {
            System.out.println("Greeting 默认: 你好");
        }

        /**
         * 接口静态方法
         */
        static void printVersion() {
            System.out.println("接口静态方法: Greeting v1.0");
        }

    }

    interface FormalGreeting {

        default void sayHello() {
            System.out.println("FormalGreeting 默认: 您好，幸会");
        }

    }

    static class ChineseGreeting implements Greeting {

        // 不重写，直接继承默认方法

    }

    static class EnglishGreeting implements Greeting {

        @Override
        public void sayHello() {
            System.out.println("重写默认方法: Hello!");
        }

    }

    /**
     * 同时实现两个有同名默认方法的接口：菱形冲突，
     * 编译器强制要求显式重写，否则编译失败
     */
    static class MixedGreeting implements Greeting, FormalGreeting {

        @Override
        public void sayHello() {
            System.out.print("MixedGreeting 显式选择 -> ");
            FormalGreeting.super.sayHello();
        }

    }

}
// Output:
// Greeting 默认: 你好
// 重写默认方法: Hello!
// 接口静态方法: Greeting v1.0
// MixedGreeting 显式选择 -> FormalGreeting 默认: 您好，幸会
