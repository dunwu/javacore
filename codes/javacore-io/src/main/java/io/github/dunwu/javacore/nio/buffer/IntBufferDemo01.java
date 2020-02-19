package io.github.dunwu.javacore.nio.buffer;

import java.nio.IntBuffer;

/**
 * Buffer 的基本操作
 */
public class IntBufferDemo01 {

    public static void main(String[] args) {
        IntBuffer buf = IntBuffer.allocate(10); // 准备出10个大小的缓冲区
        System.out.print("1、写入数据之前的position、limit和capacity：");
        System.out.println("position = " + buf.position() + "，limit = " + buf.limit() + "，capacty = " + buf.capacity());
        int[] temp = { 5, 7, 9 };// 定义一个int数组
        buf.put(3); // 设置一个数据
        buf.put(temp); // 此时已经存放了四个记录
        System.out.print("2、写入数据之后的position、limit和capacity：");
        System.out.println("position = " + buf.position() + "，limit = " + buf.limit() + "，capacty = " + buf.capacity());

        buf.flip(); // 重设缓冲区
        // postion = 0 ,limit = 原本position
        System.out.print("3、准备输出数据时的position、limit和capacity：");
        System.out.println("position = " + buf.position() + "，limit = " + buf.limit() + "，capacty = " + buf.capacity());
        System.out.print("缓冲区中的内容：");
        while (buf.hasRemaining()) {
            int x = buf.get();
            System.out.print(x + "、");
        }
    }

}
