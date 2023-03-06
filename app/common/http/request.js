/*
 * @Author: zhanch
 * @Date: 2022-12-13 11:05:29
 * @LastEditors: zhanch
 * @FilePath: /http/request.js
 * @Description: 网络请求服务
 */

import {
	env
} from '../../config/env';
import {
	isApp
} from '../utils/util';
import {
	log
} from '../utils/log';

import {
	getToken
} from '@/common/utils/auth.js'

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
	// if(isApp()) {
	// 	 _url = env.basePath + url;
	// } else  {
	// 	_url = url;
	// }
	 _url = env.basePath + url;
	const showLoading = process.showLoading !== false, // 是否展示加载中
		errorHandle = process.error !== false; // 是否使用统一报错
	console.log('请求URL|入参：' + url + ' | ' + JSON.stringify(options || {}));
	return new Promise((resolve, reject) => {
		if (showLoading) uni.showLoading();
		requestMethod(_url, options, method).then(result => {
			const {
				statusCode,
				data
			} = result;
			console.log(result, '请求结果' + url);
			if (statusCode == 200 && data.code === 200) {
				resolve(data);
			} else {
				if (errorHandle) {
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
				reject(data);
			}
			if (showLoading) uni.hideLoading();
		}, error => {
			log.apm({
				url,
				options,
				error
			});
			if (errorHandle) uni.showModal({
				title: '提示',
				content: '当前网络不稳定，请重试'
			});
			if (showLoading) uni.hideLoading();
			reject();
		})
	});
};


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
			http = httpRequest(url, options, method);
			break;
	}
	return http;
}

/**
 * post、get方法
 * @param {Object} url
 * @param {Object} data
 * @param {Object} method
 */
function httpRequest(url, data, method) {
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
			success: res => resolve(res),
			fail: error => reject(error)
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
 * post请求
 */
const post = (url, options, process = {}) => request(url, options, process, 'POST');

/**
 * get请求
 */
const get = (url, options, process = {}) => request(url, options, process, 'GET');

/**
 * uoload
 */
const upload = (url, options = {}) => request(url, options, process = {}, 'UPLOAD');

module.exports = {
	post,
	get,
	upload
};
