package cn.yunyichina.log.service.collector.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/10 10:13
 * @Description: 用法:注入CacheManager后,调用getCache可以获得缓存器,然后利用缓存器就可以添加kv缓存了.
 */
@Component
public class SessionCacheManager {


    /**
     * 默认使用的cache
     */
    private LoadingCache<String, Object> minutesCache;

    @PostConstruct
    private void init() {
        minutesCache = this.<String, Object>defaultCacheBuilder(30, TimeUnit.MINUTES);
    }

    /**
     * 存在则返回值,不存在则返回null
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return minutesCache.getIfPresent(key);
    }

    public void put(String key, Object value) {
        minutesCache.put(key, value);
    }

    public boolean containsKey(String key) {
        ConcurrentMap<String, Object> minutesCacheMap = minutesCache.asMap();
        return minutesCacheMap.containsKey(key);
    }

    public void remove(String key) {
        ConcurrentMap<String, Object> minutesCacheMap = minutesCache.asMap();
        if (minutesCacheMap.containsKey(key)) {
            minutesCacheMap.remove(key);
        }
    }

    private <K, V> LoadingCache<K, V> defaultCacheBuilder(long duration, TimeUnit unit) {
        return CacheBuilder.newBuilder().<K, V>expireAfterAccess(duration, unit).<K, V>build(new CacheLoader<K, V>() {
            @Override
            public V load(K key) throws Exception {
                return null;
            }
        });
    }


}
