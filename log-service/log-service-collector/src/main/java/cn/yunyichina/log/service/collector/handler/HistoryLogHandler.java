package cn.yunyichina.log.service.collector.handler;

import cn.yunyichina.log.service.collector.util.HistoryLogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/8 15:10
 * @Description:
 */
public class HistoryLogHandler extends AbstractWebSocketHandler {

    final Logger logger = LoggerFactory.getLogger(HistoryLogHandler.class);

    @Autowired
    HistoryLogManager historyLogManager;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String conditionJSON = message.getPayload().toString();
        logger.info(conditionJSON);
        historyLogManager.loadMore(session, conditionJSON);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info(session.getId());
        historyLogManager.removeListenerBy(session.getId());
    }
}
