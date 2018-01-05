package io.github.dunwu.javase.concurrent.chapter03;

public class UnsafeLazyInitialization {
    private static Instance instance;

    public static Instance getInstance() {
        if (instance == null) //1：A线程执行
            instance = new Instance(); //2：B线程执行
        return instance;
    }

    static class Instance {
    }
}
