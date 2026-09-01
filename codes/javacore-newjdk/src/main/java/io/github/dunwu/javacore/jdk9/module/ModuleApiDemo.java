package io.github.dunwu.javacore.jdk9.module;

/**
 * Java 9 模块系统（JPMS）Module API 示例。
 * <p>
 * Java 9 引入模块系统（Project Jigsaw），将 JDK 拆分为 90 多个模块。
 * 每个类都归属于一个 {@link Module}：
 * <ul>
 * <li>JDK 核心类位于具名模块中（如 {@code java.base}）</li>
 * <li>未定义 module-info.java 的类路径代码位于"未命名模块"（unnamed module）</li>
 * </ul>
 * 本示例不引入 module-info.java，演示通过反射 Module API 查看模块信息，避免 JPMS 对构建的侵入。
 */
public class ModuleApiDemo {

    /**
     * 示例 1：查看 JDK 核心类所在的具名模块 java.base 及其导出的包
     */
    public static void baseModuleInfo() {
        Module baseModule = String.class.getModule();
        System.out.println("String 所属模块: " + baseModule.getName());
        System.out.println("String 模块是否为具名模块: " + baseModule.isNamed());
        System.out.println("String 模块导出的包（前 3 个）:");
        baseModule.getPackages().stream()
            .filter(baseModule::isExported)
            .sorted()
            .limit(3)
            .forEach(pkg -> System.out.println("  " + pkg));
    }

    /**
     * 示例 2：类路径代码（未定义 module-info.java）位于未命名模块
     */
    public static void unnamedModuleInfo() {
        Module appModule = ModuleApiDemo.class.getModule();
        // 未命名模块的 getName() 返回 null，toString() 输出 "unnamed module"
        System.out.println("本示例类所属模块: " + appModule);
        System.out.println("本示例类模块是否为具名模块: " + appModule.isNamed());
    }

    /**
     * 示例 3：查看当前模块层（boot layer）中包含的部分 JDK 模块
     */
    public static void bootLayerModules() {
        System.out.println("Boot Layer 中的部分 JDK 模块：");
        ModuleLayer.boot().modules().stream()
            .map(Module::getName)
            .filter(name -> name.startsWith("java."))
            .sorted()
            .limit(5)
            .forEach(name -> System.out.println("  " + name));
    }

    /**
     * 示例 4：读取当前运行的 Java 版本
     */
    public static void runtimeVersion() {
        System.out.println("java.base 版本: " + Runtime.version());
    }

    public static void main(String[] args) {
        baseModuleInfo();
        unnamedModuleInfo();
        bootLayerModules();
        runtimeVersion();
    }

}
