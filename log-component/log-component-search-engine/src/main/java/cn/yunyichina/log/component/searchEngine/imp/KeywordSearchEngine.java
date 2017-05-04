package cn.yunyichina.log.component.searchengine.imp;

import cn.yunyichina.log.common.entity.dto.SearchConditionDTO;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.searchengine.AbstractSearchEngine;
import cn.yunyichina.log.component.searchengine.SearchEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
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
    private ConcurrentHashMap<String, ContextInfo> contextInfoMap;

    public KeywordSearchEngine(ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap, ConcurrentHashMap<String, ContextInfo> contextInfoMap, SearchConditionDTO searchCondition) throws Exception {
        this.keywordIndexMap = keywordIndexMap;
        this.contextInfoMap = contextInfoMap;
        if (searchCondition.getBeginDateTime().after(searchCondition.getEndDateTime())) {
            throw new Exception("开始时间不能小于结束时间");
        }
        this.searchCondition = searchCondition;
    }

    @Override
    public Set<ContextInfo> search() throws Exception {
        logger.info("keyword 搜索开始");
        long begin = System.nanoTime();
        try {
            Set<KeywordIndex> keywordIndexSet = keywordIndexMap.get(searchCondition.getKeyword());
            if (null == keywordIndexSet || keywordIndexSet.isEmpty()) {
                return new HashSet<>();
            } else {
                matchedContextInfoSet = new HashSet<>(keywordIndexSet.size());
                for (KeywordIndex keywordIndex : keywordIndexSet) {
                    String contextId = keywordIndex.getContextId();
                    if (contextId != null) {
                        ContextInfo contextInfo = contextInfoMap.get(contextId);
                        if (inDateTimeRange(contextInfo)) {
                            matchedContextInfoSet.add(contextInfo);
                        }
                    }
                }
                return matchedContextInfoSet;
            }
        } finally {
            logger.info("keyword 搜索结束,耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9));
        }
    }

}
