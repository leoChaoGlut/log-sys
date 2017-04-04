package cn.yunyichina.log.service.collector.cache;

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
 * @CreateTime: 2017/3/4 18:13
 * @Description:
 */
@Getter
@Setter
public class CollectedItemCache implements Serializable {
    private static final long serialVersionUID = -5359229848606435316L;

    private BaseInfo baseInfo;

    private ConcurrentHashMap<String, ContextInfo> contextInfoMap;
    private ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap;
    private ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexMap;

    @Getter
    @Setter
    public static class BaseInfo implements Serializable {

        private static final long serialVersionUID = 1553068597241585195L;

        private Integer collectedItemId;
        private String contextId;
        private String lastModifyTimeStr;

    }

}
