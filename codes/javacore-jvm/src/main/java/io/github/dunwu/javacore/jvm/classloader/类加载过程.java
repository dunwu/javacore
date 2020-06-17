package io.github.dunwu.javacore.jvm.classloader;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-06-17
 */
public class 类加载过程 {

public static void main(String[] args) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    System.out.println(loader);
    System.out.println(loader.getParent());
    System.out.println(loader.getParent().getParent());
}

}
