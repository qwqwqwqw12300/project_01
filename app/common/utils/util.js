/*
 * @Author: zhanch
 * @Date: 2022-12-13 10:45:06
 * @LastEditors: zhanch
 * @FilePath: /utils/util.js
 * @Description: 工具方法
 */
import {
	env
} from '@/config/env';
import {
	WEEKMAP
} from '../../config/db';

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
export const getCurPage = () => {
	const pages = getCurrentPages();
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
export const isIos = () => uni.getSystemInfoSync().platform === 'ios';

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
 * 是否是生产环境
 * @returns {boolean}
 */
export const isProd = () => {
	return env.mode !== 'dev';
};

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
 * 数组深拷贝
 * @param {Object} obj
 */
export function arrayAssign(obj) {
	// 只拷贝对象
	if (typeof obj !== 'object') return;
	// 根据obj的类型判断是新建一个数组还是一个对象
	const newObj = obj instanceof Array ? [] : {};
	for (let key in obj) {
		// 遍历obj,并且判断是obj的属性才拷贝
		if (obj.hasOwnProperty(key)) {
			// 判断属性值的类型，如果是对象递归调用深拷贝
			newObj[key] = typeof obj[key] === 'object' ? arrayAssign(obj[key]) : obj[key];
		}
	}
	return newObj;
}

/**
 * versionCompare 比较版本大小如果curV 小于 reqV，则返回false，否则返回ture
 */
export const versionCompare = (curV, reqV) => {
	console.log(curV, reqV, 'vvv')
	const v1parts = curV.toString().split('.')
	const v2parts = reqV.toString().split('.')
	for (var i = 0; i < v1parts.length; ++i) {
	    if (v2parts.length == i) {
	      return 1;
	    }
	    
	    if (v1parts[i] == v2parts[i]) {
	      continue;
	    }
	    else if (v1parts[i] > v2parts[i]) {
	      return 1;
	    }
	    else {
	      return -1;
	    }
	  }
	  
	  if (v1parts.length != v2parts.length) {
	    return -1;
	  }
	  
	  return 0;
}

/**
 * hh:MM 获取时间戳 eg： 01:00
 */
export const getHoursTime = (date) => {
	return date && new Date('1970/1/1 ' + date + ':00').getTime();
}

/**
 * hh:MM 获取分钟 eg： 01:00 => 60
 */
export const getMinute = (date) => {
	return date && new Date('1970/1/1 ' + date + ':00').getHours();
}


/**
 * 使用分钟计算时间戳 eg 1 -> 60000
 */
export const minuteToTime = (min) => min && min * 60;

/**
 * 手机号校验 1开头 11位
 */
export function phoneValidator(phone) {
	const reg = /^1\d{10}$/
	return reg.test(phone)
}

/**
 * 手机号脱敏
 */
export function phoneHide(phone) {
	const pho = /(\d{3})\d*(\d{4})/;
	return phone.replace(pho, '$1****$2');
}

/**
 * 手机号去除非数字
 */
export function phoneRemove(phone) {
	const reg = /[^\d]/g
	return phone.replace(reg, '');
}

export function wddkAbbreviation(week) {
	let text = '';
	const weekend = ['5', '6'];
	// 是否包含周末
	const includeEnd = weekend.filter(ele => week.includes(ele)).length === 2;
	if (week.length === 7) return '每天';
	if (week.length === 2 && includeEnd) return '周末';
	if (week.length === 5 && !includeEnd) return '工作日（不含节假日';
	week.forEach((ele, index) => {
		text += `${!index ? '' : ', '}${WEEKMAP[ele]}`;
	});
	return text;
}

export function deepClone(target) {
	let copy_obj = [];

	function _deepCopy(target) {
		if ((typeof target !== 'object') || !target) {
			return target;
		}
		for (let i = 0; i < copy_obj.length; i++) {
			if (copy_obj[i].target === target) {
				return copy_obj[i].copyTarget;
			}
		}
		let obj = {};
		if (Array.isArray(target)) {
			obj = []; //处理target是数组的情况 
		}
		copy_obj.push({
			target: target,
			copyTarget: obj
		})
		Object.keys(target).forEach(key => {
			if (obj[key]) {
				return;
			}
			obj[key] = _deepCopy(target[key]);
		});
		return obj;
	}
	return _deepCopy(target);
}
