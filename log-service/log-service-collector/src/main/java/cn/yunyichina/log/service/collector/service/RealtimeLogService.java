package cn.yunyichina.log.service.collector.service;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.service.collector.util.SessionCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/6 18:25
 * @Description:
 */
@Service
public class RealtimeLogService {

    final Logger logger = LoggerFactory.getLogger(RealtimeLogService.class);

    public static final int INTERVAL_MS = 100;

    private ReentrantLock lock = new ReentrantLock();

    @Autowired
    CacheService cacheService;

    @Autowired
    SessionCacheManager sessionCacheManager;

    /**
     * impl tail -f
     * 该方法每读一行,推一次消息到客户端,可以优化为:缓存100行数据,如果1秒内没有变动,就推送
     *
     * @param collectedItemId
     * @param session
     */
    @Async
    public void tail(Integer collectedItemId, WebSocketSession session) {
        String sessionId = session.getId();
        addSession(sessionId);
        BufferedReader br = null;
        try {
            String stdoutFilePath = getStdoutFilePathBy(collectedItemId);
            FileInputStream fis = new FileInputStream(stdoutFilePath);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(stdoutFilePath), StandardCharsets.UTF_8));
            br.skip(fis.available());
            String tailLine;
            while (true) {
                if (sessionCacheManager.containsKey(sessionId)) {
                    tailLine = br.readLine();
                    if (tailLine != null) {
                        logger.info(tailLine.length() + "");
                        session.sendMessage(new TextMessage(tailLine));
                    }
                    TimeUnit.MILLISECONDS.sleep(INTERVAL_MS);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Exception occured when tailing", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getStdoutFilePathBy(int collectedItemId) {
        CollectedItemDO collectedItem = cacheService.getCollectedItemBy(collectedItemId);
        return collectedItem.getStdoutFilePath();
    }

    private void addSession(String sessionId) {
        lock.lock();
        try {
            sessionCacheManager.put(sessionId, sessionId);
        } finally {
            lock.unlock();
        }
    }


    public void removeSession(String sessionId) {
        lock.lock();
        try {
            sessionCacheManager.remove(sessionId);
        } finally {
            lock.unlock();
        }
    }
}
