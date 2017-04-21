package cn.yunyichina.log.service.collector.lifecircle;


import cn.yunyichina.log.common.entity.do_.CollectorDO;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.common.util.ResponseUtil;
import cn.yunyichina.log.service.collector.client.CollectorServiceClient;
import cn.yunyichina.log.service.collector.exception.CollectorException;
import cn.yunyichina.log.service.collector.service.CacheService;
import cn.yunyichina.log.service.collector.task.ScheduleTask;
import cn.yunyichina.log.service.collector.task.ScheduleTaskV2;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/9/29 17:56
 * @Description:
 */
@Component
@RefreshScope
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    final Logger logger = LoggerFactory.getLogger(ApplicationReadyListener.class);

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${eureka.instance.ipAddress}")
    String ip;

    @Value("${server.port}")
    String port;

    @Autowired
    CacheService cacheService;

    @Autowired
    ScheduleTask scheduleTask;
    @Autowired
    ScheduleTaskV2 scheduleTaskV2;

    @Autowired
    CollectorServiceClient collectorServiceClient;

    final int MAX_RETRY_COUNT = 3;

    int retryCount = 0;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        init();
    }

    private void init() {
        try {
            registerAndGetData();
        } catch (Exception e) {
            e.printStackTrace();
            if (retryCount < MAX_RETRY_COUNT) {
                retryCount++;
                logger.error("初始化异常，3秒后重试", e);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                init();
            } else {
//                3次尝试失败，退出程序
                logger.error("3次尝试失败，退出程序");
                System.exit(0);
            }
        }
    }

    public void registerAndGetData() throws IOException, CollectorException, ClassNotFoundException {
        ResponseBodyDTO<CollectorDO> responseBody = collectorServiceClient.registerAndGetData(ip, port, applicationName);
        logger.info(JSON.toJSONString(responseBody, true));
        CollectorDO collector = ResponseUtil.getResult(responseBody);
        cacheService.setCollector(collector);
        scheduleTask.buildCollectedItemList();
        scheduleTaskV2.buildCollectedItemList();
    }


}
