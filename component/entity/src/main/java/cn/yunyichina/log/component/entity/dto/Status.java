package cn.yunyichina.log.component.entity.dto;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 16-9-1 上午10:38
 * @Description: 交易结果代码(和旧版的前置机一样)
 */
public enum Status {
    /**
     * 接口转换程序异常
     */
    INTERFACE_CONVERSION_PROGRAM_ERROR(-2, "接口转换程序异常"),
    /**
     * 请求医院接口异常
     */
    REQUEST_HIS_INTERFACE_ERROR(-1, "请求医院接口异常"),
    /**
     * 成功
     */
    SUCCESS(0, "成功"),
    /**
     * 成功,但未查询到数据
     */
    SUCCESS_NO_DATA(1, "未查询到数据"),
    /**
     * 成功,但不符合医院限定
     */
    SUCCESS_NOT_HIS_LIMIT(2, "不符合医院限定"),
    /**
     * 成功,但没有返回任何信息
     */
    SUCCESS_NOT_ANY_INFO(3, "医院没有返回任何信息"),;

    private int code;
    private String msg;

    Status(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
