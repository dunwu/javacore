package io.github.dunwu.javacore.jdk16.pattern;

import java.util.List;

/**
 * Java 16 instanceof 模式匹配示例。
 * <p>
 * instanceof 模式匹配在 Java 14/15 预览、Java 16 正式转正（JEP 394）。
 * 它将"类型判断 + 强制转换 + 赋值"三步合并为一步：
 * {@code obj instanceof String s} 为 true 时，s 即为转换后的 String 变量。
 */
public class InstanceofPatternDemo {

    /**
     * 示例 1：新旧写法对比——将类型判断 + 强制转换 + 赋值三步合并为一步
     */
    public static void patternMatchingBasic() {
        List<Object> objects = List.of("Java 16", 42, List.of(1, 2), 3.14);
        for (Object obj : objects) {
            describe(obj);
        }
    }

    /**
     * 示例 2：模式变量与逻辑运算符组合（作用域由编译器流分析决定）
     */
    public static void patternWithLogicalOperators() {
        Object obj = "Hello Pattern Matching";
        if (obj instanceof String s && s.length() > 5) {
            System.out.println("长度大于 5 的字符串: " + s.toUpperCase());
        }

        // 取反时，模式变量只在能确定类型的分支内有效
        if (!(obj instanceof String s)) {
            System.out.println("不是字符串");
        } else {
            // 这里 s 依然有效
            System.out.println("取反分支外仍可访问 s: " + s.length() + " 个字符");
        }
    }

    public static void main(String[] args) {
        patternMatchingBasic();
        patternWithLogicalOperators();
    }

    /**
     * 新旧写法对比
     */
    private static void describe(Object obj) {
        // 旧写法：instanceof 判断 + 强制转换 + 赋值，三步
        if (obj instanceof String) {
            String s = (String) obj;
            System.out.println("字符串，长度 " + s.length() + ": " + s);
        }
        // 新写法：一步完成
        else if (obj instanceof Integer i) {
            System.out.println("整数: " + (i * 2));
        } else if (obj instanceof List<?> list) {
            System.out.println("列表，元素个数: " + list.size());
        } else {
            System.out.println("其他类型: " + obj.getClass().getSimpleName());
        }
    }

}
// Output:
// 字符串，长度 7: Java 16
// 整数: 84
// 列表，元素个数: 2
// 其他类型: Double
// 长度大于 5 的字符串: HELLO PATTERN MATCHING
// 取反分支外仍可访问 s: 22 个字符
