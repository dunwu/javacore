package io.github.dunwu.javacore.method;

/**
 * @author Zhang Peng
 * @date 2019-03-16
 */
public class VarargsDemo {
    public static void method(String... params) {
        System.out.println("params.length = " + params.length);
        for (String param : params) {
            System.out.println("params = [" + param + "]");
        }
    }

    public static void main(String[] args) {
        method("red");
        method("red", "yellow");
        method("red", "yellow", "blue");
    }
}
// Output:
// params.length = 1
// params = [red]
// params.length = 2
// params = [red]
// params = [yellow]
// params.length = 3
// params = [red]
// params = [yellow]
// params = [blue]
