package cn.yunyichina.log.service.tracer.service;

import cn.yunyichina.log.common.base.AbstractService;
import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import cn.yunyichina.log.common.exception.TracerException;
import cn.yunyichina.log.service.tracer.manager.JedisManager;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import static cn.yunyichina.log.service.tracer.manager.JedisManager.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/29 18:41
 * @Description:
 */
@Service
public class TraceService extends AbstractService {

    @Autowired
    JedisManager jedisManager;

    public void appendLinkedNode(LinkedTraceNode linkedTraceNode) {
        try (
                Jedis jedis = jedisManager.getJedis();
        ) {
            int i = 0;
            while (true) {
                boolean success = appendLinkedNode(linkedTraceNode, jedis);
                if (success) {
                    break;
                } else {
                    if (i < MAX_RETRY_COUNT) {
                        i++;
                        try {
                            TimeUnit.MILLISECONDS.sleep(RETRY_INTERVAL_IN_MILLIES);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        new TracerException("appendLinkedNode 超过最大重试次数:" + MAX_RETRY_COUNT + ", 仍失败. JSON:" + JSON.toJSONString(linkedTraceNode));
                    }
                }
            }
        }
    }

    private boolean appendLinkedNode(LinkedTraceNode linkedTraceNode, Jedis jedis) {
        String traceId = linkedTraceNode.getTraceId();
        String contextId = linkedTraceNode.getContextId();
        jedis.watch(traceId, contextId);
        Transaction tx = jedis.multi();
        tx.zadd(
                traceId,
                linkedTraceNode.getTimestamp(),
                JSON.toJSONString(linkedTraceNode)
        );
        tx.set(contextId, traceId);
        List<Object> resultList = tx.exec();
        return trasactionFailure(resultList);
    }

    public TreeSet<LinkedTraceNode> getTraceByContextId(String contextId) {
        try (
                Jedis jedis = jedisManager.getJedis();
        ) {
            String traceId = jedis.get(contextId);
            if (null == traceId) {
                return new TreeSet<>();
            } else {
                return getLinkedTraceNodeSet(traceId, jedis);
            }
        }
    }

    public TreeSet<LinkedTraceNode> getTraceByTraceId(String traceId) {
        try (
                Jedis jedis = jedisManager.getJedis();
        ) {
            return getLinkedTraceNodeSet(traceId, jedis);
        }
    }

    private TreeSet<LinkedTraceNode> getLinkedTraceNodeSet(String traceId, Jedis jedis) {
        Set<String> traceNodeStrSet = jedis.zrange(traceId, 0, -1);
        if (CollectionUtils.isEmpty(traceNodeStrSet)) {
            return new TreeSet<>();
        } else {
            TreeSet<LinkedTraceNode> traceNodeSet = new TreeSet<>();
            LinkedTraceNode traceNode;
            for (String traceNodeStr : traceNodeStrSet) {
                traceNode = JSON.parseObject(traceNodeStr, LinkedTraceNode.class);
                traceNodeSet.add(traceNode);
            }
            return traceNodeSet;
        }
    }
}
