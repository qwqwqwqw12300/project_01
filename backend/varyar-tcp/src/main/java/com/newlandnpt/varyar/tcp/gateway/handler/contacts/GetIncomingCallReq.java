package com.newlandnpt.varyar.tcp.gateway.handler.contacts;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

/**
 * 呼入号码获取<br>
 * 报文体格式：1
 * 报文示例：
 *[35380100360174,898600MFSSYYGXXXXXXP,201805082000001000, GET_INCOMING_CALL,3, 20180528111820,1,1]
 * @author chenxw1
 * @date 2023-02-22
 */
public class GetIncomingCallReq extends MessageHead implements Req{


	/**
	 * 报文请求体
	 */
	private String requestStatus = "1";


	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	@Override
	public void handleMessage(String body) {
		String[] str = body.split("@");
		setRequestStatus(str[0]);
	}

}
