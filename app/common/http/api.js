/*
 * @Author: zhanch
 * @Date: 2022-12-13 14:37:03
 * @LastEditors: Please set LastEditors
 * @FilePath: /http/api.js
 * @Description: 请求接口
 */

import http from './requset/request.js';
/**
 * 获取请求验证码
 */
export const getDemo = () => http.get('/api/captchaImage');
export const postDemo = () => http.post('/api/sendSms', {
	"uuid": "c8916a767204456fa970c675c71048e5",
	"captcha": "6d23"
});

export const mergeDemo = () => http.merge([{
		url: '/api/captchaImage',
		method: 'GET',
		data: {},
	},
	{
		url: '/api/sendSms',
		data: {
			"uuid": "c8916a767204456fa970c675c71048e5",
			"captcha": "6d23"
		},
	}
], {
	showLoading: false
});

export const forIndex = () => http.merge([{
		url: '/api/family/list',
		method: 'GET',
		data: {
			pageSize: 10000
		},
	},
	{
		url: '/api/device/listState',
		method: 'post',
		data: {
			pageSize: 10000
		},
	}
], {
	showLoading: false,
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
export const applyLoginBySms = (data) => http.post('/api/applyLoginBySms', data);

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
export const PostVersionInfo = (params) => http.get('/api/version/selectVersionInfo', params, {
	error: false
})

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
export const PostSetBatchMsgInfo = (params) => http.post('/api/message/setBatchMessageInfoByParams', params);
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
 * 设置电子围栏2
 */
export const PostAddFence2 = (params) => http.post('/api/careCard/setDeviceFence', params);

/**
 * 获取电子围栏信息
 */
export const GetFenceInfo = (params) => http.get('/api/fence/getDeFeInfo', params);

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
export const PostUpdatePhoneByToken = (params) => http.post('/api/memberInfo/updatePhoneByToken', params, {
	error: false,
})

/**
 * 修改昵称
 */
export const PostUpdateNick = (params) => http.post('/api/memberInfo/updateMemInfo', params)

/**
 * 修改头像
 */
export const PostUpdateAvatar = (params) => http.upload('/api/memberInfo/avatar', params)

/**
 *获取亲情号码*
 */
export const GetFamilyNumber = (params) => http.get('/api/careCard/getFamilyNumber', params, {})

/**
 *保存亲情号码*
 */
export const PostSetFamilyNumber = (params) => http.post('/api/careCard/setFamilyNumber', params, {})

/**
 *保存通讯录白名单*
 */
export const PostAddOrUpdateAddressBook = (params) => http.post('/api/careCard/addOrUpdateAddressBook', params, {})

/**
 *获取通讯录白名单*
 */
export const GetAddressBook = (params) => http.get('/api/careCard/getAddressBook', params, {})


/**
 *删除通讯录白名单*
 */
export const PostDeleteAddressBook = (params) => http.post('/api/careCard/deleteAddressBook', params, {})


/**
 *开始远程监听*
 */
export const PostRemoteMonitor = (params) => http.post('/api/careCard/remoteMonitor', params, {})

/**
 *获取时段禁用时段*
 */
export const GetPeriodDisableList = (params) => http.get('/api/careCard/getPeriodDisableList', params, {})

/**
 *设置时段禁用时段*
 */
export const PostSetPeriodDisable = (params) => http.post('/api/careCard/setPeriodDisable', params, {})

/**
 *删除时段禁用时段*
 */
export const PostDeletePeriodDisable = (params) => http.post('/api/careCard/deletePeriodDisable', params, {})


/**
 * 添加/修改无人预警
 */
export const PostSetRadarWaveNobodyWarn = (params) => http.post('/api/device/setRadarWaveNobodyWarn', params, {});

/**
 * 删除无人预警
 */
export const PostDelRadarWaveNobodyWarn = (params) => http.post('/api/device/delRadarWaveNobodyWarn', params, {});

/**
 * 设置雷达波
 */
export const PostSetRadarWaveDevice = (params) => http.post('/api/device/setRadarWaveDevice', params, {});

/**
 * 设置雷达波
 */
export const PostSetRadarWareShadowZone = (params) => http.post('/api/roomZone/setRadarWareShadowZone', params, {});

/**
 * 添加离床预警区域
 */
export const PostSetRadarWareBedZone = (params) => http.post('/api/roomZone/setRadarWareBedZone', params, {});

/**
 * 添加/修改预警规则
 */
export const PostSetRadarWaveLeaveBedRulesDate = (params) => http.post('/api/roomZone/setRadarWaveLeaveBedRulesDate',
	params, {});

/**
 * 删除预警规则
 */
export const PostDelRadarWaveLeaveBedRulesDate = (params) => http.post('/api/roomZone/delRadarWaveLeaveBedRulesDate',
	params, {});

/**
 * 重启设备
 */
export const PostRebootDevice = (params) => http.post('/api/careCard/rebootDevice', params, {});

/**
 * 获取位置守护
 */
export const GetLocationGuardList = (params) => http.get('/api/careCard/getLocationGuardList', params, {});

/**
 * 设置位置守护
 */
export const PostSetLocationGuard = (params) => http.post('/api/careCard/setLocationGuard', params, {});

/**
 * 添加家庭成员
 */
export const PostinsertTHuman = (params) => http.post('/api/tHuman/insertTHuman', params, {});
/**
 * 删除位置守护
 */
export const PostDeleteLocationGuard = (params) => http.post('/api/careCard/deleteLocationGuard', params, {});

/**
 * 电子牵挂卡绑定
 */
export const PostUpdateCareCardBind = (params) => http.post('/api/careCard/updateCareCardBind', params, {});


/**
 * 根据家庭id查询电子牵挂卡
 */
export const PostSelectTHumanListByFamilyId = (params) => http.get('/api/tHuman/selectTHumanListByFamilyId',
	params, {});

/**
 * 电子牵挂卡解绑
 */
export const PostCareCardUnBind = (params) => http.post('/api/careCard/careCardUnBind',
	params, {});

/**
 * 修改人员信息
 */
export const PostUpdateTHuman = (params) => http.post('/api/tHuman/updateTHuman',
	params, {});


/**
 * 删除人员信息
 */
export const PostDeleteTHumanByHumanIds = (params) => http.post('/api/tHuman/deleteTHumanByHumanIds',
	params, {});
/**
 * 获取位置追踪
 */
export const GetsetAddressBook = (params) => http.get('/api/careCard/track/points', params, {
	showLoading: false
});

/**
 * 获取位置追踪（最后一次）
 */
export const GetLastPoint = (params) => http.get('/api/careCard/track/lastPoint', params, {
	showLoading: false
});

/**
 * 获取紧急联系人列表
 */
export const GetMemberContactskList = (params) => http.get('/api/memberContacts/list', params, {});

/**
 * 设置紧急联系人列表
 */
export const PostSetMemberConWithFamilyId = (params) => http.post('/api/memberContacts/setMemberConWithFamilyId',
	params, {});

//-----------------------------------------------------------------------健康手表-----------------------------------------------------------------------

/**
 * 获取手表跌倒检测开关的状态
 */
export const GetFallCheckInfo = (params) => http.get('/api/tWatch/getFallCheckInfo', params, {});

/**
 * 设置手表跌倒检测开关
 */
export const PostUpdateFallCheck = (params) => http.post('/api/tWatch/updateFallCheck', params, {});


/**
 * 获取手表自动定位的状态
 */
export const GetAutoLocationInfo = (params) => http.get('/api/tWatch/getAutoLocationInfo', params, {});

/**
 * 设置手表自动定位的状态
 */
export const PostUpdateAutoLocation = (params) => http.post('/api/tWatch/updateAutoLocation', params, {});


/**
 * 查询日血压数据
 */
export const GetListBloodPressureByDay = (params) => http.get('/api/tWatch/listBloodPressureByDay', params, {});
/**
 * 查询周血压数据
 */
export const GetListBloodPressureByWeek = (params) => http.get('/api/tWatch/listBloodPressureByWeek', params, {});

/**
 * 查询日心率数据
 */
export const GetListHeartRateByDay = (params) => http.get('/api/tWatch/listHeartRateByDay', params, {});

/**
 * 查询周心率数据
 */
export const GetListHeartRateByWeek = (params) => http.get('/api/tWatch/listHeartRateByWeek', params, {});

/* 
 *查询彩虹图数据 *
 */
export const GetCaiHongData = (params) => http.get('/api/tWatch/rainbowDiagram', params, {});

/**
 * 查询日血氧数据
 */
export const GetListBloodOxygenByDay = (params) => http.get('/api/tWatch/listBloodOxygenByDay', params, {});

/**
 * 查询周血氧数据
 */
export const GetListBloodOxygenByWeek = (params) => http.get('/api/tWatch/listBloodOxygenByWeek', params, {});

/**
 * 查询心电数据
 */
export const GetListElectrocardiogramByDay = (params) => http.get('/api/tWatch/listElectrocardiogramByDay', params, {});


/**
 * 插入心电备注信息
 */
export const PostAddElectrocardiogramReMake = (params) => http.post('/api/tWatch/addElectrocardiogramReMake',
	params, {});


/**
 * 获取通讯录
 */
export const GetWatchAddressBook = (params) => http.get('/api/tWatch/getAddressBook', params, {});

/**
 * 下发通讯录
 */
export const PostWatchContactsSync = (params) => http.post('/api/tWatch/contactsSync', params, {});

/**
 * 删除通讯录
 */
export const PostWatchDeleteAddressBook = (params) => http.post('/api/tWatch/deleteAddressBook', params, {});

/**
 * 绑定手表
 */
export const PostUpdateWatchBind = (params) => http.post('/api/tWatch/updateWatchBind', params, {});

/**
 * 解绑手表
 */
export const PostWatchUnBind = (params) => http.post('/api/tWatch/watchUnBind', params, {});

/**
 * 查询日步数数据
 */
export const GetListStepNumByDay = (params) => http.get('/api/tWatch/listStepNumByDay', params, {});

/**
 * 查询周步数数据
 */
export const GetListStepNumByWeek = (params) => http.get('/api/tWatch/listStepNumByWeek', params, {});

/**
 * 查询日卡路里数据
 */
export const GetListCalorieByDay = (params) => http.get('/api/tWatch/listCalorieByDay', params, {});

/**
 * 查询周卡路里数据
 */
export const GetListCalorieByWeek = (params) => http.get('/api/tWatch/listCalorieByWeek', params, {});

/**
 * 查询日时长数据
 */
export const GetListExerciseDurationByDay = (params) => http.get('/api/tWatch/listExerciseDurationByDay', params, {});

/**
 * 查询周时长数据
 */
export const GetListExerciseDurationByWeek = (params) => http.get('/api/tWatch/listtExerciseDurationByWeek',
	params, {});

/*
 * 获取手表信息
 */
export const GetWatchInfo = (params) => http.get('/api/tWatch/getWatchInfo', params, {});

/*
 * 获取手表最后定位
 */
export const GetWatchTrack = (params) => http.get('/api/tWatch/track/lastPoint', params, {
	showLoading: false
});

/**
 * 查询活动周平均数据
 */
export const GetExerciseAvg = (params) => http.get('/api/tWatch/exerciseAvg', params, {});

/*
 * 设置手表sim卡号码
 */
export const PostSetSimNumber = (params) => http.post('/api/tWatch/setSimNumber', params, {});

/*
 * 获取手表sim卡号码
 */
export const GetSimNumber = (params) => http.get('/api/tWatch/getSimNumber', params, {});

/*
 * 数据一键同步
 */
export const PostWatchDataSync = (params) => http.post('/api/tWatch/dataSync', params, {});

/*
 * 获取活动目标
 */
export const GetExerciseTarget = (params) => http.get('/api/tWatch/getExerciseTarget', params, {});

/*
 * 新增/修改活动目标
 */
export const PostCreateExerciseTarget = (params) => http.post('/api/tWatch/createExerciseTarget', params, {});

/*
 * 获取家庭成员信息
 */
export const GetHumanInfo = (params) => http.get('/api/tWatch/getHumanInfo', params, {});

/*
 * 设置家庭成员信息
 */
export const PostSetHumanInfo = (params) => http.post('/api/tWatch/setHumanInfo', params, {});

/*
 * 获取月份数据标记
 */
export const GetMonthDataFlag = (params) => http.get('/api/tWatch/getMonthDataFlag', params, {});

/*
 * 获取牵挂卡当前位置
 */
export const GetNowLocation = (params) => http.get('/api/careCard/getNowLocation', params, {
	showLoading: false
});

/*
 * 手表设备实时定位
 */
export const GetWatchLocation = (params) => http.post('/api/tWatch/location', params, {
	showLoading: false
});

/**
 * 极光推送设置用户id
 */
export const PsotSetJGInfo = (params) => http.post('/api/setJGInfo', params, {
	showLoading: false
});

/**
 * 睡眠日数据
 */
export const GetSleepDayData = (params) => http.get('/api/tWatch/listSleepByDay', params, {
	// showLoading: false
});

/**
 * 睡眠周数据
 */
export const GetSleepWeekData = (params) => http.get('/api/tWatch/listSleepByWeek', params, {
	// showLoading: false
});

/**
 * 睡眠质量
 */
export const GetDaySleepQuality = (params) => http.get('/api/tWatch/getDaySleepQuality', params, {
	// showLoading: false
});
