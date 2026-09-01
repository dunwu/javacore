package io.github.dunwu.javacore.oop;

import java.util.Date;

/**
 * 通过 import 导入其它包的类到当前类
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class PackageDemo2 {

    /**
     * 通过 import 导入后，直接使用短类名创建 Date 对象。
     */
    public static void demo() {
        System.out.println(new Date());
    }

    public static void main(String[] args) {
        demo();
    }

}
