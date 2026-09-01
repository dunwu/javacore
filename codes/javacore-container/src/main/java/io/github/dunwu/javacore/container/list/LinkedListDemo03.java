package io.github.dunwu.javacore.container.list;

import java.util.LinkedList;

/**
 * 示例：用 poll 以 FIFO 方式取出 LinkedList 全部元素；队列取空后 poll 返回 null 而非报错。
 */
public class LinkedListDemo03 {

    /** 演示 FIFO 方式连续弹出元素直至队列取空。 */
    public static void demo() {
        LinkedList<String> link = new LinkedList<String>();
        link.add("A");    // 增加元素
        link.add("B");    // 增加元素
        link.add("C");    // 增加元素
        System.out.print("以FIFO的方式输出：");
        for (int i = 0; i <= link.size() + 1; i++) {
            System.out.print(link.poll() + "、");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
