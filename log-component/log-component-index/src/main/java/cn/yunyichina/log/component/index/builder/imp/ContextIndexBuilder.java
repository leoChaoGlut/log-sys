package cn.yunyichina.log.component.index.builder.imp;

import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.component.index.base.AbstractBuilder;
import cn.yunyichina.log.component.index.builder.IndexBuilder;
import cn.yunyichina.log.component.index.entity.ContextIndex;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;

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
public class ContextIndexBuilder extends AbstractBuilder implements IndexBuilder<ConcurrentHashMap<Long, ContextInfo>>, Serializable {

    private static final long serialVersionUID = -6007560470667273849L;
    /**
     * key: context count
     * value: @See ContextInfo
     */
    private ConcurrentHashMap<Long, ContextInfo> contextInfoMap = new ConcurrentHashMap<>(1024);

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
            e.printStackTrace();
        }
    }

    @Override
    public ConcurrentHashMap<Long, ContextInfo> build() {
        markTag(Tag.CONTEXT_BEGIN, true);
        markTag(Tag.CONTEXT_END, false);
        return contextInfoMap;
    }

    private void markTag(String tag, boolean isBeginTag) {
        int cursor = 0;
        int contextCountBeginTagIndex;
        int contextCountEndTagIndex;
        int tagLength = tag.length();
        while (0 <= (cursor = logContent.indexOf(tag, cursor))) {
            contextCountBeginTagIndex = cursor + tagLength;
            contextCountEndTagIndex = logContent.indexOf(Tag.CONTEXT_COUNT_END, contextCountBeginTagIndex);
            if (0 <= contextCountBeginTagIndex && contextCountBeginTagIndex < contextCountEndTagIndex) {//防止标记异常
                String countStr = logContent.substring(contextCountBeginTagIndex, contextCountEndTagIndex);
                if (StringUtils.isNumeric(countStr)) {//防止没有启用contextBegin，导致count="null"的情况
                    Long count = Long.valueOf(countStr);
                    ContextInfo contextInfo = contextInfoMap.get(count);
                    if (null == contextInfo) {
                        contextInfo = new ContextInfo();
                    }
                    ContextIndex contextIndex = new ContextIndex(logFile, cursor);
                    if (isBeginTag) {
                        contextInfo.setBegin(contextIndex);
                    } else {
                        contextInfo.setEnd(contextIndex);
                    }
                    contextInfoMap.put(count, contextInfo);//理论上 key( AtomicLong ) 不会有重复
                }
            }
            cursor = contextCountBeginTagIndex;
        }
    }

}
