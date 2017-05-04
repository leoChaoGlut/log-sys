package cn.yunyichina.log.common;

import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import com.alibaba.fastjson.JSON;
import lombok.Setter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/29 9:59
 * @Description: 用于向tracer添加traceNode, 用于适配dubbo类型的应用
 */
public class TracerClient {
    private static final Logger logger = LoggerFactory.getLogger(TracerClient.class);
    private static final int TIMEOUT_IN_MILLIS = 2000;
    private static final int BATCH_SIZE = 100;

    private static ConcurrentLinkedQueue<LinkedTraceNode.DTO> traceNodeQueue = new ConcurrentLinkedQueue<>();
    private static ExecutorService threadPool;
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
            .setConnectionRequestTimeout(TIMEOUT_IN_MILLIS)
            .setConnectTimeout(TIMEOUT_IN_MILLIS)
            .setSocketTimeout(TIMEOUT_IN_MILLIS)
            .build();

    private static final BasicHeader CONTENT_TYPE_HEADER = new BasicHeader("Content-type", "application/json;charset=UTF-8");

    private LoggerWrapper loggerWrapper;
    private AtomicBoolean hasInit = new AtomicBoolean(false);
    private AtomicBoolean hasSetUrl = new AtomicBoolean(false);
    /**
     * format:"http://$gatewayIp:$port/$tracerApplicationName"   eg: "http://127.0.0.1:10300/log-service-tracer"
     */
    @Setter
    private static String url;

    static {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int maximumPoolSize = corePoolSize * 100;
        threadPool = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
        new Thread(new Runnable() {
            @Override
            public void run() {
                long ONE_MILLIS = 100_000_000L;
                while (true) {
                    if (url == null) {//说明还没有调用aroundRPC.
                        LockSupport.parkNanos(ONE_MILLIS);
                    } else {
                        consume();
                    }
                }
            }

            private void consume() {
                if (!traceNodeQueue.isEmpty()) {
                    List<LinkedTraceNode> linkedTraceNodeList = buildTraceNodeList();
                    sendRequest(linkedTraceNodeList);
                    consume();
                }
            }

            private List<LinkedTraceNode> buildTraceNodeList() {
                List<LinkedTraceNode> linkedTraceNodeList = new ArrayList<>(BATCH_SIZE);
                for (int i = 0; i < BATCH_SIZE; i++) {
                    LinkedTraceNode.DTO dto = traceNodeQueue.poll();
                    if (dto == null) {
                        break;
                    }
                    try {
                        linkedTraceNodeList.add(LinkedTraceNode.parseBy(dto));
                    } catch (ParseException e) {
                        logger.error(e.getMessage());
                    }
                }
                return linkedTraceNodeList;
            }

            private void sendRequest(final List<LinkedTraceNode> linkedTraceNodeList) {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        CloseableHttpResponse response = null;
                        try {
                            CloseableHttpClient httpClient = HttpClients.createDefault();
                            HttpPost post = new HttpPost(url);
                            post.setConfig(REQUEST_CONFIG);
                            post.setHeader(CONTENT_TYPE_HEADER);
                            post.setEntity(new StringEntity(JSON.toJSONString(linkedTraceNodeList), StandardCharsets.UTF_8));
                            response = httpClient.execute(post);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (response != null) {
                                try {
                                    response.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        }).start();
    }

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
        try {
            if (!hasSetUrl.get()) {
                this.url = url;
                hasSetUrl.set(true);
            }
            String msg = url + " - " + traceId + " - " + dateTimeStr + " - " + applicationName;
            int beginIndex = applicationName.indexOf(":") + 3;
            int endIndex = applicationName.indexOf("?");
            applicationName = applicationName.substring(beginIndex, endIndex);
            if (contextBegin) {
                String contextId = loggerWrapper.contextBegin(msg);
                LinkedTraceNode.DTO dto = new LinkedTraceNode.DTO()
                        .setTraceId(traceId)
                        .setContextId(contextId)
                        .setTimestamp(dateTimeStr)
                        .setApplicationName(applicationName);
                traceNodeQueue.add(dto);
            } else {
                loggerWrapper.contextEnd(msg);
            }
        } catch (Exception e) {
//            ignore all exception
//            loggerWrapper.error(e.getMessage(), e);
        }
    }

}
