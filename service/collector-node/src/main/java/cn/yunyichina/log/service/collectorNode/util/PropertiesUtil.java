package cn.yunyichina.log.service.collectorNode.util;

import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/6 14:32
 * @Description:
 */
@Component
public class PropertiesUtil {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Properties prop = new Properties();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Value("${path.properties}")
    private String propertiesPath;

    @PostConstruct
    public void init() throws Exception {
        File propertiesFile = new File(propertiesPath);
        if (!propertiesFile.exists()) {
            Files.createParentDirs(propertiesFile);
            propertiesFile.createNewFile();
        }
        FileInputStream fis = new FileInputStream(propertiesFile);
        prop.load(fis);
        fis.close();
    }

    public String get(String key) {
        readWriteLock.readLock().lock();
        try {
            return prop.getProperty(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void put(String key, String value) {
        readWriteLock.writeLock().lock();
        FileOutputStream fos = null;
        try {
            prop.setProperty(key, value);
            fos = new FileOutputStream(propertiesPath);
            prop.store(fos, sdf.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            readWriteLock.writeLock().unlock();
        }
    }

    public void remove(String key) {
        readWriteLock.writeLock().lock();
        FileOutputStream fos = null;
        try {
            prop.remove(key);
            fos = new FileOutputStream(propertiesPath);
            prop.store(fos, sdf.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            readWriteLock.writeLock().unlock();
        }
    }

}
