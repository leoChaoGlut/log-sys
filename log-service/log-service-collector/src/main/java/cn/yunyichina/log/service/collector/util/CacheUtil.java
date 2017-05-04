package cn.yunyichina.log.service.collector.util;

import cn.yunyichina.log.component.common.constant.IndexType;
import cn.yunyichina.log.service.collector.cache.CollectedItemCache;
import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 17:21
 * @Description:
 */
public class CacheUtil {

    public static final String CACHE_DIR = System.getProperty("user.dir") + File.separator + "cache";
    public static final String APPLICATION_CACHE_PATH_TMPL = CACHE_DIR + File.separator + "%s" + File.separator + "application.cache";

    public static CollectedItemCache readCollectedItemCache(Integer collectedItemId) throws IOException {
        String path = String.format(APPLICATION_CACHE_PATH_TMPL, collectedItemId);
        File collectedItemCacheFile = new File(path);
        if (collectedItemCacheFile.exists()) {
            byte[] bytes = Files.toByteArray(collectedItemCacheFile);
            return JSON.parseObject(bytes, CollectedItemCache.class);
        } else {
            return null;
        }
    }

    public static void writeCollectedItemCache(Integer collectedItemId, CollectedItemCache collectedItemCache) throws IOException {
        String path = String.format(APPLICATION_CACHE_PATH_TMPL, collectedItemId);
        File collectedItemCacheFile = new File(path);
        if (!collectedItemCacheFile.exists()) {
            Files.createParentDirs(collectedItemCacheFile);
        }
        Files.write(JSON.toJSONBytes(collectedItemCache), collectedItemCacheFile);
    }

    public static void writeIndexCache(byte[] bytes, String cacheFilePath) throws IOException {
        File indexCacheFile = new File(CACHE_DIR + File.separator + cacheFilePath);
        if (!indexCacheFile.exists()) {
            Files.createParentDirs(indexCacheFile);
        }
        Files.write(bytes, indexCacheFile);
    }

    public static void deleteIndexCache(File file, IndexType indexType) {
        if (file.isFile()) {
            file.delete();
        }
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            File[] listFiles = parentFile.listFiles();
            if (listFiles != null) {
                if (listFiles.length > 1) {

                } else {
                    if (parentFile.getName().equals(indexType.getVal())) {

                    } else {
                        parentFile.delete();
                        deleteIndexCache(parentFile, indexType);
                    }
                }
            }
        }
    }

}
