package cn.yunyichina.log.component.searchEngine.imp;

import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.common.util.ThreadPool;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.searchEngine.AbstractSearchEngine;
import cn.yunyichina.log.component.searchEngine.SearchEngine;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

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
        ThreadPool threadPool = new ThreadPool();
        threadPool.init();
        List<Future> futureList = new ArrayList<>(10);
        for (File file : files) {
            if (file.exists()) {
                int threadNum = Runtime.getRuntime().availableProcessors();
                System.out.println("线程数：" + Runtime.getRuntime().availableProcessors());

                String logContent = Files.asCharSource(file, Charsets.UTF_8).read();

                //单线程
                Future<Set<KeywordIndexBuilder.IndexInfo>> futures = threadPool.getThreadPool().submit(new ThreadCallable(0, file, logContent, threadNum));
                futureList.add(futures);
                //多线程
//                for (int i = 0; i < threadNum; i++) {
//                    Future<KeywordIndexBuilder.IndexInfo> futures = threadPool.getThreadPool().submit(new ThreadCallable(i,file, logContent, threadNum));
//                    futureList.add(futures);
//                }

            } else {
                throw new Exception("日志文件不存在");
            }
        }
        threadPool.getThreadPool().shutdown();
        while (!threadPool.getThreadPool().isTerminated()) {

        }

        Set<KeywordIndexBuilder.IndexInfo> indexInfoSet = new HashSet<>();
        for (Future<Set<KeywordIndexBuilder.IndexInfo>> future : futureList) {
            if (future.get() == null) {

            } else {
                indexInfoSet.addAll(future.get());
            }
        }

        return indexInfoSet;
    }

    public class ThreadCallable implements Callable<Set<KeywordIndexBuilder.IndexInfo>> {

        private int cursor;
        private File file;
        private String logContent;
        private int threadNum;

        public ThreadCallable(int cursor, File file, String logContent, int threadNum) {
            this.cursor = cursor;
            this.file = file;
            this.logContent = logContent;
            this.threadNum = threadNum;
        }

        @Override
        public Set<KeywordIndexBuilder.IndexInfo> call() throws Exception {

            Set<KeywordIndexBuilder.IndexInfo> indexInfoset = new HashSet<>();

            int fileSize = logContent.length();
            int block = fileSize / threadNum;
            int i;

            //单线程
            String blockContent = logContent;
            //多线程
//            String blockContent;
//            if (cursor + 1 == threadNum) {
//                blockContent = logContent.substring(cursor * block, fileSize);
//            } else {
//                blockContent = logContent.substring(cursor * block, (cursor + 1) * block);
//            }

            for (i = 0; i < blockContent.length(); ) {
                int position = blockContent.substring(i).indexOf(keyword);
                if (position != -1) {
                    i = i + position + keyword.length();
//                    System.out.println("============="+i);

                    int contextTagBegin = i + blockContent.substring(i).indexOf(Tag.ROW_END) + Tag.ROW_END.length();
                    int contextTagEnd = i + blockContent.substring(i).indexOf(Tag.CONTEXT_COUNT_END);
//                    System.out.println(contextTagBegin+"---"+contextTagEnd);

                    String contextCount = blockContent.substring(contextTagBegin, contextTagEnd);

//                    System.out.println("--->>>>>>>>>"+contextCount);
                    KeywordIndexBuilder.IndexInfo indexInfo = new KeywordIndexBuilder.IndexInfo();
                    indexInfo.setLogFile(file);
                    indexInfo.setContextCount(Long.valueOf(contextCount));
                    indexInfoset.add(indexInfo);
                } else {
                    break;
                }
            }
            return indexInfoset;

        }
    }
}
