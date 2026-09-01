package io.github.dunwu.javacore.jdk14.npe;

/**
 * Java 14 NullPointerException 精准提示示例。
 * <p>
 * Java 14 之前，链式调用发生 NPE 时（如 {@code a.b.c.length()}），
 * 异常信息只有笼统的 "NullPointerException"，无法定位是哪个引用为 null。
 * <p>
 * Java 14 起（JEP 358），JVM 会在 NPE 信息中精确描述为 null 的变量/表达式，
 * 例如："Cannot invoke \"String.length()\" because \"a.b.c\" is null"。
 * 该能力自 Java 15 起默认开启（Java 14 需参数 -XX:+ShowCodeDetailsInExceptionMessages）。
 */
public class NullPointerExceptionDemo {

    /**
     * 示例 1：简单空引用调用，NPE 信息精确指出为 null 的变量
     */
    public static void simpleNullInvoke() {
        String text = null;
        try {
            System.out.println(text.length());
        } catch (NullPointerException e) {
            System.out.println("场景一 NPE 信息: " + e.getMessage());
        }
    }

    /**
     * 示例 2：链式调用中某一环为 null，NPE 信息定位到具体表达式
     */
    public static void chainedNullReference() {
        Order order = new Order();
        order.user = new User(); // order.user.address 为 null
        try {
            System.out.println(order.user.address.city.length());
        } catch (NullPointerException e) {
            System.out.println("场景二 NPE 信息: " + e.getMessage());
        }
    }

    /**
     * 示例 3：数组元素为 null，NPE 信息包含数组下标
     */
    public static void nullArrayElement() {
        String[] names = new String[2];
        try {
            System.out.println(names[0].toUpperCase());
        } catch (NullPointerException e) {
            System.out.println("场景三 NPE 信息: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        simpleNullInvoke();
        chainedNullReference();
        nullArrayElement();
    }

    static class Order {

        User user;

    }

    static class User {

        Address address;

    }

    static class Address {

        String city;

    }

}
// Output:
// 场景一 NPE 信息: Cannot invoke "String.length()" because "text" is null
// 场景二 NPE 信息: Cannot read field "city" because "order.user.address" is null
// 场景三 NPE 信息: Cannot invoke "String.toUpperCase()" because "names[0]" is null
