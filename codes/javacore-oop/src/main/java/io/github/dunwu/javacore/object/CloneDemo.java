package io.github.dunwu.javacore.object;

/**
 * 示例：{@code clone} 与浅拷贝 / 深拷贝。
 * <p>
 * {@code Object.clone()} 是一个 {@code protected native} 方法，它做的是<b>逐字段的浅拷贝</b>：
 * 基本类型字段复制值，引用类型字段只复制引用（两个对象的该字段指向同一个实例）。
 * 要调用它还必须先实现 {@link Cloneable} —— 一个<b>没有任何方法的标记接口</b>，
 * 不实现就会抛 {@code CloneNotSupportedException}。
 * <p>
 * <b>为什么《Effective Java》item 13 建议改用拷贝构造器</b>：
 * <ul>
 *     <li>{@code Cloneable} 用「是否实现某接口」来决定一个 protected 方法能否成功，属于异常的设计，
 *     违背了接口的正常用法</li>
 *     <li>{@code clone} 绕过构造器直接分配内存，因此 <b>final 字段无法正常初始化</b>，
 *     也会跳过构造器里的校验逻辑</li>
 *     <li>浅拷贝带来的「两个对象共享同一个可变子对象」是最常见的事故来源，见 {@link #shallowClone()}</li>
 *     <li>拷贝构造器 / 静态工厂没有上述限制，且能明确表达「这是一次深拷贝还是浅拷贝」</li>
 * </ul>
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class CloneDemo {

    /**
     * 未实现 Cloneable 就调用 clone()，会抛 CloneNotSupportedException
     */
    public static void notCloneable() {
        Plain plain = new Plain("张三", 30);
        try {
            plain.tryClone();
            System.out.println("不会执行到这里");
        } catch (CloneNotSupportedException e) {
            System.out.println("未实现 Cloneable 时抛出的异常: " + e.getClass().getSimpleName());
        }
    }

    /**
     * 浅拷贝：引用字段被两个对象共享，修改克隆体会连带影响原对象
     */
    public static void shallowClone() throws CloneNotSupportedException {
        ShallowPerson origin = new ShallowPerson("张三", 30, new Address("南京"));
        ShallowPerson copy = origin.clone();

        System.out.println("克隆后是两个不同的对象: " + (origin != copy));
        // 基本类型与 String（不可变）字段是各自独立的，修改互不影响
        copy.setName("李四");
        System.out.println("修改克隆体的 name 后，原对象的 name: " + origin.getName());
        System.out.println("克隆体的 name: " + copy.getName());

        // 引用字段只复制了引用：两个对象的 address 指向同一个实例
        System.out.println("两个对象的 address 是同一个实例: " + (origin.getAddress() == copy.getAddress()));
        // 于是修改克隆体的 address，原对象也跟着变——这就是浅拷贝的典型事故
        copy.getAddress().setCity("北京");
        System.out.println("修改克隆体的城市后，原对象的城市: " + origin.getAddress().getCity());
    }

    /**
     * 深拷贝：在 clone() 中把可变字段也复制一份，两个对象彻底独立
     */
    public static void deepClone() throws CloneNotSupportedException {
        DeepPerson origin = new DeepPerson("张三", 30, new Address("南京"));
        DeepPerson copy = origin.clone();

        System.out.println("两个对象的 address 是同一个实例: " + (origin.getAddress() == copy.getAddress()));
        copy.getAddress().setCity("北京");
        System.out.println("修改克隆体的城市后，原对象的城市: " + origin.getAddress().getCity());
        System.out.println("克隆体的城市: " + copy.getAddress().getCity());
    }

    /**
     * 更推荐的做法：拷贝构造器
     */
    public static void copyConstructor() {
        Person origin = new Person("张三", 30, new Address("南京"));
        // 拷贝构造器不依赖 Cloneable，可以正常给 final 字段赋值，也一定会走构造器的校验逻辑
        Person copy = new Person(origin);

        System.out.println("拷贝构造器得到的是独立对象: " + (origin != copy));
        copy.getAddress().setCity("北京");
        System.out.println("修改副本的城市后，原对象的城市: " + origin.getAddress().getCity());
        System.out.println("副本的城市: " + copy.getAddress().getCity());
        System.out.println("final 字段可以正常赋值: " + (origin.getName() != null && copy.getName() != null));
    }

    /**
     * 依次演示 Cloneable 的约束、浅拷贝的隐患、深拷贝的修正与拷贝构造器
     */
    public static void demo() throws CloneNotSupportedException {
        notCloneable();
        shallowClone();
        deepClone();
        copyConstructor();
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        demo();
    }

    /**
     * 可变的引用字段，用来暴露浅拷贝共享同一实例的问题
     */
    static class Address {

        private String city;

        Address(String city) {
            this.city = city;
        }

        String getCity() {
            return city;
        }

        void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return city;
        }

    }

    /**
     * 未实现 Cloneable 的类。
     * <p>
     * {@code Plain} 直接继承 {@code Object}，因此 {@code super.clone()} 就是 {@code Object.clone()}，
     * 调用它会因为缺少 {@code Cloneable} 标记而失败。
     */
    static class Plain {

        private final String name;

        private final int age;

        Plain(String name, int age) {
            this.name = name;
            this.age = age;
        }

        Object tryClone() throws CloneNotSupportedException {
            return super.clone();
        }

    }

    /**
     * 浅拷贝：直接返回 {@code super.clone()} 的结果，不处理引用字段
     */
    static class ShallowPerson implements Cloneable {

        private String name;

        private int age;

        private Address address;

        ShallowPerson(String name, int age, Address address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        Address getAddress() {
            return address;
        }

        /**
         * 返回类型可以收窄为 {@code ShallowPerson}（协变返回类型），调用方就不必再强转
         */
        @Override
        protected ShallowPerson clone() throws CloneNotSupportedException {
            return (ShallowPerson) super.clone();
        }

    }

    /**
     * 深拷贝：在 clone() 中把可变的引用字段也重新构造一份
     */
    static class DeepPerson implements Cloneable {

        private String name;

        private int age;

        private Address address;

        DeepPerson(String name, int age, Address address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        Address getAddress() {
            return address;
        }

        @Override
        protected DeepPerson clone() throws CloneNotSupportedException {
            DeepPerson copy = (DeepPerson) super.clone();
            // 关键的一步：可变字段必须单独复制，否则仍是浅拷贝
            copy.address = new Address(this.address.getCity());
            return copy;
        }

    }

    /**
     * 用拷贝构造器代替 clone 的写法：字段可以是 final，也不必实现 Cloneable
     */
    static class Person {

        private final String name;

        private final int age;

        private final Address address;

        Person(String name, int age, Address address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        /**
         * 拷贝构造器
         */
        Person(Person other) {
            this.name = other.name;
            this.age = other.age;
            // 可变对象需要新建一份，效果等同于深拷贝
            this.address = new Address(other.address.getCity());
        }

        String getName() {
            return name;
        }

        Address getAddress() {
            return address;
        }

    }

}
