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

/**错误处理文案**/
const errText = {
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
	if (isApp()) {
		_url = env.basePath + url;
	} else {
		_url = url;
	}
	const showLoading = process.showLoading !== false,
		errorHandle = process.error !== false;
	console.info('请求URL|入参：' + url + ' | ' + JSON.stringify(options || {}));
	return new Promise((resolve, reject) => {
		if (showLoading) uni.showLoading();
		uni.request({
			url: _url,
			method,
			withCredentials: true,
			success: result => {
				const {
					statusCode,
					data
				} = result;
				if (statusCode == 200) {
					resolve(data);
				} else {
					if (errorHandle) uni.showModal({
						title: '提示',
						content: errText[statusCode] || '系统错误'
					});
					reject();
				}
				if (showLoading) uni.hideLoading();
			},
			fail: error => {
				if (errorHandle) uni.showModal({
					title: '提示',
					content: '网络请求错误' + error.errMsg
				});

				if (showLoading) uni.hideLoading();
				reject();
			}

		});
	});
};

/**
 * post请求
 */
const post = (url, options, process = {}) => request(url, options, process, 'POST');

/**
 * get请求
 */
const get = (url, options, process = {}) => request(url, options, process, 'GET');

module.exports = {
	post,
	get
};
