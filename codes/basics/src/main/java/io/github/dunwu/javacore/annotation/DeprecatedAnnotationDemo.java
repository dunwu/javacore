package io.github.dunwu.javacore.annotation;

/**
 * "@Deprecated" 示例
 * @author Zhang Peng
 * @date 2019-03-30
 */
public class DeprecatedAnnotationDemo {
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

    public static void main(String[] args) {
        System.out.println(DeprecatedField.DEPRECATED_FIELD);

        DeprecatedMethod dm = new DeprecatedMethod();
        System.out.println(dm.print());


        DeprecatedClass dc = new DeprecatedClass();
        System.out.println(dc.print());
    }
}
//Output:
//DeprecatedField
//DeprecatedMethod
//DeprecatedClass
