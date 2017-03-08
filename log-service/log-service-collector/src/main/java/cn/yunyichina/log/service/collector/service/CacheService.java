package cn.yunyichina.log.service.collector.service;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.do_.CollectorDO;
import cn.yunyichina.log.service.collector.constants.CacheName;
import lombok.Getter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 16:57
 * @Description:
 */
@Service
public class CacheService {

    private volatile boolean hasCacheCollector = false;

    @Getter
    private CollectorDO collector;

    public synchronized void setCollector(CollectorDO collector) {
        if (hasCacheCollector) {

        } else {
            this.collector = collector;
            this.hasCacheCollector = true;
        }
    }


    @Cacheable(cacheNames = CacheName.NORMAL, key = "#collectedItemId")
    public CollectedItemDO getCollectedItemBy(Integer collectedItemId) {
        List<CollectedItemDO> collectedItemList = collector.getCollectedItemList();
        for (CollectedItemDO collectedItem : collectedItemList) {
            if (Objects.equals(collectedItem.getId(), collectedItemId)) {
                return collectedItem;
            }
        }
        return null;
    }
}
