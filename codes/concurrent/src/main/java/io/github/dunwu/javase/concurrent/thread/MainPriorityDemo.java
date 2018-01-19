package io.github.dunwu.javase.concurrent.thread;

public class MainPriorityDemo {
    public static void main(String[] args) {
        System.out.println("主方法的优先级：" + Thread.currentThread().getPriority());
        System.out.println("MAX_PRIORITY = " + Thread.MAX_PRIORITY);
        System.out.println("NORM_PRIORITY = " + Thread.NORM_PRIORITY);
        System.out.println("MIN_PRIORITY = " + Thread.MIN_PRIORITY);
    }
};
