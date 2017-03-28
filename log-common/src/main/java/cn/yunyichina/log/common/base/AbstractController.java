package cn.yunyichina.log.common.base;

import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/27 10:54
 * @Description: 仅供内部系统使用, 提供外部接口的controller不要继承该类
 */
public abstract class AbstractController {
    private final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected ResponseBodyDTO exceptionHandler(Exception e, WebRequest req) {
        logger.error(req.toString(), e);
        return ResponseBodyDTO.error(e.getMessage());
    }

}
