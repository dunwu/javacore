package io.github.dunwu.javacore.jdk8.methodref;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法引用通过方法的名字来指向一个方法。 方法引用可以使语言的构造更紧凑简洁，减少冗余代码。 方法引用使用一对冒号(::)。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class MethodReferenceDemo {

    public static void main(String[] args) {
        List names = new ArrayList();
        names.add("楼");
        names.add("主");
        names.add("威");
        names.add("武");
        names.forEach(System.out::print);
    }

}
