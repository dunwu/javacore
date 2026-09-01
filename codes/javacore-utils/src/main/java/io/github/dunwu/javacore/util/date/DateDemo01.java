package io.github.dunwu.javacore.util.date;

import java.util.Date;

/**
 * 示例：直接实例化 java.util.Date 即可取得当前日期时间。
 */
public class DateDemo01 {

    /**
     * 演示取得当前日期。
     */
    public static void demo() {
        Date date = new Date(); // 直接实例化Date对象
        System.out.println("当前日期为：" + date);
    }

    public static void main(String[] args) {
        demo();
    }

}
