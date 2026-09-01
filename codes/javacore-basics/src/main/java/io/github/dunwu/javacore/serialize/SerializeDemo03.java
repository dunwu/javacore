package io.github.dunwu.javacore.serialize;

import java.io.*;

/**
 * 序列化示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see SerializeDemo01
 * @see SerializeDemo03
 * @see UnSerializeDemo
 * @since 2018/6/4
 */
@SuppressWarnings("all")
public class SerializeDemo03 {

    /**
     * 演示自定义 writeObject/readObject：虽然 age 被 transient 修饰，但通过自定义序列化逻辑仍能保存和恢复 age
     */
    public static void demo() throws IOException, ClassNotFoundException {
        final String filename = "temp_serialize03.dat";
        serialize(filename);
        deserialize(filename);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        demo();
    }

    /**
     * 序列化
     */
    private static void serialize(String filename) throws IOException {
        File f = new File(filename); // 定义保存路径
        OutputStream out = new FileOutputStream(f); // 文件输出流
        ObjectOutputStream oos = new ObjectOutputStream(out); // 对象输出流
        oos.writeObject(new Person("Jack", 30, Sex.MALE)); // 保存对象
        oos.close();
        out.close();
    }

    /**
     * 反序列化
     */
    private static void deserialize(String filename) throws IOException, ClassNotFoundException {
        File f = new File(filename); // 定义保存路径
        InputStream in = new FileInputStream(f); // 文件输入流
        ObjectInputStream ois = new ObjectInputStream(in); // 对象输入流
        Object obj = ois.readObject(); // 读取对象
        ois.close();
        in.close();
        System.out.println(obj);
    }

    enum Sex {

        MALE,
        FEMALE
    }

    static class Person implements Serializable {

        private static final long serialVersionUID = 1L;

        private String name = null;

        transient private Integer age = null;

        private Sex sex;

        public Person() {
            System.out.println("call Person()");
        }

        public Person(String name, Integer age, Sex sex) {
            this.name = name;
            this.age = age;
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "name: " + this.name + ", age: " + this.age + ", sex: " + this.sex;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            out.writeInt(age);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            age = in.readInt();
        }

    }

}
// Output:
// name: Jack, age: 30, sex: MALE
