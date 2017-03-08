package cn.yunyichina.log.service.collector.listener;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/8 18:25
 * @Description:
 */
public class HistoryLogListener {

    private File[] logs;
    private int logSize = 0;
    private int logIndex = 0;
    private int segmentIndex = 0;
    private int segmentCount = 0;
    private String logContent;
    private WebSocketSession session;

    private final String SEPARATOR = "========↓↓ %s ↓↓========\n";
    private final String END_TAG = "\n======================= 结束 =======================";
    private final int SEGMENT_SIZE = 2000;
    private final File[] FILE = new File[0];

    public HistoryLogListener() {
    }

    public HistoryLogListener(Collection<File> logs, WebSocketSession session) throws IOException {
        this.logs = logs.toArray(FILE);
        this.logSize = logs.size();
        this.session = session;
        initDataForNextRound();
    }

    private void initDataForNextRound() throws IOException {
        if (logIndex < logSize) {
            logContent = Files.toString(this.logs[logIndex], Charsets.UTF_8);
            logIndex++;
            segmentCount = logContent.length() / SEGMENT_SIZE;
            segmentIndex = 0;
        } else {
            session.sendMessage(new TextMessage(END_TAG));
            session.close();
        }
    }

    public void sendMsg() throws IOException {
        if (segmentCount == 0) {
            String separator = String.format(SEPARATOR, logs[logIndex].getName());
            session.sendMessage(new TextMessage(separator + logContent));
            initDataForNextRound();
        } else {
            if (segmentIndex < segmentCount) {
                if (segmentIndex == 0) {
                    String separator = String.format(SEPARATOR, logs[logIndex].getName());
                    session.sendMessage(new TextMessage(separator + logContent.substring(segmentIndex * SEGMENT_SIZE, (segmentIndex + 1) * SEGMENT_SIZE)));
                } else {
                    session.sendMessage(new TextMessage(logContent.substring(segmentIndex * SEGMENT_SIZE, (segmentIndex + 1) * SEGMENT_SIZE)));
                }
                segmentIndex++;
            } else {
                session.sendMessage(new TextMessage(logContent.substring(segmentIndex * SEGMENT_SIZE)));
                initDataForNextRound();
            }
        }
    }

    public boolean isFinished() {
        return logIndex == logSize;
    }

}
