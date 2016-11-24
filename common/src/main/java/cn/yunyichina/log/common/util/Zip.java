package cn.yunyichina.log.common.util;

/**
 * Created by Jonven on 2016/11/21.
 */

import com.google.common.io.ByteStreams;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

public class Zip {

    private static void compress(File[] files, ZipOutputStream out) {
        compressFile(files, out);
    }

    /**
     * 压缩一个文件
     */
    private static void compressFile(File[] files, ZipOutputStream out) {
        for (File file : files) {
            if (file.exists()) {
                try {
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                    ZipEntry entry = new ZipEntry(file.getName());
                    out.putNextEntry(entry);
                    byte data[] = ByteStreams.toByteArray(bis);
                    out.write(data);
                    bis.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void zip(File[] files, String zipFileName) {
        File zipFile = new File(zipFileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            out.setEncoding(System.getProperty("sun.jnu.encoding"));//设置文件名编码方式
            compress(files, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}