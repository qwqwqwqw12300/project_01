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
export const GetContactsList = (params) => http.get('/api/memberContacts/list', params)

/* 
 *添加紧急联系人*
 */
export const PostAddContacts = (params) => http.post('/api/memberContacts/creMemberCon', params)

/* 
 *删除紧急联系人*
 */
export const PostDelContacts = (params) => http.post('/api/memberContacts/remMemberCon', params)

/* 
 *编辑紧急联系人*
 */
export const PostEditContacts = (params) => http.post('/api/memberContacts/editMemberCon', params)

/* 
 *查询协议*
 */
export const PostSelectAgreement = (params) => http.post('/api/agreement/selectAgreement', params)
/* 
 *查询版本*
 */
export const PostVersionInfo = (params) => http.get('/api/version/selectVersionInfo', params)

/* 
 *修改密码
 */
export const PostUpdatePwd = (params) => http.post('/api/memberInfo/updatePwd', params)

/* 
 *获取家庭列表
 */
export const PostFamilyList = (params) => http.get('/api/family/list', params)

/* 
 *删除家庭
 */
export const PostDelFamily = (params) => http.post('/api/family/remFamily', params)

/* 
 *创建家庭
 */
export const PostAddFamily = (params) => http.post('/api/family/creFamily', params)

/* 
 *编辑家庭
 */
export const PostEditFamily = (params) => http.post('/api/family/editFamily', params)

/* 
 *编辑家庭
 */
export const PostDeviceList = (params) => http.post('/api/device/list', params)