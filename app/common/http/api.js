/*
 * @Author: zhanch
 * @Date: 2022-12-13 14:37:03
 * @LastEditors: zhanch
 * @FilePath: /http/api.js
 * @Description: 请求接口
 */

import http from './request.js';
/**
 * 获取请求验证码
 */
export const getDemo = () => http.get('/api/captchaImage');
export const postDemo = () => http.post('/api/sendSms', {
	"uuid": "c8916a767204456fa970c675c71048e5",
	"captcha": "6d23"
});

/* 
 *获取图形验证码 *
 */
export const GetCaptchaImage = (params) => http.get('/api/captchaImage', params)

/* 
 *登录 *
 */
export const PostLoginByPwd = (params) => http.post('/api/loginByPwd', params)
