package cn.yunyichina.log.service.api.mapper;

import cn.yunyichina.log.common.entity.base.BaseMapper;
import cn.yunyichina.log.common.entity.entity.do_.Group;
import org.springframework.stereotype.Repository;

/**
 * Created by Jonven on 2017/1/4.
 */
@Repository
public interface GroupMapper extends BaseMapper<Group> {

    public Group findByGroupName(String groupName);

    public Group findByGroupId(Integer id);

}
