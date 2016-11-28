package cn.yunyichina.log.common.entity.dto;


import cn.yunyichina.log.common.constant.Status;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 16-9-1 上午10:39
 * @Description:
 */
public class Response {

    private String code;

    private String msg;

    private Object result;

    public Response() {

    }

    public static Response success() {
        return new Response(Status.SUCCESS_NO_DATA, null);
    }

    public static Response success(Object result) {
        return new Response(Status.SUCCESS, result);
    }

    public static Response success(Status status, Object result) {
        return new Response(status, result);
    }

    public static Response failure(String resultMessage) {
        return new Response(String.valueOf(Status.INTERFACE_CONVERSION_PROGRAM_ERROR.getCode()), resultMessage);
    }

    public static Response failure(Status status) {
        return new Response(status, null);
    }

    public Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(Status status, Object result) {
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
