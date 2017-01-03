package cn.yunyichina.log.service.frontEnd.repository;

import cn.yunyichina.log.service.frontEnd.entity.do_.option.GroupDo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/3 16:48
 * @Description:
 */
public interface GroupRepository extends MongoRepository<GroupDo, String> {
}
