package io.github.dunwu.javacore.object;

import java.util.Arrays;
import java.util.List;

/**
 * 示例：继承、重写与多态分派。
 * <p>
 * 本模块的 {@code ObjectRefDemo} 系列讲的是对象引用如何传递，{@code ClassDemo} 系列讲的是类与对象的关系，
 * 这里补齐继承体系的四个核心机制：
 * <ul>
 *     <li>{@link #override()} —— 重写（override）与重载（overload）的本质区别：
 *     <b>重载在编译期按静态类型选定，重写在运行期按实际类型分派</b></li>
 *     <li>{@link #upcastAndDispatch()} —— 向上转型与动态分派，多态的基础</li>
 *     <li>{@link #fieldAndStaticBinding()} —— 最容易踩的坑：<b>字段与静态方法不参与多态</b></li>
 *     <li>{@link #constructorChain()} —— 构造器调用链：父类构造器总在子类之前完成</li>
 *     <li>{@link #downcast()} —— 向下转型必须先用 instanceof 判断</li>
 * </ul>
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class InheritanceDemo {

    /**
     * 重写与重载
     * <p>
     * 重写（override）：子类覆盖父类中<b>签名完全相同</b>的方法，运行期按对象的实际类型分派。
     * 重载（overload）：同一个类中<b>方法名相同、参数列表不同</b>，编译期就按参数的静态类型选定。
     * <p>
     * {@code @Override} 注解的价值在于：签名写错（例如漏了参数、拼错方法名）时编译期立即报错，
     * 而不是被当成一个新的重载方法，静默地永远不被调用。
     */
    public static void override() {
        OverloadSub sub = new OverloadSub();

        // 参数的静态类型是 Object，编译期选中 call(Object)，运行期分派到子类的重写版本
        Object arg = "hello";
        System.out.println("静态类型为 Object 时选中: " + sub.call(arg));
        // 参数的静态类型是 String，编译期就选中了父类的 call(String)——子类并没有重写它
        System.out.println("静态类型为 String 时选中: " + sub.call("hello"));

        // 协变返回类型：重写方法的返回类型可以是父类返回类型的子类型（JDK 5 起支持）
        Sub covariant = new Sub();
        System.out.println("协变返回类型得到的是: " + covariant.copy().getClass().getSimpleName());
        // 重写可以放宽访问权限（protected → public），但不能收窄
        System.out.println("子类放宽了访问权限: " + covariant.protectedMethod());
    }

    /**
     * 向上转型与动态分派
     */
    public static void upcastAndDispatch() {
        // 向上转型：用父类引用指向子类对象，是安全的隐式转型
        Animal dog = new Dog();
        Animal cat = new Cat();

        // 实例方法是动态绑定的：调用哪个实现取决于对象的「运行时类型」，与引用的声明类型无关
        System.out.println("声明为 Animal，实际是 Dog: " + dog.speak());
        System.out.println("声明为 Animal，实际是 Cat: " + cat.speak());

        // 多态最典型的用法：同一份处理代码，无需为每个子类写分支
        List<Animal> animals = Arrays.asList(new Dog(), new Cat());
        for (Animal animal : animals) {
            System.out.println("统一遍历时调用 speak(): " + animal.speak());
        }
    }

    /**
     * 字段与静态方法<b>不参与</b>多态
     * <p>
     * 这是继承体系里最容易出错的地方：字段访问和静态方法调用都在<b>编译期</b>按引用的声明类型绑定，
     * 运行时的实际类型完全不起作用。子类中同名字段叫「隐藏（hiding）」而不是「重写」。
     */
    public static void fieldAndStaticBinding() {
        Animal dog = new Dog();

        // Dog 里也声明了 name 字段，但通过 Animal 引用访问到的是父类的那个
        System.out.println("通过 Animal 引用访问 name: " + dog.name);
        System.out.println("强转为 Dog 后访问 name: " + ((Dog) dog).name);

        // 静态方法属于类而不是对象，同样在编译期绑定
        System.out.println("通过 Animal 类名调用: " + Animal.staticSpeak());
        System.out.println("通过 Animal 引用调用（仍绑定到 Animal）: " + dog.staticSpeak());
        System.out.println("通过 Dog 类名调用: " + Dog.staticSpeak());

        // 对照：实例方法才是运行期分派
        System.out.println("同一个引用的实例方法则是运行期分派: " + dog.speak());
    }

    /**
     * 构造器调用链
     * <p>
     * 子类构造器的第一行总有 {@code super(...)}——没写就由编译器插入无参的 {@code super()}。
     * 因此父类的字段初始化与构造逻辑一定先于子类完成；这也是「不要在构造器里调用可被子类重写的方法」的原因：
     * 那时子类的字段还没初始化，重写方法读到的是默认值。
     */
    public static void constructorChain() {
        System.out.println("--- 创建 GrandChild（三层继承） ---");
        new GrandChild();

        // 父类没有无参构造器时，子类必须显式写出 super(参数)，否则编译不通过
        System.out.println("--- 创建 ExplicitChild（父类无无参构造器） ---");
        new ExplicitChild();
    }

    /**
     * 向下转型必须先用 instanceof 判断
     */
    public static void downcast() {
        Animal animal = new Dog();

        System.out.println("animal instanceof Dog: " + (animal instanceof Dog));
        System.out.println("animal instanceof Cat: " + (animal instanceof Cat));

        if (animal instanceof Dog) {
            Dog dog = (Dog) animal;
            System.out.println("判断后转型成功: " + dog.speak());
        }

        // 不判断直接转型，运行时抛 ClassCastException
        try {
            Cat cat = (Cat) animal;
            System.out.println("不会执行到这里: " + cat);
        } catch (ClassCastException e) {
            System.out.println("不判断直接转型抛出: " + e.getClass().getSimpleName());
        }

        // instanceof 对 null 恒为 false，因此它同时起到了判空作用
        Animal nothing = null;
        System.out.println("null instanceof Dog: " + (nothing instanceof Dog));
        // JDK 16 起可写成 if (animal instanceof Dog matched) { matched.speak(); }，
        // 把判断与转型合成一步，省去重复书写类型名
    }

    /**
     * 依次演示重写与重载、向上转型、静态绑定、构造链与向下转型
     */
    public static void demo() {
        override();
        upcastAndDispatch();
        fieldAndStaticBinding();
        constructorChain();
        downcast();
    }

    public static void main(String[] args) {
        demo();
    }

    // ==================== 多态与静态绑定 ====================

    /**
     * 父类：字段与静态方法都用于演示「不参与多态」
     */
    static class Animal {

        String name = "动物";

        String speak() {
            return "...";
        }

        static String staticSpeak() {
            return "Animal 的静态方法";
        }

    }

    /**
     * 子类：同名字段隐藏了父类字段，同名静态方法也隐藏了父类的静态方法
     */
    static class Dog extends Animal {

        String name = "狗";

        @Override
        String speak() {
            return "汪汪";
        }

        static String staticSpeak() {
            return "Dog 的静态方法";
        }

    }

    static class Cat extends Animal {

        @Override
        String speak() {
            return "喵喵";
        }

    }

    // ==================== 重载与重写 ====================

    /**
     * 父类提供两个重载方法
     */
    static class OverloadBase {

        String call(Object o) {
            return "call(Object) —— 被子类重写";
        }

        String call(String s) {
            return "call(String) —— 子类没有重写";
        }

    }

    /**
     * 子类只重写 {@code call(Object)}，因此选中哪个重载由编译期的静态类型决定
     */
    static class OverloadSub extends OverloadBase {

        @Override
        String call(Object o) {
            return "子类重写后的 call(Object)";
        }

    }

    // ==================== 协变返回类型与访问权限 ====================

    static class Base {

        protected Base copy() {
            return new Base();
        }

        protected String protectedMethod() {
            return "父类的 protected 方法";
        }

    }

    /**
     * 子类把返回类型收窄为 {@code Sub}（协变），并把访问权限从 protected 放宽到 public
     */
    static class Sub extends Base {

        @Override
        protected Sub copy() {
            return new Sub();
        }

        @Override
        public String protectedMethod() {
            return "子类把 protected 放宽为 public";
        }

    }

    // ==================== 构造器链 ====================

    static class Parent {

        Parent() {
            System.out.println("1. Parent 无参构造器");
        }

    }

    static class Child extends Parent {

        Child() {
            // 编译器在这里自动插入 super()，它必须是构造器的第一条语句
            System.out.println("2. Child 构造器");
        }

    }

    static class GrandChild extends Child {

        GrandChild() {
            System.out.println("3. GrandChild 构造器");
        }

    }

    /**
     * 没有无参构造器的父类
     */
    static class StrictParent {

        StrictParent(String msg) {
            System.out.println("1. StrictParent 构造器: " + msg);
        }

    }

    /**
     * 父类没有无参构造器时，子类构造器必须显式调用 {@code super(参数)}
     */
    static class ExplicitChild extends StrictParent {

        ExplicitChild() {
            super("必须显式调用");
            System.out.println("2. ExplicitChild 构造器");
        }

    }

}
