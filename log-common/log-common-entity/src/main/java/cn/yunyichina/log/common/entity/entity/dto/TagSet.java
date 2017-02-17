package cn.yunyichina.log.common.entity.entity.dto;

import cn.yunyichina.log.common.entity.entity.do_.KvIndex;

import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/29 17:04
 * @Description:
 */
public class TagSet {
    private Set<String> keywordSet;
    private Set<KvIndex> kvTagSet;

    public Set<String> getKeywordSet() {
        return keywordSet;
    }

    public TagSet setKeywordSet(Set<String> keywordSet) {
        this.keywordSet = keywordSet;
        return this;
    }

    public Set<KvIndex> getKvTagSet() {
        return kvTagSet;
    }

    public TagSet setKvTagSet(Set<KvIndex> kvTagSet) {
        this.kvTagSet = kvTagSet;
        return this;
    }
}
