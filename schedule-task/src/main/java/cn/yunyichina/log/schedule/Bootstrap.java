package cn.yunyichina.log.schedule;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 16-11-17 下午5:15
 * @Description: 变量从当前jar包的同目录下的config.properties中获取
 */
public class Bootstrap {

    public static void main(String[] args) throws IOException {
        Process process = Runtime.getRuntime().exec(buildCmd());
        byte[] bytes = new byte[1024];
        InputStream is = process.getInputStream();
        int len = is.read(bytes);
        is.close();
        String result = new String(bytes, 0, len);
        if ("success".equals(result.trim())) {
            System.out.println("success");
        } else {
            System.out.println("failure");
        }
    }

    private static String[] buildCmd() throws IOException {
        Properties prop = loadProperties();
        String[] cmdArr = {"/bin/bash", prop.getProperty("shellName"), prop.getProperty("ip"), prop.getProperty("username"), prop.getProperty("password"), prop.getProperty("destDir"), prop.getProperty("srcFile")};
        return cmdArr;
    }

    private static Properties loadProperties() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + File.separator + "config.properties");
        prop.load(fis);
        fis.close();
        return prop;
    }
}
