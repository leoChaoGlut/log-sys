package cn.yunyichina.log.service.collector.service;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
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
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

    private HashSet<String> sessionIdSet = new HashSet<>();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();

    @Autowired
    CacheService cacheService;

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
            br = new BufferedReader(new InputStreamReader(fis));
            br.skip(fis.available());
            String tailLine;
            while (true) {
                if (sessionIdSet.contains(sessionId)) {
                    tailLine = br.readLine();
                    if (tailLine == null) {

                    } else {
                        logger.info(tailLine);
                        session.sendMessage(new TextMessage(tailLine));
                    }
                    TimeUnit.MILLISECONDS.sleep(INTERVAL_MS);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        writeLock.lock();
        try {
            sessionIdSet.add(sessionId);
        } finally {
            writeLock.unlock();
        }
    }


    public void removeSession(String sessionId) {
        writeLock.lock();
        try {
            sessionIdSet.remove(sessionId);
        } finally {
            writeLock.unlock();
        }
    }
}
