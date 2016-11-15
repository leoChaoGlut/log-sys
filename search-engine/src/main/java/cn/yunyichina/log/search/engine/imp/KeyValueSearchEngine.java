package cn.yunyichina.log.search.engine.imp;

import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.search.engine.AbstractSearchEngine;
import cn.yunyichina.log.search.engine.SearchEngine;
import cn.yunyichina.log.search.engine.entity.SearchCondition;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 11:30
 * @Description: kv搜索引擎, 支持精确key, 模糊value搜索
 * 只允许选择精确搜索或模糊搜索其一,不能同时选择.
 */
public class KeyValueSearchEngine extends AbstractSearchEngine implements SearchEngine<List<ContextIndexBuilder.ContextInfo>> {

    private Map<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>> keyValueIndex;
    private Map<Long, ContextIndexBuilder.ContextInfo> contextIndex;

    public KeyValueSearchEngine(Map<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>> keyValueIndex, Map<Long, ContextIndexBuilder.ContextInfo> contextIndex, SearchCondition searchCondition) {
        this.keyValueIndex = keyValueIndex;
        this.contextIndex = contextIndex;
        this.searchCondition = searchCondition;
    }

    @Override
    public List<ContextIndexBuilder.ContextInfo> search() throws Exception {
        Map<String, List<KeyValueIndexBuilder.IndexInfo>> valueIndex = keyValueIndex.get(searchCondition.getKey());
        List<KeyValueIndexBuilder.IndexInfo> indexInfoList = null;
        if (fuzzySearch) {
            Set<String> valueSet = valueIndex.keySet();
            if (CollectionUtils.isEmpty(valueSet)) {

            } else {
                indexInfoList = new ArrayList<>(valueSet.size() << 1);//避免扩容,
                for (String value : valueSet) {
                    if (value.contains(searchCondition.getValue())) {
                        indexInfoList.addAll(valueIndex.get(value));
                    }
                }
            }
        } else {
            if (CollectionUtils.isEmpty(valueIndex)) {

            } else {
                indexInfoList = valueIndex.get(searchCondition.getValue());
            }
        }
        if (CollectionUtils.isEmpty(indexInfoList)) {

        } else {
            matchedContextInfoList = new ArrayList<>(indexInfoList.size());
            for (KeyValueIndexBuilder.IndexInfo indexInfo : indexInfoList) {
                Long contextCount = indexInfo.getContextCount();
                ContextIndexBuilder.ContextInfo contextInfo = contextIndex.get(contextCount);
                if (inDateTimeRange(contextInfo)) {
                    matchedContextInfoList.add(contextInfo);
                } else {

                }
            }
        }
        return matchedContextInfoList;
    }
}
