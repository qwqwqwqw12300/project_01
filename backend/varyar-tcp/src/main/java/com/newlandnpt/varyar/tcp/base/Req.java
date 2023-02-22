package com.newlandnpt.varyar.tcp.base;

public interface Req {


	String getDeviceNo();
	void setDeviceNo(String deviceNo);
	String getIccid();
	void setIccid(String iccid);
	String getTranNo();
	void setTranNo(String tranNo);
	String getApiType();
	void setApiType(String apiType);
	String getMsgType();
	void setMsgType(String msgType);
	String getMsgTime();
	void setMsgTime(String msgTime);
	String getMsgLen();
	void setMsgLen(String msgLen);

	/**
	 * 根据字段设置报文头
	 * @param field1
	 * @param field2
	 * @param field3
	 * @param field4
	 * @param field5
	 * @param field6
	 * @param field7
	 */
	default void setHead(String field1, String field2, String field3, String field4, String field5, String field6, String field7){
		setDeviceNo(field1);
		setIccid(field2);
		setTranNo(field3);
		setApiType(field4);
		setMsgType(field5);
		setMsgTime(field6);
		setMsgLen(field7);
	}

	/**
	 * 处理报文体
	 * @param body
	 */
	public void handleMessage(String body);
}
