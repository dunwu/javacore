package io.github.dunwu.javacore.serialize;

import io.github.dunwu.javacore.DemoFiles;

import java.io.*;

/**
 * 序列化示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see SerializeDemo01
 * @see ExternalizeDemo01
 * @see UnSerializeDemo
 * @since 2018/6/4
 */
@SuppressWarnings("all")
public class ExternalizeDemo01 {

    /**
     * 演示 Externalizable：writeExternal/readExternal 为空实现，所有字段都不会被序列化
     */
    public static void demo() throws IOException, ClassNotFoundException {
        // 临时文件统一写到 target/ 目录下，避免污染仓库工作目录，详见 DemoFiles
        final String filename = DemoFiles.tempPath("temp_externalize01.dat");
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

    static class Person implements Externalizable {

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
        public void writeExternal(ObjectOutput out) throws IOException {
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
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
// call Person()
// name: null, age: null, sex: null
