package io.github.dunwu.javacore.annotation;

/**
 * "@SuppressWarnings" 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-30
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SuppressWarningsAnnotationDemo {

    /**
     * 演示 {@code @SuppressWarnings} 压制告警
     * <p>
     * 类级别压制 {@code rawtypes}/{@code unchecked}（下面使用了泛型类 {@code SuppressDemo} 的原始类型），
     * 方法级别压制 {@code deprecation}。注意它只影响编译告警，不改变任何运行期行为
     */
    @SuppressWarnings({ "deprecation" })
    public static void demo() {
        SuppressDemo d = new SuppressDemo();
        d.setValue("南京");
        System.out.println("地名：" + d.getValue());
    }

    public static void main(String[] args) {
        demo();
    }
    // Output:
    // 地名：南京

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
