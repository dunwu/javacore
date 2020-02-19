// Service provider framework sketch

// Noninstantiable class for service registration and access - Pages 8-9
package io.github.dunwu.javacore.effective.chapter02.item01;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Services {

    public static final String DEFAULT_PROVIDER_NAME = "<def>";

    // 根据名称映射Provider的实现类
    private static final Map<String, Provider> providers = new ConcurrentHashMap<String, Provider>();

    private Services() {
    } // 私有构造器，阻止实例化 (Item 4)

    // Service 访问接口
    public static Service newInstance() {
        return newInstance(DEFAULT_PROVIDER_NAME);
    }

    public static Service newInstance(String name) {
        Provider p = providers.get(name);
        if (p == null) { throw new IllegalArgumentException("No provider registered with name: " + name); }
        return p.newService();
    }

    // Provider 注册接口
    public static void registerDefaultProvider(Provider p) {
        registerProvider(DEFAULT_PROVIDER_NAME, p);
    }

    public static void registerProvider(String name, Provider p) {
        providers.put(name, p);
    }

}
