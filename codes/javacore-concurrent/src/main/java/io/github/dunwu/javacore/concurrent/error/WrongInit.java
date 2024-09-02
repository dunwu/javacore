package io.github.dunwu.javacore.concurrent.error;

import java.util.HashMap;
import java.util.Map;

public class WrongInit {

    private Map<Integer, String> students;

    public WrongInit() {
        new Thread(() -> {
            students = new HashMap<>();
            students.put(1, "王小美");
            students.put(2, "钱二宝");
            students.put(3, "周三");
            students.put(4, "赵四");
        }).start();
    }

    public Map<Integer, String> getStudents() {
        return students;
    }

    public static void main(String[] args) throws InterruptedException {
        WrongInit demo = new WrongInit();
        System.out.println(demo.getStudents().get(1));
    }

}
