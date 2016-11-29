package cn.yunyichina.log.component.aggregator.util;

import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeyValueIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jonven on 2016/11/26.
 */
public class AggregatorUtil {

    private static String CONTEXT_INDEX_DIR = "E:\\zTest\\index\\context.index";
    private static String KEY_WORD_INDEX_DIR = "E:\\zTest\\index\\keyWord.index";
    private static String KEY_VALUE_INDEX_DIR = "E:\\zTest\\index\\keyValue.index";
    private static String BASE_DIR = "E:\\zTest\\index\\";

    static {
        File file = new File(BASE_DIR);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 获取所有索引，包括(context索引,keyword索引,keyvalue索引)
     * @param files
     * @return
     */
    public static File[] getAllIndex(File[] files){
        /********************聚合索引 contextIndex******************************/
        ContextIndexAggregator contextAggregator = new ContextIndexAggregator();
        for (File f : files) {
            ContextIndexBuilder builder = new ContextIndexBuilder(f);
            Map<Long, ContextIndexBuilder.ContextInfo> map = builder.build();
            contextAggregator.aggregate(map);
        }
        Map<Long, ContextIndexBuilder.ContextInfo> contextAggregatedCollection = contextAggregator.getAggregatedCollection();
        try {
            Files.write(JSON.toJSONString(contextAggregatedCollection), new File(CONTEXT_INDEX_DIR), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /***********************************************************************/

        /********************聚合索引 keywordIndex******************************/
        Set<String> keywordSet = new HashSet<>();

        keywordSet.add("pat_card_no");
        keywordSet.add("将要返回给平台的Response As Follow");
        keywordSet.add("patCardNo");
        keywordSet.add("patCardNo1");

        KeywordIndexAggregator keyWordAggregator = new KeywordIndexAggregator();

        for (File file : files) {
            KeywordIndexBuilder builder = new KeywordIndexBuilder(file, keywordSet);
            Map<String, Set<KeywordIndexBuilder.IndexInfo>> map = builder.build();
            keyWordAggregator.aggregate(map);
        }

        Map<String, Set<KeywordIndexBuilder.IndexInfo>> keyWordAggregatedCollection = keyWordAggregator.getAggregatedCollection();

        try {
            Files.write(JSON.toJSONString(keyWordAggregatedCollection),new File(KEY_WORD_INDEX_DIR), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /***********************************************************************/

        /********************聚合索引 keyValueIndex******************************/
        KeyValueIndexAggregator keyValueAggregator = new KeyValueIndexAggregator();

        Set<KeyValueIndexBuilder.KvTag> kvTagSet = new HashSet<>();

        kvTagSet.add(new KeyValueIndexBuilder.KvTag("patCardNo", "<patCardNo>", "</patCardNo>"));
        kvTagSet.add(new KeyValueIndexBuilder.KvTag("sign", "\"sign\":\"", "\""));
        kvTagSet.add(new KeyValueIndexBuilder.KvTag("pat_mobile", "\"pat_mobile\":\"", "\""));

        for (File file : files) {
            KeyValueIndexBuilder builder = new KeyValueIndexBuilder(kvTagSet, file);
            Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> map = builder.build();
            keyValueAggregator.aggregate(map);
        }

        Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueAggregatedCollection = keyValueAggregator.getAggregatedCollection();
        try {
            // TODO: 2016/11/28 ObjectoutputStream
            Files.write(JSON.toJSONString(keyValueAggregatedCollection), new File(KEY_VALUE_INDEX_DIR), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /***********************************************************************/

        File baseFile = new File(BASE_DIR);
        if (!baseFile.exists()){
            baseFile.mkdirs();
        }
        return baseFile.listFiles();
    }

}
