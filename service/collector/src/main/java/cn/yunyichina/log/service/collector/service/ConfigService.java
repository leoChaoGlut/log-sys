package cn.yunyichina.log.service.collector.service;

import cn.yunyichina.log.component.entity.dto.TagSet;
import cn.yunyichina.log.service.collector.constants.Key;
import cn.yunyichina.log.service.collector.util.PropertiesUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/29 16:52
 * @Description:
 */
@Service
public class ConfigService {

    @Autowired
    PropertiesUtil propUtil;

    public void resetTag(TagSet tagSet) {
        propUtil.put(Key.KEYWORD_SET, JSON.toJSONString(tagSet.getKeywordSet()));
        propUtil.put(Key.KV_TAG_SET, JSON.toJSONString(tagSet.getKvTagSet()));
    }

}
