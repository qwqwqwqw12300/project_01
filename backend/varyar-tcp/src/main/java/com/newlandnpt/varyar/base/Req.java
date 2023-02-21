package com.newlandnpt.varyar.base;

public interface Req {

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
	public void setHead(String field1, String field2, String field3, String field4, String field5, String field6, String field7);
	
	/**
	 * 处理报文体
	 * @param body
	 */
	public void handelMessage(String body);
	
	public void response();
}
