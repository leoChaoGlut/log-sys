package cn.yunyichina.log.component.index.builder.imp;

import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.common.entity.do_.KvTagDO;
import cn.yunyichina.log.component.index.base.AbstractBuilder;
import cn.yunyichina.log.component.index.builder.IndexBuilder;
import cn.yunyichina.log.component.index.entity.KvIndex;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/6 0:33
 * @Description: 键值对索引构造器
 */
public class KvIndexBuilder extends AbstractBuilder implements IndexBuilder<ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>>>, Serializable {

    private static final long serialVersionUID = 8379700811052368709L;
    /**
     * key: context count
     * value keyValuTagSetMap
     */
    private ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexMap = new ConcurrentHashMap<>(1024);
    private Set<KvTagDO> kvTagSet;

    public KvIndexBuilder(File logFile, Set<KvTagDO> kvTagSet) {
        this.kvTagSet = kvTagSet;
        this.logFile = logFile;
        try {
            if (this.logFile.getName().endsWith(LOG_SUFFIX)) {
                logContent = Files.asCharSource(logFile, Charsets.UTF_8).read();
            } else {
                logContent = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> build() {
        int rowEndTagLength = Tag.ROW_END.length();
        for (KvTagDO kvTag : kvTagSet) {
            String key = kvTag.getKey();
            String keyTag = kvTag.getKeyTag();
            int cursor = 0;
            int keyTagLength = keyTag.length();
            String valueEndTag = kvTag.getValueEndTag();
            while (0 <= (cursor = logContent.indexOf(keyTag, cursor))) {
                int valueBeginIndex = cursor + keyTagLength;
                int valueEndIndex = logContent.indexOf(valueEndTag, valueBeginIndex);
                if (0 <= valueBeginIndex && valueBeginIndex < valueEndIndex) {
                    String value = logContent.substring(valueBeginIndex, valueEndIndex);
                    if (StringUtils.isNotBlank(value)) {
                        int rowEndTagIndex = logContent.indexOf(Tag.ROW_END, valueEndIndex);
                        int contextCountBeginTagIndex = rowEndTagIndex + rowEndTagLength;
                        int contextCountEndTagIndex = logContent.indexOf(Tag.CONTEXT_COUNT_END, contextCountBeginTagIndex);
                        if (0 <= contextCountBeginTagIndex && contextCountBeginTagIndex < contextCountEndTagIndex) {
                            String count = logContent.substring(contextCountBeginTagIndex, contextCountEndTagIndex);
                            if (StringUtils.isNumeric(count)) {
                                ConcurrentHashMap<String, Set<KvIndex>> valueMap = kvIndexMap.get(key);
                                if (valueMap == null) {
                                    valueMap = new ConcurrentHashMap<>();
                                }
                                Set<KvIndex> kvIndexSet = valueMap.get(value);
                                if (kvIndexSet == null) {
                                    kvIndexSet = new HashSet<>();
                                }
                                kvIndexSet.add(new KvIndex(logFile, cursor, Long.valueOf(count)));
                                valueMap.put(value, kvIndexSet);
                                kvIndexMap.put(key, valueMap);
                            }
                        }
                    }
                }
                cursor = valueEndIndex;
            }
        }
        return kvIndexMap;
    }


}
