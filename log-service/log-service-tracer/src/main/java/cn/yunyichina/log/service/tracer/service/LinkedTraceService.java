package cn.yunyichina.log.service.tracer.service;

import cn.yunyichina.log.common.base.AbstractService;
import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import cn.yunyichina.log.service.tracer.manager.JedisManager;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/29 18:41
 * @Description:
 */
@Service
public class LinkedTraceService extends AbstractService {

    final Logger logger = LoggerFactory.getLogger(LinkedTraceService.class);

    @Autowired
    JedisManager jedisManager;

    public TreeSet<LinkedTraceNode> getTraceByContextId(
            String contextId
    ) {
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

    public TreeSet<LinkedTraceNode> getTraceByTraceId(
            String traceId
    ) {
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

    /**
     * 如果发送的数据特别多,可以考虑使用消息队列.
     *
     * @param linkedTraceNodeList
     */
    @Async
    public void appendLinkedNodeBatch(List<LinkedTraceNode> linkedTraceNodeList) {
        try (
                Jedis jedis = jedisManager.getJedis();
        ) {
            Pipeline p = jedis.pipelined();
            for (LinkedTraceNode linkedTraceNode : linkedTraceNodeList) {
                p.zadd(
                        linkedTraceNode.getTraceId(),
                        linkedTraceNode.getTimestamp(),
                        JSON.toJSONString(linkedTraceNode)
                );
                p.set(linkedTraceNode.getContextId(), linkedTraceNode.getTraceId());
            }
            p.sync();
        }
    }
}

