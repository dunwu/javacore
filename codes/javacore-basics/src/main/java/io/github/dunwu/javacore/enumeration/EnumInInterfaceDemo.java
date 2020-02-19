package io.github.dunwu.javacore.enumeration;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-19
 */
public class EnumInInterfaceDemo {

    public static void main(String[] args) {
        for (Plant.Fruit f : Plant.Fruit.values()) {
            System.out.println(f.getDescription());
        }
    }

    public interface INumberEnum {

        int getCode();

        String getDescription();

    }

    public interface Plant {

        enum Vegetable implements INumberEnum {

            POTATO(0, "土豆"),
            TOMATO(0, "西红柿");

            private int code;

            private String description;

            Vegetable(int number, String description) {
                this.code = number;
                this.description = description;
            }

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

            private int code;

            private String description;

            Fruit(int number, String description) {
                this.code = number;
                this.description = description;
            }

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

}
// Output:
// 苹果
// 桔子
// 香蕉
