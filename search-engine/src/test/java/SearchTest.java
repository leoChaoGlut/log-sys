import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.search.builder.SearchBuilder;
import cn.yunyichina.log.search.builder.imp.KeyWordSearchBuilder;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * Created by Jonven on 2016/11/14.
 */
public class SearchTest {

    @Test
    public void contextIndexBuilderTest() throws IOException {
        ContextIndexBuilder builder = new ContextIndexBuilder(new File("E://testLog/2.txt"));
        Map<Long, ContextIndexBuilder.ContextInfo> map = builder.build();
        Map<Long, ContextIndexBuilder.ContextInfo> mapNext = new ContextIndexBuilder(new File("E://testLog/3.txt")).build();
        System.out.println("2222222222===="+toJSONString(map, true));
        for (Map.Entry<Long, ContextIndexBuilder.ContextInfo> entry:mapNext.entrySet()){
            map.get(entry.getKey()).setEnd(entry.getValue().getEnd());
        }
        System.out.println("2222222222===="+toJSONString(map, true));
    }

    @Test
    public void keywordIndexBuilderTest() {
        List<String> keywordList = new ArrayList<>();
        keywordList.add("zhou");
        KeywordIndexBuilder builder = new KeywordIndexBuilder(new File("E://testLog/2.txt"), keywordList);
        Map<String, List<KeywordIndexBuilder.IndexInfo>> map = builder.build();
        Map<String, List<KeywordIndexBuilder.IndexInfo>> mapNext = new KeywordIndexBuilder(new File("E://testLog/3.txt"), keywordList).build();
        System.out.println("2222222222===="+toJSONString(map, true));
        for (Map.Entry<String, List<KeywordIndexBuilder.IndexInfo>> entry:mapNext.entrySet()) {
            map.get(entry.getKey()).addAll(entry.getValue());
        }
        System.out.println("3333333333===="+toJSONString(map, true));
    }

    @Test
    public void keyValueIndexBuilderTest() {
        List<KeyValueIndexBuilder.KvTag> kvTagList = new ArrayList<>();
        kvTagList.add(new KeyValueIndexBuilder.KvTag("Test", "\"Test\":\"", "\""));
        KeyValueIndexBuilder builder = new KeyValueIndexBuilder(kvTagList, new File("E://1.txt"));
        Map<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>> map = builder.build();
        System.out.println(toJSONString(map, true));
    }

    @Test
    public void searchForKeyWordTest() throws IOException {
        String testFileName1 = "E://testLog/2016/11/14/15/35/201611141535.log";
        String testFileName2 = "E://testLog/2016/11/14/15/36/201611141536.log";

        //定义测试的搜索关键词
        String keyWord = "zhou";

        /********************构造关键词索引*****************************/
        List<String> keywordList = new ArrayList<>();
        keywordList.add(keyWord);
        KeywordIndexBuilder keywordBuilder = new KeywordIndexBuilder(new File(testFileName1), keywordList);
        Map<String, List<KeywordIndexBuilder.IndexInfo>> keyWordMap = keywordBuilder.build();

        Map<String, List<KeywordIndexBuilder.IndexInfo>> keyWordMapNext = new KeywordIndexBuilder(new File(testFileName2), keywordList).build();

        for (Map.Entry<String, List<KeywordIndexBuilder.IndexInfo>> entry:keyWordMapNext.entrySet()) {
            if(keyWordMap.get(entry.getKey())!=null){
                keyWordMap.get(entry.getKey()).addAll(entry.getValue());
            }else{
                keyWordMap.put(entry.getKey(),entry.getValue());
            }

        }
//        System.err.println("keyWordMap========="+JSON.toJSONString(keyWordMap,true));
        /**************************************************************/

        /********************构造上下文索引*****************************/
        ContextIndexBuilder ContextBuilder = new ContextIndexBuilder(new File(testFileName1));
        Map<Long, ContextIndexBuilder.ContextInfo> contextMap = ContextBuilder.build();
        Map<Long, ContextIndexBuilder.ContextInfo> contextMapNext = new ContextIndexBuilder(new File(testFileName2)).build();

        for (Map.Entry<Long, ContextIndexBuilder.ContextInfo> entry:contextMapNext.entrySet()){
            if(contextMap.get(entry.getKey()) != null){
                contextMap.get(entry.getKey()).setEnd(entry.getValue().getEnd());
            }else{
                contextMap.put(entry.getKey(),entry.getValue());
            }

        }
//        System.err.println("contextMap========="+JSON.toJSONString(contextMap,true));
        /**************************************************************/

        SearchBuilder searchBuilder = new KeyWordSearchBuilder(keyWord,keyWordMap,contextMap);
        System.err.println(searchBuilder.builder());
    }

    @Test
    public void getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmm");
        Date date = new Date();
        System.out.println(sdf.format(date));
    }

}
