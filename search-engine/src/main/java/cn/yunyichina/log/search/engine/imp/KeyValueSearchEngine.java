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

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 11:30
 * @Description:
 */
public class KeyValueSearchEngine extends AbstractSearchEngine implements SearchEngine<List<ContextIndexBuilder.ContextInfo>> {

    private Map<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>> keyValueIndex;
    private Map<Long, ContextIndexBuilder.ContextInfo> contextIndex;

    public KeyValueSearchEngine(Map<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>> keyValueIndex, Map<Long, ContextIndexBuilder.ContextInfo> contextIndex, SearchCondition searchCondition) {
        this.keyValueIndex = keyValueIndex;
        this.contextIndex = contextIndex;
        this.searchCondition = searchCondition;
    }

    //TODO 模糊搜索
    @Override
    public List<ContextIndexBuilder.ContextInfo> search() throws Exception {
        Map<String, List<KeyValueIndexBuilder.IndexInfo>> valueIndex = keyValueIndex.get(searchCondition.getKey());
        if (CollectionUtils.isEmpty(valueIndex)) {

        } else {
            List<KeyValueIndexBuilder.IndexInfo> indexInfoList = valueIndex.get(searchCondition.getValue());
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
        }
        return matchedContextInfoList;
    }
}
