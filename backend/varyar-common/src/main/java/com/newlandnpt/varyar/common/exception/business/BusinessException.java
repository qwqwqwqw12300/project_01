package com.newlandnpt.varyar.common.exception.business;


import com.newlandnpt.varyar.common.enums.ExCodeEnum;

/**
 * business业务异常
 *
 * @author ljx
 */
public class BusinessException extends RuntimeException {

    private ExCodeEnum exCodeEnum;
    private String msg;

    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BusinessException(ExCodeEnum exCodeEnum) {
        super(exCodeEnum.getMsg());
        this.exCodeEnum = exCodeEnum;
    }

    public BusinessException(ExCodeEnum exCodeEnum, String msg) {
        super(msg);
        this.msg = msg;
        this.exCodeEnum = exCodeEnum;
    }

    public ExCodeEnum getExCodeEnum() {
        return exCodeEnum;
    }

    public void setExCodeEnum(ExCodeEnum exCodeEnum) {
        this.exCodeEnum = exCodeEnum;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
