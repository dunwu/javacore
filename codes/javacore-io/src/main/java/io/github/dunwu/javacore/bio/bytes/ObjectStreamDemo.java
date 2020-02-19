package io.github.dunwu.javacore.bio.bytes;

import java.io.*;

/**
 * 对象输入输出流，一般用于对象序列化
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ObjectStreamDemo {

    public static void main(String[] args) throws Exception {
        final String filepath = "d:\\object.txt";
        Person[] per = { new Person("张三", 30), new Person("李四", 31), new Person("王五", 32) };
        writeObject(filepath, per);
        Object[] o = readObject(filepath);
        for (int i = 0; i < o.length; i++) {
            Person p = (Person) o[i];
            System.out.println(p);
        }
    }

    public static void writeObject(String filepath, Object[] obj) throws Exception {
        // 1.使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2.把 File 对象绑定到流对象上
        OutputStream out = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(out);

        // 3.进行读或写操作
        oos.writeObject(obj);

        // 4.关闭流
        oos.close();
    }

    public static Object[] readObject(String filepath) throws Exception {
        // 1.使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2.把 File 对象绑定到流对象上
        InputStream input = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(input);

        // 3.进行读或写操作
        Object[] objects = (Object[]) ois.readObject();

        // 4.关闭流
        ois.close();
        return objects;
    }

    public static class Person implements Serializable {

        private String name;

        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "姓名：" + this.name + "；年龄：" + this.age;
        }

    }

}
