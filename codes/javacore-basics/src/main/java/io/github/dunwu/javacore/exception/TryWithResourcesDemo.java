package io.github.dunwu.javacore.exception;

/**
 * try-with-resources 示例
 * <p>
 * Java 7 引入。凡是实现了 {@link AutoCloseable}（或其子接口 {@link java.io.Closeable}）的对象，都可以声明在
 * {@code try} 后的小括号里，编译器会在 try 块结束时<b>自动调用 close()</b>，不必再手写 {@code finally} 去关闭。
 * <p>
 * 它的价值不只是少写几行代码，更关键的是解决了「原始异常被 close 异常覆盖」的问题：
 * 对比 {@link #suppressedException()} 与 {@link #compareWithFinally()} 的输出，后者会让业务异常彻底消失，
 * 排查问题时根本看不到真正的错误原因。{@link FinallyOverrideExceptionDemo} 演示的也是同一类问题。
 * <ul>
 *     <li>{@link #autoClose()} —— 基本用法：正常路径与异常路径都会关闭资源</li>
 *     <li>{@link #closeOrder()} —— 声明多个资源时，关闭顺序与声明顺序<b>相反</b></li>
 *     <li>{@link #suppressedException()} —— try 块与 close() 都抛异常时，主异常保留，close 异常挂到 suppressed</li>
 *     <li>{@link #compareWithFinally()} —— 对照：手写 finally 关闭会让原始异常被彻底替换</li>
 * </ul>
 * <p>
 * Java 9 起，小括号里还可以直接写「已声明的 final / effectively final 变量」，不必在括号内重新声明，
 * 见 javacore-newjdk 模块的 {@code jdk9/trywith/ImprovedTryWithResourcesDemo}。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class TryWithResourcesDemo {

    /**
     * 基本用法：无论 try 块正常结束还是中途抛异常，close() 都一定会执行
     */
    public static void autoClose() {
        // 正常路径：try 块执行完毕，编译器自动插入 close() 调用
        try (DemoResource resource = new DemoResource("A")) {
            resource.use();
        } catch (Exception e) {
            System.out.println("正常路径不会走到这里");
        }

        // 异常路径：try 块中途抛异常，close() 依然先于 catch 执行
        try (DemoResource resource = new DemoResource("B")) {
            resource.use();
            throw new IllegalStateException("B 使用中途出错");
        } catch (Exception e) {
            System.out.println("捕获到: " + e.getMessage());
        }
    }

    /**
     * 声明多个资源时，关闭顺序与声明顺序相反
     * <p>
     * 这个顺序是必要的：资源之间常有依赖关系（例如先打开的文件通道再打开的锁），后获取的必须先释放。
     */
    public static void closeOrder() {
        try (DemoResource r1 = new DemoResource("R1");
             DemoResource r2 = new DemoResource("R2")) {
            r1.use();
            r2.use();
        } catch (Exception e) {
            System.out.println("不会走到这里");
        }
    }

    /**
     * try 块与 close() 同时抛异常：主异常保留，close() 的异常被「抑制」
     * <p>
     * 被抑制的异常不会丢失，可以通过 {@link Throwable#getSuppressed()} 取到，
     * 打印堆栈时也会以 {@code Suppressed:} 的形式附在主异常下面。
     */
    public static void suppressedException() {
        // 资源 S 的 close() 被设定为抛异常，try 块内也主动抛异常
        try (DemoResource resource = new DemoResource("S", true)) {
            resource.use();
            throw new IllegalStateException("业务异常");
        } catch (Exception e) {
            System.out.println("主异常: " + e.getMessage());
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("被抑制的异常: " + suppressed.getMessage());
            }
        }
    }

    /**
     * 对照：不用 try-with-resources，close() 的异常会<b>替换</b>掉 try 块的异常
     * <p>
     * 输出里只剩「F 关闭失败」，真正的业务异常没有任何痕迹（suppressed 个数为 0）。
     * 这是手写 {@code finally { resource.close(); }} 最大的隐患，也是应该优先使用 try-with-resources 的原因。
     */
    public static void compareWithFinally() {
        try {
            closeManually();
        } catch (Exception e) {
            System.out.println("最终抛出的异常: " + e.getMessage());
            System.out.println("被抑制的异常个数: " + e.getSuppressed().length);
        }
    }

    /**
     * 传统写法：在 finally 中手动关闭资源
     */
    private static void closeManually() throws Exception {
        DemoResource resource = new DemoResource("F", true);
        try {
            resource.use();
            throw new IllegalStateException("业务异常");
        } finally {
            // close() 在此抛出的异常会直接覆盖 try 块的「业务异常」
            resource.close();
        }
    }

    /**
     * 依次演示自动关闭、关闭顺序、异常抑制，以及与手写 finally 的对照
     */
    public static void demo() {
        autoClose();
        closeOrder();
        suppressedException();
        compareWithFinally();
    }

    public static void main(String[] args) {
        demo();
    }

    /**
     * 演示用的资源。
     * <p>
     * 关闭时是否抛异常可配置，用来对比「异常被抑制」与「异常被覆盖」两种结果。
     */
    static class DemoResource implements AutoCloseable {

        private final String name;

        private final boolean throwOnClose;

        DemoResource(String name) {
            this(name, false);
        }

        DemoResource(String name, boolean throwOnClose) {
            this.name = name;
            this.throwOnClose = throwOnClose;
            System.out.println("创建资源 " + name);
        }

        void use() {
            System.out.println("使用资源 " + name);
        }

        @Override
        public void close() throws Exception {
            System.out.println("关闭资源 " + name);
            if (throwOnClose) {
                throw new Exception(name + " 关闭失败");
            }
        }

    }
    // Output:
    // 创建资源 A
    // 使用资源 A
    // 关闭资源 A
    // 创建资源 B
    // 使用资源 B
    // 关闭资源 B
    // 捕获到: B 使用中途出错
    // 创建资源 R1
    // 创建资源 R2
    // 使用资源 R1
    // 使用资源 R2
    // 关闭资源 R2
    // 关闭资源 R1
    // 创建资源 S
    // 使用资源 S
    // 关闭资源 S
    // 主异常: 业务异常
    // 被抑制的异常: S 关闭失败
    // 创建资源 F
    // 使用资源 F
    // 关闭资源 F
    // 最终抛出的异常: F 关闭失败
    // 被抑制的异常个数: 0
}
