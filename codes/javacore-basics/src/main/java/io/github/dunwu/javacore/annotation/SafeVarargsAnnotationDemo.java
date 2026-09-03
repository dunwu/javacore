package io.github.dunwu.javacore.annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-30
 */
public class SafeVarargsAnnotationDemo {

    /**
     * 演示 {@code @SafeVarargs} 的局限：它只是<b>压制</b>了泛型可变参数（varargs）的「堆污染」告警，
     * 并不能让方法真的安全
     * <p>
     * {@code wrongMethod} 内部把 {@code List<Integer>} 塞进了 {@code Object[]}（即 {@code List<String>[]}），
     * 再按 {@code String} 取出时，运行期抛 {@code ClassCastException}
     */
    public static void demo() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");

        List<String> list2 = new ArrayList<>();
        list2.add("1");
        list2.add("2");

        try {
            wrongMethod(list, list2);
            System.out.println("wrongMethod 正常返回（未发生堆污染）");
        } catch (ClassCastException e) {
            System.out.println("捕获到 ClassCastException：@SafeVarargs 只压制告警，无法阻止堆污染导致的运行期异常");
        }
    }

    public static void main(String[] args) {
        demo();
    }
    // Output:
    // 捕获到 ClassCastException：@SafeVarargs 只压制告警，无法阻止堆污染导致的运行期异常

    /**
     * 此方法实际上并不安全，不使用此注解，编译时会告警
     */
    @SafeVarargs
    static void wrongMethod(List<String>... stringLists) {
        Object[] array = stringLists;
        List<Integer> tmpList = Arrays.asList(42);
        array[0] = tmpList; // 语法错误，但是编译不告警
        String s = stringLists[0].get(0); // 运行时报 ClassCastException
    }

}
