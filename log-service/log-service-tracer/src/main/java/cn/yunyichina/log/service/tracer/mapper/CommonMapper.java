package cn.yunyichina.log.service.tracer.mapper;

import cn.yunyichina.log.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/21 15:24
 * @Description:
 */
@Repository
public interface CommonMapper extends BaseMapper {

    void createTraceTable(@Param("tableName") String tableName);

    void createReverseIndexTable(@Param("tableName") String tableName);

}
