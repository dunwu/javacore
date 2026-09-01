package io.github.dunwu.javacore.container.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * {@link java.util.Comparator} 示例：比较规则由外部提供（外部比较器），
 * 适合不能修改类本身或需要多种排序规则的场景；与 {@link ComparableDemo}（内部比较器）对比。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2024-09-19
 */
public class ComparatorDemo {

    /** 演示传入 Comparator 匿名类按年龄升序排序。 */
    public static void demo() {
        User a = new User("A", 18);
        User b = new User("B", 17);
        User c = new User("C", 20);
        List<User> list = new ArrayList<>(Arrays.asList(a, b, c));
        Collections.sort(list, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.age - o2.age;
            }
        });
        list.forEach(System.out::println);
    }

    public static void main(String[] args) {
        demo();
    }
    // 输出：
    // User{age=17, name='B'}
    // User{age=18, name='A'}
    // User{age=20, name='C'}

    static class User {

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
        public String toString() {
            return "User{" + "age=" + age + ", name='" + name + '\'' + '}';
        }

    }

}
