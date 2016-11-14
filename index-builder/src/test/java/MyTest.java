import com.google.common.base.Predicate;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/11 16:48
 * @Description:
 */
public class MyTest {

    @Test
    public void test() {
        File[] files = Files.fileTreeTraverser().breadthFirstTraversal(new File("D:\\tmp")).filter(new Predicate<File>() {
            @Override
            public boolean apply(File input) {
                if (input.isDirectory()) {
                    return false;
                } else if (input.getName().endsWith(".log")) {
                    return true;
                }
                return false;
            }
        }).toArray(File.class);
        for (File f : files) {
            System.out.println(f.getName());
        }
    }

    @Test
    public void test1() throws IOException {
        String str = "测试";
        byte[] bytes = new byte[3];
        ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = bis.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, len));
        }
        System.out.println(sb.toString());
    }

}
