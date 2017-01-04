package cn.yunyichina.log.service.frontEnd.service;

import cn.yunyichina.log.component.entity.do_.Collector;
import cn.yunyichina.log.component.entity.do_.Group;
import cn.yunyichina.log.component.entity.do_.KeywordIndex;
import cn.yunyichina.log.component.entity.do_.KvIndex;
import cn.yunyichina.log.service.frontEnd.mapper.CollectorMapper;
import cn.yunyichina.log.service.frontEnd.mapper.GroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static cn.yunyichina.log.service.frontEnd.constants.CacheName.OPTION;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 10:34
 * @Description:
 */
@Service
public class FrontEndService {

    @Autowired
    GroupMapper groupMapper;

    @Autowired
    CollectorMapper collectorMapper;

    @Cacheable(cacheNames = {OPTION}, key = "#root.methodName")
    public Collection<Group> getSearchOption() {
        return getGroups();
    }


    @CachePut(cacheNames = {OPTION}, key = "#root.methodName")
    public Collection<Group> updateSearchOptionCache() {
        return getGroups();
    }

    private Collection<Group> getGroups() {
        Map<Integer, Collector> collectorMap = new HashMap<>(1024);
        buildCollectorKeywordSet(collectorMap);
        buildCollectorKeyValueSet(collectorMap);
        Collection<Collector> collectors = collectorMap.values();
        List<Group> groupList = groupMapper.selectAll();
        Collection<Group> groups = buildGroup(collectors, groupList);
        return groups;
    }

    private void buildCollectorKeywordSet(Map<Integer, Collector> collectorMap) {
        List<Collector> collectorAndKeywordList = collectorMapper.findCollectorAndItsKeywordIndex();
        if (CollectionUtils.isEmpty(collectorAndKeywordList)) {

        } else {
            for (Collector collector : collectorAndKeywordList) {
                Collector oldCollector = collectorMap.get(collector.getId());
                if (null == oldCollector) {
                    oldCollector = collector;
                    Set<KeywordIndex> keywordIndexSet = new HashSet<>();
                    keywordIndexSet.add(collector.getKeywordIndex());
                    oldCollector.setKeywordIndexSet(keywordIndexSet);
                } else {
                    Set<KeywordIndex> keywordIndexSet = oldCollector.getKeywordIndexSet();
                    keywordIndexSet.add(collector.getKeywordIndex());
                }
                collectorMap.put(collector.getId(), oldCollector);
            }
        }
    }

    private void buildCollectorKeyValueSet(Map<Integer, Collector> collectorMap) {
        List<Collector> collectorAndKeyValueList = collectorMapper.findCollectorAndItsKeyValueIndex();
        if (CollectionUtils.isEmpty(collectorAndKeyValueList)) {

        } else {
            for (Collector collector : collectorAndKeyValueList) {
                Collector oldCollector = collectorMap.get(collector.getId());
                if (null == oldCollector) {
                    oldCollector = collector;
                    Set<KvIndex> kvIndexSet = new HashSet<>();
                    kvIndexSet.add(collector.getKvIndex());
                    oldCollector.setKvIndexSet(kvIndexSet);
                } else {
                    Set<KvIndex> kvIndexSet = oldCollector.getKvIndexSet();
                    if (null == kvIndexSet) {
                        kvIndexSet = new HashSet<>();
                    }
                    kvIndexSet.add(collector.getKvIndex());
                }
                collectorMap.put(collector.getId(), oldCollector);
            }
        }
    }

    private Collection<Group> buildGroup(Collection<Collector> collectors, List<Group> groupList) {
        Map<Integer, Group> groupMap;
        if (CollectionUtils.isEmpty(groupList)) {
            groupMap = new HashMap<>();
        } else {
            groupMap = new HashMap<>(groupList.size());
            for (Group group : groupList) {
                groupMap.put(group.getId(), group);
            }
            for (Collector collector : collectors) {
                Integer groupId = collector.getGroup_id();
                Group group = groupMap.get(groupId);
                Set<Collector> collectorSet = group.getCollectorSet();
                if (null == collectorSet) {
                    collectorSet = new HashSet<>();
                }
                collectorSet.add(collector);
                group.setCollectorSet(collectorSet);
                groupMap.put(groupId, group);
            }
        }
        return groupMap.values();
    }


}
