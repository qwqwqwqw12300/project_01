package com.newlandnpt.varyar.system.domain.req;

/**
 * @author lisd
 * @date 2023/1/5
 **/
public class MsgSendReq {

    private String msg;

    private int code;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
