/**设备参数**/
export const INIT_DEIVCE_SET = {
	deviceId: '',
	deviceName: '',
	deviceType: '',
	deviceNo: '',
	familyId: '',
	roomId: '',
	roomLeft: 3,
	roomHeight: 4,
	roomRight: 3,
	roomLength: 4,
	existFlag: 0,
	fallFlag: 1,
	entryTime: 1,
	departureTime: 0,
	/**开始监控时间**/
	startTime: '00:00',
	/**结束监控时间**/
	endTime: '23:59',

	inMonitorFlag: 1,
	outMonitorFlag: 1,
};

/**监控子区域**/
export const ZONE = {
	zoneType: '1',
	/**区域id**/
	roomZoneId: '',
	/**设备id**/
	deviceId: '',
	/**房间id**/
	roomId: '',
	/**区域名称**/
	name: '',
	/*监控类型 0-监控区域  1-私人区域**/
	existFlag: 1,
	/**跌倒监控**/
	fallFlag: 1,
	x1: 0,
	x2: 0,
	y1: 0,
	y2: 0,
	z1: 0,
	z2: 1,
	width: '',
	height: '',
	// /**进入报警区域时间**/
	// entryTime: null,
	// /**离开报警区域时间**/
	// departureTime: null,
	// /**开始监控时间**/
	// startTime: null,
	// /**结束监控时间**/
	// endTime: null,
	/**进入报警区域时间**/
	entryTime: 0,
	/**离开报警区域时间**/
	departureTime: 0,
	/**开始监控时间**/
	startTime: '00:00',
	/**结束监控时间**/
	endTime: '23:59',
	/**进入监控区域报警**/
	inMonitorFlag: 1,
	/**离开监控区域报警**/
	outMonitorFlag: 1
};

/**
 * 查询条件
 */
const dateTime = new Date();
export const INIT_SELECT = {
	startDate: dateTime.setDate(dateTime.getMonth() - 3), // 默认查近三个月
	endDate: new Date().getTime(),
	deviceType: '',
	eventlevel: '',
	readFlag: '',
	pageSize: 10,
	pageNum: 1
};


/**
 * 星期关系
 */
export const WEEKMAP = {
	0: '星期一',
	1: '星期二',
	2: '星期三',
	3: '星期四',
	4: '星期五',
	5: '星期六',
	6: '星期日',
}