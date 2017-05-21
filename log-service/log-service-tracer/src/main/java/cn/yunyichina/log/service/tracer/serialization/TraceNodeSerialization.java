package cn.yunyichina.log.service.tracer.serialization;

import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/21 14:23
 * @Description:
 */
public class TraceNodeSerialization implements Serializer<LinkedTraceNode> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, LinkedTraceNode data) {
        return JSON.toJSONBytes(data);
    }

    @Override
    public void close() {

    }
}
