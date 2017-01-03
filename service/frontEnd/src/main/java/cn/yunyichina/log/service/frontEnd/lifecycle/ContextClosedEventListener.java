package cn.yunyichina.log.service.frontEnd.lifecycle;

import cn.yunyichina.log.service.frontEnd.util.JedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 17:13
 * @Description:
 */
@Component
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    JedisManager jedisManager;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        jedisManager.destroy();
    }
}
