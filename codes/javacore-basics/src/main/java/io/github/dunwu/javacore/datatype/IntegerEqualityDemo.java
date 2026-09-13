package io.github.dunwu.javacore.datatype;

/**
 * 演示 Integer 缓存池（-128~127）对 == 判等的影响。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-06
 */
public class IntegerEqualityDemo {

    /**
     * 演示 Integer 缓存池（-128~127）对 == 判等的影响
     */
    public static void demo() {
        Integer a = 127; //Integer.valueOf(127)
        Integer b = 127; //Integer.valueOf(127)
        System.out.println("\nInteger a = 127;\nInteger b = 127;\na == b ? " + (a == b));    // true

        Integer c = 128; //Integer.valueOf(128)
        Integer d = 128; //Integer.valueOf(128)
        System.out.println("\nInteger c = 128;\nInteger d = 128;\nc == d ? " + (c == d));   //false
        //设置-XX:AutoBoxCacheMax=1000再试试

        Integer e = 127; //Integer.valueOf(127)
        Integer f = new Integer(127); //new instance
        System.out.println("\nInteger e = 127;\nInteger f = new Integer(127);\ne == f ? " + (e == f));   //false

        Integer g = new Integer(127); //new instance
        Integer h = new Integer(127); //new instance
        System.out.println("\nInteger g = new Integer(127);\nInteger h = new Integer(127);\ng == h ? "
            + (g == h));  //false

        Integer i = 128; //unbox
        int j = 128;
        System.out.println("\nInteger i = 128;\nint j = 128;\ni == j ? " + (i == j)); //true
    }

    public static void main(String[] args) {
        demo();
    }

}
// Output:
//
// Integer a = 127;
// Integer b = 127;
// a == b ? true
//
// Integer c = 128;
// Integer d = 128;
// c == d ? false
//
// Integer e = 127;
// Integer f = new Integer(127);
// e == f ? false
//
// Integer g = new Integer(127);
// Integer h = new Integer(127);
// g == h ? false
//
// Integer i = 128;
// int j = 128;
// i == j ? true
