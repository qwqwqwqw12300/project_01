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


/**
 * 对象深拷贝
 */
/**
 * 对象合并深拷贝
 */
export function assignDeep(target, ...sources) {
	// 1. 参数校验
	if (target == null) {
		throw new TypeError('Cannot convert undefined or null to object');
	}

	// 2. 如果是基本类型，则转换包装对象
	const result = Object(target);
	// 3. 缓存已拷贝过的对象
	const hash = new WeakMap();

	// 4. 目标属性是否可直接覆盖赋值判断
	function canPropertyCover(node) {
		if (!node.target[node.key]) {
			return true;
		}
		if (node.target[node.key] == null) {
			return true;
		}
		if (!(typeof node.target[node.key] === 'object')) {
			return true;
		}
		if (Array.isArray(node.target[node.key]) !== Array.isArray(node.data)) {
			return true;
		}
		return false;
	}

	sources.forEach(v => {
		const source = Object(v);

		const stack = [{
			data: source,
			key: undefined,
			target: result
		}];

		while (stack.length > 0) {
			const node = stack.pop();
			if (typeof node.data === 'object' && node.data !== null) {
				let isPropertyDone = false;
				if (hash.get(node.data) && node.key !== undefined) {
					if (canPropertyCover(node)) {
						node.target[node.key] = hash.get(node.data);
						isPropertyDone = true;
					}
				}

				if (!isPropertyDone) {
					// tslint:disable-next-line:no-shadowed-variable
					let target;
					if (node.key !== undefined) {
						if (canPropertyCover(node)) {
							target = Array.isArray(node.data) ? [] : {};
							hash.set(node.data, target);
							node.target[node.key] = target;
						} else {
							target = node.target[node.key];
						}
					} else {
						target = node.target;
					}

					Reflect.ownKeys(node.data).forEach(key => {
						// 过滤不可枚举属性
						const desc = Object.getOwnPropertyDescriptor(node.data, key);
						if (desc && !desc.enumerable) {
							return;
						}
						stack.push({
							data: node.data[key],
							key: key,
							target: target
						});
					});
				}
			} else {
				Object.assign(node.target, {
					[node.key]: node.data
				});
			}
		}

	});
	return result;
}

/**
 * versionCompare 比较版本大小如果curV 小于 reqV，则返回false，否则返回ture
 */
export const versionCompare = (curV, reqV) => {
	console.log(curV, reqV, 'vvv')
	const arr1 = curV.toString().split('.')
	const arr2 = reqV.toString().split('.')
	//将两个版本号拆成数字
	const minL = Math.min(arr1.length, arr2.length);
	let pos = 0; //当前比较位
	let diff = 0; //当前为位比较是否相等
	let flag = true;
	//逐个比较如果当前位相等则继续比较下一位
	while (pos < minL) {
		diff = parseInt(arr1[pos]) - parseInt(arr2[pos]);
		if (diff == 0) {
			pos++;
			continue;
		} else if (diff > 0) {
			flag = true;
			break;
		} else {
			flag = false;
			break;
		}
	}
	return flag;
}

/**
 * hh:MM 获取时间戳 eg： 01:00
 */
export const getHoursTime = (date) => {
	return date && new Date('1970-1-1 ' + date + ':00').getTime();
}
