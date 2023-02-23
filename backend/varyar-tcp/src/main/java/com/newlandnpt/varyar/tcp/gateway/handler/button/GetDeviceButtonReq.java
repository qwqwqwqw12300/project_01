package com.newlandnpt.varyar.tcp.gateway.handler.button;

import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;

/**
 * 设备按键获取
 * 注意：以下接口获取在设备开机或者重启后，请求一次。
 * 报文体格式：无
 * 报文示例：
 *[35380100360174,898600MFSSYYGXXXXXXP,201805082000001000, GET_NORMAL_BUTTON,3, 20180528111820,1,1]
 ** @author chenxw1
 * @date 2023-02-22
 */
public class GetDeviceButtonReq extends MessageHead implements Req{

	@Override
	public void handleMessage(String body) {
		if(StringUtils.isBlank(body)){
			return ;
		}
	}

}
