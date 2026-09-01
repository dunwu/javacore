package io.github.dunwu.javacore.enumeration;

import java.util.EnumSet;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-19
 */
public class EnumSetDemo {

    /**
     * 演示 EnumSet：一次性取出全部枚举常量并按定义顺序遍历。
     */
    public static void demo() {
        System.out.println("EnumSet展示");
        EnumSet<ErrorCodeEn> errSet = EnumSet.allOf(ErrorCodeEn.class);
        for (ErrorCodeEn e : errSet) {
            System.out.println(e.name() + " : " + e.ordinal());
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
