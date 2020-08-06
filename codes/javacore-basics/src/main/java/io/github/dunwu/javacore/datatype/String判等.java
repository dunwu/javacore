package io.github.dunwu.javacore.datatype;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-06
 */
@Slf4j
public class String判等 {

    public static void main(String[] args) {
        String a = "1";
        String b = "1";
        log.info("\nString a = \"1\";\nString b = \"1\";\na == b ? {}", a == b); //true

        String c = new String("2");
        String d = new String("2");
        log.info("\nString c = new String(\"2\");\nString d = new String(\"2\");\nc == d ? {}", c == d); //false

        String e = new String("3").intern();
        String f = new String("3").intern();
        log.info("\nString e = new String(\"3\").intern();\nString f = new String(\"3\").intern();\ne == f ? {}", e == f); //true

        String g = new String("4");
        String h = new String("4");
        log.info("\nString g = new String(\"4\");\nString h = new String(\"4\");\ng == h ? {}", g.equals(h)); //true
    }

}
