package cn.yunyichina.log.service.collectorNode.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/5 14:06
 * @Description:
 */
@Configuration
@PropertySource("classpath:config.yml")
public class Config {

    @Value("${cron}")
    private String cron;

    @Value("${dir.logRoot}")
    private String logRootDir;

    @Value("${dir.tmpZip}")
    private String tmpZipDir;

    @Value("${cache.path}")
    private String cachePath;

    @Value("${fixedRate}")
    private long fixedRate;

    @Value("${url.uploadServer}")
    private String uploadServerUrl;

    public String getCron() {
        return cron;
    }

    public String getLogRootDir() {
        return logRootDir;
    }

    public String getTmpZipDir() {
        return tmpZipDir;
    }

    public String getCachePath() {
        return cachePath;
    }

    public long getFixedRate() {
        return fixedRate;
    }

    public String getUploadServerUrl() {
        return uploadServerUrl;
    }
}
