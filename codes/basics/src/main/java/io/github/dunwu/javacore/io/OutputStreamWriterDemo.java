package io.github.dunwu.javacore.io;

import java.io.*;

/**
 * 将 OutputStream 转为 Writer
 * @author Zhang Peng
 */
public class OutputStreamWriterDemo {

    public static void main(String args[]) throws IOException {
        File f = new File("d:" + File.separator + "test.txt");
        Writer out = new OutputStreamWriter(new FileOutputStream(f));
        out.write("hello world!!");
        out.close();
    }
}
