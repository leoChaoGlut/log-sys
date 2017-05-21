package cn.yunyichina.log.service.tracer.serialization;

import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/21 14:23
 * @Description:
 */
public class TraceNodeDeserialization implements Deserializer<LinkedTraceNode> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public LinkedTraceNode deserialize(String topic, byte[] data) {
        return JSON.parseObject(data, LinkedTraceNode.class);
    }

    @Override
    public void close() {

    }
}
