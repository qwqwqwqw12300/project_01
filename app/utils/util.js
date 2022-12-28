/*
 * @Author: zhanch
 * @Date: 2022-12-13 10:45:06
 * @LastEditors: zhanch
 * @FilePath: /utils/util.js
 * @Description: 工具方法
 */
import __config from '@/config/env';

/**
 * formatTime 时间格式化
 * @param {Object} date
 * @returns {string} eg: YYYY/MM/DD hh:mm:ss
 */
export const formatTime = date => {
	const year = date.getFullYear();
	const month = date.getMonth() + 1;
	const day = date.getDate();
	const hour = date.getHours();
	const minute = date.getMinutes();
	const second = date.getSeconds();
	return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(
		':');
};

/**
 * formatNumber 数字格式化
 * @param {string} n
 * @returns {string} eg: 1 => 01
 */
export const formatNumber = n => {
	n = n.toString();
	return n[1] ? n : '0' + n;
};


/**
 * getCurPage 获取当前页面路由或 path
 * @param {Array} pages 路由栈
 * @returns {string}
 */
export const getCurPage = pages => {
	const curPage = pages[pages.length - 1];
	return curPage.route;
}

/**
 * 跳转到页面栈中已存在的页面时，直接返回到该页面中去
 * @param {string} url 目标地址
 */
export const goNavigatePage = url => {
	const paramsIndex = url.indexOf('?');
	// 要调整的url
	const pages = getCurrentPages(),
		goUrl = paramsIndex > -1 ? url.substring(0, paramsIndex) : url;
	let delta = 0,
		hasUrl = false;
	pages.reverse().forEach((item) => {
		const page = '/' + item.route;
		if (page === goUrl) {
			hasUrl = true;
			return;
		} else if (!hasUrl) {
			delta++;
		}
	})
	if (hasUrl) {
		uni.navigateBack({
			delta: delta
		})
	} else {
		uni.navigateTo({
			url: url
		})
	}
}

/**
 * 是否为苹果客户端
 * @returns {boolean}
 */
export const isIos = () => uni.getSystemInfoSync().os.osName === ios;

/**
 * 是否是APP环境
 * @returns {boolean}
 */
export const isApp = () => {
	/*#ifdef APP-PLUS*/
	return true;
	/*#endif*/
	return false;
};
