package cn.yunyichina.log.component.index.builder.imp;

import cn.yunyichina.log.common.constant.GlobalConst;
import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.component.index.base.AbstractBuilder;
import cn.yunyichina.log.component.index.builder.IndexBuilder;
import cn.yunyichina.log.component.index.entity.ContextIndex;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/3 14:23
 * @Description: 上下文索引构造器
 */
public class ContextIndexBuilder extends AbstractBuilder implements IndexBuilder<ConcurrentHashMap<String, ContextInfo>>, Serializable {
    private static final long serialVersionUID = -6007560470667273849L;
    private final Logger logger = LoggerFactory.getLogger(ContextIndexBuilder.class);
    /**
     * key: context id (eg:UUID) 当数据量大的时候,可以考虑用"UUID+suffix"的形式,提高查询效率
     * value:{@link ContextInfo}
     */
    private ConcurrentHashMap<String, ContextInfo> contextInfoMap = new ConcurrentHashMap<>(1024);

    public ContextIndexBuilder(File logFile) {
        this.logFile = logFile;
        try {
            String logFileName = this.logFile.getName();
            if (logFileName.endsWith(LOG_SUFFIX)) {
                logContent = Files.asCharSource(logFile, Charsets.UTF_8).read();
            } else {
                logContent = "";
            }
        } catch (IOException e) {
            logger.error("上下文索引构造器初始化异常", e);
        }
    }

    @Override
    public ConcurrentHashMap<String, ContextInfo> build() {
        try {
            markTag(Tag.CONTEXT_BEGIN, true);
            markTag(Tag.CONTEXT_END, false);
            return contextInfoMap;
        } catch (Exception e) {
            logger.error("上下文索引构造器构造期间异常", e);
            return new ConcurrentHashMap<>();
        }
    }

    private void markTag(String tag, boolean isBeginTag) {
        int cursor = 0;
        int contextIdBeginTagIndex;
        int contextIdEndTagIndex;
        int tagLength = tag.length();
        while (0 <= (cursor = logContent.indexOf(tag, cursor))) {
            contextIdBeginTagIndex = cursor + tagLength;
            contextIdEndTagIndex = logContent.indexOf(Tag.CONTEXT_ID_END, contextIdBeginTagIndex);
            if (0 <= contextIdBeginTagIndex && contextIdBeginTagIndex < contextIdEndTagIndex) {//防止标记异常
                String contextId = logContent.substring(contextIdBeginTagIndex, contextIdEndTagIndex);
                if (contextId.length() >= GlobalConst.UUID_LENGTH) {
                    ContextInfo contextInfo = contextInfoMap.get(contextId);
                    if (null == contextInfo) {
                        contextInfo = new ContextInfo().setContextId(contextId);
                    }
                    ContextIndex contextIndex = new ContextIndex(logFile, cursor);
                    if (isBeginTag) {
                        contextInfo.setBegin(contextIndex);
                    } else {
                        contextInfo.setEnd(contextIndex);
                    }
                    contextInfoMap.put(contextId, contextInfo);//理论上 key( UUID ) 不会有重复
                }
            }
            cursor = contextIdBeginTagIndex;
        }
    }

}
