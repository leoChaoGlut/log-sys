package cn.yunyichina.log.service.collector.handler;

import cn.yunyichina.log.service.collector.service.RealtimeLogService;
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
 * @CreateTime: 2017/3/6 10:25
 * @Description:
 */
public class RealtimeLogHandler extends AbstractWebSocketHandler {

    final Logger logger = LoggerFactory.getLogger(RealtimeLogHandler.class);

    @Autowired
    RealtimeLogService realtimeLogService;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Integer collectedItemId = Integer.valueOf(message.getPayload().toString());
        logger.info(collectedItemId + "");
        realtimeLogService.tail(collectedItemId, session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info(session.getId());
        realtimeLogService.removeSession(session.getId());
    }

}
