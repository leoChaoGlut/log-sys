package cn.yunyichina.log.service.tracer.mapper;

import cn.yunyichina.log.common.base.BaseMapper;
import cn.yunyichina.log.service.common.entity.do_.ReverseIndexDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/21 22:58
 * @Description:
 */
@Repository
public interface ReverseIndexMapper extends BaseMapper<ReverseIndexDO> {

    int insertList(@Param("reverseIndexDOList") List<ReverseIndexDO> reverseIndexDOList);
}
