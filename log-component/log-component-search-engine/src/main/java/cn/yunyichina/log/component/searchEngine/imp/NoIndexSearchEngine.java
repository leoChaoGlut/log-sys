package cn.yunyichina.log.component.searchengine.imp;

import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.searchengine.AbstractSearchEngine;
import cn.yunyichina.log.component.searchengine.SearchEngine;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;

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
    private ConcurrentHashMap<Long, ContextInfo> contextInfoMap;

    public NoIndexSearchEngine(Collection<File> logs, ConcurrentHashMap<Long, ContextInfo> contextInfoMap, String keyword) {
        this.keyword = keyword;
        this.contextInfoMap = contextInfoMap;
        this.logs = logs;
    }

    @Override
    public Set<ContextInfo> search() throws Exception {
        int keywordLength = keyword.length();
        int rowEndTagLength = Tag.ROW_END.length();
        List<Long> contextCountList = new ArrayList<>(1024);
        for (File log : logs) {
            if (log.exists()) {
                String logContent = Files.asCharSource(log, Charsets.UTF_8).read();
                int cursor = 0;
                while (0 <= (cursor = logContent.indexOf(keyword, cursor))) {
                    int rowEndTagIndex = logContent.indexOf(Tag.ROW_END, cursor + keywordLength);
                    int contextCountBeginTagIndex = rowEndTagIndex + rowEndTagLength;
                    int contextCountEndTagIndex = logContent.indexOf(Tag.CONTEXT_COUNT_END, contextCountBeginTagIndex);
                    if (0 <= contextCountBeginTagIndex && contextCountBeginTagIndex < contextCountEndTagIndex) {
                        String count = logContent.substring(contextCountBeginTagIndex, contextCountEndTagIndex);
                        if (StringUtils.isNumeric(count)) {
                            contextCountList.add(Long.valueOf(count));
                        }
                    }
                    cursor = contextCountEndTagIndex;
                }
            }
        }
        if (contextCountList.isEmpty()) {
            return new HashSet<>();
        } else {
            matchedContextInfoSet = new HashSet<>(contextCountList.size());
            for (Long contextCount : contextCountList) {
                ContextInfo contextInfo = contextInfoMap.get(contextCount);
                if (contextInfo != null) {
                    matchedContextInfoSet.add(contextInfo);
                }
            }
            return matchedContextInfoSet;
        }
    }

}
