package com.example.superdiff.utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 *
 * @author gaozhuo
 * @date 2017/12/30
 */
public class FileUtils {

    /**
     * 关闭文件流
     *
     * @param closeable
     */
    public static void closeStream(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 找目录或文件时的回调接口
     */
    public interface FileFinder {
        /**
         * 找到了一个目录或文件
         *
         * @param file 普通文件或目录文件
         */
        void found(File file);
    }

    public static void traverseDirectory(String dir, FileFinder fileFinder) {
        traverseDirectory(new File(dir), fileFinder);
    }

    /**
     * 遍历目录
     *
     * @param dir        要遍历的目录
     * @param fileFinder 找到目录或文件后的回调接口
     */
    public static void traverseDirectory(File dir, FileFinder fileFinder) {
        if (!dir.isDirectory()) {
            throw new RuntimeException(dir.getAbsolutePath() + "不是目录");
        }
        File files[] = dir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            fileFinder.found(file);
            if (file.isDirectory()) {
                traverseDirectory(file, fileFinder);
            }
        }
    }
}
