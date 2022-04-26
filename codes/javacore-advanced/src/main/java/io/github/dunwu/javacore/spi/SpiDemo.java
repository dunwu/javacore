package io.github.dunwu.javacore.spi;

import java.util.ServiceLoader;

/**
 * Java SPI Demo
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2022-04-26
 */
public class SpiDemo {

    public static void main(String[] args) {
        ServiceLoader<DataStorage> serviceLoader = ServiceLoader.load(DataStorage.class);
        System.out.println("============ Java SPI 测试============");
        serviceLoader.forEach(loader -> System.out.println(loader.search("Yes Or No")));
    }

}
