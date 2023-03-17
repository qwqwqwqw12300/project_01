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
	/**业务回调**/
	callBack = n => n;
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
		8: '设备正在扫描Wifi网络',
		// 自定义事件
		100: '正在获取设备token',
		101: '获取设备token成功',
		102: '正在尝试连接设备',
		103: '网络账号推送',
		104: '选择wifi',
		105: 'token获取异常'
	}

	constructor() {
		if (isApp()) this.vpModule = uni.requireNativePlugin("vpsdk-module-VPairSDKModule");
	}

	/**
	 * 配网成功回调
	 */
	fairFinish = e => {
		console.log(e, 'onPairFinish');
		if (e.isSuccess === 'fail') {
			this.stopPairing(); // 失败强制结束sdk
		}
		this.callBack({
			type: e.isSuccess ? 'success' : 'fail',
			data: e.deviceId
		});
	};


	/**
	 * 配网事件
	 */
	pairEvent = e => {
		this.pageEventCall(e.eventType);
	}

	/**
	 * 选择wifi
	 */
	selectWifi = e => {
		console.log(e.wifiList, 'wifi信息');
		this.pageEventCall(104);
		cb({
			type: 'wifi',
			data: e.wifiList
		});
	}

	/**
	 * 设备连接
	 * @param {Object} cb
	 */
	async connect(cb) {
		this.callBack = n => cb(n); // 设置回调
		// 获取token
		this.pageEventCall(100);
		const {
			token: {
				idToken
			},
			uid
		} = await this.getToken();
		// 获取token
		this.pageEventCall(101);
		console.log('开始设备配网');
		console.log(this.vpModule, 'vpModule');
		try {
			// 配网成功监听
			plus.globalEvent.addEventListener('onPairFinish', this.fairFinish);
			// 配网事件监听
			plus.globalEvent.addEventListener('onPairEvent', this.pairEvent);
			// 选择wifi监听
			plus.globalEvent.addEventListener('onWifiShouldSelect', this.selectWifi);
			// 注册事件
			cb({
				type: 'event',
				data: {
					msg: this.connentMap[102],
					code: 102
				}
			});
			this.vpModule.startPairing(uid, idToken); // 开始配对
			console.log('等待设备配网');
		} catch (e) {
			console.log('配网失败', e);
			//TODO handle the exception
		}
	}

	/**
	 * 执行页面回调
	 */
	pageEventCall(code) {
		this.callBack({
			type: 'event',
			data: {
				msg: this.connentMap[code] || '处理中...',
				code
			}
		});
	}

	/**
	 * 结束配网
	 */
	stopPairing() {
		this.vpModule.stopPairing && this.vpModule.stopPairing();
		console.log(typeof plus.globalEvent.removeEventListener, '结束配网');
		// 配网成功监听
		plus.globalEvent.removeEventListener('onPairFinish', this.fairFinish);
		// 配网事件监听
		plus.globalEvent.removeEventListener('onPairEvent', this.pairEvent);
		// 选择wifi监听
		plus.globalEvent.removeEventListener('onWifiShouldSelect', this.selectWifi);
	}

	/**
	 * 连接wifi
	 */
	connectWifi(ssid, bssid, rssi, password) {
		console.log(ssid, bssid, rssi, password, 'connectWifi');
		this.pageEventCall(103);
		this.vpModule.connectWifi(ssid, bssid, rssi, password);
	}

	/**
	 * 获取token
	 */
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
					this.pageEventCall(105);
					console.log(err, 'getTokenErr');
				}
			});
		});
	}
}

export const vpsdk = new Vpsdk();
