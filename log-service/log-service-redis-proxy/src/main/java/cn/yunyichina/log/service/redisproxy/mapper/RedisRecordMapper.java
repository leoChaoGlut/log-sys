package cn.yunyichina.log.service.redisproxy.mapper;

import cn.yunyichina.log.common.base.BaseMapper;
import cn.yunyichina.log.common.entity.do_.RedisRecordDO;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/19 15:36
 * @Description:
 */
public interface RedisRecordMapper extends BaseMapper<RedisRecordDO> {

    RedisRecordDO selectLastestOne(@Param("collectorId") Integer collectorId);
}
