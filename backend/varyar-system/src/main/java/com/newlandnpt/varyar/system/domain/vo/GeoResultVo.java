package com.newlandnpt.varyar.system.domain.vo;

import java.util.Map;

/**
 * @author lisd
 * @date 2023/1/10
 **/
public class GeoResultVo {

    /**
     * 响应码 10000代表成功
     */
    private Integer errcode;

    /**
     * 响应消息 成功时为"OK"
     */
    private String errmsg;

    /**
     * 其他响应参数 通常为空
     */
    private String errdetail;

    /**
     * 响应数据
     */
    private Map<String, Object> data;

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getErrdetail() {
		return errdetail;
	}

	public void setErrdetail(String errdetail) {
		this.errdetail = errdetail;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}


}
