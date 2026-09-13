package io.github.dunwu.javacore.datatype;

/**
 * 演示 String 的 == 与 equals 判等差异，以及字符串常量池、intern() 的作用。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-06
 */
public class StringEqualityDemo {

    /**
     * 演示 String 的 == 与 equals 判等差异，以及字符串常量池、intern() 的作用
     */
    public static void demo() {
        String a = "1";
        String b = "1";
        System.out.println("\nString a = \"1\";\nString b = \"1\";\na == b ? " + (a == b)); //true

        String c = new String("2");
        String d = new String("2");
        System.out.println("\nString c = new String(\"2\");\nString d = new String(\"2\");\nc == d ? "
            + (c == d)); //false

        String e = new String("3").intern();
        String f = new String("3").intern();
        System.out.println("\nString e = new String(\"3\").intern();\nString f = new String(\"3\").intern();\ne == f ? "
            + (e == f)); //true

        String g = new String("4");
        String h = new String("4");
        System.out.println("\nString g = new String(\"4\");\nString h = new String(\"4\");\ng == h ? "
            + g.equals(h)); //true
    }

    public static void main(String[] args) {
        demo();
    }

}
// Output:
//
// String a = "1";
// String b = "1";
// a == b ? true
//
// String c = new String("2");
// String d = new String("2");
// c == d ? false
//
// String e = new String("3").intern();
// String f = new String("3").intern();
// e == f ? true
//
// String g = new String("4");
// String h = new String("4");
// g == h ? true
