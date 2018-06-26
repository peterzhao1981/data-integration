package com.mode.checkProduct.fileutils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
    // 创建文件目录，先判断目录是否存在
    public static void fileDirectorCreate(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("文件夹创建失败，请检查是否具有该目录的权限");
            }
        }
    }

    // 清理文件夹
    public static void clearDirectory(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                file2.delete();
            }
        }
    }

    // 每次把指针移到文件末尾，追加写入文件

    /**
     * 指定位置开始写入文件
     * 
     * @param tempFile
     *            输入文件
     * @param outPath
     *            输出文件的路径(路径+文件名)
     * @throws IOException
     */
    public static void randomAccessFile(String outPath, File tempFile) throws IOException {
        RandomAccessFile raFile = null;
        BufferedInputStream inputStream = null;
        try {
            File dirFile = new File(outPath);
            // 以读写的方式打开目标文件
            raFile = new RandomAccessFile(dirFile, "rw");
            raFile.seek(raFile.length());
            inputStream = new BufferedInputStream(new FileInputStream(tempFile));
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buf)) != -1) {
                raFile.write(buf, 0, length);
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (raFile != null) {
                    raFile.close();
                }
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    // 判断文件夹是否存在，如果不存在就创建文件夹.
    public static void deltAndCreatFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    // 获取文件夹下的所有文件
    public static List<File> getFileList(String path) {
        File file = new File(path);
        List<File> fileList = new ArrayList<>();
        fileList = Arrays.asList(file.listFiles());
        return fileList;
    }

    public static void main(String[] args) {
    }
}
