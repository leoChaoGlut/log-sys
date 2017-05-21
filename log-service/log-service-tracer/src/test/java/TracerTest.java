import cn.yunyichina.log.common.ContextId;
import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import cn.yunyichina.log.component.index.entity.ContextIndex;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.File;
import java.util.*;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 16:46
 * @Description:
 */
public class TracerTest {

    @Test
    public void test1() {
        String ctxId = "ctxId";
        ContextInfo contextInfo = new ContextInfo()
                .setContextId(ctxId)
                .setBegin(new ContextIndex(new File("D://1"), 0))
                .setEnd(new ContextIndex(new File("D://2"), 0));
        Map<String, ContextInfo> map = new HashMap<>();
        map.put(ctxId, contextInfo);
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void test0() {
        JedisCluster jc = new JedisCluster(new HostAndPort("192.168.1.173", 7001));
        String v = jc.set("k", "v");
        System.out.println();
        String k = jc.get("k");
        System.out.println(k);
    }

    @Test
    public void producer() throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.159:9000,192.168.1.159:9001,192.168.1.159:9002");
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "cn.yunyichina.log.service.tracer.serialization.TraceNodeSerialization");
        Producer<String, LinkedTraceNode> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            LinkedTraceNode node = new LinkedTraceNode()
                    .setContextId(ContextId.getStr())
                    .setTraceId(ContextId.getStr());
            producer.send(new ProducerRecord<String, LinkedTraceNode>("topic-trace", node));
//            TimeUnit.MILLISECONDS.sleep(100);
        }
        producer.close();
    }


    @Test
    public void test3() {
        String str = ContextId.getStr();
        System.out.println(str);
    }

    @Test
    public void consumer() {
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "192.168.1.159:9000,192.168.1.159:9002,192.168.1.159:9002");
        props.put(GROUP_ID_CONFIG, "group-trace2");
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, "cn.yunyichina.log.service.tracer.serialization.TraceNodeDeserialization");
        KafkaConsumer consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("topic-trace"));
        int MIN_BATCH_SIZE = 5;
        List<ConsumerRecord<String, LinkedTraceNode>> buffer = new ArrayList<>(MIN_BATCH_SIZE << 1);
        while (true) {
            try {
                ConsumerRecords<String, LinkedTraceNode> records = consumer.poll(100);
                for (ConsumerRecord<String, LinkedTraceNode> record : records) {
                    System.out.println("======" + record.offset() + "," + record.key() + "," + record.value());
                    buffer.add(record);
                }
//                if (buffer.size() >= MIN_BATCH_SIZE) {
////                    insertIntoDb(buffer);
//                    consumer.commitSync();
////                    logger.info("========committed========");
//                    buffer.clear();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test5() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.159:9000,192.168.1.159:9002,192.168.1.159:9002");
        props.put("group.id", "group-trace1");
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "cn.yunyichina.log.service.tracer.serialization.TraceNodeDeserialization");
        KafkaConsumer<String, LinkedTraceNode> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("topic-trace"));
        while (true) {
            ConsumerRecords<String, LinkedTraceNode> records = consumer.poll(100);
            for (ConsumerRecord<String, LinkedTraceNode> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }

    @Test
    public void test6() {
        System.out.println(new Date().getTime());
    }

}
