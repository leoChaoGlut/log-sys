package cn.yunyichina.log.service.collectorservice.service;

import cn.yunyichina.log.common.base.AbstractService;
import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.do_.CollectorDO;
import cn.yunyichina.log.common.entity.do_.KeywordTagDO;
import cn.yunyichina.log.common.entity.do_.KvTagDO;
import cn.yunyichina.log.service.collectorservice.exception.CollectorServiceException;
import cn.yunyichina.log.service.collectorservice.mapper.CollectedItemMapper;
import cn.yunyichina.log.service.collectorservice.mapper.CollectorMapper;
import cn.yunyichina.log.service.collectorservice.mapper.KeywordTagMapper;
import cn.yunyichina.log.service.collectorservice.mapper.KvTagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 12:17
 * @Description:
 */
@Service
public class CollectorService extends AbstractService {

    @Autowired
    CollectorMapper collectorMapper;

    @Autowired
    CollectedItemMapper collectedItemMapper;

    @Autowired
    KeywordTagMapper keywordTagMapper;

    @Autowired
    KvTagMapper kvTagMapper;

    public CollectorDO registerAndGetData(String applicationName, String ip, String port) {
        boolean exists = checkExistsBy(applicationName);
        CollectorDO collector;
        if (exists) {
            collector = selectCollectorBy(applicationName);
            updateCollectorIpAndPort(collector, ip, port);
            return collector;
        } else {
            collector = insertNewCollector(applicationName, ip, port);
            return collector;
        }
    }

    public List<CollectorDO> listAllCollector() {
        List<String> applicationNameList = collectorMapper.selectAllApplicationName();
        if (CollectionUtils.isEmpty(applicationNameList)) {
            throw new CollectorServiceException("log_collector 表为空");
        } else {
            List<CollectorDO> collectorList = new ArrayList<>(applicationNameList.size());
            for (String applicationName : applicationNameList) {
                CollectorDO collector = selectCollectorBy(applicationName);
                collectorList.add(collector);
            }
            return collectorList;
        }
    }

    @Transactional
    private CollectorDO insertNewCollector(String applicationName, String ip, String port) {
        CollectorDO collector = new CollectorDO()
                .setIp(ip)
                .setPort(port)
                .setApplicationName(applicationName);
        int count = collectorMapper.insertOne(collector);
        judgeAnOperation(count, "插入新的采集节点失败:" + collector.toString());
        return collector;
    }

    private boolean checkExistsBy(String applicationName) {
        CollectorDO collector = new CollectorDO()
                .setApplicationName(applicationName);
        int count = collectorMapper.count(collector);
        return count > 0 ? true : false;
    }


    private CollectorDO selectCollectorBy(String applicationName) {
        CollectorDO collectorResult = selectCollector(applicationName);
        if (null == collectorResult) {
            throw new CollectorServiceException(applicationName + " 对应的采集器信息不存在");
        } else {
            List<CollectedItemDO> collectedItemList = selectCollectedItemList(collectorResult);

            for (CollectedItemDO collectedItem : collectedItemList) {
                Integer collectedItemId = collectedItem.getId();

                List<KeywordTagDO> keywordTagList = keywordTagMapper.selectListByCollectedItemId(collectedItemId);
                collectedItem.setKeywordTagList(keywordTagList);

                List<KvTagDO> kvTagList = kvTagMapper.selectListByCollectedItemId(collectedItemId);
                collectedItem.setKvTagList(kvTagList);
            }
            collectorResult.setCollectedItemList(collectedItemList);
            return collectorResult;
        }
    }

    @Transactional
    private void updateCollectorIpAndPort(CollectorDO collector, String ip, String port) {
        collector.setIp(ip).setPort(port);
        int count = collectorMapper.update(collector);
        judgeAnOperation(count, "更新collector的ip和port时出错");
    }

    private List<CollectedItemDO> selectCollectedItemList(CollectorDO collectorResult) {
        CollectedItemDO collectedItemParam = new CollectedItemDO()
                .setCollectorId(collectorResult.getId());
        return collectedItemMapper.selectList(collectedItemParam);
    }

    private CollectorDO selectCollector(String applicationName) {
        CollectorDO collectorParam = new CollectorDO()
                .setApplicationName(applicationName);
        return collectorMapper.selectOne(collectorParam);
    }

    public Map<String, Object> getByApplicationName(String applicationName) {
        CollectedItemDO collectedItemParam = new CollectedItemDO()
                .setApplicationName(applicationName);
        CollectedItemDO collectedItemResult = collectedItemMapper.selectOne(collectedItemParam);
        if (null == collectedItemResult) {
            throw new CollectorServiceException("ApplicationName:" + applicationName + "所对应的采集项不存在");
        } else {
            CollectorDO collectorParam = new CollectorDO()
                    .setId(collectedItemResult.getCollectorId());
            CollectorDO collectorResult = collectorMapper.selectOne(collectorParam);
            if (collectorResult == null) {
                throw new CollectorServiceException("ApplicationName:" + applicationName + "所对应的采集节点不存在");
            } else {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("collector", collectorResult);
                resultMap.put("collectedItem", collectedItemResult);
                return resultMap;
            }
        }
    }
}
