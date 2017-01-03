package cn.yunyichina.log.service.frontEnd.repository;


import cn.yunyichina.log.service.frontEnd.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/3 14:35
 * @Description:
 */
public interface UserRepository extends MongoRepository<User, Long> {

    List<User> findByAgeLessThan(Integer age);



}
