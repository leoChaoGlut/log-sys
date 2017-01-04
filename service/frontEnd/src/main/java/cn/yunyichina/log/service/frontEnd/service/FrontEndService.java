package cn.yunyichina.log.service.frontEnd.service;

import cn.yunyichina.log.component.entity.po.Collector;
import cn.yunyichina.log.component.entity.po.Group;
import cn.yunyichina.log.service.frontEnd.entity.do_.GroupDo;
import cn.yunyichina.log.service.frontEnd.repository.CollectorRepository;
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

    @Autowired
    CollectorRepository collectorRepository;

    @Cacheable(cacheNames = {OPTION}, key = "#root.methodName")
    public List<GroupDo> getSearchOption() {
        List<Group> groupList = groupRepository.findAll();
        List<Collector> collectorList = collectorRepository.findAll();


        return null;
    }

    @CachePut(cacheNames = {OPTION}, key = "#root.methodName")
    public List<GroupDo> updateSearchOptionCache() {
        return null;
    }


}
