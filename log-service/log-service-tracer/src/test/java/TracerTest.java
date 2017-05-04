import cn.yunyichina.log.component.index.entity.ContextIndex;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

}
