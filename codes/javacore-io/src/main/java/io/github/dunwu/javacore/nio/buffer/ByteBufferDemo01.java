package io.github.dunwu.javacore.nio.buffer;

import java.nio.ByteBuffer;

/**
 * 示例：创建直接缓冲区（{@link ByteBuffer#allocateDirect}），写入一组字节后 flip 读出。
 */
public class ByteBufferDemo01 {

    /**
     * 演示直接缓冲区的 put / flip / get 基本流程。
     */
    public static void demo() {
        ByteBuffer buf = ByteBuffer.allocateDirect(10); // 准备出10个大小的缓冲区，分配在堆外内存中，适合频繁的 IO 操作
        byte[] temp = { 1, 3, 5, 7, 9 }; // 设置内容
        buf.put(temp); // 设置一组内容
        buf.flip();

        System.out.print("主缓冲区中的内容：");
        while (buf.hasRemaining()) {
            int x = buf.get();
            System.out.print(x + "、");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        demo();
    }

}
