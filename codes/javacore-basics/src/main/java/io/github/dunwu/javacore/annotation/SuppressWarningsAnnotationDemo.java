package io.github.dunwu.javacore.annotation;

/**
 * "@SuppressWarnings" 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-30
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SuppressWarningsAnnotationDemo {

    @SuppressWarnings({ "deprecation" })
    public static void main(String[] args) {
        SuppressDemo d = new SuppressDemo();
        d.setValue("南京");
        System.out.println("地名：" + d.getValue());
    }

    static class SuppressDemo<T> {

        private T value;

        public T getValue() {
            return this.value;
        }

        public void setValue(T var) {
            this.value = var;
        }

    }

}
