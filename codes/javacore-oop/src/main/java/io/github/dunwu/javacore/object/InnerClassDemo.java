package io.github.dunwu.javacore.object;

/**
 * 示例：四类内部类（嵌套类）。
 * <p>
 * Java 把「定义在另一个类内部的类」统称为嵌套类，按两个维度分成四种：
 * <ul>
 *     <li><b>① 成员内部类</b>（{@code class Inner}，不带 static）—— 依附于外部类的<b>实例</b>，
 *     隐含持有 {@code Outer.this} 引用，因此能直接访问外部类的私有实例字段。
 *     代价是外部对象无法先于它被回收，<b>容易造成内存泄漏</b></li>
 *     <li><b>② 静态内部类</b>（{@code static class Nested}）—— 只依附于外部类的<b>类</b>，
 *     不持有外部实例引用，因此只能访问外部类的静态成员。没有泄漏风险，是默认应当选择的写法，
 *     Builder、Holder 等都用它</li>
 *     <li><b>③ 局部内部类</b>（定义在方法体内）—— 作用域仅限于该方法，不能加访问修饰符，
 *     只能捕获 <b>final 或事实 final</b> 的局部变量</li>
 *     <li><b>④ 匿名内部类</b>（{@code new 接口/类() { ... }}）—— 没有名字，定义的同时完成实例化，
 *     适合只用一次的实现；捕获变量的规则与局部内部类相同。它的 {@code this} 指向自身，
 *     这一点与 lambda 不同（详见 javacore-newjdk 的 {@code jdk8/lambda/EffectivelyFinalDemo}）</li>
 * </ul>
 * <b>选择建议</b>：能用静态内部类就用静态内部类；只在一个方法里用则考虑局部内部类；
 * 一次性实现接口用匿名内部类（JDK 8 之后多数场景可以直接换成 lambda）。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class InnerClassDemo {

    /**
     * ① 成员内部类：依附于外部类实例，可访问外部私有字段
     */
    public static void memberInner() {
        Outer outer = new Outer("外部实例 A 的私有字段");
        // 成员内部类不能独立创建，必须先有外部类实例，语法是 outer.new Member()
        Outer.Member member = outer.new Member();
        System.out.println("成员内部类读到外部私有字段: " + member.readOuterSecret());
        System.out.println("成员内部类自己的字段: " + member);
        System.out.println("成员内部类持有的外部实例是同一个: " + (member.outerRef() == outer));

        // 依附于不同外部实例的成员内部类，各自读到不同的值
        Outer another = new Outer("外部实例 B 的私有字段");
        System.out.println("换一个外部实例后读到: " + another.new Member().readOuterSecret());
    }

    /**
     * ② 静态内部类：不依附外部实例，Builder 是它最典型的用法
     */
    public static void staticNested() {
        // 创建方式与普通类一样，不需要外部实例
        Outer.Nested nested = new Outer.Nested();
        System.out.println("静态内部类读到静态字段: " + nested.readStaticValue());

        // Builder 必须声明为 static：它不需要外部实例的状态，
        // 若漏写 static，每次 new Builder() 都得先持有一个 Outer 实例，用法就很荒谬了
        Outer built = new Outer.Builder().secret("由 Builder 构造").build();
        System.out.println("Builder 构造出的外部对象字段: " + built.readSecret());
    }

    /**
     * ③ 局部内部类：定义在方法体内，捕获事实 final 的局部变量
     */
    public static void localInner() {
        // 这两个局部变量赋值后不再改变，属于「事实 final」，因此可以被局部内部类捕获。
        // 捕获的是值的副本，所以在内部类里给它们重新赋值会编译失败。
        String prefix = "局部内部类";
        int counter = 10;

        // 局部内部类不能加 public / private / static 等修饰符，作用域仅限于本方法
        class LocalTask {

            String run() {
                return prefix + " 读到了 counter = " + counter;
            }

        }

        System.out.println(new LocalTask().run());

        // 定义在「实例方法」里的局部内部类，还能访问外部类的实例字段
        Outer outer = new Outer("外部实例的私有字段");
        System.out.println("实例方法里的局部内部类读到: " + outer.callLocalInner());
    }

    /**
     * ④ 匿名内部类：没有名字，定义即实例化
     */
    public static void anonymous() {
        // 实现接口：JDK 8 之前这是唯一能给接口「就地」提供实现的方式
        Runnable task = new Runnable() {

            @Override
            public void run() {
                System.out.println("匿名内部类实现的 Runnable");
            }

        };
        task.run();

        // 匿名内部类的 this 指向它自己，而不是外部类；lambda 的 this 则指向定义它的外部类实例
        Runnable showingThis = new Runnable() {

            @Override
            public void run() {
                System.out.println("匿名内部类中 this 指向自身: " + (this instanceof Runnable));
            }

        };
        showingThis.run();

        // 继承抽象类：匿名子类必须实现全部抽象方法，同时也能调用父类已有的方法
        Greeter greeter = new Greeter("你好") {

            @Override
            String greeting() {
                return "我是匿名子类";
            }

        };
        System.out.println("匿名子类实现了抽象方法: " + greeter.greet());
    }

    /**
     * 依次演示成员内部类、静态内部类、局部内部类与匿名内部类
     */
    public static void demo() {
        memberInner();
        staticNested();
        localInner();
        anonymous();
    }

    public static void main(String[] args) {
        demo();
    }

    /**
     * 供匿名内部类继承的抽象类
     */
    static abstract class Greeter {

        private final String word;

        Greeter(String word) {
            this.word = word;
        }

        /**
         * 留给子类实现的抽象方法
         */
        abstract String greeting();

        String greet() {
            return word + "，" + greeting();
        }

    }

    /**
     * 承载四类内部类的外部类
     */
    static class Outer {

        /**
         * 静态字段：静态内部类与成员内部类都能访问
         */
        private static final String STATIC_VALUE = "外部类的静态字段";

        /**
         * 实例字段：只有依附于实例的内部类（成员 / 局部 / 匿名）能直接访问
         */
        private final String secret;

        Outer(String secret) {
            this.secret = secret;
        }

        String readSecret() {
            return secret;
        }

        /**
         * 定义在实例方法里的局部内部类，可以访问外部类的实例字段
         */
        String callLocalInner() {
            class Local {

                String read() {
                    return secret;
                }

            }
            return new Local().read();
        }

        /**
         * ① 成员内部类：不带 static，每个实例都隐含持有外部类实例的引用
         */
        class Member {

            /**
             * 成员内部类可以有自己的实例字段，但不能声明 static 字段（编译期常量除外）
             */
            private final String own = "成员内部类自己的字段";

            String readOuterSecret() {
                // 直接访问外部类的 private 字段，编译器通过合成方法实现这一访问
                return secret;
            }

            Outer outerRef() {
                // Outer.this 显式取得所依附的外部类实例——这就是内存泄漏的根源
                return Outer.this;
            }

            @Override
            public String toString() {
                return own;
            }

        }

        /**
         * ② 静态内部类：带 static，不持有外部实例引用
         */
        static class Nested {

            String readStaticValue() {
                return STATIC_VALUE;
            }

            // 这里若写 return secret; 会编译失败——静态内部类没有外部实例可依附。
            // 反过来说，正因如此它才不会把外部对象的生命周期拉长，也就不会泄漏。

        }

        /**
         * ② 静态内部类的实际应用：Builder
         */
        static class Builder {

            private String secret;

            Builder secret(String secret) {
                this.secret = secret;
                return this;
            }

            Outer build() {
                return new Outer(secret);
            }

        }

    }

}
