package cn.yunyichina.log.service.collectorNode.service;

import cn.yunyichina.log.common.entity.po.Keyword;
import cn.yunyichina.log.service.collectorNode.mapper.KeywordMapper;
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
public class KeywordService {

    @Autowired
    KeywordMapper keywordMapper;

    public Set<String> findAll(){
        List<Keyword> keywordList = keywordMapper.selectAll();
        if(CollectionUtils.isEmpty(keywordList)){
            return null;
        }else{
            Set<String> keywordSet = new HashSet<>(keywordList.size());
            for (Keyword keyword:keywordList){
                keywordSet.add(keyword.getKeyword());
            }
            return  keywordSet;
        }
    }

}
