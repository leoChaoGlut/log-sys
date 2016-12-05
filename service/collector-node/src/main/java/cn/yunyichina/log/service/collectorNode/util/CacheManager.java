package cn.yunyichina.log.service.collectorNode.util;

import cn.yunyichina.log.service.collectorNode.constants.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/5 14:27
 * @Description:
 */
@Component
public class CacheManager {
    private Map<String, Object> cacheMap;

    @Autowired
    private Config config;

    @PostConstruct
    public void init() throws Exception {
        File cacheFile = new File(config.getCachePath());
        if (cacheFile.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(cacheFile));
                cacheMap = (Map<String, Object>) ois.readObject();
            } finally {
                if (ois != null) {
                    ois.close();
                }
            }
        } else {
//    TODO  如果没有缓存,则需要发送网络请求获取
        }
    }

    public Map<String, Object> getCacheMap() {
        return cacheMap;
    }
}
