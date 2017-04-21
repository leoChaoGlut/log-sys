import cn.yunyichina.log.component.filter.entity.FilterConditionDTO;
import cn.yunyichina.log.component.filter.imp.ContextFilter;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by Csy on 2017年4月11日
 */
public class FilterTest {

    public static String str = "C:\\Users\\Administrator\\Desktop\\a.txt";

    @Test
    public void test() {

        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(str), "GBK"));

            String content = "";
            StringBuilder sb = new StringBuilder();

            while (content != null) {
                content = bf.readLine();
                if (content == null) {
                    break;
                }
                sb.append(content);
            }

            bf.close();
            FilterConditionDTO fcd = new FilterConditionDTO();
            fcd.setContextId("191");
            ContextFilter cf = new ContextFilter(sb.toString(), fcd);
            System.out.println(cf.filter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
