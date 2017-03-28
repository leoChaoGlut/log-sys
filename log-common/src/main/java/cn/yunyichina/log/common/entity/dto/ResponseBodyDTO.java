package cn.yunyichina.log.common.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/23 9:36
 * @Description:
 */
@Getter
@Setter
public class ResponseBodyDTO<T> implements Serializable {
    private static final long serialVersionUID = 7652944959075328721L;
    
    private String code;
    private String msg;
    private T result;

    public static ResponseBodyDTO ok() {
        return new ResponseBodyDTO<>()
                .setCode(Status.OK.code)
                .setMsg(Status.OK.msg);
    }

    public static <T> ResponseBodyDTO<T> ok(T result) {
        return ok().setResult(result);
    }

    public static ResponseBodyDTO error(String msg) {
        return error(Status.ERROR.code, msg);
    }

    public static ResponseBodyDTO error(String code, String msg) {
        return new ResponseBodyDTO<>()
                .setCode(code)
                .setMsg(msg);
    }

    @Getter
    public enum Status {
        OK("0", "ok"),
        ERROR("-1", "error");

        private String code;
        private String msg;

        Status(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

    }
}
