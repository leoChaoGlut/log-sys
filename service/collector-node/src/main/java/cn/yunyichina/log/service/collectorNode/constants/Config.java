package cn.yunyichina.log.service.collectorNode.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/5 14:06
 * @Description: 配置文件在配置中心, 不在本地
 */
@Configuration
public class Config {

    @Value("${cron}")
    private String cron;

    @Value("${dir.logRoot}")
    private String logRootDir;

    @Value("${dir.tmpZip}")
    private String tmpZipDir;

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

    public long getFixedRate() {
        return fixedRate;
    }

    public String getUploadServerUrl() {
        return uploadServerUrl;
    }
}
