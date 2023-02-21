package com.newlandnpt.varyar.base.login;

import com.newlandnpt.varyar.base.MessageHead;
import com.newlandnpt.varyar.base.Req;
import com.newlandnpt.varyar.common.utils.StringUtils;

/**
 * 设备登录报文体<br>
 * 报文体格式： 终端普通键数量@终端有没有SOS键@终端类型@终端是否具备区域报警功能@终端是否具备设置呼入号码功能@终端软件协议版本<br>
 * 字段说明： 
 * 1. 终端普通键数量(如值是5则表示终端有5个按键， 序号为1-5) 
 * 2. 终端有没有SOS键(0=没有, 1=有) 
 * 3. 终端类型(1=GPS, 2=CellID, 3=AGPS) 
 * 4. 终端是否支持区域报警终端侧解决方案功能(0= 不具备, 1=具备)
 * 5. 终端是否具备设置呼入号码功能(0=不具备, 1= 具备) 
 * 6. 终端软件协议版本号,一共2位,第一位为大版本 号,第二位为小版本号,目前该字段值为21，小版本号 为0为每一版本的基础版本
 * 报文示例：
 * [868976030354448,89860321245910333529,202302170933320000,DEVICE_LOGIN,3,20230217093332,12,3@1@1@0@1@21]
 * @author Bean
 * @date 2023-02-20
 */
public class DeviceLogin extends MessageHead implements Req {

	private String field1;
	private String field2;
	private String field3;
	private String field4;
	private String field5;
	private String field6;
	
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	public String getField3() {
		return field3;
	}
	public void setField3(String field3) {
		this.field3 = field3;
	}
	public String getField4() {
		return field4;
	}
	public void setField4(String field4) {
		this.field4 = field4;
	}
	public String getField5() {
		return field5;
	}
	public void setField5(String field5) {
		this.field5 = field5;
	}
	public String getField6() {
		return field6;
	}
	public void setField6(String field6) {
		this.field6 = field6;
	}
	
	@Override
	public void handelMessage(String body) {
		if(StringUtils.isBlank(body)){
			return ;
		}
		String[] str = body.split("@");
		setField1(str[0]);
		setField2(str[1]);
		setField3(str[2]);
		setField4(str[3]);
		setField5(str[4]);
		setField6(str[5]);
	}
	@Override
	public void setHead(String field1, String field2, String field3, String field4, String field5, String field6, String field7) {
		setDeviceNo(field1);
		setIccid(field2);
		setTranNo(field3);
		setApiType(field4);
		setMsgType(field5);
		setMsgTime(field6);
		setMsgLen(field7);
		
	}
	@Override
	public void response() {
		// TODO Auto-generated method stub
		
	}
	
	
}
