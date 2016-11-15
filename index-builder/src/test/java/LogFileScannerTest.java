import cn.yunyichina.log.index.builder.util.LogFileScanner;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.File;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 9:41
 * @Description:
 */
public class LogFileScannerTest {

    @Test
    public void logScannerTest() {
        LogFileScanner logFileScanner = new LogFileScanner("2016-01-01 01:02", "2016-01-01 01:02", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        System.out.println(JSON.toJSONString(fileMap, true));
    }
}
