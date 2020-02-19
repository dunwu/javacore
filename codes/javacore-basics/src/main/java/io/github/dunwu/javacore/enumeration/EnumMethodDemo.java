package io.github.dunwu.javacore.enumeration;

/**
 * 本例展示枚举各种方法的使用
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016/11/24.
 */
public class EnumMethodDemo {

    public static void main(String[] args) {
        System.out.println("=========== Print all Color ===========");
        for (Color c : Color.values()) {
            System.out.println(c + " ordinal: " + c.ordinal());
        }
        System.out.println("=========== Print all Size ===========");
        for (Size s : Size.values()) {
            System.out.println(s + " ordinal: " + s.ordinal());
        }

        Color green = Color.GREEN;
        System.out.println("green name(): " + green.name());
        System.out.println("green valueOf(): " + Color.valueOf(Color.class, "BLUE"));
        System.out.println("green getDeclaringClass(): " + green.getDeclaringClass());
        System.out.println("green hashCode(): " + green.hashCode());
        System.out.println("green compareTo Color.GREEN: " + green.compareTo(Color.GREEN));
        System.out.println("green equals Color.GREEN: " + green.equals(Color.GREEN));
        System.out.println("green equals Size.MIDDLE: " + green.equals(Size.MIDDLE));
        System.out.println("green equals 1: " + green.equals(1));
        System.out.format("green == Color.BLUE: %b\n", green == Color.BLUE);
    }

    // 如果枚举中没有定义方法，也可以在最后一个实例后面加逗号或分号
    // enum Color { RED, GREEN, BLUE }
    // enum Color { RED, GREEN, BLUE, }
    enum Color {

        RED,
        GREEN,
        BLUE
    }

    enum Size {

        BIG,
        MIDDLE,
        SMALL
    }

}
