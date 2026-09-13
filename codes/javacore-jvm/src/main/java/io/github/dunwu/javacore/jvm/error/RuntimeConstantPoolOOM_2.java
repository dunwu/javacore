package io.github.dunwu.javacore.jvm.error;

/**
 * {@link String#intern()} 行为差异示例。
 * <p>
 * 注意：类名带有 OOM，但本类并不会抛出 OutOfMemoryError，也无需任何 VM 参数，直接运行即可。
 * 它与 {@link RuntimeConstantPoolOOM_1} 成对出现：后者演示 JDK 6 下 intern() 会把字符串
 * 实例复制到永久代从而撑爆永久代；本类则用于对比 JDK 7 之后 intern() 的语义变化——
 * 字符串常量池不再保存副本，而只保存堆中已有对象的引用。
 * <p>
 * 运行结果（JDK 21 / Temurin 21.0.12 实测，连续 3 次一致）：
 *
 * <pre>
 * true
 * true
 * </pre>
 *
 * 第一行为 true：“计算机软件”首次出现，intern() 记录的就是 str1 自身的引用。
 * 第二行为 true：本次运行中“java”尚未进入字符串常量池，intern() 同样记录了 str2 自身的引用。
 * <p>
 * 需要特别注意：《深入理解 Java 虚拟机》中此例第二行的结论是 false，那是基于早期 JDK 7
 * （彼时“java”已在 JVM 启动过程中被 intern）。因此本示例的输出依赖 JDK 版本，
 * 不应作为断言依据。
 */
public class RuntimeConstantPoolOOM_2 {

    public static void main(String[] args) {
        String str1 = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str1.intern() == str1);

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2);
    }
}
