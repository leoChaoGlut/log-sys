package cn.yunyichina.log.service.collector.entity.dto;

import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;

import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/29 17:04
 * @Description:
 */
public class TagSet {
    private Set<String> keywordSet;
    private Set<KeyValueIndexBuilder.KvTag> kvTagSet;

    public Set<String> getKeywordSet() {
        return keywordSet;
    }

    public TagSet setKeywordSet(Set<String> keywordSet) {
        this.keywordSet = keywordSet;
        return this;
    }

    public Set<KeyValueIndexBuilder.KvTag> getKvTagSet() {
        return kvTagSet;
    }

    public TagSet setKvTagSet(Set<KeyValueIndexBuilder.KvTag> kvTagSet) {
        this.kvTagSet = kvTagSet;
        return this;
    }
}
