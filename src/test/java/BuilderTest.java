import cn.yy.log.util.builder.imp.ContextIndexBuilder;
import cn.yy.log.util.builder.imp.KeyValueIndexBuilder;
import cn.yy.log.util.builder.imp.KeywordIndexBuilder;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        Map<Long, ContextIndexBuilder.IndexInfo> contextIndex = builder.build();
        Files.write(JSON.toJSONString(contextIndex), new File("D://context.index"), Charsets.UTF_8);
        System.out.println(contextIndex);
//        String json = Files.asCharSource(new File("D://context.index"), Charsets.UTF_8).read();
//        Map<Long, ContextIndexBuilder.IndexInfo> map = JSON.parseObject(json, Map.class);
//        System.out.println(map);
    }

    @Test
    public void keywordIndexBuilderTest() {
        List<String> keywordList = new ArrayList<>();
        keywordList.add("Test");
        keywordList.add("Context Begin");
        keywordList.add("Context End");
        KeywordIndexBuilder builder = new KeywordIndexBuilder(new File("D://1.txt"), keywordList);
        Map<String, List<KeywordIndexBuilder.IndexInfo>> map = builder.build();
        System.out.println(JSON.toJSONString(map, true));
    }

    @Test
    public void keyValueIndexBuilderTest() {
        List<KeyValueIndexBuilder.KvTag> kvTagList = new ArrayList<>();
        kvTagList.add(new KeyValueIndexBuilder.KvTag("Test", "\"Test\":\"", "\""));
        KeyValueIndexBuilder builder = new KeyValueIndexBuilder(kvTagList, new File("D://1.txt"));
        Map<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>> map = builder.build();
        System.out.println(JSON.toJSONString(map, true));
    }

}
