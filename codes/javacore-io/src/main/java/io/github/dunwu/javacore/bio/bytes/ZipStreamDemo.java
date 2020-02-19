package io.github.dunwu.javacore.bio.bytes;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ZipStreamDemo {

    public static final String ZIP_FILE_PATH = "d:\\zipdemo.zip";

    public static void demo01(String zipfilepath) throws IOException {
        File file = new File(zipfilepath);
        ZipFile zipFile = new ZipFile(file);
        ZipEntry entry = zipFile.getEntry("mldn.txt");
        System.out.println("压缩文件的名称：" + zipFile.getName());

        File outputFile = new File("d:" + File.separator + "mldn_unzip.txt");
        OutputStream out = new FileOutputStream(outputFile); // 实例化输出流
        InputStream input = zipFile.getInputStream(entry); // 得到一个压缩实体的输入流
        int temp = 0;
        while ((temp = input.read()) != -1) {
            out.write(temp);
        }
        input.close(); // 关闭输入流
        out.close(); // 关闭输出流
    }

    public static void main(String[] args) throws Exception {
        final String filepath = "d:\\demo.txt";
        final String zipfilepath = "d:\\demo.zip";

        final String dirpath = "d:\\demo2";
        final String dirpath2 = "d:\\new";
        final String zipfilepath2 = "d:\\demo2.zip";

        // demo01(ZIP_FILE_PATH);
        output1(filepath, zipfilepath);
        input1(zipfilepath, filepath);

        output2(dirpath, zipfilepath2);
        input2(zipfilepath2, dirpath2);
    }

    /**
     * 压缩一个文件
     */
    public static void output1(String filepath, String zipfilepath) throws Exception {
        // 1.使用 File 类绑定一个文件
        // 定义要压缩的文件
        File file = new File(filepath);
        // 定义压缩文件名称
        File zipFile = new File(zipfilepath);

        // 2.把 File 对象绑定到流对象上
        InputStream input = new FileInputStream(file);
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));

        // 3.进行读或写操作
        zipOut.putNextEntry(new ZipEntry(file.getName()));
        zipOut.setComment("This is a zip file.");
        int temp = 0;
        while ((temp = input.read()) != -1) { // 读取内容
            zipOut.write(temp); // 压缩输出
        }

        // 4.关闭流
        input.close();
        zipOut.close();
    }

    /**
     * 读取实体为一个文件的压缩包
     */
    public static void input1(String zipfilepath, String filepath) throws Exception {
        // 1.使用 File 类绑定一个文件
        File zipFile = new File(zipfilepath);

        // 2.把 File 对象绑定到流对象上
        ZipInputStream input = new ZipInputStream(new FileInputStream(zipFile));

        // 3.进行读或写操作
        ZipEntry entry = input.getNextEntry(); // 得到一个压缩实体
        System.out.println("压缩实体名称：" + entry.getName());

        // 4.关闭流
        input.close();
    }

    /**
     * 压缩一个目录
     */
    public static void output2(String dirpath, String zipfilepath) throws Exception {
        // 1.使用 File 类绑定一个文件
        // 定义要压缩的文件夹
        File file = new File(dirpath);
        // 定义压缩文件名称
        File zipFile = new File(zipfilepath);

        // 2.把 File 对象绑定到流对象上
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
        zipOut.setComment("This is zip folder.");

        // 3.进行读或写操作
        int temp = 0;
        if (file.isDirectory()) { // 判断是否是文件夹
            File[] lists = file.listFiles(); // 列出全部文件
            for (int i = 0; i < lists.length; i++) {
                InputStream input = new FileInputStream(lists[i]);
                // 设置ZipEntry对象
                zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + lists[i].getName()));
                while ((temp = input.read()) != -1) {
                    zipOut.write(temp);
                }
                input.close();
            }
        }

        // 4.关闭流
        zipOut.close();
    }

    /**
     * 解压实体为一个目录的压缩包
     */
    public static void input2(String zipfilepath, String dirpath) throws Exception {
        // 1.使用 File 类绑定一个文件
        File file = new File(zipfilepath);
        ZipFile zipFile = new ZipFile(file);

        // 2.把 File 对象绑定到流对象上
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));

        // 3.进行读或写操作
        ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null) { // 得到一个压缩实体
            System.out.println("解压缩" + entry.getName() + "文件。");
            // 定义输出的文件路径
            File outFile = new File(dirpath + File.separator + entry.getName());
            if (!outFile.getParentFile().exists()) { // 如果输出文件夹不存在
                outFile.getParentFile().mkdirs(); // 创建文件夹
            }
            if (!outFile.exists()) { // 判断输出文件是否存在
                outFile.createNewFile(); // 创建文件
            }
            InputStream input = zipFile.getInputStream(entry); // 得到每一个实体的输入流
            OutputStream out = new FileOutputStream(outFile); // 实例化文件输出流
            int temp = 0;
            while ((temp = input.read()) != -1) {
                out.write(temp);
            }
            input.close(); // 关闭输入流
            out.close(); // 关闭输出流
        }

        // 4.关闭流
        zis.close();
    }

}
