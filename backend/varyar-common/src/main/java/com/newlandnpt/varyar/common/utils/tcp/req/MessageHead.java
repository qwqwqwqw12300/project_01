package com.newlandnpt.varyar.common.utils.tcp.req;

/**
 * 报文头
 * @author Bean
 * @date 2023-02-20
 */
public class MessageHead {

	/**
	 * 设备号,设备唯一编号
	 */
	private String deviceNo;

	/**
	 * SIM 卡卡号
	 */
	private String iccid;

	/**
	 * 交易流水号
	 * 时间格式+4位循环数，时间格式 : yyyyMMddHHmmss 4 位循环数范围 0..9999，从 0 开始， 递增赋值，步长为 1， 增加到 9999 后，再从 0 开始
	 */
	private String tranNo;

	/**
	 * 接口唯一标识，唯一标识每个接 口
	 */
	private String apiType;

	/**
	 * 报文类型
	 * 标识通信方向， 取值如下: 1. 下发请求 2. 下发响应 3. 上报请求
	 */
	private String msgType;

	/**
	 * 报文时间
	 * 设备或者服务器时间，时间格式为： yyyyMMddHHmmss
	 */
	private String msgTime;

	/**
	 * 报文内容的长度
	 */
	private String msgLen;

	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getTranNo() {
		return tranNo;
	}
	public void setTranNo(String tranNo) {
		this.tranNo = tranNo;
	}
	public String getApiType() {
		return apiType;
	}
	public void setApiType(String apiType) {
		this.apiType = apiType;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}
	public String getMsgLen() {
		return msgLen;
	}
	public void setMsgLen(String msgLen) {
		this.msgLen = msgLen;
	}

}
