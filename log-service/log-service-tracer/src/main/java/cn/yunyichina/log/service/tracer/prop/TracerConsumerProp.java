package cn.yunyichina.log.service.tracer.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/21 10:52
 * @Description:
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "kafka.consumer")
public class TracerConsumerProp {
    private String bootstrapServers;
    private List<String> topicList;
    private String groupId;
}
