import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.*;

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

    @Test
    public void read() throws IOException, ClassNotFoundException {
        String cachePath = "E:\\cache\\7\\context.cache";
        File cacheFile = new File(cachePath);
        if (cacheFile.exists()) {

        } else {
            Files.createParentDirs(cacheFile);
            cacheFile.createNewFile();
            try (
                    FileOutputStream fos = new FileOutputStream(cachePath);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
            ) {
                oos.writeObject(null);
                oos.flush();
            }
        }
        try (
                FileInputStream fis = new FileInputStream(cachePath);
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            System.out.println(JSON.toJSONString(ois.readObject(), true));
            ;
        }
    }
}
