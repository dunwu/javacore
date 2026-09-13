package io.github.dunwu.javacore.jvm.classloader.exception;

/**
 * UnsatisfiedLinkError 示例
 * <p>
 * 静态初始化块中故意加载一个并不存在的本地库 "NoLib"，从而触发
 * java.lang.UnsatisfiedLinkError。关键在于：错误发生在类初始化（{@code <clinit>}）阶段，
 * 而不是调用 native 方法时，所以 main 中“new 出对象”这个动作本身就足以让程序崩溃。
 * <p>
 * VM Args: 无需任何 VM 参数。NoLib 本就不存在，设置 -Djava.library.path 也无法避免该错误。
 * <p>
 * 运行结果（JDK 21 实测，java.library.path 部分随环境而异）：
 *
 * <pre>
 * Exception in thread "main" java.lang.UnsatisfiedLinkError: no NoLib in java.library.path: ...
 *     at java.base/java.lang.ClassLoader.loadLibrary(ClassLoader.java:2458)
 *     at java.base/java.lang.Runtime.loadLibrary0(Runtime.java:916)
 *     at java.base/java.lang.System.loadLibrary(System.java:2064)
 *     at ...UnsatisfiedLinkErrorDemo.&lt;clinit&gt;(UnsatisfiedLinkErrorDemo.java:14)
 * </pre>
 *
 * 注意抛出的是 UnsatisfiedLinkError（Error 而非 Exception），因此不会被包装成
 * ExceptionInInitializerError，而是直接向上传播。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-03-07
 */
public class UnsatisfiedLinkErrorDemo {

    public native void nativeMethod();

    static {
        System.loadLibrary("NoLib");
    }

    public static void main(String[] args) {
        new UnsatisfiedLinkErrorDemo().nativeMethod();
    }

}
