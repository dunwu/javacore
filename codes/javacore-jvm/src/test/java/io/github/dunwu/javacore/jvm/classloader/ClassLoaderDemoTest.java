package io.github.dunwu.javacore.jvm.classloader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * classloader 包类加载与类初始化示例单元测试
 * <p>
 * <b>重要前提</b>：类的初始化（{@code <clinit>}）在同一个 JVM 中只会执行一次。因此涉及初始化输出的断言
 * 必须放在同一个测试方法内按固定顺序执行，且每个类只能被一个测试方法触发初始化，否则断言会因执行顺序而失效。
 * <p>
 * <b>刻意不覆盖</b>：{@code DeadLoopClassDemo} —— 它的静态初始化块是 {@code while(true)} 死循环，
 * 用于演示「一个线程在类初始化中死循环会导致其他线程永久阻塞」。执行后会留下两个永不结束的非守护线程，
 * 将挂起整个测试 JVM，因此只能单独运行它的 main 方法观察。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("类加载与类初始化示例测试")
public class ClassLoaderDemoTest {

    @FunctionalInterface
    private interface ThrowingRunnable {

        void run() throws Exception;
    }

    private static String captureOutput(ThrowingRunnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test
    @DisplayName("被动引用三种情形：子类引用父类静态字段、数组定义、编译期常量，均不触发目标类初始化")
    void testPassiveReference() {
        // 三个示例必须在同一次输出捕获中按 01 -> 02 -> 03 的顺序执行：
        // SuperClass 只会被初始化一次，若顺序颠倒，01 将不再有初始化输出
        String output = captureOutput(() -> {
            PassiveRefDemo01.main(new String[0]);
            PassiveRefDemo02.main(new String[0]);
            PassiveRefDemo03.main(new String[0]);
        });
        assertThat(output).isEqualTo(
            // 01：SubClass.value 读取的其实是父类 SuperClass 的静态字段，
            // 只初始化 SuperClass（输出 SuperClass init!），不初始化 SubClass（无 SubClass init!）
            "SuperClass init!\n"
                + "123\n"
                // 02：new SuperClass[10] 只创建数组对象，不触发 SuperClass 初始化，故无任何输出
                // 03：ConstClass.value 是编译期常量，已内联进调用方常量池，
                // 不触发 ConstClass 初始化（无 ConstClass init!），直接输出常量值
                + "hello world\n");
    }

    @Test
    @DisplayName("类初始化顺序：初始化子类必先初始化父类，父类静态块按声明顺序执行")
    void testParentAndSon() {
        // Sub.B = A，而 Parent 的静态块把 A 从 1 改为 2；
        // 初始化 Sub 会先初始化 Parent，因此 B 取到的是 2 而不是 1
        assertThat(captureOutput(() -> ParentAndSon.main(new String[0]))).isEqualTo("2\n");
    }

    @Test
    @DisplayName("静态内部类单例：创建内部类数组不触发其初始化，首次访问 INSTANCE 才触发")
    void testSingletonLazyHolder() {
        // getInstance(true) 返回 new LazyHolder[2]，数组创建不触发 LazyHolder 初始化；
        // getInstance(false) 访问 LazyHolder.INSTANCE 才真正触发 <clinit>，因此输出顺序是先 ---- 后 <clinit>
        assertThat(captureOutput(() -> Singleton.main(new String[0]))).isEqualTo("----\n"
            + "LazyHolder.<clinit>\n");
    }

    @Test
    @DisplayName("字段解析：子类自有同名字段优先于父类与各层接口中的同名字段")
    void testFieldResolution() {
        // Interface0.A=0、Interface1.A=1、Interface2.A=2、Parent.A=3、Sub.A=4；
        // 字段解析先查 Sub 自身即命中，故结果为 4
        assertThat(captureOutput(() -> FieldResolution.main(new String[0]))).isEqualTo("4\n");
    }

    @Test
    @DisplayName("自定义类加载器：同一个类被不同加载器加载后互为不同类型，instanceof 判定为 false")
    void testClassLoaderDemo() {
        // 自定义加载器绕过了双亲委派，自行 defineClass 出一份新的 ClassLoaderDemo，
        // 两个 Class 对象不相等，因此 instanceof 为 false —— 这正是「类相等性 = 类加载器 + 全限定名」的体现
        String output = captureOutput(() -> ClassLoaderDemo.main(new String[0]));
        assertThat(output).isEqualTo("class io.github.dunwu.javacore.jvm.classloader.ClassLoaderDemo\n"
            + "false\n");
    }

    @Test
    @DisplayName("非法向前引用：静态块中可以给字段赋值，但读取语句写在声明之前会被编译器拒绝")
    void testIllegalForwardDemo() {
        // 字段声明处初始化为 1，随后静态块把它改为 0；读取非 final 静态字段会触发类初始化，故最终为 0
        assertThat(IllegalForwardDemo.i).isEqualTo(0);
    }

    @Test
    @DisplayName("类加载过程：线程上下文类加载器的双亲链，顶层启动类加载器在 Java 侧表现为 null")
    void test类加载过程() {
        String output = captureOutput(() -> 类加载过程.main(new String[0]));
        String[] lines = output.split("\n");
        assertThat(lines).hasSize(3);
        assertThat(lines[0]).isNotBlank();
        assertThat(lines[1]).isNotBlank();
        // 应用类加载器 -> 平台类加载器 -> 启动类加载器；
        // 启动类加载器由 C++ 实现，没有对应的 Java 对象，因此打印为 null
        assertThat(lines[2]).isEqualTo("null");
    }

}
