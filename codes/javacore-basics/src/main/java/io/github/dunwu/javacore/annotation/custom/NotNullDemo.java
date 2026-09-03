package io.github.dunwu.javacore.annotation.custom;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-31
 */
public class NotNullDemo {

    /**
     * 演示自定义注解 {@code @NotNull} 的运行期校验
     * <p>
     * {@link NotNullUtil} 通过反射遍历字段，用 {@code isAnnotationPresent} 判断字段是否被 {@code @NotNull} 修饰，
     * 再 {@code setAccessible(true)} 读取私有字段的值，为 null 时抛出 {@code NullPointerException}。
     * 这正是 Bean Validation（如 Hibernate Validator）的基本原理：注解本身不产生行为，必须由处理器配合
     */
    public static void demo() {
        // 反例：被 @NotNull 修饰的 id 传入 null，校验失败
        check(new MyBean(null, "jack"), "id 为 null");
        // 正例：id 不为 null，校验通过（未标 @NotNull 的 name 不参与校验）
        check(new MyBean(1, "jack"), "id 不为 null");
    }

    private static void check(MyBean myBean, String scene) {
        try {
            NotNullUtil.check(myBean);
            System.out.println(scene + "：校验通过");
        } catch (Exception e) {
            System.out.println(scene + "：校验失败 -> " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        demo();
    }

    static class MyBean {

        @NotNull
        private Integer id;

        private String name;

        public MyBean(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
