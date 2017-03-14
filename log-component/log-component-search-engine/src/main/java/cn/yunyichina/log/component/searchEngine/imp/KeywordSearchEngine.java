package cn.yunyichina.log.component.searchengine.imp;

import cn.yunyichina.log.common.entity.dto.SearchConditionDTO;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.searchengine.AbstractSearchEngine;
import cn.yunyichina.log.component.searchengine.SearchEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author: Jonven
 * @ModifiyedBy: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 18:13
 * @Description: 关键词搜索引擎, 不支持模糊搜索.
 */
public class KeywordSearchEngine extends AbstractSearchEngine implements SearchEngine<Set<ContextInfo>> {

    private static final Logger logger = LoggerFactory.getLogger(KeywordSearchEngine.class);

    private ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap;
    private ConcurrentHashMap<Long, ContextInfo> contextInfoMap;

    public KeywordSearchEngine(ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap, ConcurrentHashMap<Long, ContextInfo> contextInfoMap, SearchConditionDTO searchCondition) throws Exception {
        this.keywordIndexMap = keywordIndexMap;
        this.contextInfoMap = contextInfoMap;
        if (searchCondition.getBeginDateTime().after(searchCondition.getEndDateTime())) {
            throw new Exception("开始时间不能小于结束时间");
        }
        this.searchCondition = searchCondition;
    }

    @Override
    public Set<ContextInfo> search() throws Exception {
        Set<KeywordIndex> keywordIndexSet = keywordIndexMap.get(searchCondition.getKeyword());
        if (null == keywordIndexSet || keywordIndexSet.isEmpty()) {
            return new HashSet<>();
        } else {
            matchedContextInfoSet = new HashSet<>(keywordIndexSet.size());
            for (KeywordIndex keywordIndex : keywordIndexSet) {
                Long contextCount = keywordIndex.getContextCount();
                ContextInfo contextInfo = contextInfoMap.get(contextCount);
                if (inDateTimeRange(contextInfo)) {
                    matchedContextInfoSet.add(contextInfo);
                }
            }
            return matchedContextInfoSet;
        }
    }

}
