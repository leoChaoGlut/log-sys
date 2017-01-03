package cn.yunyichina.log.service.frontEnd.service;

import cn.yunyichina.log.service.frontEnd.entity.dto.SearchOption;
import cn.yunyichina.log.service.frontEnd.util.JedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 10:34
 * @Description:
 */
@Service
public class FrontEndService {

    @Autowired
    JedisManager jedisManager;

    public SearchOption getOption() {
//        getOptionFromCache();
        getOptionFromDB();
        return null;
    }

    private SearchOption getOptionFromCache() {
        try (
                Jedis jedis = jedisManager.getInstance();
        ) {
            Set<String> collectorSet = jedis.smembers("all_collectors");
            if (CollectionUtils.isEmpty(collectorSet)) {

            } else {
                for (String colelctorName : collectorSet) {

                }
            }
        }
        return null;
    }

    private SearchOption getOptionFromDB() {
        return null;
    }
}
