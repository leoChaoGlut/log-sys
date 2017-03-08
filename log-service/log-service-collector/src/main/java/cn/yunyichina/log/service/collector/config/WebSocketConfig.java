package cn.yunyichina.log.service.collector.config;

import cn.yunyichina.log.service.collector.handler.HistoryLogHandler;
import cn.yunyichina.log.service.collector.handler.RealtimeLogHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/6 14:44
 * @Description:
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public RealtimeLogHandler getRealtimeLogHandler() {
        return new RealtimeLogHandler();
    }

    @Bean
    public HistoryLogHandler getHistoryLogHandler() {
        return new HistoryLogHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getRealtimeLogHandler(), "/realtime")
                .addHandler(getHistoryLogHandler(), "/history")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
