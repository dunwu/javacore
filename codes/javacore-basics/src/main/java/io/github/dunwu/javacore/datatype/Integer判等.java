package io.github.dunwu.javacore.datatype;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-06
 */
@Slf4j
public class Integer判等 {

    public static void main(String[] args) {
        Integer a = 127; //Integer.valueOf(127)
        Integer b = 127; //Integer.valueOf(127)
        log.info("\nInteger a = 127;\nInteger b = 127;\na == b ? {}", a == b);    // true

        Integer c = 128; //Integer.valueOf(128)
        Integer d = 128; //Integer.valueOf(128)
        log.info("\nInteger c = 128;\nInteger d = 128;\nc == d ? {}", c == d);   //false
        //设置-XX:AutoBoxCacheMax=1000再试试

        Integer e = 127; //Integer.valueOf(127)
        Integer f = new Integer(127); //new instance
        log.info("\nInteger e = 127;\nInteger f = new Integer(127);\ne == f ? {}", e == f);   //false

        Integer g = new Integer(127); //new instance
        Integer h = new Integer(127); //new instance
        log.info("\nInteger g = new Integer(127);\nInteger h = new Integer(127);\ng == h ? {}", g == h);  //false

        Integer i = 128; //unbox
        int j = 128;
        log.info("\nInteger i = 128;\nint j = 128;\ni == j ? {}", i == j); //true
    }

}
