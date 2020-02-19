package io.github.dunwu.javacore.util;

public class RuntimeDemo01 {

    public static void main(String[] args) {
        Runtime run = Runtime.getRuntime(); // 通过Runtime类的静态方法进行实例化操作
        System.out.println("JVM最大内存量：" + run.maxMemory()); // 观察最大的内存，根据机器的不同，环境也会有所不同
        System.out.println("JVM空闲内存量：" + run.freeMemory()); // 取得程序运行的空闲内存
        String str = "Hello " + "World" + "!!!" + "\t" + "Welcome " + "To " + "JAVA" + "~";
        System.out.println(str);
        for (int x = 0; x < 1000; x++) {
            str += x; // 循环修改内容，会产生多个垃圾
        }
        System.out.println("操作String之后的,JVM空闲内存量：" + run.freeMemory());
        run.gc(); // 进行垃圾收集，释放空间
        System.out.println("垃圾回收之后的,JVM空闲内存量：" + run.freeMemory());
    }

}
