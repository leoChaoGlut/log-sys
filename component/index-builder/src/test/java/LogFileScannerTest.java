import cn.yunyichina.log.component.indexBuilder.util.LogFileScanner;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
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
        Map<String, File> map = logFileScanner.scan();
        System.out.println(JSON.toJSONString(map, true));
    }

}
