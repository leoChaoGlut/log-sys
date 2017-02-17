package cn.yunyichina.log.service.api.service;

import cn.yunyichina.log.common.entity.entity.do_.*;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.api.entity.dto.SearchOption;
import cn.yunyichina.log.service.api.mapper.CollectorMapper;
import cn.yunyichina.log.service.api.mapper.GroupMapper;
import cn.yunyichina.log.service.api.mapper.KeywordIndexMapper;
import cn.yunyichina.log.service.api.mapper.KvIndexMapper;
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
    GroupMapper groupMapper;

    @Autowired
    CollectorMapper collectorMapper;

    @Autowired
    KeywordIndexMapper keywordIndexMapper;

    @Autowired
    KvIndexMapper kvIndexMapper;

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

        Map<Integer, Group> groupMap = bulidGroupMap();
        Map<Integer, Collector> collectorMap = bulidCollectorMap();

        Map<String, List<Collector>> groupCollectorMap = buildGroupCollectorMap(groupMap, collectorMap);
        Map<String, List<KeywordIndex>> collectorKeywordMap = buildCollectorKeywordMap(collectorMap);
        Map<String, List<KvIndex>> collectorKeyMap = buildCollectorKeyMap(collectorMap);

        return new SearchOption()
                .setGroupCollectorMap(groupCollectorMap)
                .setCollectorKeywordMap(collectorKeywordMap)
                .setCollectorKeyMap(collectorKeyMap);
    }

    private Map<Integer, Group> bulidGroupMap() {
        List<Group> groupList = groupMapper.selectAll();
        Map<Integer, Group> groupMap = null;
        if (CollectionUtils.isEmpty(groupList)) {

        } else {
            groupMap = new HashMap<>((groupList.size()));
            for (Group group : groupList) {
                Integer groupId = group.getId();
                groupMap.put(groupId, group);
            }
        }
        return groupMap;
    }

    private Map<Integer, Collector> bulidCollectorMap() {
        List<Collector> collectorList = collectorMapper.selectAll();
        Map<Integer, Collector> collectorMap = null;
        if (CollectionUtils.isEmpty(collectorList)) {

        } else {
            collectorMap = new HashMap<>(collectorList.size());
            for (Collector collector : collectorList) {
                Integer collectorId = collector.getId();
                collectorMap.put(collectorId, collector);
            }
        }
        return collectorMap;
    }

    private Map<String, List<Collector>> buildGroupCollectorMap(Map<Integer, Group> groupMap, Map<Integer, Collector> collectorMap) {
        Map<String, List<Collector>> groupCollectorMap;
        if (CollectionUtils.isEmpty(groupMap)) {
            groupCollectorMap = new HashMap<>();
        } else {
            if (CollectionUtils.isEmpty(collectorMap)) {
                groupCollectorMap = new HashMap<>();
            } else {
                groupCollectorMap = new HashMap<>(collectorMap.size());
                for (Map.Entry<Integer, Collector> entry : collectorMap.entrySet()) {
                    Collector collector = entry.getValue();

                    Group group = groupMap.get(collector.getGroup_id());
                    if (group == null) {

                    } else {
                        String groupName = group.getName();
                        List<Collector> collectorNameList = groupCollectorMap.get(groupName);
                        if (null == collectorNameList) {
                            collectorNameList = new ArrayList<>();
                        }
                        collectorNameList.add(collector);
                        groupCollectorMap.put(groupName, collectorNameList);
                    }
                }
            }
        }
        return groupCollectorMap;
    }

    private Map<String, List<KeywordIndex>> buildCollectorKeywordMap(Map<Integer, Collector> collectorMap) {
        Map<String, List<KeywordIndex>> collectorKeywordMap = new HashMap<>();
        if (CollectionUtils.isEmpty(collectorMap)) {

        } else {
            List<KeywordIndex> keywordIndexList = keywordIndexMapper.selectAll();
            if (CollectionUtils.isEmpty(keywordIndexList)) {

            } else {
                Map<Integer, KeywordIndex> keywordIndexMap = new HashMap<>(keywordIndexList.size());
                for (KeywordIndex keywordIndex : keywordIndexList) {
                    keywordIndexMap.put(keywordIndex.getId(), keywordIndex);
                }
                List<MidCollectorKeyword> midCollectorKeywordList = keywordIndexMapper.selectAllMid();
                if (CollectionUtils.isEmpty(midCollectorKeywordList)) {

                } else {
                    for (MidCollectorKeyword mid : midCollectorKeywordList) {
                        Integer collectorId = mid.getCollector_id();
                        Integer keywordId = mid.getKeyword_id();
                        Collector collector = collectorMap.get(collectorId);
                        KeywordIndex keywordIndex = keywordIndexMap.get(keywordId);
                        if (collector == null || keywordIndex == null) {

                        } else {
                            List<KeywordIndex> keywordIndices = collectorKeywordMap.get(collector.getName());
                            if (keywordIndices == null) {
                                keywordIndices = new ArrayList<>();
                            }
                            keywordIndices.add(keywordIndex);
                            collectorKeywordMap.put(collector.getName(), keywordIndices);
                        }
                    }
                }
            }
        }
        return collectorKeywordMap;
    }

    private Map<String, List<KvIndex>> buildCollectorKeyMap(Map<Integer, Collector> collectorMap) {
        Map<String, List<KvIndex>> collectorKvMap = new HashMap<>();
        if (CollectionUtils.isEmpty(collectorMap)) {

        } else {
            List<KvIndex> kvIndexList = kvIndexMapper.selectAll();
            if (CollectionUtils.isEmpty(kvIndexList)) {

            } else {
                Map<Integer, KvIndex> kvIndexMap = new HashMap<>(kvIndexList.size());
                for (KvIndex kvIndex : kvIndexList) {
                    kvIndexMap.put(kvIndex.getId(), kvIndex);
                }
                List<MidCollectorKv> midCollectorKvList = kvIndexMapper.selectAllMid();
                if (CollectionUtils.isEmpty(midCollectorKvList)) {

                } else {
                    for (MidCollectorKv mid : midCollectorKvList) {
                        Integer collectorId = mid.getCollector_id();
                        Integer kvId = mid.getKv_id();
                        Collector collector = collectorMap.get(collectorId);
                        KvIndex kvIndex = kvIndexMap.get(kvId);
                        if (collector == null || kvIndex == null) {

                        } else {
                            List<KvIndex> kvIndices = collectorKvMap.get(collector.getName());
                            if (kvIndices == null) {
                                kvIndices = new ArrayList<>();
                            }
                            kvIndices.add(kvIndex);
                            collectorKvMap.put(collector.getName(), kvIndices);
                        }
                    }
                }
            }
        }
        return collectorKvMap;
    }


}
