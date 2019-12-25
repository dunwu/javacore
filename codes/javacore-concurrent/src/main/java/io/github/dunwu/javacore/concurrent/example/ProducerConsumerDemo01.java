package io.github.dunwu.javacore.concurrent.example;

public class ProducerConsumerDemo01 {

    public static void main(String[] args) {
        Info01 info = new Info01(); // 实例化Info对象
        Producer01 pro = new Producer01(info); // 生产者
        Consumer01 con = new Consumer01(info); // 消费者
        new Thread(pro).start();
        new Thread(con).start();
    }

}

class Info01 {

    private String name = "JAVA";

    private String content = "JAVASE";

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

class Producer01 implements Runnable {

    private Info01 info = null;

    public Producer01(Info01 info) {
        this.info = info;
    }

    @Override
    public void run() {
        boolean flag = false; // 定义标记位
        for (int i = 0; i < 50; i++) {
            if (flag) {
                this.info.setName("JAVA"); // 设置名称
                try {
                    Thread.sleep(90);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.info.setContent("JAVASE"); // 设置内容
                flag = false;
            } else {
                this.info.setName("并发"); // 设置名称
                try {
                    Thread.sleep(90);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.info.setContent("多线程"); // 设置内容
                flag = true;
            }
        }
    }

}

class Consumer01 implements Runnable {

    private Info01 info = null;

    public Consumer01(Info01 info) {
        this.info = info;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(90);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.info.getName() + " --> " + this.info.getContent());
        }
    }

}
