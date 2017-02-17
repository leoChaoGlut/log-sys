package cn.yunyichina.log.service.collector.util;

import cn.yunyichina.log.service.collector.constants.ServiceConfig;
import com.google.common.io.Files;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/6 14:32
 * @Description:
 */
@Component
public class PropertiesMapUtil {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    //    private final Properties prop = new Properties();
    private final FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");
    private Map<String, Properties> propMap = new HashMap<>();
    private Map<String, String> propertiesPathMap = new HashMap<>();

    @Autowired
    ServiceConfig config;

    //不允许直接new,只能通过spring来使用,保证properties安全.
    private PropertiesMapUtil() {

    }

    @PostConstruct
    public void init() throws Exception {
        Map<String, ServiceConfig.ConfigCollector> collectorMap = config.getCollectorMap();
        for (Map.Entry<String, ServiceConfig.ConfigCollector> entry : collectorMap.entrySet()) {
            String logCollectorName = entry.getKey();
            String propertiesPath = entry.getValue().getPropertiesPath();
            System.out.println("==========" + propertiesPath);
            File propertiesFile = new File(propertiesPath);
            if (!propertiesFile.exists()) {
                Files.createParentDirs(propertiesFile);
                boolean succeed = propertiesFile.createNewFile();
                if (succeed) {

                } else {
                    throw new Exception(logCollectorName + "创建properties文件失败");
                }
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(propertiesFile);
                Properties prop = new Properties();
                prop.load(fis);
                propMap.put(logCollectorName, prop);
                propertiesPathMap.put(logCollectorName, propertiesPath);
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        }

    }

    public String get(String collectorName, String key) {
        readWriteLock.readLock().lock();
        try {
            Properties prop = propMap.get(collectorName);
            return prop.getProperty(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void put(String collectorName, String key, String value) {
        Properties prop = null;
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        FileOutputStream fos = null;
        try {
            prop = propMap.get(collectorName);
            prop.setProperty(key, value);
            fos = new FileOutputStream(propertiesPathMap.get(collectorName));
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

    public void remove(String collectorName, String key) {
        Properties prop = null;
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        FileOutputStream fos = null;
        try {
            prop = propMap.get(collectorName);
            prop.remove(key);
            fos = new FileOutputStream(propertiesPathMap.get(collectorName));
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
