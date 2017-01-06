package cn.yunyichina.log.service.frontEnd.mapper;

import cn.yunyichina.log.common.base.BaseMapper;
import cn.yunyichina.log.component.entity.do_.Collector;
import cn.yunyichina.log.component.entity.do_.Group;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/4 12:10
 * @Description:
 */
@Repository
public interface CollectorMapper extends BaseMapper<Collector> {

    List<Group> findAllGroupAndCollector();

    List<Collector> findCollectorAndItsKeywordIndex();

    List<Collector> findCollectorAndItsKeyValueIndex();

}
