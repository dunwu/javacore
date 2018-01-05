package io.github.dunwu.javase.concurrent.chapter03;

class VolatileExample {
    int              a    = 0;
    volatile boolean flag = false;

    public void writer() {
        a = 1; //1
        flag = true; //2
    }

    public void reader() {
        if (flag) { //3
            int i = a; //4
            //……
        }
    }
}
