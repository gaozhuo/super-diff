package com.example.superdiff.utils;

import jdk.internal.org.objectweb.asm.tree.IincInsnNode;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.*;


/**
 * zip工具类
 *
 * @author gaozhuo
 * @date 2017/12/30
 */
public class ZipUtils {
    private static final int BUF_SIZE = 4096;
    private static final String BASE_DIR = "";
    private static final String SEPARATOR = File.separator;

    /**
     * 压缩文件
     *
     * @param srcFile
     * @param destFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void compress(String srcFile, String destFile) throws FileNotFoundException, IOException {
        compress(new File(srcFile), new File(destFile));
    }


    /**
     * 压缩文件
     *
     * @param srcFile
     * @param destFile
     * @param includeRoot 是否包含最外层目录
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void compress(String srcFile, String destFile, boolean includeRoot) throws FileNotFoundException, IOException {
        compress(new File(srcFile), new File(destFile), includeRoot);
    }

    /**
     * 压缩
     *
     * @param srcFile  源路径
     * @param destFile 目标路径
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void compress(File srcFile, File destFile) throws FileNotFoundException, IOException {
        compress(srcFile, destFile, true);
    }

    /**
     * 压缩
     *
     * @param srcFile     源路径
     * @param destFile    目标路径
     * @param includeRoot 是否包含最外层目录
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void compress(File srcFile, File destFile, boolean includeRoot) throws FileNotFoundException, IOException {
        ZipOutputStream zos = null;
        try {
            CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(destFile), new CRC32());
            zos = new ZipOutputStream(cos);
            zos.setLevel(Deflater.BEST_COMPRESSION);
            compress(srcFile, zos, BASE_DIR, includeRoot);
            zos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } finally {
            FileUtils.closeStream(zos);
        }
    }

    /**
     * 压缩
     *
     * @param srcFile     源路径
     * @param zos         ZipOutputStream
     * @param basePath    压缩包内相对路径
     * @param includeRoot 是否包含最外层目录
     * @throws IOException
     */
    private static void compress(File srcFile, ZipOutputStream zos, String basePath, boolean includeRoot) throws IOException {
        if (srcFile.isDirectory()) {
            compressDir(srcFile, zos, basePath, includeRoot);
        } else {
            compressFile(srcFile, zos, basePath, includeRoot);
        }
    }

    /**
     * 压缩目录
     *
     * @param dir
     * @param zos
     * @param basePath
     * @param includeRoot 是否包含最外层目录
     * @throws IOException
     */
    private static void compressDir(File dir, ZipOutputStream zos, String basePath, boolean includeRoot) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        if (files.length == 0) {// 构建空目录
            String name = basePath + dir.getName() + SEPARATOR;
            if (!includeRoot) {
                name = name.substring(name.indexOf(File.separator) + 1);
            }
            ZipEntry entry = new ZipEntry(name);
            try {
                zos.putNextEntry(entry);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            } finally {
                try {
                    zos.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            for (File file : files) {
                String path = basePath + dir.getName() + SEPARATOR;
                // 递归压缩
                compress(file, zos, path, includeRoot);
            }
        }
    }

    /**
     * 文件压缩
     *
     * @param file        待压缩文件
     * @param zos         ZipOutputStream
     * @param dir         压缩文件中的当前路径
     * @param includeRoot 是否包含最外层目录
     * @throws IOException
     */
    private static void compressFile(File file, ZipOutputStream zos, String dir, boolean includeRoot) throws IOException {
        String name = dir + file.getName();
        if (!includeRoot) {
            name = name.substring(name.indexOf(File.separator) + 1);
        }
        ZipEntry entry = new ZipEntry(name);
        System.out.println("name=" + entry.getName());
        BufferedInputStream bis = null;
        try {
            zos.putNextEntry(entry);
            bis = new BufferedInputStream(new FileInputStream(file));
            int count;
            byte buf[] = new byte[BUF_SIZE];
            while ((count = bis.read(buf, 0, BUF_SIZE)) != -1) {
                zos.write(buf, 0, count);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            FileUtils.closeStream(bis);
            try {
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 文件 解压缩
     *
     * @param srcPath 源文件路径
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void decompress(String srcPath) throws FileNotFoundException, IOException {
        File srcFile = new File(srcPath);

        decompress(srcFile);
    }

    /**
     * 解压缩
     *
     * @param srcFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void decompress(File srcFile) throws FileNotFoundException, IOException {
        if (!srcFile.isFile()) {
            throw new IllegalArgumentException(srcFile.getAbsolutePath() + " 不是文件");
        }
        decompress(srcFile, srcFile.getParentFile());
    }


    /**
     * 文件 解压缩
     *
     * @param srcPath  源文件路径
     * @param destPath 目标文件路径
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void decompress(String srcPath, String destPath) throws FileNotFoundException, IOException {
        decompress(new File(srcPath), new File(destPath));
    }

    /**
     * 解压缩
     *
     * @param srcFile
     * @param destFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void decompress(File srcFile, File destFile) throws FileNotFoundException, IOException {
        if (!srcFile.isFile()) {
            throw new IllegalArgumentException(srcFile.getAbsolutePath() + " 不是文件");
        }

        if (!destFile.isDirectory()) {
            throw new IllegalArgumentException(destFile.getAbsolutePath() + " 不是目录");
        }

        ZipInputStream zis = null;
        try {
            CheckedInputStream cis = new CheckedInputStream(new FileInputStream(srcFile), new CRC32());
            zis = new ZipInputStream(cis);
            decompress(destFile, zis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } finally {
            FileUtils.closeStream(zis);
        }

    }


    /**
     * 文件 解压缩
     *
     * @param destFile 目标文件
     * @param zis      ZipInputStream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void decompress(File destFile, ZipInputStream zis) throws FileNotFoundException, IOException {
        ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null) {
            File file = new File(destFile.getPath() + File.separator + entry.getName());
            if (entry.isDirectory()) {
                file.mkdirs();
            } else {
                decompressFile(file, zis);
            }
            zis.closeEntry();
        }
    }

    /**
     * 文件解压缩
     *
     * @param destFile 目标文件
     * @param zis      ZipInputStream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void decompressFile(File destFile, ZipInputStream zis) throws FileNotFoundException, IOException {

        File parentFile = destFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(destFile));
            int count;
            byte buf[] = new byte[BUF_SIZE];
            while ((count = zis.read(buf, 0, BUF_SIZE)) != -1) {
                bos.write(buf, 0, count);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            FileUtils.closeStream(bos);
        }

    }
}
