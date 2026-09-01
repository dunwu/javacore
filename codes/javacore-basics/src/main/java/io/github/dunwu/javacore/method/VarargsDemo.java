package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class VarargsDemo {

    /**
     * 演示可变参数：参数个数不限，本质是数组。
     */
    public static void demo() {
        method("red");
        method("red", "yellow");
        method("red", "yellow", "blue");
    }

    public static void main(String[] args) {
        demo();
    }

    public static void method(String... params) {
        System.out.println("params.length = " + params.length);
        for (String param : params) {
            System.out.println("params = [" + param + "]");
        }
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
