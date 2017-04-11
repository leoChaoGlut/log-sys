package cn.yunyichina.log.service.tracer.manager;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/2 15:52
 * @Description:
 */
@Component
public class JedisManager {
    final Logger logger = LoggerFactory.getLogger(JedisManager.class);

    @Autowired
    private RedisProperties redisProperties;
    @Getter
    private JedisPool pool;

    public static final int MAX_RETRY_COUNT = 10;
    public static final int RETRY_INTERVAL_IN_MILLIES = 1_000;

    @PostConstruct
    private void init() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        int timeout = redisProperties.getTimeout();
        String password = redisProperties.getPassword();
        logger.info(host + " - " + port + " - " + timeout + " - " + password);
        pool = new JedisPool(poolConfig, host, port, timeout, password);
    }

    @PreDestroy
    private void destroy() {
        pool.destroy();
    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    public static boolean trasactionFailure(List<Object> resultList) {
        if (resultList == null || resultList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
