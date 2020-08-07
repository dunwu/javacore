package io.github.dunwu.javacore.jvm.memory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 方法区出现 OutOfMemoryError
 * <li>(JDK8 以前)-XX:PermSize=10m -XX:MaxPermSize=10m</li>
 * <li>(JDK8 及以后)-XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m</li>
 * <p>
 * Linux Test Cli: nohup java -verbose:gc -XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m -XX:+HeapDumpOnOutOfMemoryError
 * -classpath "target/javacore-jvm-1.0.1.jar:target/lib/*" io.github.dunwu.javacore.jvm.memory.MethodAreaOutOfMemoryDemo
 * >> output.log 2>&1 &
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-26
 */
public class MethodAreaOutOfMemoryDemo {

    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Bean.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    return proxy.invokeSuper(obj, args);
                }
            });
            enhancer.create();
        }
    }

    static class Bean {}

}
