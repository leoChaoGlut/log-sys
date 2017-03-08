package cn.yunyichina.log.service.collectorservice.mapper;

import cn.yunyichina.log.common.base.BaseMapper;
import cn.yunyichina.log.common.entity.do_.CollectorDO;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 12:24
 * @Description:
 */
public interface CollectorMapper extends BaseMapper<CollectorDO> {

    List<String> selectAllApplicationName();

}
