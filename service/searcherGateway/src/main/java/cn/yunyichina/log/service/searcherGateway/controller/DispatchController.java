package cn.yunyichina.log.service.searcherGateway.controller;

import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.component.entity.dto.Response;
import cn.yunyichina.log.component.entity.dto.SearchCondition;
import cn.yunyichina.log.service.searcherGateway.service.DispatchService;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 14:29
 * @Description:
 */
@RestController
public class DispatchController {

    final LoggerWrapper logger = LoggerWrapper.getLogger(DispatchController.class);

    @Autowired
    DispatchService dispatchService;

    @PostMapping
    public Response dispatch(String json) {
        try {
            logger.contextBegin("搜索网关接收到请求:" + json);
            json = URLDecoder.decode(json, Charsets.UTF_8.name());
            logger.info("decode:" + json + "--");
            SearchCondition condition = JSON.parseObject(json, SearchCondition.class);
            logger.info("obj:" + JSON.toJSONString(condition, true));
            Response response = dispatchService.dispatch(condition);
            logger.contextEnd("搜索网关正常返回:" + JSON.toJSONString(response.getResult(), true));
            return response;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            logger.contextEnd("搜索网关返回,但是出现异常:" + e.getLocalizedMessage());
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
