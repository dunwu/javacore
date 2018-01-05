package io.github.dunwu.javase.concurrent.chapter03;

public class SafeLazyInitialization {
    private static Instance instance;

    public synchronized static Instance getInstance() {
        if (instance == null)
            instance = new Instance();
        return instance;
    }

    static class Instance {
    }
}
