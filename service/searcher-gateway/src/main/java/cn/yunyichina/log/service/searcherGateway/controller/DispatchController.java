package cn.yunyichina.log.service.searcherGateway.controller;

import cn.yunyichina.log.common.entity.dto.Response;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.searcherGateway.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 14:29
 * @Description:
 */
@RestController
public class DispatchController {

    final LoggerWrapper logger = LoggerWrapper.newInstance(DispatchController.class);

    @Autowired
    DispatchService dispatchService;

    @PostMapping("dispatch")
    public Response dispatch(@RequestBody String jsonParam) {
        try {
            logger.info("搜索网关接收到请求:" + jsonParam);
//            TODO 正确代码
//            SearchCondition condition = JSON.parseObject(jsonParam, SearchCondition.class);
//            Response response = dispatchService.dispatch(condition);
//            return response;
            return Response.success("搜索网关成功");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return Response.failure(e.getLocalizedMessage());
        }
    }

}
