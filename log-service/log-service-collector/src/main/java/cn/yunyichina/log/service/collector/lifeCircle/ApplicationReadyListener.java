package cn.yunyichina.log.service.collector.lifecircle;


import cn.yunyichina.log.common.entity.do_.CollectorDO;
import cn.yunyichina.log.common.entity.dto.ResponseDTO;
import cn.yunyichina.log.common.entity.dto.ResponseDTO.Status;
import cn.yunyichina.log.common.exception.CollectorException;
import cn.yunyichina.log.service.collector.service.CacheService;
import cn.yunyichina.log.service.collector.task.ScheduleTask;
import cn.yunyichina.log.service.collector.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.ObjectUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/9/29 17:56
 * @Description:
 */
@Component
@RefreshScope
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    final Logger logger = LoggerFactory.getLogger(ApplicationReadyListener.class);

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${eureka.instance.ipAddress}")
    String ip;

    @Value("${server.port}")
    String port;

    @Value("${url.gateway}")
    String gatewayUrl;

    @Value("${collectorServiceName}")
    String collectorServiceName;

    @Autowired
    CacheService cacheService;

    @Autowired
    ScheduleTask scheduleTask;

    final int MAX_RETRY_COUNT = 3;

    int retryCount = 0;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        init();
    }

    private void init() {
        try {
            registerAndGetData();
            initContextCount();
        } catch (Exception e) {
            e.printStackTrace();
            if (retryCount < MAX_RETRY_COUNT) {
                logger.error("初始化异常，3秒后重试", e);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                init();
            } else {
//                3次尝试失败，退出程序
                logger.error("3次尝试失败，退出程序");
                System.exit(0);
            }
        }
    }

    private void registerAndGetData() throws IOException, CollectorException, ClassNotFoundException {
        String url = gatewayUrl + "/" + collectorServiceName + "/collector/register";
        logger.info("即将发送请求到: " + url);
        List<BasicNameValuePair> paramList = Arrays.asList(
                new BasicNameValuePair("ip", ip),
                new BasicNameValuePair("port", port),
                new BasicNameValuePair("applicationName", applicationName)
        );
        HttpResponse httpResp = HttpUtil.post(url, paramList);
        judgeHttpResponse(httpResp);
        ResponseDTO resp = HttpUtil.parse(httpResp);
        logger.info("Resp body json: " + resp.toString());
        judgeResponseBody(resp);
        cacheCollector(resp);
    }

    private void cacheCollector(ResponseDTO resp) throws IOException, ClassNotFoundException {
        CollectorDO collector = JSON.parseObject(JSON.toJSONString(resp.getResult()), CollectorDO.class);
        cacheService.setCollector(collector);
        scheduleTask.initCollectedItemList();
    }

    private void judgeResponseBody(ResponseDTO resp) throws IOException, CollectorException {
        if (ObjectUtils.notEqual(Status.OK.getCode(), resp.getCode())) {
            String errorMsg = "Http 连接成功,服务器抛出异常:" + resp.getCode() + " - " + resp.getMsg();
            throw new CollectorException(errorMsg);
        }
    }

    private void judgeHttpResponse(HttpResponse resp) throws CollectorException {
        StatusLine statusLine = resp.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (ObjectUtils.notEqual(statusCode, HttpStatus.SC_OK)) {
            String errorMsg = "Http 连接异常:" + statusCode + " - " + statusLine.getReasonPhrase();
            throw new CollectorException(errorMsg);
        }
    }

    //TODO
//TODO
//TODO
//TODO
    private void initContextCount() throws IOException, ClassNotFoundException {
//        ApplicationCacheV1 applicationCache = CacheUtil.read();
//        if (null == applicationCache) {
//            LoggerWrapper.initCounter(0);
//        } else {
//            LoggerWrapper.initCounter(applicationCache.getContextCount());
//        }
    }


}
