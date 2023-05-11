/*
 * @Author: zhanch
 * @Date: 2023-01-09 16:01:39
 * @FilePath: /common/sdk/vpsdk.js
 * @Description: 激活设备
 */

import {
	env
} from "../../config/env";
import {
	isApp,
	isIos
} from "../utils/util";


// buf数据处理工具
import protobuf from '@/static/js/weichatPb/protobuf.js';

// 设备接口工具
import message from '@/static/js/weichatPb/message.js';
import {
	resolve
} from "../../static/js/weichatPb/src/path";


class VpActivation {
	/**连接事件字典**/
	connentMap = {
		1: '正在建立与设备的连接',
		2: '设备连接到Wi-Fi',
		3: '将设备连接到您的帐户',
		4: '云配对',
		5: '配对完成',
		6: '重新启动设备',
		998: '正在检查更新',
		999: '正在更新设备',
		7: '正在将设备重新启动到工厂',
		8: '设备正在扫描Wifi网络',
		// 自定义事件
		100: '正在获取设备token',
		101: '获取设备token成功',
		102: '正在尝试连接设备',
		103: '网络账号推送',
		104: '选择wifi',
		105: 'token获取失败,请检查网络状态是否正常',
		106: '获取蓝牙权限失败，请确认蓝牙已正常开启',
		107: '获取wifi权限失败，请确认wifi已正常开启',
		108: '正在检测系统权限',
		109: '蓝牙已正常开启',
		110: 'wifi正常开启',
		111: '搜寻蓝牙设备失败，请确认设备是否为蓝灯闪烁状态',
		112: '搜寻设备成功，正在与设备建立连接',
		113: '连接设备成功',
		114: '连接设备失败，请确认设备是否为蓝灯闪烁状态',
		115: '开始选择设备服务',
		116: '未搜寻到主服务',
		117: '选择设备服务失败',
		118: '开始读取蓝牙特征值',
		119: '读取特征值失败, 请确认设备是否为蓝灯闪烁状态',
		200: '获取wifi信息失败，请确认手机是否已经连接上wifi',
		201: '正在搜寻设备',
		202: '搜寻设备成功，正在尝试配对'
	}

	/**wifi信息**/
	wifiInfo = {
		ssid: '',
		pwd: ''
	};

	/**设备信息**/
	deviceId = '';

	MessageRoot = protobuf.Root.fromJSON(message);

	callBack = null;

	constructor() {}

	/**
	 * 初始化
	 */
	async init(cb = n => {
		console.log('obj-----', n)
	}) {
		console.log('开始权限检测-------', typeof cb)
		try {
			this.callBack = n => cb(n); // 设置回调
			// 检测系统权限，判断蓝牙与wifi是否开启
			console.log('开始权限检测-------1')
			this.pageEventCall(108);
			console.log('开始权限检测-------2')
			if (!await this.bluetoothCheck()) return; // 检测蓝牙
			if (!await this.wifiCheck()) return; // 检测wifi
			const wifi = await this.getWifiList();
			console.log(wifi, 'wifi--------')
			this.callBack({
				type: 'wifi',
				data: [{
					ssid: wifi.SSID
				}]
			});
		} catch (e) {
			console.log(e, 'error-------')
			//TODO handle the exception
		}

	}

	/**
	 * 启动连接
	 * @param {Object} ssid
	 * @param {Object} pwd
	 */
	async connect(ssid, pwd) {
		this.wifiInfo = {
			ssid,
			pwd
		}
		// 步骤1 获取token
		const token = await this.getToken();
		if (!token) return;
		console.log('开始获取蓝牙---------');
		/**=========步骤2 连接蓝牙==========**/
		await this.bluetoothConnect();

		/**=========步骤3 连接wifi==========**/
		await this.wiFiConnect();
	}

	/**
	 * 获取wifi列表
	 */
	getWifiList() {
		return new Promise(resolve => {
			uni.getConnectedWifi({
				partialInfo: true,
				success: data => {
					console.log(data, 'data------');
					resolve(data.wifi)
				},
				fail: error => {
					this.pageEventCall(200, (error.errCode || -1));
				}
			})
		})

	}

	/**
	 * 连接蓝牙
	 */
	bluetoothConnect() {
		return new Promise(async resolve => {
			try {
				this.
				// 通过蓝牙检测附近设备
				this.deviceId = await this.startBluetoothDevicesDiscovery();
				// 连接蓝牙
				await this.bindCreateBLEConnection(this.deviceId);
				//  获取要连接设备的服务
				const {
					uuid
				} = await this.getBLEDeviceServices(this.deviceId);
				// 获取要连接的设备属性
				await this.getBLEDeviceCharacteristics(this.deviceId, uuid);
				resolve();
			} catch (e) {
				//TODO handle the exception
				console.log(e, 'bluetoothConnect-----');
			}

		})
	}

	/**
	 * 获取设备字典
	 */
	getVPType() {
		return {
			WifiCred: this.MessageRoot.lookupType("WifiCred"),
			ToDeviceMessage: this.MessageRoot.lookupType("ToDeviceMessage"),
			CloudDetails: this.MessageRoot.lookupType("CloudDetails"),
			Pair: this.MessageRoot.lookupType("Pair"),
			PairingComplete: this.MessageRoot.lookupType("PairingComplete"),
			ToAppMessage: this.MessageRoot.lookupType("ToAppMessage"),
			PairResult: this.MessageRoot.lookupType("PairResult")
		}
	}

	/**
	 * 蓝牙权限检测
	 */
	bluetoothCheck() {
		return new Promise(resolve => {
			console.log('========开始验证蓝牙========');
			// 验证蓝牙权限
			uni.openBluetoothAdapter();
			console.log('========蓝牙模块启动成功========');
			uni.getBluetoothAdapterState({
				success: res => {
					//如果res.avaliable==false 说明没打开蓝牙 反之则打开
					console.log(res.available, '========蓝牙是否打开========');
					this.pageEventCall(109);
					resolve(res.available != false)
				},
				fail: error => {
					this.pageEventCall(106);
					resolve(false);
				}
			})
		});
	}

	/**
	 * wifi检测
	 */
	wifiCheck() {
		return new Promise(resolve => {
			console.log('========开始验证wifi========');
			uni.startWifi({ // 验证wifi权限
				success: res => {
					this.pageEventCall(110);
					resolve(true)
				},
				fail: err => {
					this.pageEventCall(107);
					resolve(false)
				}
			})
		});
	}

	/**
	 * 获取token
	 */
	getToken() {
		this.pageEventCall(100);
		return new Promise(resolve => {
			const {
				email,
				password,
				url
			} = env.device;
			uni.request({
				url,
				data: {},
				method: 'GET',
				header: {
					'Accept': 'application/json',
					'Content-Type': 'application/json',
					'Authorization': `Basic ${btoa(email + ':' + password)}`
				},
				withCredentials: true,
				success: result => {
					resolve(result.data);
					this.pageEventCall(101);
					console.log(result, 'getToken');
				},
				fail: err => {
					this.pageEventCall(105);
					resolve();
					console.log(err, 'getTokenErr');
				}
			});
		});
	}

	/**
	 * 检测附近设备
	 */
	startBluetoothDevicesDiscovery() {
		return new Promise(resolve => {
			// 查询设备回调
			let timeout, interval;
			const deviceFoundCallBck = res => {
				console.log('查询附近蓝牙设备---', res.device.name);
				res.devices.forEach(device => {
					if (!device.name && !device.localName) return;
					//wh开头的是雷达设备
					if (device.name.startsWith("wh")) {
						// 停止扫描蓝牙设备
						uni.stopBluetoothDevicesDiscovery();
						console.log("设备name：" + device.name + " 设备id：" + device.deviceId)
						this.pageEventCall(202);
						resolve(device.uuid);
					}
				})
			};
			this.pageEventCall(201);
			uni.startBluetoothDevicesDiscovery({
				allowDuplicatesKey: true,
				success: res => {
					interval = setInterval(() => { // 每三秒查一次
						uni.getBluetoothDevices({
							success: deviceFoundCallBck
						})
					}, 3000);
					setInterval(() => {
						uni.onBluetoothDeviceFound(
							deviceFoundCallBck
					}, 1000)
				},
				fail: err => {
					this.pageEventCall(111, '错误码' + (err.code || -1));
				}
			})
		})
	}


	/**
	 * 连接雷达
	 */
	bindCreateBLEConnection(deviceId) {
		return new Promise(resolve => {
			this.pageEventCall(112);
			if (deviceId) {
				uni.createBLEConnection({
					deviceId,
					success: res => {
						this.pageEventCall(113);
						resolve(true)
					},
					fail: err => this.pageEventCall(114, '错误码' + (err.code || -1))
				})
			}
		})
	}


	/**
	 * 选择设备服务
	 */
	getBLEDeviceServices(deviceId) {
		return new Promise(resolve => {
			uni.getBLEDeviceServices({
				deviceId,
				success: data => {
					const service = res.services.find(ele => ele.isPrimary);
					if (service) {
						resolve(service)
					} else {
						this.pageEventCall(116)
					}
				},
				fail: err => {
					fail: err => this.pageEventCall(117, '错误码' + (err.code || -1))
				}
			})
		})
	}

	/**
	 * 获取要连接得设备属性
	 */
	getBLEDeviceCharacteristics(deviceId, uuId) {
		this.pageEventCall(118);
		const serviceId = isIos() ? uuId.toUpperCase() : uuId.toLowerCase()
		return new Promise(resolve => {
			uni.getBLEDeviceCharacteristics({
				deviceId,
				success: res => {
					res.characteristics.forEach(item => {
						if (item.properties.notify || item.properties.indicate) {
							console.log("============notify==========")
							wx.notifyBLECharacteristicValueChange({
								deviceId,
								serviceId,
								characteristicId: item.uuid.toUpperCase(),
								state: true,
							})

						}
					});
					resolve();
				},
				fail: err => {
					this.pageEventCall(119);
				}
			})
		})
	}


	/**
	 * 监听蓝牙设备推送信息
	 */
	onBLECharacteristicValueChange() {
		uni.onBLECharacteristicValueChange(res => {

			// console.log("收到设备恢复的消息：" + ab2hex(characteristic.value))
			// var decode = ToAppMessage.decode(characteristic.value);
		})
	}

	/**
	 * 连接wifi
	 */
	wiFiConnect() {

	}


	// return new Promise(resolve => {
	// 		uni.startBluetoothDevicesDiscovery({
	// 				allowDuplicatesKey: true,
	// 				success: res => {
	// 					// 监听检测到得设备
	// 					uni.onBluetoothDeviceFound(() => {

	// 					})
	// 				});
	// 		})
	// });
	// }


	/**
	 * 配网成功回调
	 */
	fairFinish = e => {
		console.log(e, 'onPairFinish');
		if (e.isSuccess === 'fail') {
			this.stopPairing(); // 失败强制结束sdk
		}
		this.callBack({
			type: e.isSuccess ? 'success' : 'fail',
			data: e.deviceId
		});
	};


	/**
	 * 配网事件
	 */
	pairEvent = e => {
		this.pageEventCall(e.eventType);
	}

	/**
	 * 选择wifi
	 */
	selectWifi = e => {
		console.log(e.wifiList, 'wifi信息');
		this.pageEventCall(104);
		this.callBack({
			type: 'wifi',
			data: e.wifiList
		});
	}

	/**
	 * 设备连接
	 * @param {Object} cb
	 */
	async connect2(cb) {
		this.callBack = n => cb(n); // 设置回调
		// 获取token
		this.pageEventCall(100);
		const {
			token: {
				idToken
			},
			uid
		} = await this.getToken();
		// 获取token
		this.pageEventCall(101);
		console.log('开始设备配网');
		console.log(this.vpModule, 'vpModule');
		try {
			// 配网成功监听
			plus.globalEvent.addEventListener('onPairFinish', this.fairFinish);
			// 配网事件监听
			plus.globalEvent.addEventListener('onPairEvent', this.pairEvent);
			// 选择wifi监听
			plus.globalEvent.addEventListener('onWifiShouldSelect', this.selectWifi);
			// 注册事件
			cb({
				type: 'event',
				data: {
					msg: this.connentMap[102],
					code: 102
				}
			});
			this.vpModule.startPairing(uid, idToken); // 开始配对
			console.log('等待设备配网');
		} catch (e) {
			console.log('配网失败', e);
			//TODO handle the exception
		}
	}

	/**
	 * 执行页面回调
	 */
	pageEventCall(code, msg = '') {
		console.log('pageEventCall------', this.connentMap[code])
		this.callBack({
			type: 'event',
			data: {
				msg: this.connentMap[code] + msg || '处理中...',
				code
			}
		});
	}

	/**
	 * 结束配网
	 */
	stopPairing() {
		this.vpModule.stopPairing && this.vpModule.stopPairing();
		console.log(typeof plus.globalEvent.removeEventListener, '结束配网');
		// 配网成功监听
		plus.globalEvent.removeEventListener('onPairFinish', this.fairFinish);
		// 配网事件监听
		plus.globalEvent.removeEventListener('onPairEvent', this.pairEvent);
		// 选择wifi监听
		plus.globalEvent.removeEventListener('onWifiShouldSelect', this.selectWifi);
	}

	/**
	 * 连接wifi
	 */
	connectWifi(ssid, bssid, rssi, password) {
		console.log(ssid, bssid, rssi, password, 'connectWifi');
		this.pageEventCall(103);
		this.vpModule.connectWifi(ssid, bssid, rssi, password);
	}
}

export const vpActivation = new VpActivation();