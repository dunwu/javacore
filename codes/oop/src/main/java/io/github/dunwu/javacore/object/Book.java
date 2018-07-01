package io.github.dunwu.javacore.object;

class Book { // 定义Book类
    private String title; // 标题
    private float price; // 价格
    private Person2 person; // 一本书属于一个人

    public Book(String title, float price) {
        this.setTitle(title);
        this.setPrice(price);
    }

    public void setTitle(String t) {
        title = t;
    }

    public void setPrice(float p) {
        price = p;
    }

    public String getTitle() {
        return title;
    }

    public float getPrice() {
        return price;
    }

    public void setPerson2(Person2 p) {
        person = p;
    }

    public Person2 getPerson2() {
        return person;
    }
};
