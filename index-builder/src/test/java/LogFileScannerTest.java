import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.index.util.LogFileScanner;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 9:41
 * @Description:
 */
public class LogFileScannerTest {

    @Test
    public void logScannerTest() throws IOException {
        LogFileScanner logFileScanner = new LogFileScanner("2016-01-01 01:02", "2017-01-01 01:02", "D:\\tmp");
    }

    @Test
    public void buildContextIndex() throws IOException {
        LogFileScanner logFileScanner = new LogFileScanner("2016-01-01 01:02", "2017-01-01 01:02", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        Collection<File> values = fileMap.values();
        Map<Long, ContextIndexBuilder.ContextInfo> contextIndex = new HashMap<>(1024);
        for (File f : values) {
            ContextIndexBuilder builder = new ContextIndexBuilder(f);
            Map<Long, ContextIndexBuilder.ContextInfo> map = builder.build();
            contextIndex.putAll(map);
        }
        Files.write(JSON.toJSONString(contextIndex), new File("D://context.index"), Charsets.UTF_8);
    }

    @Test
    public void buildKeywordIndex() throws IOException {
        LogFileScanner logFileScanner = new LogFileScanner("2016-01-01 01:02", "2017-01-01 01:02", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        Collection<File> values = fileMap.values();
        Map<String, List<KeywordIndexBuilder.IndexInfo>> keywordIndex = new HashMap<>(1024);

    }


}
