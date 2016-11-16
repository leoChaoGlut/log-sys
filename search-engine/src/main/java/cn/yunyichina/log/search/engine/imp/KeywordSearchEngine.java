package cn.yunyichina.log.search.engine.imp;

import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.search.engine.AbstractSearchEngine;
import cn.yunyichina.log.search.engine.SearchEngine;
import cn.yunyichina.log.search.engine.entity.SearchCondition;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * @Author: Jonven
 * @ModifiyedBy: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 18:13
 * @Description: 关键词搜索引擎, 不支持模糊搜索.
 */
public class KeywordSearchEngine extends AbstractSearchEngine implements SearchEngine<Set<ContextIndexBuilder.ContextInfo>> {

    private Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap;
    private Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap;

    public KeywordSearchEngine(Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap, Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap, SearchCondition searchCondition) {
        this.keywordIndexMap = keywordIndexMap;
        this.contextIndexMap = contextIndexMap;
        this.searchCondition = searchCondition;
    }

    @Override
    public Set<ContextIndexBuilder.ContextInfo> search() throws Exception {
        Set<KeywordIndexBuilder.IndexInfo> indexInfoSet = keywordIndexMap.get(searchCondition.getKeyword());
        if (CollectionUtils.isEmpty(indexInfoSet)) {

        } else {
            matchedContextInfoSet = new HashSet<>(indexInfoSet.size());
            for (KeywordIndexBuilder.IndexInfo indexInfo : indexInfoSet) {
                Long contextCount = indexInfo.getContextCount();
                ContextIndexBuilder.ContextInfo contextInfo = contextIndexMap.get(contextCount);
                if (inDateTimeRange(contextInfo)) {
                    matchedContextInfoSet.add(contextInfo);
                } else {

                }
            }
        }
        return matchedContextInfoSet;
    }


}
