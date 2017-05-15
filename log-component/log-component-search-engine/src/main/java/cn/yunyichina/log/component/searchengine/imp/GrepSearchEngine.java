package cn.yunyichina.log.component.searchengine.imp;

import cn.yunyichina.log.common.constant.GlobalConst;
import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.searchengine.AbstractSearchEngine;
import cn.yunyichina.log.component.searchengine.SearchEngine;
import cn.yunyichina.log.component.searchengine.exception.SearchEngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/15 11:12
 * @Description: use 'grep' to search log files
 */
public class GrepSearchEngine extends AbstractSearchEngine implements SearchEngine<Set<ContextInfo>> {
    final Logger logger = LoggerFactory.getLogger(GrepSearchEngine.class);

    //常量与shell是关联的
    public static final String RESULT_SEPARATOR = ",";
    public static final String CONTAINS_KEYWORD = "1";

    private String keyword;
    private String shellPath;
    private File[] logs;
    private ConcurrentHashMap<String, ContextInfo> contextInfoMap;

    public GrepSearchEngine(String keyword, String shellPath, File[] logs, ConcurrentHashMap<String, ContextInfo> contextInfoMap) {
        this.keyword = keyword;
        this.shellPath = shellPath;
        this.logs = logs;
        this.contextInfoMap = contextInfoMap;
    }

    @Override
    public Set<ContextInfo> search() throws Exception {
        logger.info("No Index 搜索开始");
        long begin = System.nanoTime();
        try {
            String logArrStr = buildLogArrStr();
            String[] results = grepLogs(logArrStr);
            List<String> contextIdList = new ArrayList<>();
            for (int i = 1; i < results.length; i++) {//第0个是空,所以从第1个开始.详情请看shell
                if (CONTAINS_KEYWORD.equals(results[i])) {
                    findAndStoreConextId(logs[i - 1], contextIdList);
                }
            }
            return findMatchedContextId(contextIdList);
        } finally {
            logger.info("No Index 搜索结束,耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9));
        }
    }

    private Set<ContextInfo> findMatchedContextId(List<String> contextIdList) {
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

    private String[] grepLogs(String logArrStr) throws IOException {
        Process p = null;
        BufferedReader br = null;
        long begin = System.nanoTime();
        try {
            File shell = new File(shellPath);
            if (shell.exists()) {
                String cmd = shellPath + " '" + keyword + "' " + logArrStr;
                ProcessBuilder pb = new ProcessBuilder("sh", "-c", cmd);
                p = pb.start();
                br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                StringBuilder resultBuilder = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    resultBuilder.append(line);
                }
                return resultBuilder.toString().split(RESULT_SEPARATOR);
            } else {
                throw new SearchEngineException("Grep脚本不存在与jar包同目录下");
            }
        } finally {
            logger.info("grep 耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9) + "s");
            p.destroy();
            if (br != null) {
                br.close();
            }
        }
    }

    private void findAndStoreConextId(File log, List<String> contextIdList) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(log), StandardCharsets.UTF_8));
            String line;
            StringBuilder logContentBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                logContentBuilder.append(line);
            }
            int keywordLength = keyword.length();
            int rowEndTagLength = Tag.ROW_END.length();
            int cursor = 0;
            while (0 <= (cursor = logContentBuilder.indexOf(keyword, cursor))) {
                int rowEndTagIndex = logContentBuilder.indexOf(Tag.ROW_END, cursor + keywordLength);
                int contextIdBeginTagIndex = rowEndTagIndex + rowEndTagLength;
                int contextIdEndTagIndex = logContentBuilder.indexOf(Tag.CONTEXT_ID_END, contextIdBeginTagIndex);
                if (0 <= contextIdBeginTagIndex && contextIdBeginTagIndex < contextIdEndTagIndex) {
                    String contextId = logContentBuilder.substring(contextIdBeginTagIndex, contextIdEndTagIndex);
                    if (contextId.length() >= GlobalConst.UUID_LENGTH) {
                        contextIdList.add(contextId);
                    }
                }
                cursor = contextIdEndTagIndex;
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    private String buildLogArrStr() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("'");
        for (File log : logs) {
            strBuilder.append(log.getAbsolutePath() + " ");
        }
        strBuilder.append("'");
        return strBuilder.toString();
    }
}
