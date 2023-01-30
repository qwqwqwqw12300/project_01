/*
 * @Author: zhanch
 * @Date: 2023-01-09 16:01:39
 * @FilePath: /common/sdk/vpsdk.js
 * @Description: 设备连接sdk
 */

import {
	env
} from "../../config/env";
import {
	isApp
} from "../utils/util";

class Vpsdk {
	/**推送模块**/
	vpModule;
	/**连接事件字典**/
	connentMap = {
		1: '正在建立与设备的连接',
		2: '设备连接到Wi-Fi',
		3: '将设备连接到您的帐户',
		4: '云配对',
		5: '配对完成',
		6: '重新启动设备',
		998: '正在检查更新',
		999: '正在更新设备',
		7: '正在将设备重新启动到工厂',
		8: '设备正在扫描Wifi网络'
	}

	constructor() {
		if (isApp()) this.vpModule = uni.requireNativePlugin("VPairSDKModule");
	}

	async connect(cb) {
		const {
			token: {
				idToken
			},
			uid
		} = await this.getToken();
		// this.vpModule.initSDK();
		console.log('开始设备配网');
		console.log(this.vpModule, 'vpModule');
		try {
			// 配网成功监听
			plus.globalEvent.addEventListener('onPairFinish', e => {
				console.log(e, 'onPairFinish');
				cb({
					type: e.isSuccess ? 'success' : 'fail',
					data: e.deviceId
				});
			});

			// 配网事件监听
			plus.globalEvent.addEventListener('onPairEvent', e => {
				console.log('配网事件' + e.eventType);
				cb({
					type: 'event',
					data: this.connentMap[e.eventType] || '处理中...'
				});
			});

			// 选择wifi监听
			plus.globalEvent.addEventListener('onWifiShouldSelect', e => {
				console.log(e.wifiList, 'wifi信息');
				cb({
					type: 'wifi',
					data: e.wifiList
				});
			});
			// 开始连接
			this.vpModule.startPairing(uid, idToken); // 开始配对
			console.log('等待设备配网');
		} catch (e) {
			console.log('配网失败', e);
			//TODO handle the exception
		}




	}

	/**
	 * 连接wifi
	 */
	connectWifi(ssid, bssid, rssi, password) {
		console.log(ssid, bssid, rssi, password, 'connectWifi');
		this.vpModule.connectWifi(ssid, bssid, rssi, password);
	}

	getToken() {
		return new Promise(resolve => {
			const {
				email,
				password,
				url
			} = env.device;
			uni.request({
				url,
				data: {},
				method: 'GET',
				header: {
					'Accept': 'application/json',
					'Content-Type': 'application/json',
					'Authorization': `Basic ${btoa(email + ':' + password)}`
				},
				withCredentials: true,
				success: result => {
					resolve(result.data);
					console.log(result, 'getToken');
				},
				fail: err => {
					console.log(err, 'getTokenErr');
				}
			});
		});
	}
}

export const vpsdk = new Vpsdk();
