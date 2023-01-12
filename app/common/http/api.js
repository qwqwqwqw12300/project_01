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

/* 
 *查看消息列表 *
 */
export const GetMessageList = (params) => http.get('/api/message/list', params)

/* 
 *查看消息详情 *
 */
export const PostMessageDeatil = (params) => http.post('/api/message/msgContent', params)

/* 
 *查看消息状态 *
 */
export const PostSetMsgInfo = (params) => http.post('/api/message/setMsgInfo', params)

/* 
 *获取推送开关信息*
 */
export const PostGetPushMsgState = (params) => http.get('/api/message/getPushMsgState', params)

/* 
 *推送开关*
 */
export const PostUpdPushMsg = (params) => http.post('/api/message/updPushMsg', params)

/* 
 *用户新增建议*
 */
export const PostAddAdvise = (params) => http.post('/api/advise/addAdvise', params)

/* 
 *获取紧急联系人列表*
 */
export const PostContactsList = (params) => http.post('/api/memberContacts/list', params)