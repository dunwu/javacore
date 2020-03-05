package io.github.dunwu.javacore.spi;

/**
 * SPI （服务发现机制）接口示例
 * <p>
 * SPI 的本质是将接口实现类的全限定名配置在文件中，并由服务加载器读取配置文件，加载实现类。
 * <p>
 * 配合 <code>src/main/resources/io.github.dunwu.javacore.spi.Robot</code> 文件，实现 SPI 功能
 */
public interface Robot {

    void sayHello();

}
