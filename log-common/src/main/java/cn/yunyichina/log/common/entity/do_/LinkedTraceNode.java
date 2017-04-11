package cn.yunyichina.log.common.entity.do_;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 17:28
 * @Description:
 */
@Getter
@Setter
@ToString(callSuper = true)
public class LinkedTraceNode implements Serializable, Comparable<LinkedTraceNode> {
    private static final long serialVersionUID = 3301244492367991817L;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    protected String contextId;
    /**
     * 整条日志链路的唯一id
     */
    protected String traceId;
    protected Long timestamp;

    /**
     * 对应数据库表'log_collected_item'的'application_name'字段
     */
    protected String applicationName;

    public LinkedTraceNode() {

    }

    public LinkedTraceNode(DTO dto) throws ParseException {
        this.contextId = dto.contextId;
        this.traceId = dto.traceId;
        this.applicationName = dto.applicationName;

        Date date = dateFormat.parse(dto.timestamp);
        this.timestamp = date.getTime();
    }

    /**
     * desc
     *
     * @param that
     * @return
     */
    @Override
    public int compareTo(LinkedTraceNode that) {
        return this.timestamp.compareTo(that.timestamp);
    }


    /**
     * 用户适配厂长的date传输格式
     */
    @Getter
    @Setter
    public static class DTO {
        protected String contextId;
        /**
         * 整条日志链路的唯一id
         */
        protected String traceId;
        protected String timestamp;

        /**
         * 对应数据库表'log_collected_item'的'application_name'字段
         */
        private String applicationName;

    }

    public static LinkedTraceNode parseBy(DTO dto) throws ParseException {
        if (null == dto) {
            return new LinkedTraceNode();
        } else {
            return new LinkedTraceNode(dto);
        }
    }
}
