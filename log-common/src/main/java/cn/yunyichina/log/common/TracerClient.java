package cn.yunyichina.log.common;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/29 9:59
 * @Description: 用于向tracer添加traceNode, 用于适配dubbo类型的应用
 */
public class TracerClient {

    public static final int TIMEOUT_IN_MILLIS = 500;
    public static final int SOCKET_TIMEOUT_MS = 10_000;
    public static final int CONNECT_TIMEOUT_MS = 3_000;

    private final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(TIMEOUT_IN_MILLIS)
            .setConnectTimeout(TIMEOUT_IN_MILLIS)
            .setSocketTimeout(TIMEOUT_IN_MILLIS)
            .build();

    private LoggerWrapper loggerWrapper;

    public TracerClient(LoggerWrapper loggerWrapper) {
        this.loggerWrapper = loggerWrapper;
    }

    /**
     * 适配dubbo应用,在rpc调用的前后,向远程tracer发送追踪请求.
     *
     * @param url
     * @param traceId
     * @param dateTimeStr     yyyy-MM-dd HH:mm:ss SSS 这个格式是为了适配dubbo.
     * @param applicationName 对应数据库表'log_collected_item'的'application_name'字段
     * @param contextBegin    true:服务被调用(代表上下文开始) false:服务主动调用(代表上下文结束)
     */
    public void aroundRPC(String url, String traceId, String dateTimeStr, String applicationName, boolean contextBegin) {
        String msg = url + " - " + traceId + " - " + dateTimeStr + " - " + applicationName;
        int beginIndex = applicationName.indexOf(":") + 3;
        int endIndex = applicationName.indexOf("?");
        applicationName = applicationName.substring(beginIndex, endIndex);
        String contextId;
        if (contextBegin) {
            contextId = loggerWrapper.contextBegin(msg);
        } else {
            contextId = loggerWrapper.contextEnd(msg);
        }
        List<NameValuePair> traceNodeParamList = buildTraceParamList(contextId, traceId, dateTimeStr, applicationName);
        loggerWrapper.info(traceNodeParamList.toString());
        try {
            sendNewTraceNodeRequest(url, traceNodeParamList);
        } catch (IOException e) {
            loggerWrapper.error(e.getMessage());
        }
    }

    private List<NameValuePair> buildTraceParamList(String contextId, String traceId, String timestamp, String applicationName) {
        List<NameValuePair> traceNodeParamList = new ArrayList<>();
        traceNodeParamList.add(new BasicNameValuePair("contextId", contextId));
        traceNodeParamList.add(new BasicNameValuePair("traceId", traceId));
        traceNodeParamList.add(new BasicNameValuePair("timestamp", timestamp));
        traceNodeParamList.add(new BasicNameValuePair("applicationName", applicationName));
        return traceNodeParamList;
    }

    /**
     * @param url                format:"http://$gatewayIp:$port/$tracerApplicationName"   eg: "http://127.0.0.1:10300/log-service-tracer"
     * @param traceNodeParamList @link cn.yunyichina.log.service.tracer.trace.TraceNode} 必备参数:[traceId,timestamp,contextId,serviceId]
     * @param handleResponseBody 是否需要处理response body
     * @param <T>
     * @return
     * @throws IOException
     */
    private <T> T sendNewTraceNodeRequest(String url, List<NameValuePair> traceNodeParamList, boolean handleResponseBody) throws IOException {
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            post.setEntity(new UrlEncodedFormEntity(traceNodeParamList, StandardCharsets.UTF_8));
            response = httpClient.execute(post);
            if (handleResponseBody) {
//            TODO 暂时默认不处理response
                return null;
            } else {
                return null;
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private <T> T sendNewTraceNodeRequest(String url, List<NameValuePair> traceNodeParamList) throws IOException {
        return sendNewTraceNodeRequest(url, traceNodeParamList, false);
    }

}
