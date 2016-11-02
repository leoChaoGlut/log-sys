package cn.yy.log.entity.vo;

import java.util.Map;
import java.util.TreeSet;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/1 18:07
 * @Description: 现在暂时把 normalIndexMap 和 accuratedIndexMap 放在了一起.可以拆开,拆开会更快一点点.但是会增加代码复杂度.
 */
public class LogIndex {
    private Map<String, TreeSet<Integer>> normalIndexMap;
    private Map<String, Map<String, TreeSet<Integer>>> accuratedIndexMap;

    public LogIndex() {
    }

    public LogIndex(Map<String, TreeSet<Integer>> normalIndexMap, Map<String, Map<String, TreeSet<Integer>>> accuratedIndexMap) {
        this.normalIndexMap = normalIndexMap;
        this.accuratedIndexMap = accuratedIndexMap;
    }

    public Map<String, TreeSet<Integer>> getNormalIndexMap() {
        return normalIndexMap;
    }

    public LogIndex setNormalIndexMap(Map<String, TreeSet<Integer>> normalIndexMap) {
        this.normalIndexMap = normalIndexMap;
        return this;
    }

    public Map<String, Map<String, TreeSet<Integer>>> getAccuratedIndexMap() {
        return accuratedIndexMap;
    }

    public LogIndex setAccuratedIndexMap(Map<String, Map<String, TreeSet<Integer>>> accuratedIndexMap) {
        this.accuratedIndexMap = accuratedIndexMap;
        return this;
    }
}
