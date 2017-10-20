package io.github.dunwu.javase.reflect.proxy;

public class Consumer implements Purchaser {
    private String name;

    public Consumer(String name) {
        this.name = name;
    }

    @Override
    public void purchase(String commodity) {
        System.out.println(this.name + " 购买 " + commodity);
    }
}
