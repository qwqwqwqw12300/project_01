/*
 * @Author: zhanch
 * @Date: 2022-12-13 14:37:03
 * @LastEditors: Please set LastEditors
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
 *注销*
 */
export const PostLoginOut = (params) => http.post('/api/loginOut', params)

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
export const PostGetPushMsgState = (...params) => http.get.apply(this, ['/api/memberInfo/getMemInfo', ...params]);

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
export const GetContactsList = (params) => http.get('/api/memberContacts/list', params, {
	error: false
})

/* 
 *添加紧急联系人*
 */
export const PostAddContacts = (params) => http.post('/api/memberContacts/creMemberCon', params)

/* 
 *删除紧急联系人*
 */
export const PostDelContacts = (params) => http.post('/api/memberContacts/remMemberCon', params)

/* 
 *设置紧急联系人*
 */
export const PostSetContacts = (params) => http.post('/api/memberContacts/setMemberCon', params)

/* 
 *编辑紧急联系人*
 */
export const PostEditContacts = (params) => http.post('/api/memberContacts/editMemberCon', params)

/**发送短信**/
export const sendSms = (data) => http.post('/api/sendSms', data);

/**验证码登录发送短信**/
export const applyLoginBySms = (data) => http.post('/api/applyLoginBySms ', data);

/**短信登录**/
export const loginBySms = (data) => http.post('/api/loginBySms', data);

/**短信校验**/
export const checkSms = (data) => http.post('/api/checkSms', data);

/**注册**/
export const regMember = (data) => http.post('/api/regMember', data);

/**重置密码**/
export const resetMemberPwd = (data) => http.post('/api/resetMemberPwd', data);

/**获取家庭列表**/
export const getFamilyList = (params) => http.get('/api/family/list', params);

/**获取设备列表**/
export const getDeviceList = (data) => http.post('/api/device/list', data);

/**获取设备列表**/
export const getDeviceListState = (data) => http.post('/api/device/listState', data);

/* 
 *删除设备*
 */
export const PostDeviceDel = (params) => http.post('/api/device/remDevice', params)

/**设备事件查询**/
export const selectEventInfo = (data) => http.post('/api/event/selectEventInfo', data);
/**事件信息**/
export const getEventList = () => http.get('/api/event/list');

/* 
 *查询协议*
 */
export const PostSelectAgreement = (params) => http.post('/api/agreement/getAgreementInfo', params)

/* 
 *用户协议*
 */
export const PostUserAgreement = (params) => http.post('/api/agreement/selectAgreement', params)

/* 
 *查询版本*
 */
export const PostVersionInfo = (params) => http.get('/api/version/selectVersionInfo', params)

/* 
 *修改密码
 */
export const PostUpdatePwd = (params) => http.post('/api/memberInfo/updatePwd', params)

export const PostUpdatePhone = (params) => http.post('/api/memberInfo/updatePhone', params)

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
 * 绑定/
 */
export const setDevice = (params) => http.post('/api/device/setDevice', params);


/**
 * 解绑设备
 */
export const relDevice = (params) => http.post('/api/device/relDevice', params);


/**
 * 修改设备名称
 */
export const PosteditDevice = (params) => http.post('/api/device/editDevice', params);

/**
 * 获取房间列表
 */
export const getRoomList = (params) => http.post('/api/room/list', params);

/**
 * 查询消息
 */
export const getMessage = (params) => http.post('/api/message/familyList', params);

/**
 * 标志所有消息状态
 */
export const PostSetBatchMsgInfo = (params) => http.post('/api/message/setBatchMsgInfo', params);
/* 
 *获取房间列表
 */
export const PostRoomList = (params) => http.post('/api/room/list', params)

/* 
 *删除家庭
 */
export const PostDelRoom = (params) => http.post('/api/room/remRoom', params)

/* 
 *创建房间
 */
export const PostAddRoom = (params) => http.post('/api/room/creRoom', params)

/* 
 *编辑房间
 */
export const PostEditRoom = (params) => http.post('/api/room/editRoom', params)

/* 
 *获取通讯录列表
 */
export const PostDevicePhoneList = (params) => http.post('/api/device/getDevicePhone', params)

/* 
 *创建电话
 */
export const PostAddDevicePhone = (params) => http.post('/api/device/creDevicePhone', params)

/* 
 *设置普通电话
 */
export const PostSetDevicePhone = (params) => http.post('/api/device/setDevicephone', params)

/* 
 *设置SOS电话
 */
export const PostSetSosDevicePhone = (params) => http.post('/api/device/setSOSDevicePhone', params)

/**
 * 获取设备定位
 */
export const GetDeviceSize = (params) => http.get('/api/device/getDeviceNow', params)

/**
 * 获取分享家庭列表
 */

export const PostSharelist = (params) => http.post('/api/family/sharelist', params)

/**
 * 创建分享家庭
 */
export const PostShareFamily = (params) => http.post('/api/family/shareFamily', params)

/**
 * 删除分享家庭
 */
export const PostRemShareFamily = (params) => http.post('/api/family/remShareFamily', params)

/**
 * 获取公告信息
 */
export const GetReadInfo = () => http.get('/api/notice/unReadNum');

/**
 * 获取公告
 */
export const GetSysNotice = () => http.get('/api/notice/sysNotice');

/**
 * 获取公告
 */
export const postSetNoticeFlag = (params) => http.post('/api/notice/setNoticeFlag', params, {
	error: false,
	showLoading: false
});

/**
 * 设置电子围栏
 */
export const PostAddFence = (params) => http.post('/api/fence/addFence', params);


/**
 * 添加设备
 */
export const PostcreDevice = (params) => http.post('/api/device/creDevice', params);

/**
 * 创建子区域
 */
export const PostRadarDevice = (params) => http.post('/api/roomZone/setRadarDevice', params);

/**
 * 删除子区域
 */
export const PostRemRadarDevice = (params) => http.post('/api/roomZone/remRadarDevice', params);

/**
 * 获取子区域列表
 */
export const GetRoomZone = (params) => http.get('/api/roomZone/getRoomZoon', params);

/**
 * 获取字典信息
 */
export const GetDict = () => http.get('/api/dictData/getDict', {}, {
	error: false,
	showLoading: false
});

/**
 * 开启获取设备位置(实时)
 */
export const GetStartDevice = (params) => http.get('/api/device/startNowInfo', params);
/**
 * 关闭获取设备位置(实时)
 */
export const GetEndDevice = (params) => http.get('/api/device/endNowInfo', params);

/**
 * 获取设备位置(实时)
 */
export const GetNowInfo = (params) => http.get('/api/device/getNowInfo', params, {
	showLoading: false
});

/**
 * 原手机短信验证
 */
export const PostUpdatePhoneBySms = (params) => http.post('/api/memberInfo/updatePhoneBySms', params)


/**
 * 密码验证
 */
export const PostUpdatePhoneByPwd = (params) => http.post('/api/memberInfo/updatePhoneByPwd', params)


/**
 * 新手机修改短信验证
 */
export const PostUpdatePhoneByToken = (params) => http.post('/api/memberInfo/updatePhoneByToken', params)
