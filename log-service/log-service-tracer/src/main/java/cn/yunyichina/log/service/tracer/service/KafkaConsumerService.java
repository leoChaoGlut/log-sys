package cn.yunyichina.log.service.tracer.service;

import cn.yunyichina.log.service.common.entity.do_.ReverseIndexDO;
import cn.yunyichina.log.service.common.entity.do_.TraceDO;
import cn.yunyichina.log.service.tracer.mapper.ReverseIndexMapper;
import cn.yunyichina.log.service.tracer.mapper.TraceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/27 15:27
 * @Description:
 */
@Service
public class KafkaConsumerService {

    @Autowired
    TraceMapper traceMapper;
    @Autowired
    ReverseIndexMapper reverseIndexMapper;

    @Transactional(rollbackFor = Exception.class)
    public void insertTraceAndReverseIndex(TraceDO traceDO, ReverseIndexDO reverseIndexDO) {
        traceMapper.insertOne(traceDO);
        if (1 == 1) {
            throw new RuntimeException("123");
        }
        reverseIndexMapper.insertOne(reverseIndexDO);
    }
}
