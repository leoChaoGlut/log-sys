import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.common.util.NetworkUtil;
import com.alibaba.fastjson.JSON;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 11:55
 * @Description:
 */
public class MyTest {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Test
    public void test() throws Exception {
        CloseableHttpClient httpsClient = NetworkUtil.createHttpsClient();
        HttpPost post = new HttpPost("https://localhost:9999/center/search/history");

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("beginDateTime", "2016-11-15 14:23"));
        nvps.add(new BasicNameValuePair("endDateTime", "2016-11-15 14:25"));
        nvps.add(new BasicNameValuePair("keyword", "pat_card_no"));
        nvps.add(new BasicNameValuePair("key", "pat_card_no"));
        nvps.add(new BasicNameValuePair("value", "0000426117"));

        post.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpsClient.execute(post);
        byte[] bytes = new byte[1024 * 1024];
        InputStream is = response.getEntity().getContent();
        int len = is.read(bytes);
        is.close();
        System.err.println(new String(bytes, 0, len));
    }

    @Test
    public void test1() throws ParseException {
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setBeginDateTime(sdf.parse("2016-11-15 14:23"));
        searchCondition.setEndDateTime(sdf.parse("2016-11-15 14:25"));
        searchCondition.setKeyword("pat_card_no");
        searchCondition.setSearchEngineType(1);
        System.out.println(JSON.toJSONString(searchCondition));
    }
}
