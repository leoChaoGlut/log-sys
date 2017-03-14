package cn.yunyichina.log.component.index.builder.imp;

import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.component.index.base.AbstractBuilder;
import cn.yunyichina.log.component.index.builder.IndexBuilder;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/3 15:53
 * @Description: 关键词索引构造器
 */
public class KeywordIndexBuilder extends AbstractBuilder implements IndexBuilder<ConcurrentHashMap<String, Set<KeywordIndex>>>, Serializable {

    private static final long serialVersionUID = -1228607753528629475L;
    private final Logger logger = LoggerFactory.getLogger(KeywordIndexBuilder.class);
    /**
     * key: context count
     * value: keyword index set
     */
    private ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap = new ConcurrentHashMap<>(1024);
    private Set<String> keywordTagSet;

    public KeywordIndexBuilder(File logFile, Set<String> keywordTagSet) {
        this.logFile = logFile;
        this.keywordTagSet = keywordTagSet;
        try {
            if (this.logFile.getName().endsWith(LOG_SUFFIX)) {
                logContent = Files.asCharSource(logFile, Charsets.UTF_8).read();
            } else {
                logContent = "";
            }
        } catch (IOException e) {
            logger.error("关键词索引构造器初始化异常", e);
        }
    }

    /**
     * 假设现在要搜索patCardNo关键词,文件里一共有6个,但是返回的结果可能是3个.这是正确的结果.
     * 原因:当搜索到关键词的时候,就会标记这一条完整的logger.info,就算这条logger.info里
     * 还有相同的关键词,他们都已经被标记在这一条logger.info里了.
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<String, Set<KeywordIndex>> build() {
        try {
            int rowEndTagLength = Tag.ROW_END.length();
            for (String keywordTag : keywordTagSet) {
                int keywordTagLength = keywordTag.length();
                int cursor = 0;
                while (0 <= (cursor = logContent.indexOf(keywordTag, cursor))) {
                    int rowEndTagIndex = logContent.indexOf(Tag.ROW_END, cursor + keywordTagLength);
                    int contextCountBeginTagIndex = rowEndTagIndex + rowEndTagLength;
                    int contextCountEndTagIndex = logContent.indexOf(Tag.CONTEXT_COUNT_END, contextCountBeginTagIndex);
                    if (0 <= contextCountBeginTagIndex && contextCountBeginTagIndex < contextCountEndTagIndex) {
                        String count = logContent.substring(contextCountBeginTagIndex, contextCountEndTagIndex);
                        if (StringUtils.isNumeric(count)) {
                            KeywordIndex keywordIndex = new KeywordIndex(logFile, cursor, Long.valueOf(count));
                            Set<KeywordIndex> keywordIndexSet = keywordIndexMap.get(keywordTag);
                            if (keywordIndexSet == null) {
                                keywordIndexSet = new HashSet<>();
                            }
                            keywordIndexSet.add(keywordIndex);
                            keywordIndexMap.put(keywordTag, keywordIndexSet);
                        }
                    }
                    cursor = contextCountEndTagIndex;
                }
            }
            return keywordIndexMap;
        } catch (Exception e) {
            logger.error("关键词索引构造器构造期间异常", e);
            return new ConcurrentHashMap<>();
        }
    }

}
