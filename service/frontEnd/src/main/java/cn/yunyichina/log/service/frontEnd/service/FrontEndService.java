package cn.yunyichina.log.service.frontEnd.service;

import cn.yunyichina.log.service.frontEnd.entity.dto.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 10:34
 * @Description:
 */
@Service
public class FrontEndService {

    @Autowired
    CollectorService collectorService;

    public Option getOption() {
        return null;
    }

}
