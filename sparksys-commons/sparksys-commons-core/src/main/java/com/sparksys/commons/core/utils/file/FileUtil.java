package com.sparksys.commons.core.utils.file;

import com.sparksys.commons.core.utils.exception.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * description: 文件工具类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:01:03
 */
@Slf4j
public class FileUtil extends FileCopyUtils {

    public static class TrueFilter implements FileFilter, Serializable {
        private static final long serialVersionUID = -6420452043795072619L;

        public final static TrueFilter TRUE = new TrueFilter();

        @Override
        public boolean accept(File pathname) {
            return true;
        }
    }

    public static void copyToFile(final InputStream source, final File destination) throws IOException {
        try (InputStream in = source;
             OutputStream out = openOutputStream(destination)) {
            copy(in, out);
        }
    }

    /**
     * 创建文件流
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static FileOutputStream openOutputStream(final File file) throws IOException {
        return openOutputStream(file, false);
    }

    /**
     * 创建文件流
     *
     * @param file
     * @param append
     * @return
     * @throws IOException
     */
    public static FileOutputStream openOutputStream(final File file, final boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            final File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

    /**
     * 文件转字节数组
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] file2ByteArray(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        byte[] data = byteArrayOutputStream.toByteArray();
        inputStream.close();
        byteArrayOutputStream.close();
        return data;
    }

    /**
     * 创建临时文件
     *
     * @param inputStream 输入文件流
     * @param name        文件名
     * @param ext         扩展名
     * @param tmpDirFile  临时文件夹目录
     * @return
     * @throws IOException
     */
    public static File createTmpFile(InputStream inputStream, String name, String ext, File tmpDirFile) throws IOException {
        File resultFile = File.createTempFile(name, '.' + ext, tmpDirFile);

        resultFile.deleteOnExit();
        copyToFile(inputStream, resultFile);
        return resultFile;
    }

    /**
     * 创建临时文件
     *
     * @param inputStream 输入文件流
     * @param name        文件名
     * @param ext         扩展名
     * @throws IOException
     */
    public static File createTmpFile(InputStream inputStream, String name, String ext) throws IOException {
        return createTmpFile(inputStream, name, ext, Files.createTempDirectory("weixin-java-tools-temp").toFile());
    }

    /**
     * base64转文件
     *
     * @param base64 字符串
     * @param type   文件类型
     * @return File
     */
    public static File base64ToFile(String base64, String type) {
        if (base64 == null || "".equals(base64)) {
            return null;
        }
        byte[] buff = Base64.getDecoder().decode(base64);
        File file = null;
        FileOutputStream fout = null;
        try {
            file = File.createTempFile("tmp", StringUtils.isEmpty(type) ? ".jpg" : type);
            fout = new FileOutputStream(file);
            fout.write(buff);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * base64转文件视频
     *
     * @param base64
     * @return File
     */
    public static File base64ToFileVideo(String base64) {
        if (base64 == null || "".equals(base64)) {
            return null;
        }
        byte[] buff = Base64.getDecoder().decode(base64);
        File file = null;
        FileOutputStream fout = null;
        try {
            file = File.createTempFile("tmp", ".mp4");
            fout = new FileOutputStream(file);
            fout.write(buff);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 获取文件后缀名
     *
     * @param fullName 文件全名
     * @return String
     */
    public static String getFileExtension(String fullName) {
        Assert.notNull(fullName, "file fullName is null.");
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * 获取文件名，去除后缀名
     *
     * @param file 文件名称
     * @return String
     */
    public static String getNameWithoutExtension(String file) {
        Assert.notNull(file, "file is null.");
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    /**
     * 转成file
     *
     * @param in   InputStream
     * @param file File
     * @return void
     */
    public static void toFile(InputStream in, final File file) {
        try (
                OutputStream out = new FileOutputStream(file)) {
            copy(in, out);
        } catch (IOException e) {
            throw ExceptionUtils.unchecked(e);
        }
    }


    /**
     * 扫描目录下的文件
     *
     * @param path 路径
     * @return List<File>
     */
    public static List<File> list(String path) {
        File file = new File(path);
        return list(file, TrueFilter.TRUE);
    }

    /**
     * 扫描目录下的文件
     *
     * @param path            路径
     * @param fileNamePattern 文件名 * 号
     * @return 文件集合
     */
    public static List<File> list(String path, final String fileNamePattern) {
        File file = new File(path);
        return list(file, pathname -> {
            String fileName = pathname.getName();
            return PatternMatchUtils.simpleMatch(fileNamePattern, fileName);
        });
    }

    /**
     * 扫描目录下的文件
     *
     * @param path   路径
     * @param filter 文件过滤
     * @return 文件集合
     */
    public static List<File> list(String path, FileFilter filter) {
        File file = new File(path);
        return list(file, filter);
    }

    /**
     * 扫描目录下的文件
     *
     * @param file 文件
     * @return 文件集合
     */
    public static List<File> list(File file) {
        List<File> fileList = new ArrayList<>();
        return list(file, fileList, TrueFilter.TRUE);
    }

    /**
     * 扫描目录下的文件
     *
     * @param file            文件
     * @param fileNamePattern Spring AntPathMatcher 规则
     * @return 文件集合
     */
    public static List<File> list(File file, final String fileNamePattern) {
        List<File> fileList = new ArrayList<>();
        return list(file, fileList, pathname -> {
            String fileName = pathname.getName();
            return PatternMatchUtils.simpleMatch(fileNamePattern, fileName);
        });
    }

    /**
     * 扫描目录下的文件
     *
     * @param file   文件
     * @param filter 文件过滤
     * @return 文件集合
     */
    public static List<File> list(File file, FileFilter filter) {
        List<File> fileList = new ArrayList<>();
        return list(file, fileList, filter);
    }

    /**
     * 扫描目录下的文件
     *
     * @param file   文件
     * @param filter 文件过滤
     * @return 文件集合
     */
    private static List<File> list(File file, List<File> fileList, FileFilter filter) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    list(f, fileList, filter);
                }
            }
        } else {
            // 过滤文件
            boolean accept = filter.accept(file);
            if (file.exists() && accept) {
                fileList.add(file);
            }
        }
        return fileList;
    }
}
