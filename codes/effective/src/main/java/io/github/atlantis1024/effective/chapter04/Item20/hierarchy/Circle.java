package io.github.atlantis1024.effective.chapter04.Item20.hierarchy;

class Circle extends Figure {
	final double radius;

	Circle(double radius) {
		this.radius = radius;
	}

	double area() {
		return Math.PI * (radius * radius);
	}
}
