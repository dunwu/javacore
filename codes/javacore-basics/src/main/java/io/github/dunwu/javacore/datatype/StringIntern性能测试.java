package io.github.dunwu.javacore.datatype;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 没事别轻易用 {@link String#intern}，如果要用一定要注意控制驻留的字符串的数量，并留意常量表的各项指标
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-06
 */
public class StringIntern性能测试 {

    /**
     * 性能基准测试：1000 万次 String.intern() 耗时，不适合在单元测试中运行
     */
    public static void demo() {
        //-XX:+PrintStringTableStatistics，可以打印出字符串常量表的统计信息
        //设置 -XX:StringTableSize=10000000 后，执行速度会快很多
        List<String> list = new ArrayList<>();
        long begin = System.currentTimeMillis();
        list = IntStream.rangeClosed(1, 10000000)
            .mapToObj(i -> String.valueOf(i).intern())
            .collect(Collectors.toList());
        System.out.println("size:" + list.size());
        System.out.println("time:" + (System.currentTimeMillis() - begin));
    }

    public static void main(String[] args) {
        demo();
    }

}
