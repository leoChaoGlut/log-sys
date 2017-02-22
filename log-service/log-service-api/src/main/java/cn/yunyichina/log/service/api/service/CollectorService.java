package cn.yunyichina.log.service.api.service;

import cn.yunyichina.log.common.entity.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.entity.do_.CollectorDO;
import cn.yunyichina.log.common.entity.entity.do_.KeywordTagDO;
import cn.yunyichina.log.common.entity.entity.do_.KvTagDO;
import cn.yunyichina.log.service.api.constants.CacheName;
import cn.yunyichina.log.service.api.mapper.CollectedItemMapper;
import cn.yunyichina.log.service.api.mapper.CollectorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/22 14:10
 * @Description:
 */
@Service
public class CollectorService {

    @Autowired
    CollectorMapper collectorMapper;

    @Autowired
    CollectedItemMapper collectedItemMapper;

    @Cacheable(cacheNames = CacheName.COLLECTOR, key = "#applicationName")
    public List<CollectedItemDO> listCollectedItem(String applicationName) {
        CollectorDO collectorDOParam = new CollectorDO()
                .setApplicationName(applicationName);
        CollectorDO collectorDOResult = collectorMapper.selectOne(collectorDOParam);

        List<CollectedItemDO> collectedItemDOList = collectorDOResult.getCollectedItemDOList();
        if (CollectionUtils.isEmpty(collectedItemDOList)) {
            return new ArrayList<>();
        } else {
            for (CollectedItemDO collectedItemDO : collectedItemDOList) {
                List<KvTagDO> kvTagDOList = collectedItemMapper.selectKvTagList(collectedItemDO.getId());
                collectedItemDO.setKvTagDOList(kvTagDOList);

                List<KeywordTagDO> keywordTagDOList = collectedItemMapper.selectKeywordTagList(collectedItemDO.getId());
                collectedItemDO.setKeywordTagDOList(keywordTagDOList);
            }
            return collectedItemDOList;
        }
    }
}
