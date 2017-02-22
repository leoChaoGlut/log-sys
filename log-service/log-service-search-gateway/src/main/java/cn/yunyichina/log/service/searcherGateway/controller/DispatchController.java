package cn.yunyichina.log.service.searcherGateway.controller;

import cn.yunyichina.log.common.entity.entity.dto.ResponseDTO;
import cn.yunyichina.log.common.entity.entity.dto.SearchCondition;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.searcherGateway.service.DispatchService;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 14:29
 * @Description: 其实只需要一个dispatch方法就好了, 根据需要转发的url路径进行转发.
 */
@RestController
public class DispatchController {

    final LoggerWrapper logger = LoggerWrapper.getLogger(DispatchController.class);

    @Autowired
    DispatchService dispatchService;

    @PostMapping
    public ResponseDTO dispatch(String json) {
        try {
            logger.contextBegin("搜索网关接收到请求:" + json);
            json = URLDecoder.decode(json, Charsets.UTF_8.name());
            logger.info("decode:" + json + "--");
            SearchCondition condition = JSON.parseObject(json, SearchCondition.class);
            logger.info("obj:" + JSON.toJSONString(condition, true));
            ResponseDTO responseDTO = dispatchService.dispatch(condition);
            logger.contextEnd("搜索网关正常返回:" + JSON.toJSONString(responseDTO.getResult(), true));
            return responseDTO;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            logger.contextEnd("搜索网关返回,但是出现异常:" + e.getLocalizedMessage());
            return ResponseDTO.failure(e.getLocalizedMessage());
        }
    }

}
