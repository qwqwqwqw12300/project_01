package com.newlandnpt.varyar.tcp.gateway.handler.login;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;
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
public class DeviceLoginReq extends MessageHead implements Req{

	/**
	 * 普通键数量(如值是5则表示终端有5个按键， 序号为1-5)
	 */
	private String NormalKeyNum;
	/**
	 * 终端有没有SOS键(0=没有, 1=有)
	 */
	private String sosKey;

	/**
	 * 终端类型(1=GPS, 2=CellID, 3=AGPS)
	 */
	private String deviceType;
	/**
	 * 终端是否支持区域报警终端侧解决方案功能(0= 不具备, 1=具备)
	 */
	private String supportAreaAlert;
	/**
	 * 终端是否具备设置呼入号码功能(0=不具备, 1= 具备)
	 */
	private String supportCallIn;
	/**
	 * 终端软件协议版本号,一共2位,第一位为大版本 号,第二位为小版本号,目前该字段值为21，小版本号 为0为每一版本的基础版本
	 */
	private String protocolVersion;
	
	public String getNormalKeyNum() {
		return NormalKeyNum;
	}
	public void setNormalKeyNum(String normalKeyNum) {
		this.NormalKeyNum = normalKeyNum;
	}
	public String getSosKey() {
		return sosKey;
	}
	public void setSosKey(String sosKey) {
		this.sosKey = sosKey;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getSupportAreaAlert() {
		return supportAreaAlert;
	}
	public void setSupportAreaAlert(String supportAreaAlert) {
		this.supportAreaAlert = supportAreaAlert;
	}
	public String getSupportCallIn() {
		return supportCallIn;
	}
	public void setSupportCallIn(String supportCallIn) {
		this.supportCallIn = supportCallIn;
	}
	public String getProtocolVersion() {
		return protocolVersion;
	}
	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	
	@Override
	public void handleMessage(String body) {
		if(StringUtils.isBlank(body)){
			return ;
		}
		String[] str = body.split("@");
		setNormalKeyNum(str[0]);
		setSosKey(str[1]);
		setDeviceType(str[2]);
		setSupportAreaAlert(str[3]);
		setSupportCallIn(str[4]);
		setProtocolVersion(str[5]);
	}

}
