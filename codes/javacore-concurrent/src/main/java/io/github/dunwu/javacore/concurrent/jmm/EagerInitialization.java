package io.github.dunwu.javacore.concurrent.jmm;

import io.github.dunwu.javacore.concurrent.annotation.ThreadSafe;

/**
 * 饿汉加载初始化（提前加载）
 * <p/>
 * Eager initialization
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class EagerInitialization {

    private static Resource resource = new Resource();

    public static Resource getResource() {
        return resource;
    }

    static class Resource { }

}
