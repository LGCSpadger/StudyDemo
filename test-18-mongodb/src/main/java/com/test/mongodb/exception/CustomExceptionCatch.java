package com.test.mongodb.exception;

import com.test.common.exception.ExceptionCatch;
import com.test.common.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 自定义的异常类，其中定义异常类型所对应的错误代码
 * @author liu.gc
 * @version 1.0
 **/
@ControllerAdvice//控制器增强
public class CustomExceptionCatch extends ExceptionCatch {

    static {
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }

}
