package cn.yunyichina.log.service.collectorservice.mapper;

import cn.yunyichina.log.common.base.BaseMapper;
import cn.yunyichina.log.common.entity.do_.KeywordTagDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 15:13
 * @Description:
 */
public interface KeywordTagMapper extends BaseMapper<KeywordTagDO> {

    List<KeywordTagDO> selectListByCollectedItemId(@Param("collectedItemId") int collectedItemId);
}
