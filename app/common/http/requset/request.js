/*
 * @Author: zhanch
 * @Date: 2022-12-13 11:05:29
 * @LastEditors: zhanch
 * @FilePath: /http/request.js
 * @Description: 网络请求服务
 */

import {
	env
} from '@/config/env.js';
import {
	log
} from '@/common/utils/log.js';

import {
	getToken
} from '@/common/utils/auth.js'
import {
	isApp
} from '../../utils/util';

/**错误处理文案**/
const ERROR_TEXT = {
	401: '访问令牌无效或已过期',
	403: '拒绝访问',
	404: '资源不存在',
	405: '请求方法未允许',
	500: '内部服务器错误',
	503: '服务不可用',
	504: '请求超时'
};

/**重试配置**/
const RETRYCONFIG = {
	// 最大重试次数
	COUNT: 3,
	// 单次重试时间
	RETRYTIME: 1500
}


/**
 * 网络请求
 * @param {string} url 请求地址
 * @param {object} options 请求参数
 * @param { object } process? 定制操作
 * @property {boolean} showLoading? 是否需要遮罩
 * @property {boolean} error? 是否进行默认报错默认处理
 * @param {string} method 请求方法
 */
const request = (url, options, process, method = 'POST') => {
	let _url;
	_url = env.basePath + url;
	// if (isApp()) {
	// 	_url = env.basePath + url;
	// } else {
	// 	_url = url;
	// }
	const showLoading = process.showLoading !== false, // 是否展示加载中
		isError = process.error !== false; // 是否使用统一报错
	console.log('请求URL|入参：' + url + ' | ' + JSON.stringify(options || {}));
	return new Promise((resolve, reject) => {
		// if (showLoading) uni.showLoading();
		requestMethod(_url, options, method).then(result => {
			const handleData = resultHandle(result, isError);
			handleData.type === 'success' ? resolve(result.data) : reject(result.data);
			// if (showLoading) uni.hideLoading();
		}, error => {
			log.apm({
				url,
				options,
				error
			});
			if (isError) uni.showModal({
				title: '提示',
				content: '当前网络不稳定，请重试'
			});
			reject(error);
			// if (showLoading) uni.hideLoading();

		})
	});
};

/**
 * 请求合并
 * @param { Array: {data, url} } requests 请求合集
 * @param { string } 请求参数
 */
const mergeRequest = (requests, process = {}) => {
	const showLoading = process.showLoading !== false, // 是否展示加载中
		isError = process.error !== false; // 是否使用统一报错
	return new Promise(async (resolve, reject) => {
		if (showLoading) uni.showLoading();
		Promise.all(requests.map(req => requestMethod(env.basePath + req.url, req.data, req
				.method || 'POST')))
			.then(data => {
				const res = [];
				for (let i = 0, len = data.length; i < len; i++) {
					const handleData = resultHandle(data[i], isError);
					if (handleData.type === 'success') {
						res.push(data[i]);
					} else {
						reject(data[i]);
						return;
					}
				}
				resolve(res);
			}, error => {
				log.apm({
					requests,
					error
				});
				if (isError) uni.showModal({
					title: '提示',
					content: '当前网络不稳定，请重试'
				});
				reject(error);
				if (showLoading) uni.hideLoading();
			})
	})
}


/**
 * 请求方法
 * @param {Object} url
 * @param {Object} options
 * @param {Object} method
 */
function requestMethod(url, options, method) {
	let http;
	switch (method) {
		case 'UPLOAD':
			http = httpUploadFile(url, options);
			break;
		default:
			http = httpRequest(url, options, method, 0);
			break;
	}
	return http;
}

/**
 * post、get方法
 * @param {Object} url 请求地址
 * @param {Object} data 请求数据
 * @param {Object} method 请求方法
 * @param {Object} count 报错重发尝试次数
 */
function httpRequest(...arg) {
	const [url,
		data,
		method,
		count
	] = arg;
	return new Promise((resolve, reject) => {
		uni.request({
			url,
			data,
			method,
			header: {
				'Accept': 'application/json',
				'Content-Type': 'application/json',
				'Authorization': getToken(),
			},
			withCredentials: true,
			success: res => {
				resolve(res)
			},
			fail: error => {
				if (count <= RETRYCONFIG.COUNT) { // 请求重试
					setTimeout(() => {
						const payload = [...arg];
						payload.splice(arg.length - 1, 1, count +
							1);
						httpRequest.call(this, ...payload).then(res => {
							resolve(res)
						}, callErr => {
							reject(callErr);
						})
					}, RETRYCONFIG.RETRYTIME)
				} else {
					reject(error);
				}
			}
		})
	})
}

function httpUploadFile(url, params) {
	return new Promise((resolve, reject) => {
		uni.uploadFile({
			url,
			filePath: params.path,
			name: params.name,
			header: {
				'Accept': 'application/json',
				// 'Content-Type': 'application/x-www-form-urlencoded',
				'Authorization': getToken(),
			},
			withCredentials: true,
			success: res => {
				resolve({
					...res,
					data: JSON.parse(res.data)
				})
			},
			fail: error => reject(error)
		});
	})
}

/**
 * 返回结果处理
 * @param {Object} result
 * @param {Object} isError
 * @param {Object} resolve
 */
function resultHandle(result, isError) {
	const {
		statusCode,
		data
	} = result;
	if (statusCode == 200 && data.code === 200) {
		return {
			type: 'success',
			data
		};
	} else {
		if (isError) {
			if (data.code === 401) { // 未登录
				uni.redirectTo({
					url: '/pages/login/login'
				});
			} else {
				uni.showModal({
					title: '提示',
					content: data.msg || ERROR_TEXT[statusCode] || '系统错误'
				});
			}
		}
		return {
			type: 'error',
			data
		};
	}
}


/**
 * 合并请求
 */
const merge = (requests, process = {}) => mergeRequest(requests, process);

/**
 * post请求
 */
const post = (url, options, process = {}) => request(url, options, process, 'POST');

/**
 * get请求
 */
const get = (url, options, process = {}) => request(url, options, process, 'GET');

/**
 * put请求
 */
const put = (url, options, process = {}) => request(url, options, process, 'PUT');

/**
 * uoload
 */
const upload = (url, options = {}) => request(url, options, process = {}, 'UPLOAD');

module.exports = {
	post,
	get,
	upload,
	merge,
	put
};
