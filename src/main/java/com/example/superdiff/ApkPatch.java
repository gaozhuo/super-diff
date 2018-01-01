package com.example.superdiff;

import com.example.superdiff.exception.CryptoException;
import com.example.superdiff.exception.PatchException;
import com.example.superdiff.exception.FileHandlerException;
import com.example.superdiff.utils.CryptoUtils;
import com.example.superdiff.utils.FileUtils;
import com.example.superdiff.utils.ZipUtils;

import java.io.*;

public class ApkPatch {
    private static final String SEPARATOR = ":";


    public static void patch(String oldFile, String diffFile, String newFile) throws PatchException {
        patch(new File(oldFile), new File(diffFile), new File(newFile));
    }

    public static void patch(File oldFile, File diffFile, File newFile) throws PatchException {
        String oldDir = oldFile.getParent() + File.separator + oldFile.getName().replace(".apk", "");
        String diffDir = diffFile.getParent() + File.separator + diffFile.getName().replace(".zip", "");
        System.out.println("oldDir=" + oldDir);
        System.out.println("diffDir=" + diffDir);


        File oldDirFile = new File(oldDir);
        File diffDirFile = new File(diffDir);

        if (!diffDirFile.exists()) {
            diffDirFile.mkdirs();
        }

        if (!oldDirFile.mkdirs()) {
            oldDirFile.mkdirs();
        }

        // 解压文件
        try {
            ZipUtils.decompress(oldFile, oldDirFile);
            ZipUtils.decompress(diffFile, diffDirFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new PatchException(e);
        }

        patch(diffDir, oldDir);


        // 压缩newDir目录，生成diff文件
        try {
            zip(diffDir, newFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new PatchException(e);
        }

    }

    private static void zip(String diffDir, File newFile) throws IOException {
        ZipUtils.compress(new File(diffDir), newFile, false);
    }

    private static void patch(String diffDir, String oldDir) throws PatchException {
        File configFile = new File(diffDir + File.separator + "diff.conf");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(configFile));
            String line;
            while ((line = reader.readLine()) != null) {
                parseConfig(line, diffDir, oldDir);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new PatchException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new PatchException(e);
        } finally {
            FileUtils.closeStream(reader);
        }

    }

    private static void parseConfig(String config, String diffDir, String oldDir) throws IOException {
        String[] temp = config.split(SEPARATOR);
        String flag = temp[0];
        String relativePath = temp[1];
        String hash = temp[2];

        String newFilePath = diffDir + File.separator + relativePath;
        String oldFilePath = oldDir + File.separator + relativePath;

        if ("1".equals(flag)) {
            FileUtils.move(oldFilePath, newFilePath);
        } else if ("2".equals(flag)) {
            FileUtils.move(oldFilePath, newFilePath);
        }
    }


}

