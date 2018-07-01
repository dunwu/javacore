package io.github.dunwu.javacore.concurrent.chapter03;

public class InstanceFactory {
    private static class InstanceHolder {
        public static Instance instance = new Instance();
    }

    public static Instance getInstance() {
        return InstanceHolder.instance; //这里将导致InstanceHolder类被初始化
    }

    static class Instance {
    }
}
