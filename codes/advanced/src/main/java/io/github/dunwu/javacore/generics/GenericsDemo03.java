package io.github.dunwu.javacore.generics;

class GenericsDemo03<X, Y> { // 此处指定了两个泛型类型
    private X x; // 此变量的类型由外部决定
    private Y y; // 此变量的类型由外部决定

    public X getX() {
        return this.x;
    }

    public Y getY() {
        return this.y;
    }

    public void setX(X x) {
        this.x = x;
    }

    public void setY(Y y) {
        this.y = y;
    }
};
