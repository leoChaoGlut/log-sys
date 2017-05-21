package cn.yunyichina.log.service.tracer.kafka;

import cn.yunyichina.log.common.ContextId;
import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import cn.yunyichina.log.service.tracer.mapper.CommonMapper;
import cn.yunyichina.log.service.tracer.mapper.TraceMapper;
import cn.yunyichina.log.service.tracer.prop.TracerConsumerProp;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/21 10:37
 * @Description:
 */
@Component
public class TraceConsumer {

    final Logger logger = LoggerFactory.getLogger(TraceConsumer.class);

    public static final int MIN_BATCH_SIZE = 1;
    public static final String TABLE_PREFIX_TRACE = "trace_";

    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private KafkaConsumer consumer;
    private FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMdd");

    private AtomicBoolean hasStart = new AtomicBoolean(false);

    @Autowired
    TracerConsumerProp tracerConsumerProp;
    @Autowired
    TraceMapper traceMapper;
    @Autowired
    CommonMapper commonMapper;

    public void createTable() {
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            commonMapper.createTable(TABLE_PREFIX_TRACE + dateFormat.format(c.getTime()));
            c.add(Calendar.DATE, 1);
        }
    }

    public void startConsumer() {
        if (!hasStart.get()) {
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    consumer = new KafkaConsumer<>(defaultProp());
                    consumer.subscribe(tracerConsumerProp.getTopicList());
                    List<ConsumerRecord<String, LinkedTraceNode>> buffer = new ArrayList<>(MIN_BATCH_SIZE << 1);
                    while (true) {
                        try {
                            ConsumerRecords<String, LinkedTraceNode> records = consumer.poll(100);
                            for (ConsumerRecord<String, LinkedTraceNode> record : records) {
                                logger.info("======" + record.offset() + "," + record.key() + "," + record.value());
                                buffer.add(record);
                            }
                            if (buffer.size() >= MIN_BATCH_SIZE) {
                                insertIntoDb(buffer);
                                consumer.commitSync();
                                logger.info("========committed size: " + buffer.size() + " ========");
                                buffer.clear();
                            }
                        } catch (Exception e) {
                            buffer.clear();
                            logger.error("kafka提交失败:" + e.getMessage());
                        }
                    }
                }
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void insertIntoDb(List<ConsumerRecord<String, LinkedTraceNode>> buffer) {
        if (CollectionUtils.isNotEmpty(buffer)) {
            ConsumerRecord<String, LinkedTraceNode> firstRecord = buffer.get(0);
            LinkedTraceNode node = firstRecord.value();
            String traceId = node.getTraceId();
            long timestamp = ContextId.getTimestamp(traceId);
            String dateStr = dateFormat.format(timestamp);
            List<LinkedTraceNode> nodeList = new ArrayList<>(buffer.size());
            for (ConsumerRecord<String, LinkedTraceNode> record : buffer) {
                nodeList.add(record.value());
            }
            traceMapper.insertListByTableNameAndList(buildTraceTableName(dateStr), nodeList);
        }
    }

    @PreDestroy
    private void preDestroy() {
        consumer.close();
    }


    public Properties defaultProp() {
        Properties props = new Properties();
        props.put("bootstrap.servers", tracerConsumerProp.getBootstrapServers());
        props.put("group.id", tracerConsumerProp.getGroupId());
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "cn.yunyichina.log.service.tracer.serialization.TraceNodeDeserialization");
        return props;
    }

    private String buildTraceTableName(String dateStr) {
        return TABLE_PREFIX_TRACE + dateStr;
    }
}
