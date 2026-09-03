package io.github.dunwu.javacore.annotation;

/**
 * "@Deprecated" 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-30
 */
public class DeprecatedAnnotationDemo {

    /**
     * 演示 {@code @Deprecated} 修饰字段、方法、类三种目标
     * <p>
     * 被标记的成员仍可在运行期正常使用，{@code @Deprecated} 只对编译期产生告警，提醒调用方不要再使用
     */
    public static void demo() {
        System.out.println(DeprecatedField.DEPRECATED_FIELD);

        DeprecatedMethod dm = new DeprecatedMethod();
        System.out.println(dm.print());

        DeprecatedClass dc = new DeprecatedClass();
        System.out.println(dc.print());
    }

    public static void main(String[] args) {
        demo();
    }

    static class DeprecatedField {

        @Deprecated
        public static final String DEPRECATED_FIELD = "DeprecatedField";

    }

    static class DeprecatedMethod {

        @Deprecated
        public String print() {
            return "DeprecatedMethod";
        }

    }

    @Deprecated
    static class DeprecatedClass {

        public String print() {
            return "DeprecatedClass";
        }

    }

}
// Output:
// DeprecatedField
// DeprecatedMethod
// DeprecatedClass
