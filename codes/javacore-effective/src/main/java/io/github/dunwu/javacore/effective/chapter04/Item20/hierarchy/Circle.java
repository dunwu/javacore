package io.github.dunwu.javacore.effective.chapter04.Item20.hierarchy;

class Circle extends Figure {

    final double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * (radius * radius);
    }

}
