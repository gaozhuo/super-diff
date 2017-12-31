package com.example.superdiff;

import com.example.superdiff.utils.CryptoUtils;
import com.example.superdiff.utils.FileUtils;
import com.example.superdiff.utils.ZipUtils;

import java.io.File;
import java.io.IOException;

public class ApkDiff {
    private static int dirNum = 0;
    private static int fileNum = 0;

    public static void diff(String newFile, String oldFile) {
        FileUtils.traverseDirectory(newFile, new FileUtils.FileFinder() {
            @Override
            public void found(File file) {
                if (file.isDirectory()) {
                    dirNum++;
                } else {
                    fileNum++;
                    handle(new File(newFile).getAbsolutePath(), file, new File(oldFile).getAbsolutePath());
                }
            }
        });
    }

    public static void diff(String newFile, String oldFile, String diffFile) {
        diff(new File(newFile), new File(oldFile), new File(diffFile));
    }

    public static void diff(File newFile, File oldFile, File diffFile) {
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

        try {
            ZipUtils.decompress(newFile, newDirFile);
            ZipUtils.decompress(oldFile, oldDirFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private static int deleteFileNum = 0;

    private static void handle(String newFileRoot, File file, String oldFileRoot) {
        String oldFilePath = file.getAbsolutePath().replace(newFileRoot, oldFileRoot);
        File oldFile = new File(oldFilePath);

        //System.out.println(file.getAbsolutePath());
        //System.out.println(oldFilePath);

        if (oldFile.exists() && oldFile.isFile()) {
            String oldFileMd5 = CryptoUtils.md5(oldFile);
            String newFileMd5 = CryptoUtils.md5(file);
            if (oldFileMd5.equalsIgnoreCase(newFileMd5)) {
                file.delete();
                deleteFileNum++;
                //System.out.println("deleteFileNum=" + deleteFileNum);
                //System.out.println("file=" + file.getAbsolutePath());
            } else {
                System.out.println("change: " + file.getAbsolutePath());
            }
        } else {
            System.out.println("not found:" + file.getAbsolutePath());
        }
    }
}

