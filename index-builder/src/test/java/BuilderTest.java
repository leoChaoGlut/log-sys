import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeywordIndexBuilder;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/10/28 14:16
 * @Description:
 */
public class BuilderTest {

    @Test
    public void contextIndexBuilderTest() throws IOException {
        ContextIndexBuilder builder = new ContextIndexBuilder(new File("D://1.txt"));
        Map<Long, ContextIndexBuilder.ContextInfo> map = builder.build();
        Files.write(JSON.toJSONString(map), new File("D://context.index"), Charsets.UTF_8);
        System.out.println(JSON.toJSONString(map, true));
//        String json = Files.asCharSource(new File("D://context.index"), Charsets.UTF_8).read();
//        Map<Long, ContextIndexBuilder.IndexInfo> map = JSON.parseObject(json, Map.class);
//        System.out.println(map);
//        System.out.println(Files.asCharSource(new File("D://1.txt"), Charsets.UTF_8).read().substring(2407, 3070));
    }

    @Test
    public void keywordIndexBuilderTest() {
        Set<String> keywordList = new HashSet<>();
        keywordList.add("Test");
        keywordList.add("Context Begin");
        keywordList.add("Context End");
        KeywordIndexBuilder builder = new KeywordIndexBuilder(new File("D://1.txt"), keywordList);
        Map<String, Set<KeywordIndexBuilder.IndexInfo>> map = builder.build();
        System.out.println(JSON.toJSONString(map, true));
    }

    @Test
    public void keyValueIndexBuilderTest() {
        Set<KeyValueIndexBuilder.KvTag> kvTagList = new HashSet<>();
        kvTagList.add(new KeyValueIndexBuilder.KvTag("Test", "\"Test\":\"", "\""));
        KeyValueIndexBuilder builder = new KeyValueIndexBuilder(kvTagList, new File("D://1.txt"));
        Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> map = builder.build();
        System.out.println(JSON.toJSONString(map, true));
    }

}
