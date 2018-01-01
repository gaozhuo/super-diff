package com.example.superdiff;

import com.example.superdiff.exception.CryptoException;
import com.example.superdiff.exception.DiffException;
import com.example.superdiff.exception.FileHandlerException;
import com.example.superdiff.utils.CryptoUtils;
import com.example.superdiff.utils.FileUtils;
import com.example.superdiff.utils.ZipUtils;

import java.io.*;

public class ApkDiff {
    private static final String SEPARATOR = ":";
    private static int dirNum = 0;
    private static int fileNum = 0;
    private static int deleteFileNum = 0;

    public static void diff(String newFile, String oldFile, String diffFile) throws DiffException {
        diff(new File(newFile), new File(oldFile), new File(diffFile));
    }

    public static void diff(File newFile, File oldFile, File diffFile) throws DiffException {
        String newDir = newFile.getParent() + File.separator + newFile.getName().replace(".apk", "");
        String oldDir = oldFile.getParent() + File.separator + oldFile.getName().replace(".apk", "");
        System.out.println("newDir=" + newDir);
        System.out.println("oldDir=" + oldDir);

        File newDirFile = new File(newDir);
        File oldDirFile = new File(oldDir);

        if (!newDirFile.exists()) {
            newDirFile.mkdirs();
        }

        if (!oldDirFile.mkdirs()) {
            oldDirFile.mkdirs();
        }

        // 解压文件
        try {
            ZipUtils.decompress(newFile, newDirFile);
            ZipUtils.decompress(oldFile, oldDirFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DiffException(e);
        }

        File configFile = new File(newDir + File.separator + "diff.conf");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(configFile));
            traverse(newDir, oldDir, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DiffException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DiffException(e);
        } catch (FileHandlerException e) {
            e.printStackTrace();
            throw new DiffException(e);
        } finally {
            FileUtils.closeStream(writer);
        }

        // 压缩newDir目录，生成diff文件
        try {
            zip(newDir, diffFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DiffException(e);
        }

    }

    private static void zip(String newDir, File diffFile) throws IOException {
        ZipUtils.compress(new File(newDir), diffFile, false);
    }

    /**
     * 遍历文件
     *
     * @param newDir 新包目录
     * @param oldDir 旧包目录
     * @param writer 用于写配置文件
     * @throws FileHandlerException
     */
    private static void traverse(String newDir, String oldDir, BufferedWriter writer) throws FileHandlerException {
        FileUtils.traverseDirectory(newDir, new FileUtils.FileFinder() {
            @Override
            public void found(File file) throws FileHandlerException {
                if (file.isFile()) {
                    try {
                        handleFile(newDir, file, oldDir, writer);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new FileHandlerException(e);
                    }
                }
            }
        });
    }


    private static void handleFile(String newDir, File newFile, String oldDir, BufferedWriter writer) throws IOException, CryptoException {
        // 文件相对于newDir的路径
        String relativePath = newFile.getAbsolutePath().substring(newDir.length() + 1);
        // 文件在旧包中文件位置
        String oldFilePath = oldDir + File.separator + relativePath;
        File oldFile = new File(oldFilePath);

        if (oldFile.exists() && oldFile.isFile()) {// 旧包中存在该文件
            String oldFileHash = CryptoUtils.md5(oldFile);
            String newFileHash = CryptoUtils.md5(newFile);
            // 新旧包中文件hash值相等，说明文件没有改变，直接在新包中删除该文件
            if (oldFileHash.equalsIgnoreCase(newFileHash)) {
                newFile.delete();
                writer.write("1" + SEPARATOR + relativePath + SEPARATOR + newFileHash);
                writer.newLine();
                deleteFileNum++;
                //System.out.println("deleteFileNum=" + deleteFileNum);
                //System.out.println("file=" + file.getAbsolutePath());
            } else {//修改
                writer.write("2" + SEPARATOR + relativePath + SEPARATOR + newFileHash);
                writer.newLine();
                System.out.println("change: " + newFile.getAbsolutePath());
            }
        } else {
            System.out.println("not found:" + newFile.getAbsolutePath());
        }
    }
}

