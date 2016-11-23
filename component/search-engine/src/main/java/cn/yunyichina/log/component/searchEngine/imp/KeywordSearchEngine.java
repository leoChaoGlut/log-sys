package cn.yunyichina.log.component.searchEngine.imp;

import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.searchEngine.AbstractSearchEngine;
import cn.yunyichina.log.component.searchEngine.SearchEngine;
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


    public KeywordSearchEngine(Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap, Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap, SearchCondition searchCondition) throws Exception {
        this.keywordIndexMap = keywordIndexMap;
        this.contextIndexMap = contextIndexMap;
        if (searchCondition.getBeginDateTime().after(searchCondition.getEndDateTime())) {
            throw new Exception("开始时间不能小于结束时间");
        }
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
//                    System.err.println(contextCount);
                    matchedContextInfoSet.add(contextInfo);
                } else {
                }
            }
        }
        return matchedContextInfoSet;
    }

}
