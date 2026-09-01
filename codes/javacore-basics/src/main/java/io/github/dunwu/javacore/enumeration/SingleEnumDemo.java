package io.github.dunwu.javacore.enumeration;

/**
 * 利用枚举实现单例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-19
 */
public class SingleEnumDemo {

    /**
     * 演示枚举单例：INSTANCE 即全局唯一实例，可持有状态。
     */
    public static void demo() {
        SingleEn.INSTANCE.setName("zp");
        System.out.println(SingleEn.INSTANCE.getName());
    }

    public static void main(String[] args) {
        demo();
    }

    public enum SingleEn {

        INSTANCE;

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
