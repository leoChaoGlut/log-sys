package cn.yunyichina.log.service.collector.lifeCircle;


import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.component.entity.dto.TagSet;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.service.collector.constants.Key;
import cn.yunyichina.log.service.collector.util.PropertiesUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/9/29 17:56
 * @Description:
 */
@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    final LoggerWrapper logger = LoggerWrapper.getLogger(ApplicationReadyListener.class);
    final int TIME_OUT = 7000;

    @Autowired
    Environment env;

    @Autowired
    PropertiesUtil propUtil;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        requestTagSet();

        initCounter();
    }

    private void requestTagSet() {
        try {
            logger.contextBegin("开始获取tagSet");
            String keywordSetJson = propUtil.get(Key.KEYWORD_SET);
            String kvTagSetJson = propUtil.get(Key.KV_TAG_SET);
            boolean doRequest = false;
            if (null == keywordSetJson || org.springframework.util.StringUtils.isEmpty(keywordSetJson)) {
                logger.contextEnd(Key.KEYWORD_SET + " 为空");
                doRequest = true;
            } else if (null == kvTagSetJson || org.springframework.util.StringUtils.isEmpty(kvTagSetJson)) {
                logger.contextEnd(Key.KV_TAG_SET + " 为空");
                doRequest = true;
            }

            if (doRequest) {
                String url = env.getProperty("url.tagSet");
                logger.info("从 " + url + " 获取tagSet");

                String json = Request.Get(url)
                        .socketTimeout(TIME_OUT)
                        .connectTimeout(TIME_OUT)
                        .execute()
                        .returnContent()
                        .asString(Charsets.UTF_8);

                TagSet tagSet = JSON.parseObject(json, TagSet.class);
                Set<String> keywordSet = tagSet.getKeywordSet();
                Set<KeyValueIndexBuilder.KvTag> kvTagSet = tagSet.getKvTagSet();

                keywordSetJson = JSON.toJSONString(keywordSet);
                kvTagSetJson = JSON.toJSONString(kvTagSet);

                propUtil.put(Key.KEYWORD_SET, keywordSetJson);
                propUtil.put(Key.KV_TAG_SET, kvTagSetJson);
            }
        } catch (IOException e) {
            String errorMsg = "获取tagSet时出现异常:" + e.getLocalizedMessage();
            logger.error(errorMsg, e);
            logger.contextEnd(errorMsg);
        }
    }

    /**
     * 当程序重启的时候,把LoggerWrapper的count恢复为之前的数字,不恢复的话,会是0
     */
    private void initCounter() {
        String countStr = propUtil.get(Key.CONTEXT_COUNT);
        if (StringUtils.isNotBlank(countStr)) {
            Long count = Long.valueOf(countStr);
            LoggerWrapper.initCounter(count);
        }
    }
}
