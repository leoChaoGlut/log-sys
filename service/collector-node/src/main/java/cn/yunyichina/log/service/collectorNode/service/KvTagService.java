package cn.yunyichina.log.service.collectorNode.service;

import cn.yunyichina.log.common.entity.po.Kvtag;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.service.collectorNode.mapper.KvTagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Jonven on 2016/11/28.
 */
@Service
public class KvTagService {

    @Autowired
    KvTagMapper kvTagMapper;

    public Set<KeyValueIndexBuilder.KvTag> findAll() {
        List<Kvtag> kvtagList = kvTagMapper.selectAll();
        if (CollectionUtils.isEmpty(kvtagList)) {
            return null;
        }else{
            Set<KeyValueIndexBuilder.KvTag> kvTagSet = new HashSet<>(kvtagList.size());
            for (Kvtag kvtag : kvtagList) {
                kvTagSet.add(new KeyValueIndexBuilder.KvTag(kvtag.getKey(), kvtag.getKey_tag(), kvtag.getValue_end_tag()));
            }
            return kvTagSet;
        }
    }

}
