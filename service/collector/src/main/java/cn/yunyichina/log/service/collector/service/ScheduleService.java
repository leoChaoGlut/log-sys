package cn.yunyichina.log.service.collector.service;

import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.service.collector.constants.Key;
import cn.yunyichina.log.service.collector.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/9 14:13
 * @Description:
 */
@Service
public class ScheduleService {

    @Autowired
    private PropertiesUtil propUtil;

    /**
     * 记录最新的上下文计数
     *
     * @param contextInfoMap
     */
    @Async
    public void recordLastestContextCount(Map<Long, ContextIndexBuilder.ContextInfo> contextInfoMap) {
        Set<Long> countSet = contextInfoMap.keySet();
        Long lastestCount = Collections.max(countSet);
        propUtil.put(Key.CONTEXT_COUNT, lastestCount + "");
    }
}
