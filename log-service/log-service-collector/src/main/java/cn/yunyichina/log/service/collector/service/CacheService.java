package cn.yunyichina.log.service.collector.service;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.do_.CollectorDO;
import lombok.Getter;
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

    @Getter
    private CollectorDO collector;

    public synchronized void setCollector(CollectorDO collector) {
        this.collector = collector;
    }


    //    @Cacheable(cacheNames = CacheName.NORMAL, key = "#collectedItemId")
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
