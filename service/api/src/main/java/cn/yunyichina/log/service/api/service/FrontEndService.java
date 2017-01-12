package cn.yunyichina.log.service.api.service;

import cn.yunyichina.log.common.entity.entity.po.Collector;
import cn.yunyichina.log.common.entity.entity.po.KeyValueTag;
import cn.yunyichina.log.common.entity.entity.po.KeywordTag;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.api.entity.dto.SearchOption;
import cn.yunyichina.log.service.api.mapper.CollectorMapper;
import cn.yunyichina.log.service.api.mapper.KeyValueTagMapper;
import cn.yunyichina.log.service.api.mapper.KeywordTagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 10:34
 * @Description:
 */
@Service
public class FrontEndService {

    final LoggerWrapper logger = LoggerWrapper.getLogger(FrontEndService.class);

    @Autowired
    CollectorMapper collectorMapper;

    @Autowired
    KeywordTagMapper keywordTagMapper;

    @Autowired
    KeyValueTagMapper keyValueTagMapper;

    //    @Cacheable(cacheNames = {OPTION}, key = "'option'")
    public SearchOption getSearchOption() {
        logger.info("缓存穿透");
        return buildSearchOption();
    }

    //    @CachePut(cacheNames = {OPTION}, key = "'option'")
    public SearchOption updateSearchOption() {
        return buildSearchOption();
    }

    private SearchOption buildSearchOption() {
        Map<String, List<Collector>> groupCollectorMap = buildGroupCollectorMap();
        Map<String, List<String>> collectorKeywordMap = buildCollectorKeywordMap();
        Map<String, List<String>> collectorKeyMap = buildCollectorKeyMap();

        return new SearchOption()
                .setGroupCollectorMap(groupCollectorMap)
                .setCollectorKeywordMap(collectorKeywordMap)
                .setCollectorKeyMap(collectorKeyMap);
    }

    private Map<String, List<Collector>> buildGroupCollectorMap() {
        List<Collector> collectorList = collectorMapper.selectAll();
        Map<String, List<Collector>> groupCollectorMap;
        if (CollectionUtils.isEmpty(collectorList)) {
            groupCollectorMap = new HashMap<>();
        } else {
            groupCollectorMap = new HashMap<>(collectorList.size());
            for (Collector collector : collectorList) {
                String group = collector.getGroup();
                List<Collector> collectorNameList = groupCollectorMap.get(group);
                if (null == collectorNameList) {
                    collectorNameList = new ArrayList<>();
                }
                collectorNameList.add(collector);
                groupCollectorMap.put(group, collectorNameList);
            }
        }
        return groupCollectorMap;
    }

    private Map<String, List<String>> buildCollectorKeywordMap() {
        List<KeywordTag> keywordTagList = keywordTagMapper.selectAll();
        Map<String, List<String>> collectorKeywordMap;
        if (CollectionUtils.isEmpty(keywordTagList)) {
            collectorKeywordMap = new HashMap<>();
        } else {
            collectorKeywordMap = new HashMap<>(keywordTagList.size());
            for (KeywordTag keywordTag : keywordTagList) {
                String collectorName = keywordTag.getCollector_name();
                List<String> keywordList = collectorKeywordMap.get(collectorName);
                if (null == keywordList) {
                    keywordList = new ArrayList<>();
                }
                keywordList.add(keywordTag.getKeyword());
                collectorKeywordMap.put(collectorName, keywordList);
            }
        }
        return collectorKeywordMap;
    }

    private Map<String, List<String>> buildCollectorKeyMap() {
        List<KeyValueTag> keyValueTagList = keyValueTagMapper.selectAll();
        Map<String, List<String>> collectorKeyMap;
        if (CollectionUtils.isEmpty(keyValueTagList)) {
            collectorKeyMap = new HashMap<>();
        } else {
            collectorKeyMap = new HashMap<>(keyValueTagList.size());
            for (KeyValueTag keyValueTag : keyValueTagList) {
                String collectorName = keyValueTag.getCollector_name();
                List<String> keyList = collectorKeyMap.get(collectorName);
                if (null == keyList) {
                    keyList = new ArrayList<>();
                }
                keyList.add(keyValueTag.getKey());
                collectorKeyMap.put(collectorName, keyList);
            }
        }
        return collectorKeyMap;
    }


}
