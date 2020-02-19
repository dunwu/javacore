package io.github.dunwu.javacore.generics;

import io.github.dunwu.javacore.generics.entity.Content;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-20
 */
public class GenericsInterfaceDemo02<T> implements Content<T> {

    private T text;

    public GenericsInterfaceDemo02(T text) {
        this.text = text;
    }

    public static void main(String[] args) {
        GenericsInterfaceDemo02<String> gen = new GenericsInterfaceDemo02<>("ABC");
        System.out.print(gen.text());
    }

    @Override
    public T text() {
        return text;
    }

}
// Output:
// ABC
