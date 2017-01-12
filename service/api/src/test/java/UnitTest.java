import cn.yunyichina.log.common.entity.entity.po.KeyValueTag;
import cn.yunyichina.log.common.entity.entity.po.KeywordTag;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.Test;

/**
 * Created by Jonven on 2017/1/11.
 */
public class UnitTest {

    final int TIME_OUT = 7000;

    @Test
    public void addKeyword() throws Exception {

        String url = "http://127.0.0.1:10600/collector/addKeyword";

        KeywordTag keywordTag = new KeywordTag();
        keywordTag.setApplication_name("collector-shensan");
        keywordTag.setCollector_name("深三(test)");
        keywordTag.setKeyword("中文");

        Content content = Request.Post(url)
                .connectTimeout(TIME_OUT)
                .socketTimeout(TIME_OUT)
                .bodyString(JSON.toJSONString(keywordTag), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent();

        String respone = content.asString(Charsets.UTF_8);
        System.out.println(JSON.toJSONString(respone, true));
    }

    @org.junit.Test
    public void addKvTag() throws Exception {

        String url = "http://127.0.0.1:10600/collector/addKvTag";

        KeyValueTag keyValueTag = new KeyValueTag();
        keyValueTag.setCollector_name("深三(test)");
        keyValueTag.setApplication_name("collector-shensan-test");
        keyValueTag.setKey("test");
        keyValueTag.setKey_tag("\"test\":\"");
        keyValueTag.setValue_end_tag("\"");

        Content content = Request.Post(url)
                .connectTimeout(TIME_OUT)
                .socketTimeout(TIME_OUT)
                .bodyString(JSON.toJSONString(keyValueTag), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent();

        String respone = content.asString(Charsets.UTF_8);
        System.out.println(JSON.toJSONString(respone, true));
    }

}
