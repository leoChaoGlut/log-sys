package cn.yunyichina.log.service.api.service;

import cn.yunyichina.log.component.entity.dto.TagSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/6 17:43
 * @Description:
 */
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class TagService {

    @Transactional(readOnly = true)
    public TagSet getTagSet(Integer collectorId) {

        return null;
    }

}
