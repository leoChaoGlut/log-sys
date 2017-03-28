package cn.yunyichina.log.common.util;

import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.common.exception.ResponseException;

import java.util.Objects;

import static cn.yunyichina.log.common.entity.dto.ResponseBodyDTO.Status.OK;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/23 11:33
 * @Description:
 */
public class ResponseUtil {

    public static <T> T getResult(ResponseBodyDTO<T> responseBodyDTO) {
        if (null == responseBodyDTO) {
            throw new ResponseException("Response body is null");
        } else {
            if (Objects.equals(OK.getCode(), responseBodyDTO.getCode())) {
                return responseBodyDTO.getResult();
            } else {
                throw new ResponseException(responseBodyDTO.getCode() + " - " + responseBodyDTO.getMsg());
            }
        }
    }

}
