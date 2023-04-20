package io.github.dunwu.javacore.javaagent;

public class AppInit {

    public static void init() {
        try {
            System.out.println("APP初始化中...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
