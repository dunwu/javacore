package io.github.dunwu.javacore.concurrent.example;

public class ProducerConsumerDemo03 {

    public static void main(String[] args) {
        Info03 info = new Info03(); // 实例化Info对象
        Producer03 pro = new Producer03(info); // 生产者
        Consumer03 con = new Consumer03(info); // 消费者
        new Thread(pro).start();
        new Thread(con).start();
    }

}

class Info03 {

    // 定义信息类
    private String name = "JAVA"; // 定义name属性

    private String content = "JAVASE"; // 定义content属性

    private boolean flag = false; // 设置标志位

    public synchronized void set(String name, String content) {
        if (!flag) {
            try {
                super.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.setName(name); // 设置名称
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.setContent(content); // 设置内容
        flag = false; // 改变标志位，表示可以取走
        super.notify();
    }

    public synchronized void get() {
        if (flag) {
            try {
                super.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getName() + " --> " + this.getContent());
        flag = true; // 改变标志位，表示可以生产
        super.notify();
    }

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

class Producer03 implements Runnable {

    // 通过Runnable实现多线程
    private Info03 info = null; // 保存Info引用

    public Producer03(Info03 info) {
        this.info = info;
    }

    @Override
    public void run() {
        boolean flag = false; // 定义标记位
        for (int i = 0; i < 50; i++) {
            if (flag) {
                this.info.set("JAVA", "JAVASE"); // 设置名称
                flag = false;
            } else {
                this.info.set("并发", "多线程"); // 设置名称
                flag = true;
            }
        }
    }

}

class Consumer03 implements Runnable {

    private Info03 info = null;

    public Consumer03(Info03 info) {
        this.info = info;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            this.info.get();
        }
    }

}
