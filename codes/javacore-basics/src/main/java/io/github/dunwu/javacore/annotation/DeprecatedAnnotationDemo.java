package io.github.dunwu.javacore.annotation;

/**
 * "@Deprecated" 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-30
 */
public class DeprecatedAnnotationDemo {

    public static void main(String[] args) {
        System.out.println(DeprecatedField.DEPRECATED_FIELD);

        DeprecatedMethod dm = new DeprecatedMethod();
        System.out.println(dm.print());

        DeprecatedClass dc = new DeprecatedClass();
        System.out.println(dc.print());
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
