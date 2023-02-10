/**设备参数**/
export const INIT_DEIVCE_SET = {
	deviceId: '',
	deviceName: '',
	deviceType: '',
	deviceNo: '',
	familyId: '',
	roomId: '',
	roomLeft: 3,
	roomHeight: 6,
	roomRight: 3,
	roomLength: 6,
	existFlag: 0,
	fallFlag: 0,
	entryTime: 0,
	departureTime: 0,
	startTime: 0,
	endTime: 0,
	inMonitorFlag: 0,
	outMonitorFlag: 0,
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
	existFlag: 0,
	/**跌倒监控**/
	fallFlag: 0,
	x1: 0,
	x2: 0,
	y1: 0,
	y2: 0,
	z1: 100,
	z2: 100,
	old: {
		x: 0,
		y: 0
	},
	x: 0,
	y: 0,
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
	entryTime: 1,
	/**离开报警区域时间**/
	departureTime: 1,
	/**开始监控时间**/
	startTime: 1,
	/**结束监控时间**/
	endTime: 1,
	/**进入监控区域报警**/
	inMonitorFlag: 0,
	/**离开监控区域报警**/
	outMonitorFlag: 0
};
