/*
* @Author: zhanch
* @Date: 2023-01-17 21:55:21
* @FilePath: /store/service/service.js
* @Description: 服务模块储存
*/

export default {
	state: {
		/**公告信息**/
		readInfo: '',
		/**智能客服坐标**/
		serviceAxisY: 300,
		
	},
	mutations: {
		/**
		 * 设置阅读信息
		 * @param {Object} state
		 * @param {Object} info
		 */
		setReadInfo(state, info) {
			state.readInfo = info;
		},
		/**
		 * 人工客服Y轴
		 * @param {Object} state
		 * @param {Object} axisY
		 */
		setServiceAxisY(state, axisY) {
			state.serviceAxisY = axisY;
		},
	},
	actions: {
	}
}
