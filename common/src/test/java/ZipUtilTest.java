import cn.yunyichina.log.common.util.ZipUtil;
import com.google.common.base.Predicate;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 18:32
 * @Description:
 */
public class ZipUtilTest {

    @Test
    public void zipTest() throws Exception {
        File[] files = Files.fileTreeTraverser().breadthFirstTraversal(new File("D:\\tmp")).filter(new Predicate<File>() {
            @Override
            public boolean apply(File input) {
                return input.isDirectory() ? false : true;
            }
        }).toArray(File.class);
        ZipUtil.zip("D:\\zip2.zip", files);
    }

    @Test
    public void unzipTest() throws Exception {
        ZipUtil.unzip("D:\\zip2.zip", "D:\\zip2\\");
    }


}
