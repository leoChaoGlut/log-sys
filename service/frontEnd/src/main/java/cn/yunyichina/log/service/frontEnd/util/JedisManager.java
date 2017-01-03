package cn.yunyichina.log.service.frontEnd.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 16:20
 * @Description:
 */
@Component
public class JedisManager {

    private JedisPool pool;
    private JedisPoolConfig config;

    @PostConstruct
    public void init() {
        config = new JedisPoolConfig();
        pool = new JedisPool(config, "localhost");
    }

    public Jedis getInstance() {
        return pool.getResource();
    }

    public void destroy() {
        pool.destroy();
    }

}
