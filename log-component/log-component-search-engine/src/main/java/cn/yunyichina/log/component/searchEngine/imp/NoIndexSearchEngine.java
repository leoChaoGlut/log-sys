package cn.yunyichina.log.component.searchengine.imp;

import cn.yunyichina.log.common.constant.GlobalConst;
import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.searchengine.AbstractSearchEngine;
import cn.yunyichina.log.component.searchengine.SearchEngine;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 15:43
 * @Description:
 */
public class NoIndexSearchEngine extends AbstractSearchEngine implements SearchEngine<Set<ContextInfo>> {

    private String keyword;
    private Collection<File> logs;
    private ConcurrentHashMap<String, ContextInfo> contextInfoMap;

    public NoIndexSearchEngine(Collection<File> logs, ConcurrentHashMap<String, ContextInfo> contextInfoMap, String keyword) {
        this.keyword = keyword;
        this.contextInfoMap = contextInfoMap;
        this.logs = logs;
    }

    @Override
    public Set<ContextInfo> search() throws Exception {
        int keywordLength = keyword.length();
        int rowEndTagLength = Tag.ROW_END.length();
        List<String> contextIdList = new ArrayList<>(1024);
        for (File log : logs) {
            if (log.exists()) {
                String logContent = Files.asCharSource(log, Charsets.UTF_8).read();
                int cursor = 0;
                while (0 <= (cursor = logContent.indexOf(keyword, cursor))) {
                    int rowEndTagIndex = logContent.indexOf(Tag.ROW_END, cursor + keywordLength);
                    int contextIdBeginTagIndex = rowEndTagIndex + rowEndTagLength;
                    int contextIdEndTagIndex = logContent.indexOf(Tag.CONTEXT_ID_END, contextIdBeginTagIndex);
                    if (0 <= contextIdBeginTagIndex && contextIdBeginTagIndex < contextIdEndTagIndex) {
                        String contextId = logContent.substring(contextIdBeginTagIndex, contextIdEndTagIndex);
                        if (contextId.length() >= GlobalConst.UUID_LENGTH) {
                            contextIdList.add(contextId);
                        }
                    }
                    cursor = contextIdEndTagIndex;
                }
            }
        }
        if (contextIdList.isEmpty()) {
            return new HashSet<>();
        } else {
            matchedContextInfoSet = new HashSet<>(contextIdList.size());
            for (String contextId : contextIdList) {
                if (contextId != null) {
                    ContextInfo contextInfo = contextInfoMap.get(contextId);
                    if (contextInfo != null) {
                        matchedContextInfoSet.add(contextInfo);
                    }
                }
            }
            return matchedContextInfoSet;
        }
    }

}
