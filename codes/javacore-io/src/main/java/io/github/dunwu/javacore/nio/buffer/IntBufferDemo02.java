package io.github.dunwu.javacore.nio.buffer;

import java.nio.IntBuffer;

/**
 * 创建子缓冲区
 */
public class IntBufferDemo02 {

    public static void main(String[] args) {
        IntBuffer buf = IntBuffer.allocate(10); // 准备出10个大小的缓冲区
        IntBuffer sub = null; // 定义子缓冲区
        for (int i = 0; i < 10; i++) {
            buf.put(2 * i + 1); // 在主缓冲区中加入10个奇数
        }

        // 需要通过slice() 创建子缓冲区
        buf.position(2);
        buf.limit(6);
        sub = buf.slice();
        for (int i = 0; i < sub.capacity(); i++) {
            int temp = sub.get(i);
            sub.put(temp - 1);
        }

        buf.flip(); // 重设缓冲区
        buf.limit(buf.capacity());
        System.out.print("主缓冲区中的内容：");
        while (buf.hasRemaining()) {
            int x = buf.get();
            System.out.print(x + "、");
        }
    }

}
