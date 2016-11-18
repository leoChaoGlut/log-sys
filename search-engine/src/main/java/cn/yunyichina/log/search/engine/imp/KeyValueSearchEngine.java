package cn.yunyichina.log.search.engine.imp;

import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.search.engine.AbstractSearchEngine;
import cn.yunyichina.log.search.engine.SearchEngine;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 11:30
 * @Description: kv搜索引擎, 支持精确key, 模糊value搜索
 * 只允许选择精确搜索或模糊搜索其一,不能同时选择.
 */
public class KeyValueSearchEngine extends AbstractSearchEngine implements SearchEngine<Set<ContextIndexBuilder.ContextInfo>> {

    private Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap;
    private Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap;

    public KeyValueSearchEngine() {
    }

    public KeyValueSearchEngine(Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap, Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap, SearchCondition searchCondition) throws Exception {
        this.keyValueIndexMap = keyValueIndexMap;
        this.contextIndexMap = contextIndexMap;
        if (searchCondition.getBeginDateTime().after(searchCondition.getEndDateTime())) {
            throw new Exception("开始时间不能小于结束时间");
        }
        this.searchCondition = searchCondition;
    }

    @Override
    public Set<ContextIndexBuilder.ContextInfo> search() throws Exception {
        Map<String, Set<KeyValueIndexBuilder.IndexInfo>> valueIndex = keyValueIndexMap.get(searchCondition.getKey());
        Set<KeyValueIndexBuilder.IndexInfo> indexInfoSet = null;
        if (fuzzySearch) {
            Set<String> valueSet = valueIndex.keySet();
            if (CollectionUtils.isEmpty(valueSet)) {

            } else {
                indexInfoSet = new HashSet<>(valueSet.size() << 1);//避免扩容,
                for (String value : valueSet) {
                    if (value.contains(searchCondition.getValue())) {
                        indexInfoSet.addAll(valueIndex.get(value));
                    }
                }
            }
        } else {
            if (CollectionUtils.isEmpty(valueIndex)) {

            } else {
                indexInfoSet = valueIndex.get(searchCondition.getValue());
            }
        }
        if (CollectionUtils.isEmpty(indexInfoSet)) {

        } else {
            matchedContextInfoSet = new HashSet<>(indexInfoSet.size());
            for (KeyValueIndexBuilder.IndexInfo indexInfo : indexInfoSet) {
                Long contextCount = indexInfo.getContextCount();
                ContextIndexBuilder.ContextInfo contextInfo = contextIndexMap.get(contextCount);
                if (inDateTimeRange(contextInfo)) {
//                    System.err.println(contextCount);
                    matchedContextInfoSet.add(contextInfo);
                } else {

                }
            }
        }
        return matchedContextInfoSet;
    }
}
