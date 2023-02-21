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
	isIos,
	versionCompare
} from "./util"

export default {

	/**app类型**/
	versionType: isIos() ? '0' : '1',

	/**
	 * 检测应用更新
	 */
	check() {
		return new Promise(resolve => {
			Promise.all([
				this.getVersion(),
				this.getCurrentVer()
			]).then(([versionInfo, currentVer]) => {
				if (versionInfo && currentVer) {
					if (!versionCompare(currentVer, versionInfo)) {
						uni.showModal({
							title: '',
							content: '发现新版本、是否更新？',
							success: res => {
								if (res.confirm) {
									let appurl = "https://sj.qq.com/"
									plus.runtime.openURL(appurl)
								} else {
									this.initInfo();
								}
							}
						});
					}


				}
				resolve(true);
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
				resolve(res.data.content);
			}, error => {
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
