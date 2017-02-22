package cn.yunyichina.log.common.entity.entity.do_;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/22 15:15
 * @Description:
 */
public class KeywordTagDO {
    private Integer id;
    private String keyword;

    public Integer getId() {
        return id;
    }

    public KeywordTagDO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public KeywordTagDO setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
}
