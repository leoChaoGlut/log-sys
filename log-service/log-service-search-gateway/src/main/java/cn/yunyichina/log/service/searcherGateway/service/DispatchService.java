package cn.yunyichina.log.service.searcherGateway.service;

import cn.yunyichina.log.common.entity.entity.dto.ResponseDTO;
import cn.yunyichina.log.common.entity.entity.dto.SearchCondition;
import cn.yunyichina.log.common.entity.entity.po.StoreRecord;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.searcherGateway.mapper.StoreRecordMapper;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/28 9:56
 * @Description:
 */
@Service
public class DispatchService {

    final LoggerWrapper logger = LoggerWrapper.getLogger(DispatchService.class);

    private final int TIME_OUT = 7000;

    @Autowired
    StoreRecordMapper storeRecordMapper;

    //    @Cacheable(cacheNames = {LOG}, key = "#condition.toString()")
    public ResponseDTO dispatch(SearchCondition condition) throws Exception {
        StoreRecord storeRecord = getStoreRecord(condition.getCollector().getName());
        if (null == storeRecord) {
            throw new Exception("无存储节点信息:" + JSON.toJSONString(condition, true));
        } else {
            String outerIp = storeRecord.getOuter_ip();
            Integer outerPort = storeRecord.getOuter_port();
            String url = "http://" + outerIp + ":" + outerPort + "/search/history";

            logger.info("即将向:" + url + " 发送搜索请求:" + JSON.toJSONString(condition, true));

            Content content = Request.Post(url)
                    .connectTimeout(TIME_OUT)
                    .socketTimeout(TIME_OUT)
                    .bodyString(JSON.toJSONString(condition), ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent();

            String respJson = content.asString(Charsets.UTF_8);
            logger.info("搜索节点返回的数据:" + respJson);
            return JSON.parseObject(respJson, ResponseDTO.class);
        }
    }

    public ResponseDTO getDownloadableLogs(SearchCondition condition) throws Exception {
        StoreRecord storeRecord = getStoreRecord(condition.getCollector().getName());
        if (null == storeRecord) {
            throw new Exception("无存储节点信息:" + JSON.toJSONString(condition, true));
        } else {
            String outerIp = storeRecord.getOuter_ip();
            Integer outerPort = storeRecord.getOuter_port();
            String url = "http://" + outerIp + ":" + outerPort + "/search/logs";

            logger.info("即将向:" + url + " 发送请求,获取可下载的日志" + JSON.toJSONString(condition, true));

            Content content = Request.Post(url)
                    .connectTimeout(TIME_OUT)
                    .socketTimeout(TIME_OUT)
                    .bodyString(JSON.toJSONString(condition), ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent();

            String respJson = content.asString(Charsets.UTF_8);
            logger.info("搜索节点返回的数据:" + respJson);
            return JSON.parseObject(respJson, ResponseDTO.class);
        }
    }

    //    @Cacheable(cacheNames = {LOG}, key = "#collectorName")
    private StoreRecord getStoreRecord(String collectorName) {
        StoreRecord storeRecordParam = new StoreRecord();
        storeRecordParam.setCollector_name(collectorName);
        return storeRecordMapper.selectOne(storeRecordParam);
    }


}
