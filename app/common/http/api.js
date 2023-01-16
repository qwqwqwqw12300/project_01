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

/**发送短信**/
export const sendSms = (data) => http.post('/api/sendSms', data);

/**短信登录**/
export const loginBySms = (data) => http.post('/api/loginBySms', data);

/**短信校验**/
export const checkSms = (data) => http.post('/api/checkSms', data);

/**注册**/
export const regMember = (data) => http.post('/api/regMember', data);

/**重置密码**/
export const resetMemberPwd = (data) => http.post('/api/resetMemberPwd', data);

/**获取家庭列表**/
export const getFamilyList = () => http.get('/api/family/list');

/**获取设备列表**/
export const getDeviceList = (data) => http.post('/api/device/list', data);

/**设备事件查询**/
export const selectEventInfo = (data) => http.post('/api/event/selectEventInfo', data);
/**事件信息**/
export const getEventList = () => http.get('/api/event/list');

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
 *修改设备列表
 */
export const PostDeviceList = (params) => http.post('/api/device/list', params);

/**
 * 绑定/解绑设备
 */
export const setDevice = (params) => http.post('/api/device/setDevice', params);

/**
 * 获取房间列表
 */
export const getRoomList = (params) => http.post('/api/room/list', params);

/* 
 *获取房间列表
 */
export const PostRoomList = (params) => http.post('/api/room/list', params)

/* 
 *删除家庭
 */
export const PostDelRoom = (params) => http.post('/api/room/remRoom', params)

/* 
 *创建家庭
 */
export const PostAddRoom = (params) => http.post('/api/room/creRoom', params)

/* 
 *编辑家庭
 */
export const PostEditRoom = (params) => http.post('/api/room/editRoom', params)