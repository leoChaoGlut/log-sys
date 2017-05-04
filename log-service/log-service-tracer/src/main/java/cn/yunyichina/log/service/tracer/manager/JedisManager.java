package cn.yunyichina.log.service.tracer.manager;

import cn.yunyichina.log.service.tracer.config.JedisProperties;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import javax.annotation.PostConstruct;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/05/01 10:45
 * @Description:
 */
@Component
public class JedisManager {

    @Getter
    private JedisCluster cluster;
    @Getter
    private JedisPool jedisPool;

    @Autowired
    JedisProperties jedisProperties;

    @PostConstruct
    private void postConstruct() {
//        initJedisCluster();
        initJedisPool();
    }

    private void initJedisPool() {
        jedisPool = new JedisPool(new JedisPoolConfig(), jedisProperties.getIp(), jedisProperties.getPort(), Protocol.DEFAULT_TIMEOUT, jedisProperties.getPassword());
    }

    private void initJedisCluster() {
        cluster = new JedisCluster(jedisProperties.getCluster().getJedisHostAndPort());
    }


    public Jedis getJedis() {
        return jedisPool.getResource();
    }

}
