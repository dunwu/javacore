package io.github.dunwu.javacore.enumeration;

/**
 * @author Zhang Peng
 * @date 2019-03-19
 */
public class EnumInClassDemo {
    public interface INumberEnum {
        int getCode();
        String getDescription();
    }

    public static class Plant2 {
        enum Vegetable implements INumberEnum {
            POTATO(0, "土豆"),
            TOMATO(0, "西红柿");

            Vegetable(int number, String description) {
                this.code = number;
                this.description = description;
            }

            private int code;
            private String description;

            @Override
            public int getCode() {
                return this.code;
            }

            @Override
            public String getDescription() {
                return this.description;
            }
        }
        enum Fruit implements INumberEnum {
            APPLE(0, "苹果"),
            ORANGE(0, "桔子"),
            BANANA(0, "香蕉");

            Fruit(int number, String description) {
                this.code = number;
                this.description = description;
            }

            private int code;
            private String description;

            @Override
            public int getCode() {
                return this.code;
            }

            @Override
            public String getDescription() {
                return this.description;
            }
        }
    }

    public static void main(String[] args) {
        for (Plant2.Vegetable v : Plant2.Vegetable.values()) {
            System.out.println(v.getDescription());
        }
    }
}
// Output:
// 土豆
// 西红柿
