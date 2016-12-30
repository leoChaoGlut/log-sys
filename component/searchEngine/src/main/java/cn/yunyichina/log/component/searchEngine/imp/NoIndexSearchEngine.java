package cn.yunyichina.log.component.searchEngine.imp;

import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.searchEngine.AbstractSearchEngine;
import cn.yunyichina.log.component.searchEngine.SearchEngine;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 15:43
 * @Description:
 */
public class NoIndexSearchEngine extends AbstractSearchEngine implements SearchEngine<Set<ContextIndexBuilder.ContextInfo>> {

    private String keyword;
    private Collection<File> logFiles;
    private Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap;

    public NoIndexSearchEngine(Collection<File> logFiles, Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap, String keyword) {
        this.keyword = keyword;
        this.contextIndexMap = contextIndexMap;
        this.logFiles = logFiles;
    }

    @Override
    public Set<ContextIndexBuilder.ContextInfo> search() throws Exception {
        Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap = new HashMap<>(1024);
        for (File logFile : logFiles) {
            if (logFile.exists()) {
                String logContent = Files.asCharSource(logFile, Charsets.UTF_8).read();

                int keywordTagIndex = 0;
                while (0 <= (keywordTagIndex = logContent.indexOf(keyword, keywordTagIndex))) {
                    int rowEndTagIndex = logContent.indexOf(Tag.ROW_END, keywordTagIndex + keyword.length());
                    int contextCountBeginTagIndex = rowEndTagIndex + Tag.ROW_END.length();
                    int contextCountEndTagIndex = logContent.indexOf(Tag.CONTEXT_COUNT_END, contextCountBeginTagIndex);
                    String count = logContent.substring(contextCountBeginTagIndex, contextCountEndTagIndex);
                    KeywordIndexBuilder.IndexInfo indexInfo = new KeywordIndexBuilder.IndexInfo(logFile, keywordTagIndex, Long.valueOf(count));
                    Set<KeywordIndexBuilder.IndexInfo> indexInfoSet = keywordIndexMap.get(keyword);
                    if (indexInfoSet == null) {
                        indexInfoSet = new HashSet<>();
                    }
                    indexInfoSet.add(indexInfo);
                    keywordIndexMap.put(keyword, indexInfoSet);
                    keywordTagIndex = contextCountEndTagIndex;
                }
            }
        }
        if (CollectionUtils.isEmpty(keywordIndexMap)) {

        } else {
            Set<KeywordIndexBuilder.IndexInfo> indexInfoSet = keywordIndexMap.get(keyword);
            if (CollectionUtils.isEmpty(indexInfoSet)) {

            } else {
                matchedContextInfoSet = new HashSet<>(indexInfoSet.size());
                for (KeywordIndexBuilder.IndexInfo indexInfo : indexInfoSet) {
                    Long contextCount = indexInfo.getContextCount();
                    ContextIndexBuilder.ContextInfo contextInfo = contextIndexMap.get(contextCount);
                    matchedContextInfoSet.add(contextInfo);
                }
            }
        }

        return matchedContextInfoSet;
    }

}
