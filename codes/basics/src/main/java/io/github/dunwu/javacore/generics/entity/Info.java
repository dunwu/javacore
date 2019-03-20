package io.github.dunwu.javacore.generics.entity;

public class Info<T> {
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
