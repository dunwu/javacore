package io.github.dunwu.javacore.nio.buffer;

import java.nio.IntBuffer;

/**
 * 创建只读缓冲区
 */
public class IntBufferDemo03 {

    public static void main(String[] args) {
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
        read.put(30); // 修改，错误
    }

}
