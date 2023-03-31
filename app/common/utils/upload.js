/*
 * @Author: zhanch
 * @Date: 2023-02-20 14:48:01
 * @FilePath: /common/utils/upload.js
 * @Description: 应用更新
 */

import {
	PostVersionInfo
} from "../http/api"
import {
	isApp,
	isIos,
	versionCompare
} from "./util"

export default {

	/**app类型**/
	versionType: isIos() ? '1' : '0',

	/**
	 * 检测应用更新
	 */
	check() {
		return new Promise(resolve => {
			if (!isApp()) return resolve({
				status: true
			});
			Promise.all([
				this.getVersion(),
				this.getCurrentVer()
			]).then(([versionInfo, currentVer]) => {
				if (versionInfo && currentVer) {
					const {
						forceUpdate,
						downloadAddress,
						content
					} = versionInfo;
					if (!versionCompare(content, versionInfo)) {
						uni.showModal({
							title: '',
							content: '发现新版本、是否立即更新？',
							showCancel: forceUpdate !== '1', // 1 - 强制更新 
							success: res => {
								if (res.confirm) {
									resolve({
										status: false,
										content
									});
									plus.runtime.openURL(downloadAddress,
										error => {
											console.log(error, '浏览器打开失败');
										});
								} else {
									resolve({
										status: true,
										content
									});
								}
							}
						});
					}
				} else {
					console.log('接口返回');
					resolve({
						status: true
					});
				}

			})
		})

	},

	/**
	 * 获取版本信息
	 */
	getVersion() {
		return new Promise(resolve => {
			PostVersionInfo({
				versionType: this.versionType
			}).then(res => {
				resolve(res.data);
			}, error => {
				console.log('报错');
				resolve(null);
			});
		})

	},

	/**
	 * 获取当前应用版本
	 */
	getCurrentVer() {
		return new Promise(resolve => {
			plus.runtime.getProperty(plus.runtime.appid, info => {
				resolve(info.version);
			})
		});
	}
}
