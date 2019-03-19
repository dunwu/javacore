package io.github.dunwu.javacore.enumeration;

/**
 * 利用枚举实现单例
 * @author Zhang Peng
 * @date 2019-03-19
 */
public class SingleEnumDemo {
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

    public static void main(String[] args) {
        SingleEn.INSTANCE.setName("zp");
        System.out.println(SingleEn.INSTANCE.getName());
    }
}
