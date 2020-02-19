package io.github.dunwu.javacore.jvm.classloader;

import java.io.IOException;
import java.io.InputStream;

/**
 * 类加载器与instanceof关键字演示
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/4/16
 */
public class ClassLoaderDemo {

    public static void main(String[] args) throws Exception {

        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is == null) {
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException(name);
                }
            }
        };

        Object obj = myLoader.loadClass("io.github.dunwu.javacore.jvm.classloader.ClassLoaderDemo").newInstance();

        System.out.println(obj.getClass());
        System.out.println(obj instanceof ClassLoaderDemo);
    }

}
