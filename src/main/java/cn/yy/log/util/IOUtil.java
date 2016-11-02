package cn.yy.log.util;

import cn.yy.log.constant.CharSet;

import java.io.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 16-9-5 下午9:15
 * @Description:
 */
public class IOUtil {

    public static String read(InputStream is) throws IOException {
        return read(new InputStreamReader(is, CharSet.UTF8.getValue()));
    }

    public static String read(String fileUrl) throws IOException {
        return read(new FileReader(fileUrl));
    }

    public static String read(File file) throws IOException {
        return read(new FileReader(file));
    }

    public static String read(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public static void write(String filePath, String fileContent) {
        write(new File(filePath), fileContent, false);
    }

    public static void write(File file, String fileContent, boolean createNotExistsDirs) {
        if (createNotExistsDirs) {
            file.mkdirs();
        }
        write(file, fileContent);
    }

    public static void write(String filePath, String fileContent, boolean createNotExistsDirs) {
        File file = new File(filePath);
        if (createNotExistsDirs) {
            file.getParentFile().mkdirs();
        }
        write(file, fileContent);
    }

    public static void write(File file, String fileContent) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(fileContent);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
