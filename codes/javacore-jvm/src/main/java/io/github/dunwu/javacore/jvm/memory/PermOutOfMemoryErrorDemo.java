package io.github.dunwu.javacore.jvm.memory;

import javassist.ClassPool;

/**
 * 永久代内存空间不足示例 错误现象：
 * <ul>
 * <li>java.lang.OutOfMemoryError: PermGen space (JDK8 以前版本)</li>
 * <li>java.lang.OutOfMemoryError: Metaspace (JDK8 及以后版本)</li>
 * </ul>
 * VM Args:
 * <ul>
 * <li>-Xmx100M -XX:MaxPermSize=16M (JDK8 以前版本)</li>
 * <li>-Xmx100M -XX:MaxMetaspaceSize=16M (JDK8 及以后版本)</li>
 * </ul>
 * Linux Test Cli: nohup java -verbose:gc -Xmx100M -XX:MaxMetaspaceSize=16M -XX:+HeapDumpOnOutOfMemoryError -classpath "target/javacore-jvm-1.0.1.jar:target/lib/*" io.github.dunwu.javacore.jvm.memory.PermOutOfMemoryErrorDemo >> output.log 2>&1 &
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-03-08
 */
public class PermOutOfMemoryErrorDemo {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100_000_000; i++) {
            generate("eu.plumbr.demo.Generated" + i);
        }
    }

    public static Class generate(String name) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        return pool.makeClass(name).toClass();
    }

}
