package cn.yunyichina.log.common.entity.entity.dto;


/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 16-9-1 上午10:39
 * @Description:
 */
public class ResponseDTO {

    private String code;

    private String msg;

    private Object result;

    public ResponseDTO() {

    }

    public static ResponseDTO success() {
        return new ResponseDTO(Status.SUCCESS_NO_DATA, null);
    }

    public static ResponseDTO success(Object result) {
        return new ResponseDTO(Status.SUCCESS, result);
    }

    public static ResponseDTO success(Status status, Object result) {
        return new ResponseDTO(status, result);
    }

    public static ResponseDTO failure(String resultMessage) {
        return new ResponseDTO(String.valueOf(Status.INTERFACE_CONVERSION_PROGRAM_ERROR.getCode()), resultMessage);
    }

    public static ResponseDTO failure(Status status) {
        return new ResponseDTO(status, null);
    }

    public ResponseDTO(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseDTO(Status status, Object result) {
        this.code = String.valueOf(status.getCode());
        this.msg = status.getMsg();
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
