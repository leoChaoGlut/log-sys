package cn.yunyichina.log.component.searchEngine.imp;

import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.searchEngine.AbstractSearchEngine;
import cn.yunyichina.log.component.searchEngine.SearchEngine;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 15:43
 * @Description:
 */
public class NoIndexSearchEngine extends AbstractSearchEngine implements SearchEngine<Set<KeywordIndexBuilder.IndexInfo>> {

    private String keyword;
    private Collection<File> files;

    public NoIndexSearchEngine(Collection<File> files, String keyword) {
        this.keyword = keyword;
        this.files = files;
    }

    @Override
    public Set<KeywordIndexBuilder.IndexInfo> search() throws Exception {
        Set<KeywordIndexBuilder.IndexInfo> indexInfoSet = new HashSet<>();
        for (File file : files) {
            if (file.exists()) {
                String logContent = Files.asCharSource(file, Charsets.UTF_8).read();

                int keywordTagIndex = 0;
                while (0 <= (keywordTagIndex = logContent.indexOf(keyword, keywordTagIndex))) {
                    int rowEndTagIndex = logContent.indexOf(Tag.ROW_END, keywordTagIndex + keyword.length());
                    int contextCountBeginTagIndex = rowEndTagIndex + Tag.ROW_END.length();
                    int contextCountEndTagIndex = logContent.indexOf(Tag.CONTEXT_COUNT_END, contextCountBeginTagIndex);
                    String count = logContent.substring(contextCountBeginTagIndex, contextCountEndTagIndex);
                    KeywordIndexBuilder.IndexInfo indexInfo = new KeywordIndexBuilder.IndexInfo(file, keywordTagIndex, Long.valueOf(count));
                    keywordTagIndex = contextCountEndTagIndex;
                    indexInfoSet.add(indexInfo);
                }
            }
        }

        return indexInfoSet;
    }

}
