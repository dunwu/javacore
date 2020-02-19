package io.github.dunwu.javacore.serial;

import java.io.*;

/**
 * 序列化、反序列化的范例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016年8月5日
 */
public class SerializeDemo {

    private final static String FILE_PATH = "d:\\test.dat";

    public static void main(String[] args) {
        Employee in = new Employee();
        in.name = "Reyan Ali";
        in.address = "Phokka Kuan, Ambehta Peer";
        in.SSN = 11122333;
        in.number = 101;
        in.demo = 5;

        serializeObject(in);
        Employee out = (Employee) deserializeObject();

        System.out.println("Deserialized Employee...");
        System.out.println("Name: " + out.name);
        System.out.println("Address: " + out.address);
        System.out.println("SSN: " + out.SSN);
        System.out.println("Number: " + out.number);
    }

    public static void serializeObject(Object obj) {
        try {
            File f = new File(FILE_PATH);
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream(FILE_PATH);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(obj);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in " + FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deserializeObject() {
        try {
            FileInputStream fileIn = new FileInputStream(FILE_PATH);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object obj = in.readObject();
            in.close();
            fileIn.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
