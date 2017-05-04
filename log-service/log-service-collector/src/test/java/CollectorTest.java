import cn.yunyichina.log.common.constant.SearchEngineType;
import com.google.common.io.ByteStreams;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/26 10:59
 * @Description:
 */
public class CollectorTest {

    @Test
    public void searchTest() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "http://127.0.0.1:10500/search/history";
        HttpPost post = new HttpPost(url);
//        Calendar begin = specifyCalender(2017, 04, 13, 00, 00);
//        Calendar end = specifyCalender(2017, 04, 14, 00, 00);
        String beginDatetimeStr = "2017-04-13 00:00";
        String endDatetimeStr = "2017-04-14 00:00";
        List<BasicNameValuePair> nvpList = Arrays.asList(
                new BasicNameValuePair("searchEngineType", SearchEngineType.NO_INDEX + ""),
                new BasicNameValuePair("noIndexKeyword", "周伟"),
                new BasicNameValuePair("beginDateTime", beginDatetimeStr),
                new BasicNameValuePair("endDateTime", endDatetimeStr),
                new BasicNameValuePair("collectedItemId", "1")
        );
        post.setEntity(new UrlEncodedFormEntity(nvpList, StandardCharsets.UTF_8));
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(post);
            byte[] bytes = ByteStreams.toByteArray(response.getEntity().getContent());
            System.out.println(new String(bytes, StandardCharsets.UTF_8));
        } finally {
            response.close();
        }
    }

    private Calendar specifyCalender(int year, int month, int date, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, date);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        return c;
    }


}
