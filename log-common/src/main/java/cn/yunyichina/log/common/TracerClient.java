package cn.yunyichina.log.common;

import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.common.util.ResponseUtil;
import com.alibaba.fastjson.JSON;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/29 9:59
 * @Description: 用于向tracer添加traceNode, 用于适配dubbo类型的应用
 */
public class TracerClient {

    public static final int SOCKET_TIMEOUT_MS = 10_000;
    public static final int CONNECT_TIMEOUT_MS = 3_000;
    public static final Charset UTF8 = Charset.forName("UTF-8");

    private LoggerWrapper loggerWrapper;

    public TracerClient(LoggerWrapper loggerWrapper) {
        this.loggerWrapper = loggerWrapper;
    }

    /**
     * 适配dubbo应用,在rpc调用的前后,向远程tracer发送追踪请求.
     *
     * @param url
     * @param traceId
     * @param timestamp
     * @param serviceId
     * @param beInvoked true:服务被调用 false:服务主动调用
     */
    public void aroundRPC(String url, String traceId, String timestamp, String serviceId, boolean beInvoked) {
        String msg = url + " - " + traceId + " - " + timestamp + " - " + serviceId;
        Long contextCount;
        if (beInvoked) {
            contextCount = loggerWrapper.contextBegin(msg);
        } else {
            contextCount = loggerWrapper.contextEnd(msg);
        }
        List<NameValuePair> traceNodeParamList = buildTraceParamList(contextCount.toString(), traceId, timestamp, serviceId);
        try {
            sendNewTraceNodeRequest(url, traceNodeParamList);
        } catch (IOException e) {
            loggerWrapper.error(e.getMessage());
        }
    }

    private List<NameValuePair> buildTraceParamList(String contextCount, String traceId, String timestamp, String serviceId) {
        List<NameValuePair> traceNodeParamList = new ArrayList<>();
        traceNodeParamList.add(new BasicNameValuePair("contextCount", contextCount));
        traceNodeParamList.add(new BasicNameValuePair("traceId", traceId));
        traceNodeParamList.add(new BasicNameValuePair("timestamp", timestamp));
        traceNodeParamList.add(new BasicNameValuePair("serviceId", serviceId));
        return traceNodeParamList;
    }

    /**
     * @param url                format:"http://$gatewayIp:$port/$tracerApplicationName"   eg: "http://127.0.0.1:10300/log-service-tracer"
     * @param traceNodeParamList @link cn.yunyichina.log.service.tracer.trace.TraceNode} 必备参数:[traceId,timestamp,contextCount,serviceId]
     * @param handleResponseBody 是否需要处理response body
     * @param <T>
     * @return
     * @throws IOException
     */
    private <T> T sendNewTraceNodeRequest(String url, List<NameValuePair> traceNodeParamList, boolean handleResponseBody) throws IOException {
        Response response = Request.Post(url)
                .bodyForm(traceNodeParamList, UTF8)
                .socketTimeout(SOCKET_TIMEOUT_MS)
                .connectTimeout(CONNECT_TIMEOUT_MS)
                .execute();
        if (handleResponseBody) {
            String json = response.returnContent().asString(UTF8);
            ResponseBodyDTO responseBodyDTO = JSON.parseObject(json, ResponseBodyDTO.class);
            return (T) ResponseUtil.getResult(responseBodyDTO);
        } else {
            return null;
        }
    }

    private <T> T sendNewTraceNodeRequest(String url, List<NameValuePair> traceNodeParamList) throws IOException {
        return sendNewTraceNodeRequest(url, traceNodeParamList, false);
    }

}
