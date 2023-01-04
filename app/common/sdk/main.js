/*
 * @Author: zhanch
 * @Date: 2022-12-13 11:24:33
 * @LastEditors: zhanch
 * @FilePath: /common/start-up.js
 * @Description: 应用启动设置
 */
// import {
// 	qiyukf
// } from './qiyukf.js';

import {
	push
} from './push.js';

class StartUp {
	constructor() {
		console.log('start-up');
	}

	ready() {
		return new Promise(resolve => {
			// 校验更新信息
			// qiyukf.init();
			// 推送配置
			// 获取后台配置
			console.log(push, 'push');
			try {
				push.init();
			} catch (error) {
				console.log('初始化报错', error);
			}

			resolve();
		});
	}

}

export const startup = new StartUp();
