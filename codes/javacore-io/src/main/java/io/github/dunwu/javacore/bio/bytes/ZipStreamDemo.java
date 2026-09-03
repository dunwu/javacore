package io.github.dunwu.javacore.bio.bytes;

import io.github.dunwu.javacore.DemoFiles;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩流示例：用 ZipOutputStream/ZipInputStream/ZipFile 完成文件与目录的压缩、解压。
 * <p>demo 会在 {@code target/zipdemo} 目录下自包含地准备素材并演示：压缩单文件、读取压缩实体、压缩目录、解压目录。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ZipStreamDemo {

    /** 演示单文件与目录的压缩、解压全流程（素材在 {@code target/zipdemo} 目录中自动准备）。 */
    public static void demo() throws Exception {
        // 在 target/ 下的专属子目录中准备演示素材，避免污染仓库工作目录，详见 DemoFiles
        File workDir = new File(DemoFiles.tempDir(), "zipdemo");
        if (!workDir.exists()) {
            workDir.mkdirs();
        }
        String filepath = new File(workDir, "demo.txt").getPath();
        String zipfilepath = new File(workDir, "demo.zip").getPath();
        String dirpath = new File(workDir, "demo2").getPath();
        String dirpath2 = new File(workDir, "new").getPath();
        String zipfilepath2 = new File(workDir, "demo2.zip").getPath();

        // 准备一个待压缩的文件
        try (OutputStream out = new FileOutputStream(filepath)) {
            out.write("MLDN：www.mldn.cn".getBytes(StandardCharsets.UTF_8));
        }
        // 准备一个待压缩的目录（内含两个文件）
        File dir = new File(dirpath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (String name : new String[] { "a.txt", "b.txt" }) {
            try (OutputStream out = new FileOutputStream(new File(dir, name))) {
                out.write(("内容：" + name).getBytes(StandardCharsets.UTF_8));
            }
        }

        output1(filepath, zipfilepath);
        input1(zipfilepath, filepath);

        output2(dirpath, zipfilepath2);
        input2(zipfilepath2, dirpath2);
    }

    public static void main(String[] args) throws Exception {
        demo();
    }

    /**
     * 解压压缩包中的指定实体到文件
     */
    public static void demo01(String zipfilepath) throws IOException {
        File file = new File(zipfilepath);
        ZipFile zipFile = new ZipFile(file);
        ZipEntry entry = zipFile.getEntry("mldn.txt");
        System.out.println("压缩文件的名称：" + zipFile.getName());

        File outputFile = DemoFiles.temp("mldn_unzip.txt");
        OutputStream out = new FileOutputStream(outputFile); // 实例化输出流
        InputStream input = zipFile.getInputStream(entry); // 得到一个压缩实体的输入流
        int temp = 0;
        while ((temp = input.read()) != -1) {
            out.write(temp);
        }
        input.close(); // 关闭输入流
        out.close(); // 关闭输出流
        zipFile.close();
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
            File outFile = new File(dirpath, entry.getName());
            if (!outFile.toPath().normalize().startsWith(dirpath)) {
                throw new IOException("Bad zip entry");
            }
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
