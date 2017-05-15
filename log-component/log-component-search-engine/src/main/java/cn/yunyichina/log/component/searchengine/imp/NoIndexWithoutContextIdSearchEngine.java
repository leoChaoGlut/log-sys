package cn.yunyichina.log.component.searchengine.imp;

import cn.yunyichina.log.component.searchengine.AbstractSearchEngine;
import cn.yunyichina.log.component.searchengine.SearchEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Csy on 2017/5/8 0008.
 */
public class NoIndexWithoutContextIdSearchEngine extends AbstractSearchEngine implements SearchEngine<Set<String>> {

    final Logger logger = LoggerFactory.getLogger(NoIndexWithoutContextIdSearchEngine.class);

    private String keyword;
    private Collection<File> logs;
    private static final String CMD_TMPL = "find %s" +
            "|xargs grep %s ";

    public NoIndexWithoutContextIdSearchEngine(Collection<File> logs, String keyword) {
        this.keyword = keyword;
        this.logs = logs;
    }

    @Override
    public Set<String> search() throws Exception {
        logger.info("不基于ContextId的No Index 搜索开始");
        long begin = System.nanoTime();
        Set<String> result = new HashSet<>();
        try {
            for (File log : logs) {
                if (log.exists()) {
                    String cmd = String.format(CMD_TMPL, log.getAbsolutePath().replace(File.separator, "/"), keyword);
                    ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", cmd);
                    Process process = processBuilder.start();
                    InputStream inputStream = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while (line != null) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        result.add(line);
                    }
                }
            }
            return result;
        } finally {
            logger.info("不基于ContextId的No Index 搜索结束,耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9));
        }
    }
}
