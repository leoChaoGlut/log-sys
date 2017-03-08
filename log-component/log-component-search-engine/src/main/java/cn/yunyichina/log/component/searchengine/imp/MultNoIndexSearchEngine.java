package cn.yunyichina.log.component.searchengine.imp;

import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.searchengine.AbstractSearchEngine;
import cn.yunyichina.log.component.searchengine.SearchEngine;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Jonven on 2017/03/01.
 */
@Deprecated
public class MultNoIndexSearchEngine extends AbstractSearchEngine implements SearchEngine<Set<ContextInfo>> {

    private String keyword;
    private Collection<File> logs;
    private ConcurrentHashMap<Long, ContextInfo> contextInfoMap;

    public MultNoIndexSearchEngine(Collection<File> logs, ConcurrentHashMap<Long, ContextInfo> contextInfoMap, String keyword) {
        this.keyword = keyword;
        this.contextInfoMap = contextInfoMap;
        this.logs = logs;
    }

    @Override
    public Set<ContextInfo> search() throws Exception {
        if (logs == null || logs.isEmpty()) {
            return new HashSet<>();
        } else {
            int cpuCount = Runtime.getRuntime().availableProcessors();
            int totalLogSize = logs.size();
            int subLogSize = totalLogSize / cpuCount;
            int remainLogSize = totalLogSize % cpuCount;
            int beginIndex = 0;
            int resizedSubLogSize;

            ExecutorService threadPool = Executors.newFixedThreadPool(cpuCount);

            List<Future> futureList = new ArrayList<>(cpuCount);
            List<Task> taskList = new ArrayList<>(cpuCount);
            List<File> logList = new ArrayList<>(logs);

            for (int i = 0; i < cpuCount; i++) {
                if (i < remainLogSize) {//将余数均匀分配给其它线程
                    resizedSubLogSize = subLogSize + 1;
                } else {
                    resizedSubLogSize = subLogSize;
                }

                int endIndex = beginIndex + resizedSubLogSize;
                List<File> subLogList = logList.subList(beginIndex, endIndex);
                beginIndex = endIndex;
                Task task = new Task(subLogList);
                taskList.add(task);
            }

            for (Task task : taskList) {
                Future<Set<ContextInfo>> future = threadPool.submit(task);
                futureList.add(future);
            }
            threadPool.shutdown();
            while (!threadPool.isTerminated()) {

            }

            Set<ContextInfo> contextInfoSet = new HashSet<>();
            for (Future<Set<ContextInfo>> future : futureList) {
                Set<ContextInfo> contextInfoFutureSet = future.get();
                if (contextInfoFutureSet == null) {

                } else {
                    contextInfoSet.addAll(contextInfoFutureSet);
                }
            }
            return contextInfoSet;
        }
    }

    public class Task implements Callable<Set<ContextInfo>> {

        private Collection<File> logs;

        public Task(Collection<File> logs) {
            this.logs = logs;
        }

        @Override
        public Set<ContextInfo> call() throws Exception {
            Set<ContextInfo> contextInfoSet = new NoIndexSearchEngine(logs, contextInfoMap, keyword).search();
            return contextInfoSet;
        }
    }

}
