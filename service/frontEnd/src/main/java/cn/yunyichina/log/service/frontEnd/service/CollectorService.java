package cn.yunyichina.log.service.frontEnd.service;

import cn.yunyichina.log.common.entity.po.Collector;
import cn.yunyichina.log.service.frontEnd.mapper.CollectorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 9:42
 * @Description: 未开启事务, 只读
 */
@Service
public class CollectorService {

    @Autowired
    CollectorMapper collectorMapper;

    public List<Collector> getAllCollectors() {
        List<Collector> collectorList = collectorMapper.selectAll();
        return collectorList;
    }

}
