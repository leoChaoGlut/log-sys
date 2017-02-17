package cn.yunyichina.log.service.searcher.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonven on 2017/1/16.
 */
@Component
@ConfigurationProperties(prefix = "config")
public class Config {

    private Map<String, ConfigItem> configItemMap = new HashMap<>();

    public Map<String, ConfigItem> getConfigItemMap() {
        return configItemMap;
    }

    public void setConfigItemMap(Map<String, ConfigItem> configItemMap) {
        this.configItemMap = configItemMap;
    }

    public static class ConfigItem {
        private String logRootDir;
        private String indexRootDir;
        private String logZip;

        public String getLogRootDir() {
            return logRootDir;
        }

        public void setLogRootDir(String logRootDir) {
            this.logRootDir = logRootDir;
        }

        public String getIndexRootDir() {
            return indexRootDir;
        }

        public void setIndexRootDir(String indexRootDir) {
            this.indexRootDir = indexRootDir;
        }

        public String getLogZip() {
            return logZip;
        }

        public void setLogZip(String logZip) {
            this.logZip = logZip;
        }
    }

}
