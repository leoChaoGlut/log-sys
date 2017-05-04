package cn.yunyichina.log.component.common.constant;

import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.index.entity.KvIndex;
import com.alibaba.fastjson.TypeReference;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/25 9:10
 * @Description:
 */
public interface IndexTypeReference {

    TypeReference<ConcurrentHashMap<String, ContextInfo>> CONTEXT = new TypeReference<ConcurrentHashMap<String, ContextInfo>>() {
    };

    TypeReference<ConcurrentHashMap<String, Set<KeywordIndex>>> KEYWORD = new TypeReference<ConcurrentHashMap<String, Set<KeywordIndex>>>() {
    };

    TypeReference<ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>>> KEY_VALUE = new TypeReference<ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>>>() {
    };
}
