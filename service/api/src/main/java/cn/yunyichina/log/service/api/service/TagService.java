package cn.yunyichina.log.service.api.service;

import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.component.entity.dto.TagSet;
import cn.yunyichina.log.component.entity.po.KeyValueTag;
import cn.yunyichina.log.component.entity.po.KeywordTag;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.service.api.constants.CacheName;
import cn.yunyichina.log.service.api.mapper.KeyValueTagMapper;
import cn.yunyichina.log.service.api.mapper.KeywordTagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/6 17:43
 * @Description:
 */
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class TagService {

    final LoggerWrapper logger = LoggerWrapper.getLogger(TagService.class);

    @Autowired
    KeyValueTagMapper keyValueTagMapper;

    @Autowired
    KeywordTagMapper keywordTagMapper;

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheName.COLLECTOR, key = "#collectorId")
    public TagSet getTagSet(Integer collectorId) {
        logger.info("缓存穿透:" + collectorId);
        return buildTagSet(collectorId);
    }


    @CachePut(cacheNames = CacheName.COLLECTOR, key = "#collectorId")
    public TagSet updateTagSetCache(Integer collectorId) {
        return buildTagSet(collectorId);
    }

    @CacheEvict(cacheNames = CacheName.COLLECTOR, allEntries = true)
    public void cleanAll() {

    }

    private TagSet buildTagSet(Integer collectorId) {
        Set<String> keywordSet = getKeywordSet(collectorId);
        Set<KeyValueIndexBuilder.KvTag> kvTagSet = getkvTagSet(collectorId);

        TagSet tagSet = new TagSet()
                .setKeywordSet(keywordSet)
                .setKvTagSet(kvTagSet);

        return tagSet;
    }

    private Set<String> getKeywordSet(Integer collectorId) {
        KeywordTag keywordTag = new KeywordTag().setCollector_id(collectorId);
        List<KeywordTag> keywordTagList = keywordTagMapper.selectList(keywordTag);
        if (CollectionUtils.isEmpty(keywordTagList)) {
            return new HashSet<>();
        } else {
            Set<String> keywordSet = new HashSet<>(keywordTagList.size());
            for (KeywordTag tag : keywordTagList) {
                keywordSet.add(tag.getKeyword());
            }
            return keywordSet;
        }
    }

    private Set<KeyValueIndexBuilder.KvTag> getkvTagSet(Integer collectorId) {
        KeyValueTag keyValueTag = new KeyValueTag().setCollector_id(collectorId);
        List<KeyValueTag> keyValueTagList = keyValueTagMapper.selectList(keyValueTag);
        if (CollectionUtils.isEmpty(keyValueTagList)) {
            return new HashSet<>();
        } else {
            Set<KeyValueIndexBuilder.KvTag> kvTagSet = new HashSet<>(keyValueTagList.size());
            for (KeyValueTag tag : keyValueTagList) {
                KeyValueIndexBuilder.KvTag kvTag = new KeyValueIndexBuilder.KvTag(tag.getKey(), tag.getKey_tag(), tag.getValue_end_tag());
                kvTagSet.add(kvTag);
            }
            return kvTagSet;
        }
    }

}
