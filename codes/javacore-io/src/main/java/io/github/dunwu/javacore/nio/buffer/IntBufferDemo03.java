package io.github.dunwu.javacore.nio.buffer;

import java.nio.IntBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * 反例：对只读缓冲区调用写操作会抛出 {@link ReadOnlyBufferException}。
 * <p>
 * 本示例故意展示错误用法，运行即抛异常，行为保持不变。
 */
public class IntBufferDemo03 {

    /**
     * 向只读缓冲区写入数据 —— 必然抛出 {@link ReadOnlyBufferException}。
     */
    public static void demo() {
        IntBuffer buf = IntBuffer.allocate(10); // 准备出10个大小的缓冲区
        IntBuffer read = null; // 定义子缓冲区
        for (int i = 0; i < 10; i++) {
            buf.put(2 * i + 1); // 在主缓冲区中加入10个奇数
        }
        read = buf.asReadOnlyBuffer();// 创建只读缓冲区
        read.flip(); // 重设缓冲区
        System.out.print("主缓冲区中的内容：");
        while (read.hasRemaining()) {
            int x = read.get();
            System.out.print(x + "、");
        }
        read.put(30); // 修改，错误：只读缓冲区不允许写入，抛出 ReadOnlyBufferException
    }

    public static void main(String[] args) {
        demo();
    }

}
