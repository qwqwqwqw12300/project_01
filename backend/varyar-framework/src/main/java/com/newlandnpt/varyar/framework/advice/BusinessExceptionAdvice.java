package com.newlandnpt.varyar.framework.advice;

import com.newlandnpt.varyar.common.core.domain.AjaxResult;
import com.newlandnpt.varyar.common.enums.ExCodeEnum;
import com.newlandnpt.varyar.common.exception.business.BusinessException;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * 业务异常处理
 * @author npt
 * @date 2023/3/4
 */
@ResponseBody
@ControllerAdvice
public class BusinessExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(BusinessException.class);

    @ExceptionHandler(BusinessException.class)
    public AjaxResult handleException(BusinessException e){
        if(Strings.isBlank(e.getMsg())){
            return defHandler(e.getExCodeEnum(),e);
        }
        if(Objects.isNull(e.getExCodeEnum())){
            return defHandler(e.getMsg(),e);
        }
        return defHandler(e.getExCodeEnum(),e.getMsg(),e);
    }

    protected AjaxResult defHandler(String msg, Exception e) {
        log.error(msg, e);
        return AjaxResult.error(msg);
    }

    protected AjaxResult defHandler(String code, String msg, Exception e) {
        log.error(msg, e);
        return AjaxResult.error(code, msg);
    }

    protected AjaxResult defHandler(ExCodeEnum exCodeEnum, String msg, Exception e) {
        log.error(msg, e);
        return AjaxResult.error(exCodeEnum.getCode(), msg);
    }

    protected AjaxResult defHandler(ExCodeEnum exCodeEnum, Exception e) {
        log.error(exCodeEnum.getMsg(), e);
        return AjaxResult.error(exCodeEnum.getCode(), exCodeEnum.getMsg());
    }
}
