package cn.yunyichina.log.service.api.controller;

import cn.yunyichina.log.common.entity.entity.dto.ResponseDTO;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.api.entity.dto.SearchOption;
import cn.yunyichina.log.service.api.service.FrontEndService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 9:37
 * @Description:
 */
@RestController
@RequestMapping("front")
public class FrontEndController {

    final LoggerWrapper logger = LoggerWrapper.getLogger(FrontEndController.class);

    @Autowired
    FrontEndService frontEndService;

    @GetMapping("options")
    public ResponseDTO getOptions() {
        try {
            logger.contextBegin("前端服务接收到请求:");
            SearchOption searchOption = frontEndService.getSearchOption();
            logger.contextEnd("前端服务正常返回:" + JSON.toJSONString(searchOption, true));
            return ResponseDTO.success(searchOption);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            logger.contextEnd("前端服务异常:" + e.getLocalizedMessage());
            return ResponseDTO.failure(e.getLocalizedMessage());
        }
    }

}
