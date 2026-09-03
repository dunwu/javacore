package io.github.dunwu.javacore.serialize;

import io.github.dunwu.javacore.DemoFiles;

import java.io.*;

/**
 * 序列化示例 由于
 * <p>
 * 没有实现 Serializable 接口，运行时会抛出 NotSerializableException 异常
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/6/4
 */
public class UnSerializeDemo {

    /**
     * 反例：Person 未实现 Serializable 接口，序列化时抛出 NotSerializableException
     */
    public static void demo() throws IOException, ClassNotFoundException {
        // 临时文件统一写到 target/ 目录下，避免污染仓库工作目录，详见 DemoFiles
        final String filename = DemoFiles.tempPath("temp_unserialize.dat");
        serialize(filename);
        deserialize(filename);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        demo();
    }

    /**
     * 序列化。
     * <p>
     * 这里必须用 try-with-resources：本示例的 {@code writeObject} <b>注定抛出</b> NotSerializableException，
     * 若按「写完再 close」的写法，异常会跳过 close，文件句柄一直不释放。在 Windows 上句柄未释放的文件
     * 是<b>删不掉</b>的（{@code File.delete()} 静默返回 false），临时文件就会残留在磁盘上。
     */
    private static void serialize(String filename) throws IOException {
        File f = new File(filename); // 定义保存路径
        // try-with-resources 保证即使 writeObject 抛异常，流也会被关闭
        try (OutputStream out = new FileOutputStream(f); // 文件输出流
             ObjectOutputStream oos = new ObjectOutputStream(out)) { // 对象输出流
            oos.writeObject(new Person("Jack", 30, Sex.MALE)); // 保存对象
        }
    }

    /**
     * 反序列化
     */
    private static void deserialize(String filename) throws IOException, ClassNotFoundException {
        File f = new File(filename); // 定义保存路径
        try (InputStream in = new FileInputStream(f); // 文件输入流
             ObjectInputStream ois = new ObjectInputStream(in)) { // 对象输入流
            Object obj = ois.readObject(); // 读取对象
            System.out.println(obj);
        }
    }

    enum Sex {

        MALE,
        FEMALE
    }

    static class Person {

        private static final long serialVersionUID = 1L;

        private String name = null;

        private Integer age = null;

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

    }

}
