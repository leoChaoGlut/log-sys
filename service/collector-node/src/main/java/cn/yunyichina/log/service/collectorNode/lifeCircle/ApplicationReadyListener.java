package cn.yunyichina.log.service.collectorNode.lifeCircle;


import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.collectorNode.constants.Key;
import cn.yunyichina.log.service.collectorNode.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/9/29 17:56
 * @Description:
 */
@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    PropertiesUtil propUtil;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initCounter();
    }

    /**
     * 当程序重启的时候,把LoggerWrapper的count恢复为之前的数字,不恢复的话,会是0
     */
    private void initCounter() {
        String countStr = propUtil.get(Key.CONTEXT_COUNT);
        if (StringUtils.isNotBlank(countStr)) {
            Long count = Long.valueOf(countStr);
            LoggerWrapper.initCounter(count);
        }
    }
}
