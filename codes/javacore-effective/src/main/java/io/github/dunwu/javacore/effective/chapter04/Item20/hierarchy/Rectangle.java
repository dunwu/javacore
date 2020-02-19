package io.github.dunwu.javacore.effective.chapter04.Item20.hierarchy;

class Rectangle extends Figure {

    final double length;

    final double width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    double area() {
        return length * width;
    }

}
