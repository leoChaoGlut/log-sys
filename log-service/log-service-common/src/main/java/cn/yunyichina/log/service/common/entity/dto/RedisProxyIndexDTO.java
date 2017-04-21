package cn.yunyichina.log.service.common.entity.dto;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.index.entity.KvIndex;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/13 15:54
 * @Description:
 */
@Getter
@Setter
public class RedisProxyIndexDTO implements Serializable {
    private static final long serialVersionUID = 4497424976163637153L;

    private CollectedItemDO collectedItem;
    private ConcurrentHashMap<String, ContextInfo> contextInfoMap;
    private ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap;
    private ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexMap;

}
