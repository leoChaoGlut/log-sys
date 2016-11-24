package cn.yunyichina.log.common.util;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 17:39
 * @Description:
 */
public class ZipUtil {


    public static void zip(String outputZipFilePath, File[] files) throws Exception {
        ZipOutputStream zipOs = null;
        try {
            zipOs = new ZipOutputStream(new FileOutputStream(outputZipFilePath), Charsets.UTF_8);
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    ZipEntry zipEntry = new ZipEntry(files[i].getName());
                    zipOs.putNextEntry(zipEntry);
                    zipOs.write(Files.asByteSource(files[i]).read());
                }
                zipOs.flush();
                zipOs.finish();
            }
        } finally {
            if (zipOs != null) {
                zipOs.close();
            }
        }
    }

    public static void unzip(String inputZipFilePath, String outputRootDir) throws Exception {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(inputZipFilePath, Charsets.UTF_8);
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
            if (zipEntries != null) {
                while (zipEntries.hasMoreElements()) {
                    ZipEntry zipEntry = zipEntries.nextElement();
                    InputStream is = zipFile.getInputStream(zipEntry);
                    byte[] bytes = ByteStreams.toByteArray(is);
                    is.close();
                    String entryName = zipEntry.getName();
                    File file = new File(outputRootDir + entryName);
                    if (!file.exists()) {
                        Files.createParentDirs(file);
                        file.createNewFile();
                    }
                    Files.write(bytes, file);
                }
            }
        } finally {
            if (zipFile != null) {
                zipFile.close();
            }
        }
    }

}
