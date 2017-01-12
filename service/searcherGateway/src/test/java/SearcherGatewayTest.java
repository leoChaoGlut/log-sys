import cn.yunyichina.log.common.entity.entity.dto.SearchCondition;
import cn.yunyichina.log.common.entity.entity.po.Collector;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/28 13:23
 * @Description:
 */
public class SearcherGatewayTest {

    @Test
    public void test0() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SearchCondition condition = new SearchCondition();
        condition.setBeginDateTime(sdf.parse("2016-10-10 10:10"));
        condition.setEndDateTime(sdf.parse("2016-10-10 10:10"));
        Collector collector = new Collector();
        collector.setName("深三");
        condition.setCollector(collector);
        condition.setKeyword("test");
        condition.setSearchEngineType(1);
        System.out.println(JSON.toJSONString(condition, true));
    }

}
