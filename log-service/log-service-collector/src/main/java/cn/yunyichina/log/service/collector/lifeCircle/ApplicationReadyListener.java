package cn.yunyichina.log.service.collector.lifeCircle;


import cn.yunyichina.log.common.entity.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.entity.dto.ResponseDTO;
import cn.yunyichina.log.common.entity.entity.dto.Status;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.collector.client.ApiClient;
import cn.yunyichina.log.service.collector.constants.Key;
import cn.yunyichina.log.service.collector.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/9/29 17:56
 * @Description:
 */
@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    final LoggerWrapper logger = LoggerWrapper.getLogger(ApplicationReadyListener.class);

    @Autowired
    Environment env;

    @Autowired
    PropertiesUtil propUtil;

    @Autowired
    ApiClient apiClient;

    @Value("${spring.application.name}")
    String applicationName;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ResponseDTO responseDTO = apiClient.listCollectedItem(applicationName);
        if (Objects.equals(responseDTO.getCode(), Status.SUCCESS.getCode() + "")) {
            List<CollectedItemDO> collectedItemDOList = (List<CollectedItemDO>) responseDTO.getResult();

        } else {
            throw new RuntimeException(responseDTO.getMsg());
        }
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
