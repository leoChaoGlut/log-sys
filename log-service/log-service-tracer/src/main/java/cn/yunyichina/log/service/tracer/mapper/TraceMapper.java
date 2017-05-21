package cn.yunyichina.log.service.tracer.mapper;

import cn.yunyichina.log.common.base.BaseMapper;
import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/21 11:26
 * @Description:
 */
@Repository
public interface TraceMapper extends BaseMapper<LinkedTraceNode> {

    int insertListByTableNameAndList(@Param("tableName") String tableName, @Param("nodeList") List<LinkedTraceNode> nodeList);

}
