package cn.yunyichina.log.service.tracer.lifecycle;


import cn.yunyichina.log.common.LoggerWrapper;
import cn.yunyichina.log.service.tracer.kafka.consumer.TraceConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/9/29 17:56
 * @Description: 项目启动完成后回调.
 * 接口装配:一个platform接口,会对应1个或多个his接口(或别的流程操作),所以需要将1个或多个his接口装配到platform接口里.
 * 并且,多个his接口之间是有顺序关系的.
 * 默认是读取mapping.properties
 */
@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    LoggerWrapper logger = LoggerWrapper.getLogger(ApplicationReadyListener.class);

    @Autowired
    TraceConsumer traceConsumer;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        traceConsumer.createTable();
        traceConsumer.startConsumer();
    }

}
