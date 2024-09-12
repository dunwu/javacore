package io.github.dunwu.javacore.concurrent.jmm;

import io.github.dunwu.javacore.concurrent.annotation.ThreadSafe;

/**
 * 懒加载初始化（延迟加载）
 * <p/>
 * Thread-safe lazy initialization
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class SafeLazyInitialization {

    private static Resource resource;

    public synchronized static Resource getInstance() {
        if (resource == null) { resource = new Resource(); }
        return resource;
    }

    static class Resource { }

}
