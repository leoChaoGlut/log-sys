package cn.yunyichina.log.service.collector.util;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.dto.SearchConditionDTO;
import cn.yunyichina.log.common.exception.CollectorException;
import cn.yunyichina.log.component.scanner.imp.LogScanner;
import cn.yunyichina.log.service.collector.listener.HistoryLogListener;
import cn.yunyichina.log.service.collector.service.CacheService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/8 18:04
 * @Description:
 */
@Component
public class HistoryLogManager {
    /**
     * key: session id
     */
    private Map<String, HistoryLogListener> listenerMap = new HashMap<>();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();

    final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");

    @Autowired
    CacheService cacheService;

    public void loadMore(WebSocketSession session, String conditionJSON) {
        try {
            HistoryLogListener historyLogListener = getListenerBy(session.getId());
            if (historyLogListener == null) {
                SearchConditionDTO condition = JSON.parseObject(conditionJSON, SearchConditionDTO.class);
                Collection<File> logs = scanLogsBy(condition);
                initAndStartListenerBy(session, logs);
            } else {
                historyLogListener.sendMsg();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                session.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    private void initAndStartListenerBy(WebSocketSession session, Collection<File> logs) throws IOException {
        HistoryLogListener historyLogListener = new HistoryLogListener(logs, session);
        writeLock.lock();
        try {
            listenerMap.put(session.getId(), historyLogListener);
        } finally {
            writeLock.unlock();
        }
        historyLogListener.sendMsg();
    }

    private HistoryLogListener getListenerBy(String sessionId) {
        readLock.lock();
        try {
            return listenerMap.get(sessionId);
        } finally {
            readLock.unlock();
        }
    }

    private Collection<File> scanLogsBy(SearchConditionDTO condition) throws CollectorException {
        CollectedItemDO collectedItem = cacheService.getCollectedItemBy(condition.getCollectedItemId());
        String beginDatetime = dateFormat.format(condition.getBeginDateTime());
        String endDatetime = dateFormat.format(condition.getEndDateTime());
        Collection<File> logs = LogScanner.scan(beginDatetime, endDatetime, collectedItem.getCollectedLogDir()).values();
        if (CollectionUtils.isEmpty(logs)) {
            throw new CollectorException("[" + beginDatetime + "," + endDatetime + "] 区间内没有日志");
        } else {
            return logs;
        }
    }

    public void removeListenerBy(String sessionId) {
        writeLock.lock();
        try {
            HistoryLogListener listener = listenerMap.remove(sessionId);
            listener = null;//help gc
        } finally {
            writeLock.unlock();
        }
    }
}
