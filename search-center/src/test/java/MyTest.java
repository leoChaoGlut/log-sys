import cn.yunyichina.log.common.util.NetworkUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 11:55
 * @Description:
 */
public class MyTest {

    @Test
    public void test() throws Exception {
        CloseableHttpClient httpsClient = NetworkUtil.createHttpsClient();
        HttpPost post = new HttpPost("https://localhost:9999/center/search/history");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("beginDateTime", "vip"));
        nvps.add(new BasicNameValuePair("endDateTime", "secret"));
        post.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpsClient.execute(post);
        byte[] bytes = new byte[1024 * 1024];
        InputStream is = response.getEntity().getContent();
        int len = is.read(bytes);
        is.close();
        System.out.println(new String(bytes, 0, len));

    }
}
