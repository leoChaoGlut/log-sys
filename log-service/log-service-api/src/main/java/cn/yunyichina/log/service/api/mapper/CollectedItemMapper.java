package cn.yunyichina.log.service.api.mapper;

import cn.yunyichina.log.common.entity.base.BaseMapper;
import cn.yunyichina.log.common.entity.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.entity.do_.KeywordTagDO;
import cn.yunyichina.log.common.entity.entity.do_.KvTagDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/22 15:37
 * @Description:
 */
public interface CollectedItemMapper extends BaseMapper<CollectedItemDO> {

    List<KvTagDO> selectKvTagList(
            @Param("collectedItemId") Integer collectedItemId
    );

    List<KeywordTagDO> selectKeywordTagList(
            @Param("collectedItemId") Integer id
    );
}
