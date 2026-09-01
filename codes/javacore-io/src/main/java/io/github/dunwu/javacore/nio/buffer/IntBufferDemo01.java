package io.github.dunwu.javacore.nio.buffer;

import java.nio.IntBuffer;

/**
 * 示例：Buffer 的基本操作 —— 观察写入前后以及 flip 后 position、limit、capacity 三个指针的变化。
 */
public class IntBufferDemo01 {

    /**
     * 演示缓冲区三个关键指针：
     * <ul>
     *     <li>capacity：缓冲区容量，固定不变</li>
     *     <li>position：下一个读/写位置，每次 put 后前进</li>
     *     <li>limit：可读/写的边界，flip 后变为原 position（即已写入数据的个数）</li>
     * </ul>
     */
    public static void demo() {
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
        System.out.println();
    }

    public static void main(String[] args) {
        demo();
    }

}
