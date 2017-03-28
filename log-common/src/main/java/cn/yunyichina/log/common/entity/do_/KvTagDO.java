package cn.yunyichina.log.common.entity.do_;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/27 18:41
 * @Description:
 */
@Setter
@Getter
public class KvTagDO implements Serializable {
    private static final long serialVersionUID = -7840708503963386116L;

    private Integer id;
    private String key;
    private String keyTag;
    private String valueEndTag;
    //    extra
    private int keyOffset;


    public KvTagDO() {
    }

    public KvTagDO(String key, String keyTag, String valueEndTag) {
        this.key = key;
        /**
         * Tag一定要足够有标识性,不要嫌它字符多.否则可能会将不必要的值作为索引.尽量大于2个字符,并且要与value的起始位置相连.
         * 如: "key":"value",
         * 那么:
         * key = "key"
         * keyTag = "\"key\":\""  -> 注意,keyTag的结束字符一定要是value的开始字符,如果想不明白,可以以"key":"value"为例子,想想,如何才能获取到它的value部分
         * valueEndTag = "\","
         */
        this.keyTag = keyTag;
        this.valueEndTag = valueEndTag;//Tag一定要足够有标识性,不要嫌它字符多.否则可能会将不必要的值作为索引.尽量大于2个字符.
        this.keyOffset = keyTag.indexOf(key);// key OffSet
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KvTagDO kvTag = (KvTagDO) o;
        return Objects.equals(key, kvTag.key) &&
                Objects.equals(keyTag, kvTag.keyTag) &&
                Objects.equals(valueEndTag, kvTag.valueEndTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, keyTag, valueEndTag);
    }

}
