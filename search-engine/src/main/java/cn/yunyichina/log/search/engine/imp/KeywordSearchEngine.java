package cn.yunyichina.log.search.engine.imp;

import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.search.engine.AbstractSearchEngine;
import cn.yunyichina.log.search.engine.SearchEngine;
import cn.yunyichina.log.search.engine.entity.SearchCondition;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Author: Jonven
 * @ModifiyedBy: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 18:13
 * @Description: 关键词搜索引擎
 */
public class KeywordSearchEngine extends AbstractSearchEngine implements SearchEngine<List<ContextIndexBuilder.ContextInfo>> {


    private Map<String, List<KeywordIndexBuilder.IndexInfo>> keywordIndex;
    private Map<Long, ContextIndexBuilder.ContextInfo> contextIndex;

    public KeywordSearchEngine(Map<String, List<KeywordIndexBuilder.IndexInfo>> keywordIndex, Map<Long, ContextIndexBuilder.ContextInfo> contextIndex, SearchCondition searchCondition) {
        this.keywordIndex = keywordIndex;
        this.contextIndex = contextIndex;
        this.searchCondition = searchCondition;
    }

    //TODO 模糊搜索
    @Override
    public List<ContextIndexBuilder.ContextInfo> search() throws Exception {
        List<KeywordIndexBuilder.IndexInfo> indexInfoList = keywordIndex.get(searchCondition.getKeyword());
        if (CollectionUtils.isEmpty(indexInfoList)) {

        } else {
            matchedContextInfoList = new ArrayList<>(indexInfoList.size());
            for (KeywordIndexBuilder.IndexInfo indexInfo : indexInfoList) {
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
