import cn.yunyichina.log.component.scanner.imp.LogScanner;
import org.junit.Test;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/28 9:38
 * @Description:
 */
public class ScannerTest {

    @Test
    public void test() {
        String beginDatetime = "2017-02-22 18:11";
        String endDatetime = "2017-02-23 10:05";
        String logDir = "D:\\gitRepo\\yunyi\\src\\log\\log-sys\\log-resource\\test-resource\\log";//log-resource/test-resource的路径
        Map<String, File> logMap = LogScanner.scan(beginDatetime, endDatetime, logDir);
        Set<Map.Entry<String, File>> entries = logMap.entrySet();
        for (Map.Entry<String, File> entry : entries) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
        System.out.println(logMap.size());
    }
}
