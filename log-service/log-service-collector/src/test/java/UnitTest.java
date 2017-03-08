import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/8 16:35
 * @Description:
 */
public class UnitTest {

    @Test
    public void test() throws IOException {
        File file = new File("D:\\gitRepo\\yunyi\\src\\log\\log-sys\\log-resource\\test-resource\\log\\2017\\02\\22\\18\\11\\201702221811.log");
        String s = Files.toString(file, Charsets.UTF_8);
        int length = s.length();
        System.out.println(length);
        int segCount = length % 1000;
        System.out.println(segCount);
    }
}
