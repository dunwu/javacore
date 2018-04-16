package io.github.dunwu.jvm.chapter07;

@SuppressWarnings("all")
public class IllegalForwardDemo {

    static {
        //  给变量复制可以正常编译通过
        i = 0;
        // 这句编译器会提示“非法向前引用”
        // System.out.print(i);
    }

    static int i = 1;
}

