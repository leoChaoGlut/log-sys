package cn.yunyichina.log.service.collector.util;

import com.google.common.io.Files;

import java.io.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 17:21
 * @Description:
 */
public class CacheUtil {

    public static final String CACHE_DIR = System.getProperty("user.dir") + File.separator + "cache";

    public static void write(Object object, Integer collectedItemId, String cacheFileName) throws IOException, ClassNotFoundException {
        String cachePath = buildCachePath(collectedItemId, cacheFileName);
        File cacheFile = new File(cachePath);
        if (cacheFile.exists()) {

        } else {
            Files.createParentDirs(cacheFile);
            cacheFile.createNewFile();
        }
        try (
                FileOutputStream fos = new FileOutputStream(cachePath);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(object);
            oos.flush();
        }
    }


    public static <T> T read(Integer collectedItemId, String cacheFileName) throws IOException, ClassNotFoundException {
        String cachePath = buildCachePath(collectedItemId, cacheFileName);
        File cacheFile = new File(cachePath);
        if (cacheFile.exists()) {

        } else {
            Files.createParentDirs(cacheFile);
            cacheFile.createNewFile();
            try (
                    FileOutputStream fos = new FileOutputStream(cachePath);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
            ) {
                oos.writeObject(null);
                oos.flush();
            }
        }
        try (
                FileInputStream fis = new FileInputStream(cachePath);
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            return (T) ois.readObject();
        }
    }

    public static String buildCachePath(Integer collectedItemId, String cacheFileName) {
        return CACHE_DIR + File.separator + collectedItemId + File.separator + cacheFileName;
    }
}
