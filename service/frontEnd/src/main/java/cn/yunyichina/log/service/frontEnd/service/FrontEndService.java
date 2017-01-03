package cn.yunyichina.log.service.frontEnd.service;

import cn.yunyichina.log.service.frontEnd.entity.do_.option.GroupDo;
import cn.yunyichina.log.service.frontEnd.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.yunyichina.log.service.frontEnd.constants.CacheName.OPTION;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 10:34
 * @Description:
 */
@Service
public class FrontEndService {

    @Autowired
    GroupRepository groupRepository;

    @Cacheable(cacheNames = {OPTION}, key = "#root.methodName")
    public List<GroupDo> getSearchOption() {
        List<GroupDo> groupList = groupRepository.findAll();
        return groupList;
    }

    @CachePut(cacheNames = {OPTION}, key = "#root.methodName")
    public List<GroupDo> updateSearchOptionCache() {
        return null;
    }


}
