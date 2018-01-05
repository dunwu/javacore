package io.github.dunwu.javase.concurrent.chapter03;

class MonitorExample {
    int a = 0;

    public synchronized void writer() { //1
        a++; //2
    } //3

    public synchronized void reader() { //4
        int i = a; //5
        //……
    } //6
}
