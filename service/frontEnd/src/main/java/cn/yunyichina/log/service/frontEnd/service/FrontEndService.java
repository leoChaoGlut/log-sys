package cn.yunyichina.log.service.frontEnd.service;

import cn.yunyichina.log.service.frontEnd.entity.do_.option.GroupDo;
import cn.yunyichina.log.service.frontEnd.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 10:34
 * @Description:
 */
@Service
public class FrontEndService {

    public List<GroupDo> getOption() {
//        getOptionFromCache();
        getOptionFromDB();
        return null;
    }


    private List<GroupDo> getOptionFromCache() {
        return null;
    }

    @Autowired
    GroupRepository groupRepository;

    private List<GroupDo> getOptionFromDB() {
        List<GroupDo> groupList = groupRepository.findAll();
        return groupList;
    }
}
