package cn.yunyichina.log.common.util;

import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import com.alibaba.fastjson.JSON;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/29 9:59
 * @Description: 用于向tracer添加traceNode
 */
public class TracerUtil {

    public static final int SOCKET_TIMEOUT = 10_000;
    public static final int CONNECT_TIMEOUT = 3_000;
    public static final Charset UTF8 = Charset.forName("UTF-8");

    /**
     * @param url                format:"http://$gatewayIp:$port/$tracerApplicationName"   eg: "http://127.0.0.1:10300/log-service-tracer"
     * @param traceNodeParamList @link cn.yunyichina.log.service.tracer.trace.TraceNode} 必备参数:[traceId,timestamp,contextCount,serviceId]
     * @param handleResponseBody 是否需要处理response body
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T addTraceNode(String url, List<NameValuePair> traceNodeParamList, boolean handleResponseBody) throws IOException {
        Response response = Request.Post(url)
                .bodyForm(traceNodeParamList, UTF8)
                .socketTimeout(SOCKET_TIMEOUT)
                .connectTimeout(CONNECT_TIMEOUT)
                .execute();
        if (handleResponseBody) {
            String json = response.returnContent().asString(UTF8);
            ResponseBodyDTO responseBodyDTO = JSON.parseObject(json, ResponseBodyDTO.class);
            return (T) ResponseUtil.getResult(responseBodyDTO);
        } else {
            return null;
        }
    }

    public static <T> T addTraceNode(String url, List<NameValuePair> traceNodeParamList) throws IOException {
        return addTraceNode(url, traceNodeParamList, false);
    }

}
