/*
 * @Author: zhanch
 * @Date: 2023-02-14 11:25:33
 * @FilePath: /utils/log.js
 * @Description: 日志管理
 */

import {
	env
} from "../../config/env";
import {
	getCurPage
} from "./util";
import store from '@/store/index.js'; //需要引入store

class Log {
	constructor() {
		if (env.mode === 'dev') {
			console.info = (...options) => {
				this.log(options);
			};
		}
	}

	/**
	 * 测试日志
	 */
	log(options) {
		const {
			basePath,
			log
		} = env;
		console.log(basePath + log, 'baseUrl + log');
		uni.request({
			url: basePath + log,
			data: options,
			method: 'get',
			withCredentials: true
		});
	}



	/**
	 * @param {Object} options
	 * 埋点日志
	 */
	apm(options) {
		const info = {
			apmUrl: getCurPage() || '',
			apmUser: store.state.userInfo.phone,
			data: options
		};
		this.log({
			message: info
		});
	}

	getLog(options) {
		let result = '';
		for (const item of options) {
			result += ' ' + this.stringify(item);
		}
		result += ' ' + Date.now();
		return result;
	}

	stringify(obj) {
		switch (typeof obj) {
			case 'string':
				return obj;
			case 'number':
				return obj + '';
			case 'undefined':
				return 'undefined';
			default:
				try {
					return JSON.stringify(obj);
				} catch (e) {
					return obj.toString();
				}
		}
	}

}

export const log = new Log();
