package cn.yunyichina.log.service.frontEnd.entity.do_;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/4 11:32
 * @Description:
 */
public class KeywordDo {
    private Integer id;
    private String keyword;

    public Integer getId() {
        return id;
    }

    public KeywordDo setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public KeywordDo setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
}
