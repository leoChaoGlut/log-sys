package cn.yunyichina.log.service.collector.util;

import cn.yunyichina.log.common.entity.dto.ResponseDTO;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 16:28
 * @Description:
 */
public class HttpUtil {

    public static final int TIME_OUT = 10_000;//ms

    public static HttpResponse get(String url) throws IOException {
        return Request.Get(url)
                .connectTimeout(TIME_OUT)
                .socketTimeout(TIME_OUT)
                .execute()
                .returnResponse();
    }

    public static HttpResponse post(String url, List<BasicNameValuePair> paramList) throws IOException {
        return Request.Post(url)
                .body(new UrlEncodedFormEntity(paramList, Charsets.UTF_8))
                .connectTimeout(TIME_OUT)
                .socketTimeout(TIME_OUT)
                .execute()
                .returnResponse();
    }


    public static ResponseDTO parse(HttpResponse resp) throws IOException {
        byte[] bytes = ByteStreams.toByteArray(resp.getEntity().getContent());
        return JSON.parseObject(new String(bytes, Charsets.UTF_8), ResponseDTO.class);
    }
}
