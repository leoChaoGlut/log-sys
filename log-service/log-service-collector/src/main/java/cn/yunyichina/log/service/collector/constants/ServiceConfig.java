package cn.yunyichina.log.service.collector.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonven on 2017/1/15.
 */
@Component
@ConfigurationProperties(prefix = "configDir")
public class ServiceConfig {

    private Map<String, ConfigCollector> collectorMap = new HashMap<>();

    public Map<String, ConfigCollector> getCollectorMap() {
        return collectorMap;
    }

    public void setCollectorMap(Map<String, ConfigCollector> collectorMap) {
        this.collectorMap = collectorMap;
    }

    public static class ConfigCollector {
        private String logRootDir;
        private String tmpZipDir;
        private String gateway;
        private String uploadServerUrl;
        private String tagSet;
        private String propertiesPath;
        private long fixedRate;

        public String getLogRootDir() {
            return logRootDir;
        }

        public void setLogRootDir(String logRootDir) {
            this.logRootDir = logRootDir;
        }

        public String getTmpZipDir() {
            return tmpZipDir;
        }

        public void setTmpZipDir(String tmpZipDir) {
            this.tmpZipDir = tmpZipDir;
        }

        public String getGateway() {
            return gateway;
        }

        public void setGateway(String gateway) {
            this.gateway = gateway;
        }

        public String getUploadServerUrl() {
            return uploadServerUrl;
        }

        public void setUploadServerUrl(String uploadServerUrl) {
            this.uploadServerUrl = uploadServerUrl;
        }

        public String getTagSet() {
            return tagSet;
        }

        public void setTagSet(String tagSet) {
            this.tagSet = tagSet;
        }

        public String getPropertiesPath() {
            return propertiesPath;
        }

        public void setPropertiesPath(String propertiesPath) {
            this.propertiesPath = propertiesPath;
        }

        public long getFixedRate() {
            return fixedRate;
        }

        public void setFixedRate(long fixedRate) {
            this.fixedRate = fixedRate;
        }
    }
}
