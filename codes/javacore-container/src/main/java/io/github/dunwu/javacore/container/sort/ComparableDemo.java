package io.github.dunwu.javacore.container.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * {@link Comparable} 示例：对象自身实现比较规则（内部比较器），与 {@link ComparatorDemo}（外部比较器）对比。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2024-09-19
 */
public class ComparableDemo {

    /** 演示实现 Comparable 接口的对象用 Collections.sort 按自然序排序（本例按年龄升序）。 */
    public static void demo() {
        User a = new User("A", 18);
        User b = new User("B", 17);
        User c = new User("C", 20);
        List<User> list = new ArrayList<>(Arrays.asList(a, b, c));
        Collections.sort(list);
        list.forEach(System.out::println);
    }

    public static void main(String[] args) {
        demo();
    }
    // 输出：
    // User{age=17, name='B'}
    // User{age=18, name='A'}
    // User{age=20, name='C'}

    static class User implements Comparable<User> {

        private String name;
        private int age;

        public User(String name, int age) {
            this.age = age;
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public User setAge(int age) {
            this.age = age;
            return this;
        }

        public String getName() {
            return name;
        }

        public User setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public int compareTo(User o) {
            return this.age - o.age;
        }

        @Override
        public String toString() {
            return "User{" + "age=" + age + ", name='" + name + '\'' + '}';
        }

    }

}
