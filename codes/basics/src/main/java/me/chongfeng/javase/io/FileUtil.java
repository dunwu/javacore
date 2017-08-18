/**
 * The Apache License 2.0
 * Copyright (c) 2016 Zhang Peng
 */
package me.chongfeng.javase.io;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 文件工具类
 *
 * @author Zhang Peng
 * @date 2017/1/5.
 */
public class FileUtil {
    public static void main(String[] args) {
        List<String> allFiles = getFilesInFolder("D:\\home", false);
        for (String item : allFiles) {
            System.out.println(item);
        }
    }

    /**
     * 获得目录下所有的文件绝对路径（包含子目录）
     * 如果hasFolder为true，会包含目录路径
     *
     * @param path
     * @param hasFolder
     * @return
     */
    public static List<String> getFilesInFolder(String path, boolean hasFolder) {
        List<String> fileList = new LinkedList<String>();
        LinkedList<File> folderList = new LinkedList<File>();

        File root = new File(path);
        if (!root.exists() || !root.isDirectory()) {
            return null;
        }

        collectFilesAndFolders(root, hasFolder, folderList, fileList);

        while (!folderList.isEmpty()) {
            File currentFolder = folderList.removeFirst();
            collectFilesAndFolders(currentFolder, hasFolder, folderList, fileList);
        }

        Collections.sort(fileList, null);
        return fileList;
    }

    private static void collectFilesAndFolders(final File folder,
                                               final boolean hasFolder,
                                               LinkedList<File> folderList,
                                               List<String> fileList) {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                folderList.add(file);
                if (hasFolder) {
                    fileList.add(file.getAbsolutePath());
                }
            } else {
                fileList.add(file.getAbsolutePath());
            }
        }
    }

    /**
     * 使用递归方式打印目录下所有文件名、文件夹名（包含子目录）
     *
     * @param path
     */
    public static void printFilesInFolder(String path) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                System.out.println("文件夹:" + file.getAbsolutePath());
                printFilesInFolder(file.getAbsolutePath());
            } else {
                System.out.println("文件:" + file.getAbsolutePath());
            }
        }
    }
}
