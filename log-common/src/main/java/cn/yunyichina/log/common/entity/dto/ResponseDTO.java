package cn.yunyichina.log.common.entity.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 16-9-1 上午10:39
 * @Description:
 */
@Getter
@Setter
@ToString
public class ResponseDTO {

    private String code;
    private String msg;
    private Object result;

    public static ResponseDTO ok() {
        return new ResponseDTO()
                .setCode(Status.OK.code)
                .setMsg(Status.OK.msg);
    }

    public static ResponseDTO ok(Object result) {
        return new ResponseDTO()
                .setCode(Status.OK.code)
                .setMsg(Status.OK.msg)
                .setResult(result);
    }

    public static ResponseDTO error(String msg) {
        return new ResponseDTO()
                .setCode(Status.ERROR.code)
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
