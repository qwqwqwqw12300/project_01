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

	constructor() {
		if (isApp()) this.vpModule = uni.requireNativePlugin("VPairSDKModule");

	}

	init() {
		// this.vpModule.initSDK();
		this.getToken();
	}

	getToken() {
		return new Promise(resolve => {
			const {
				email,
				password,
				url
			} = env.device;
			uni.request({
				url: '/authenticate',
				data: {},
				method: 'GET',
				header: {
					'Accept': 'application/json',
					'Content-Type': 'application/json',
					'Authorization': `Basic ${btoa(email + ':' + password)}`
				},
				withCredentials: true,
				success: result => {
					console.log(`Basic ${btoa(email + ':' + password)}`, '12121');
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
