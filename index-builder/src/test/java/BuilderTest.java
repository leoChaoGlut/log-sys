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
        ContextIndexBuilder builder = new ContextIndexBuilder(new File("D:\\tmp\\2016\\11\\15\\14\\25\\201611151425.log"));
        Map<Long, ContextIndexBuilder.ContextInfo> map = builder.build();
        Files.write(JSON.toJSONString(map), new File("D://context.index"), Charsets.UTF_8);
        System.out.println(JSON.toJSONString(map, true));
    }

    @Test
    public void keywordIndexBuilderTest() {
        Set<String> keywordList = new HashSet<>();
        keywordList.add("<branchCode>");
        keywordList.add("patName");
        keywordList.add("pat_mobile");
        KeywordIndexBuilder builder = new KeywordIndexBuilder(new File("D:\\tmp\\2016\\11\\15\\14\\25\\201611151425.log"), keywordList);
        Map<String, Set<KeywordIndexBuilder.IndexInfo>> map = builder.build();
        System.out.println(JSON.toJSONString(map, true));
    }

    @Test
    public void keyValueIndexBuilderTest() {
        Set<KeyValueIndexBuilder.KvTag> kvTagList = new HashSet<>();
        kvTagList.add(new KeyValueIndexBuilder.KvTag("patCardNo", "<patCardNo>", "</patCardNo>"));
        kvTagList.add(new KeyValueIndexBuilder.KvTag("patCardNo", "<patCardNo>", "</patCardNo>"));
        KeyValueIndexBuilder builder = new KeyValueIndexBuilder(kvTagList, new File("D:\\tmp\\2016\\11\\15\\14\\25\\201611151425.log"));
        Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> map = builder.build();
        System.out.println(JSON.toJSONString(map, true));
    }

}
