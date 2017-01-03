package cn.yunyichina.log.service.frontEnd.service;

import cn.yunyichina.log.service.frontEnd.entity.dto.Option;
import cn.yunyichina.log.service.frontEnd.mapper.CollectorMapper;
import cn.yunyichina.log.service.frontEnd.mapper.KeywordeIndexMapper;
import cn.yunyichina.log.service.frontEnd.mapper.KvIndexMapper;
import cn.yunyichina.log.service.frontEnd.util.JedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 10:34
 * @Description:
 */
@Service
public class FrontEndService {

    @Autowired
    CollectorMapper collectorMapper;

    @Autowired
    KeywordeIndexMapper keywordeIndexMapper;

    @Autowired
    KvIndexMapper kvIndexMapper;

    @Autowired
    JedisManager jedisManager;

    public Option getOption() {
//        try (
////                Jedis jedis = jedisManager.getInstance();
//        ) {
////            jedis.lpush()
//        }
        return null;
    }

}
