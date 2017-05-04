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

    public static String str = "E:\\yunyi\\log-sys\\log-component\\log-component-filter\\src\\test\\resource\\test.log";

    @Test
    public void test() {

        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(str), "UTF-8"));

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
            fcd.setContextId("5900388c85f35a2cce1cec09");
            ContextFilter cf = new ContextFilter(sb.toString(), fcd);
            System.out.println(cf.filter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
