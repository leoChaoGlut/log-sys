package cn.yunyichina.log.search.builder.imp;

import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.index.util.LogFileScanner;
import cn.yunyichina.log.search.builder.SearchBuilder;
import cn.yunyichina.log.search.constant.ResultMessage;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonven on 2016/11/14.
 */
public class KeyWordSearchBuilder implements SearchBuilder{

    private String keyWord;
    private Map<String, List<KeywordIndexBuilder.IndexInfo>> keyWordMap;
    private Map<Long, ContextIndexBuilder.ContextInfo> contextMap;

    public KeyWordSearchBuilder(String keyWord, Map<String, List<KeywordIndexBuilder.IndexInfo>> keyWordMap, Map<Long, ContextIndexBuilder.ContextInfo> contextMap) {
        this.keyWord = keyWord;
        this.keyWordMap = keyWordMap;
        this.contextMap = contextMap;
    }

    @Override
    public Object builder() throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder messageBuilder = new StringBuilder();

        //过去关键词所在的关键词索引表的索引
        List<KeywordIndexBuilder.IndexInfo> keyWordLists = keyWordMap.get(keyWord);
        //上下文一致的去重map
        Map<Long,ContextIndexBuilder.ContextInfo> toRepeatMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(keyWordLists)){
            //遍历关键词索引对应的关键词的list集合
            for (KeywordIndexBuilder.IndexInfo indexInfo:keyWordLists) {
                Long contextCount = indexInfo.getContextCount();
                //为了去重复（上下文contextCount）
                if(!toRepeatMap.containsKey(contextCount)){
                    ContextIndexBuilder.ContextInfo contextInfo = contextMap.get(contextCount);
                    toRepeatMap.put(contextCount,contextInfo);

                    if(contextInfo != null){
                        ContextIndexBuilder.IndexInfo begin = contextInfo.getBegin();
                        ContextIndexBuilder.IndexInfo end = contextInfo.getEnd();
                        File beginLogFile = begin.getLogFile();
                        File endLogFile = end.getLogFile();
                        LogFileScanner scanner = new LogFileScanner(beginLogFile,endLogFile,"E:\\testLog");
                        Map<String, File> fileMaps = scanner.scan();

                        if(fileMaps.size() != 0){
                            if(fileMaps.size() == 1){
                                stringBuilder.append(Files.asCharSource(begin.getLogFile(), Charsets.UTF_8)
                                        .read().substring(begin.getIndexOfLogFile(),end.getIndexOfLogFile()));
                            }else{
                                int count = 0;
                                for (Map.Entry<String, File> fileMap:fileMaps.entrySet()) {
                                    if(count == 0){
                                        stringBuilder.append(Files.asCharSource(fileMap.getValue(), Charsets.UTF_8)
                                                .read().substring(begin.getIndexOfLogFile()))
                                                .append("\n");
                                    }else if(count == fileMaps.size()-1){
                                        stringBuilder.append(Files.asCharSource(fileMap.getValue(), Charsets.UTF_8)
                                                .read().substring(0,end.getIndexOfLogFile()))
                                                .append("\n");
                                    }else{
                                        stringBuilder.append(Files.asCharSource(fileMap.getValue(), Charsets.UTF_8))
                                                .append("\n");
                                    }
                                    count++;
                                }
                            }
                        }else{
                            messageBuilder.append(ResultMessage.RESULT_IS_NULL);
                        }
                    }else{
                        messageBuilder.append(ResultMessage.RESULT_IS_NULL);
                    }
                }
            }
        }else{
            messageBuilder.append(ResultMessage.RESULT_IS_NULL);
        }


        return stringBuilder;
    }
}
