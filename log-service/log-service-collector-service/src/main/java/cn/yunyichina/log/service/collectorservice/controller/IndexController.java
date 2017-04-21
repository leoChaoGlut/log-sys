package cn.yunyichina.log.service.collectorservice.controller;

import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.index.entity.KvIndex;
import cn.yunyichina.log.service.collectorservice.service.IndexService;
import cn.yunyichina.log.service.common.entity.dto.RedisProxyIndexDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/20 9:31
 * @Description:
 */
@RestController
@RequestMapping("index")
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    IndexService indexService;

    @PostMapping("cache")
    ResponseBodyDTO cacheIndex(
            @RequestBody RedisProxyIndexDTO redisProxyIndexDTO
    ) throws Exception {
        Integer collectedItemId = redisProxyIndexDTO.getCollectedItem().getId();

        ConcurrentHashMap<String, ContextInfo> contextInfoMap = redisProxyIndexDTO.getContextInfoMap();
        ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap = redisProxyIndexDTO.getKeywordIndexMap();
        ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexMap = redisProxyIndexDTO.getKvIndexMap();

        int contextInfoMapSize = contextInfoMap == null ? 0 : contextInfoMap.size();
        int keywordIndexMapSize = keywordIndexMap == null ? 0 : keywordIndexMap.size();
        int kvIndexMapSize = kvIndexMap == null ? 0 : kvIndexMap.size();

        logger.info("CollectedItemId:" + collectedItemId +
                ",contextInfoMapSize:" + contextInfoMapSize +
                ",keywordIndexMapSize:" + keywordIndexMapSize +
                ",kvIndexMapSize:" + kvIndexMapSize
        );
        indexService.cacheIndex(redisProxyIndexDTO);
        logger.info("异步返回");
        return ResponseBodyDTO.ok();
    }

}
