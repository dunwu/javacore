package io.github.dunwu.javacore.container.list;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-11
 */
@Slf4j
public class AsList示例 {

    public static void main(String[] args) {
        System.out.println("====================== wrong1 ======================");
        wrong1();
        System.out.println("====================== right1 ======================");
        right1();
        System.out.println("====================== wrong2 ======================");
        wrong2();
        System.out.println("====================== right2 ======================");
        right2();
    }

    private static void wrong1() {
        int[] arr = { 1, 2, 3 };
        List list = Arrays.asList(arr);
        log.info("list:{} size:{} class:{}", list, list.size(), list.get(0).getClass());
    }

    private static void right1() {
        int[] arr1 = { 1, 2, 3 };
        List list1 = Arrays.stream(arr1).boxed().collect(Collectors.toList());
        log.info("list:{} size:{} class:{}", list1, list1.size(), list1.get(0).getClass());

        Integer[] arr2 = { 1, 2, 3 };
        List list2 = Arrays.asList(arr2);
        log.info("list:{} size:{} class:{}", list2, list2.size(), list2.get(0).getClass());
    }

    private static void wrong2() {
        String[] arr = { "1", "2", "3" };
        List list = Arrays.asList(arr);
        arr[1] = "4";
        try {
            list.add("5");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        log.info("arr:{} list:{}", Arrays.toString(arr), list);
    }

    private static void right2() {
        String[] arr = { "1", "2", "3" };
        List list = new ArrayList(Arrays.asList(arr));
        arr[1] = "4";
        try {
            list.add("5");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        log.info("arr:{} list:{}", Arrays.toString(arr), list);
    }

}
