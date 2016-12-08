package cn.yunyichina.log.service.searcherGateway.controller;

import cn.yunyichina.log.common.entity.dto.Response;
import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.searcherGateway.service.DispatchService;
import com.alibaba.fastjson.JSON;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
            SearchCondition condition = JSON.parseObject(jsonParam, SearchCondition.class);
            Response response = dispatchService.dispatch(condition);
            return response;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return Response.failure(e.getLocalizedMessage());
        }
    }

    @Value("${server.port}")
    int serverPort;

    @Autowired
    EurekaClient eurekaClient;

    @GetMapping("test")
    public String test() {
        Application application = eurekaClient.getApplication("searcher-gateway");
        List<InstanceInfo> instanceInfoList = application.getInstances();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (InstanceInfo ii : instanceInfoList) {
            sb.append("{" + ii.getHostName() + "," + ii.getIPAddr() + "," + ii.getPort() + "}");
        }
        sb.append("]");
        return serverPort + sb.toString();
    }

}
