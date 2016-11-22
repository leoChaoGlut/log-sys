package cn.yunyichina.log.common.util;

/**
 * Created by Jonven on 2016/11/21.
 */
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

public class Zip
{
    static final int BUFFER = 8192;

    public Zip()
    {

    }

    private static void compress(File file, ZipOutputStream out, String basedir)
    {
    /* 判断是目录还是文件 */
        if (file.isDirectory())
        {
            // System.out.println("压缩：" + basedir + file.getName());
            compressDirectory(file, out, basedir);
        }
        else
        {
            // System.out.println("压缩：" + basedir + file.getName());
            compressFile(file, out, basedir);
        }
    }

    /** 压缩一个目录 */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir)
    {
        if (!dir.exists()) return;

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++)
        {
      /* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /** 压缩一个文件 */
    private static void compressFile(File file, ZipOutputStream out, String basedir)
    {
        if (!file.exists())
        {
            return;
        }
        try
        {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1)
            {
                out.write(data, 0, count);
            }
            bis.close();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void zip(String srcPathName, String zipFileName)
    {
        File file = new File(srcPathName);
        File zipFile = new File(zipFileName);
        if (!file.exists()) throw new RuntimeException(srcPathName + "不存在！");
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            out.setEncoding(System.getProperty("sun.jnu.encoding"));//设置文件名编码方式
            String basedir = "";
            compress(file, out, basedir);
            out.close();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        Zip.zip("E:\\testLog", "E:\\testLog.zip");
    }
}