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

import {
	WifiCred,
	ToDeviceMessage,
	CloudDetails,
	Pair,
	PairingComplete,
	ToAppMessage,
	PairResult,
	ToDeviceMessageType
} from '@/common/utils/messageUtil.js';

import {
	ab2hex,
	hexToString,
	stringToBytes,
	buf2hex,
	hex2ArrayBuffer
} from '@/common/utils/toBufUtils.js'


class VpActivation {
	/**连接事件字典**/
	connentMap = {
		// 权限检查
		101: '正在检测系统权限',
		102: '蓝牙已正常开启',
		103: 'wifi正常开启',
		104: '获取wifi权限失败，请确认wifi已正常开启',
		105: '获取蓝牙权限失败，请确认蓝牙已正常开启',
		106: '权限检查成功, 正在获取设备wifi',
		107: '正在查询设备wifi成功,请输入密码',
		108: '获取当前连接的wifi失败，请确认手机已经连接上wifi网络',
		// token
		200: '正在获取设备token',
		201: '获取设备token成功',
		202: 'token获取失败,请检查网络状态是否正常',

		// 连接蓝牙
		301: '正在搜寻附近设备',
		302: '搜寻蓝牙设备失败，请确认设备是否为蓝灯闪烁状态',
		303: '搜寻设备成功，正在尝试配对',
		304: '正在尝试连接设备',
		305: '连接设备成功',
		306: '连接设备失败，请确认设备是否为蓝灯闪烁状态',
		307: '开始选择设备服务',
		308: '未搜寻到主服务',
		309: '选择设备服务失败，请确认设备是否为蓝灯闪烁状态',
		310: '开始读取蓝牙特征值',
		311: '读取特征值失败, 请确认设备是否为蓝灯闪烁状态',
		// 推送消息
		401: '开始连接wifi',
		403: 'wifi连接成功，正在将设备推送至云端',
		402: '连接wifi失败，请检查wifi密码是否正确',
		404: '推送云端成功，正在进行用户配对',
		405: '配对成功，正在激活设备',
		406: '激活成功，正在同步云端',
		501: '激活设备成功',
		502: '激活设备失败，请检查网络状态是否正常',
		503: '设备连接超时，请检查设备状态后重试'

	}

	/**wifi信息**/
	wifiInfo = {
		ssid: '',
		pwd: ''
	};

	baseData = {
		deviceId: '',
		serviceId: '21A07E04-1FBF-4BF6-B484-D319B8282A1C',
		WritecharacteristicId: '21A07E06-1FBF-4BF6-B484-D319B8282A1C',
		ReadcharacteristicId: '21A07E05-1FBF-4BF6-B484-D319B8282A1C',
	}


	callBack = null;

	/**定时器合集**/
	timeouts = [];
	intervals = [];

	/**wifi连接尝试次数**/
	wifiCount = 1;

	/**token信息**/
	token = {};

	constructor() {}

	/**
	 * 初始化
	 */
	async init(cb) {
		if(!isApp())return;
		this.callBack = n => cb(n); // 设置回调
		// 检测系统权限，判断蓝牙与wifi是否开启
		this.pageEventCall(101);
		if (!await this.bluetoothCheck()) return; // 检测蓝牙
		if (!await this.wifiCheck()) return; // 检测wifi
		const wifi = await this.getWifiList();
		this.callBack({
			type: 'wifi',
			data: [{
				ssid: wifi.SSID
			}]
		});
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
		};

		this.timeouts.push(setTimeout(() => { // 五分钟没有连接完成就强制断开
			this.pageErrorCall(503);
		}, 5 * 60 * 1000));
		console.log('wifi信息', this.wifiInfo);
		// 步骤1 获取token
		this.token = await this.getToken();
		if (!this.token) return;
		console.log('开始获取蓝牙---------');
		/**=========步骤2 连接蓝牙==========**/
		await this.bluetoothConnect();
		/**=========步骤3 连接wifi==========**/
		await this.wifiConnect();
	}

	/**
	 * 获取wifi列表
	 */
	getWifiList() {
		return new Promise(resolve => {
			this.pageEventCall(106);
			this.timeouts.push(setTimeout(() =>{
				uni.getConnectedWifi({ // start需要延迟获取列表
					partialInfo: true,
					success: data => {
						console.log(data, 'data------');
						if (data.wifi.SSID) {
							this.pageEventCall(107);
							resolve(data.wifi)
						} else {
							this.pageErrorCall(108);
						}
					},
					fail: error => {
						this.pageErrorCall(108, (error.errCode || -1));
					}
				})
			}, 3000))
			
		})
	}

	/**
	 * 连接蓝牙
	 */
	bluetoothConnect() {
		return new Promise(async resolve => {
			try {
				// 通过蓝牙检测附近设备
				this.baseData.deviceId = await this.startBluetoothDevicesDiscovery();
				console.log(this.baseData.deviceId, 'deviceId------')
				// 连接蓝牙
				await this.bindCreateBLEConnection(this.baseData.deviceId);
				//  获取要连接设备的服务
				const {
					uuid
				} = await this.getBLEDeviceServices(this.baseData.deviceId);
				console.log(uuid, 'uuId----')
				// 获取要连接的设备属性
				this.onBLECharacteristicValueChange(); // 先设置监听
				await this.getBLEDeviceCharacteristics(this.baseData.deviceId, uuid);
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
			PairResult: this.MessageRoot.lookupType("PairResult"),
			CONNECT_WIFI: this.MessageRoot.ToDeviceMessageType.CONNECT_WIFI
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
					this.pageEventCall(102);
					resolve(res.available != false)
				},
				fail: error => {
					this.pageErrorCall(105);
					resolve(false);
				}
			})
		});
	}

	/**
	 * wifi检测
	 */
	wifiCheck() {
		console.log('========开始验证wifi========');
		let count = 3; // wifi和定位权限相关，因此需要多次尝试
		const call = resolve => {
			try{
				console.log(typeof uni.startWifi, 'uni.startWifi');
				uni.startWifi({ // 验证wifi权限
					success: res => {
						this.pageEventCall(103);
						resolve(true)
					},
					fail: err => {
						if (count === 0) {
							this.pageEventCall(104);
							resolve(false)
						} else {
							count--;
							this.timeouts.push(setTimeout(() => {
								call(resolve)
							}, 3000)); // 权限未开三秒后重试
				
						}
				
					}
				})
			}catch(e){
				console.log(e, '-------');
				//TODO handle the exception
			}
			
		};
		return new Promise(call);
	}

	/**
	 * 获取token
	 */
	getToken() {
		this.pageEventCall(200);
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
					this.pageEventCall(201);
					console.log(result, 'getToken');
				},
				fail: err => {
					this.pageErrorCall(202);
					resolve();
					console.log(err, 'getTokenErr');
				}
			});
		});
	}


	/**
	 * 激活设备
	 */
	putPairing(code) {
		try {
			console.log(this.token.token.idToken, 'this.token.idToken');
			console.log(code, 'code------');
			return new Promise(resolve => {
				const {
					par
				} = env.device;
				uni.request({
					url: `${par}/${code}`,
					data: {},
					method: 'put',
					header: {
						'Accept': 'application/json',
						'Content-Type': 'application/json',
						'Authorization': `Bearer ${this.token.token.idToken}`
					},
					withCredentials: true,
					success: result => {
						console.log('putPairingresult', result)
						if (result.statusCode === 200) {
							resolve(result);
						} else {
							resolve(false);
						}
					},
					fail: err => {
						resolve(false);
						console.log(err, 'getTokenErr');
					}
				});
			});
		} catch (e) {
			console.log('putPairin报错', e);
		}

	}


	/**
	 * 检测附近设备
	 */
	startBluetoothDevicesDiscovery() {
		return new Promise(resolve => {
			// 查询设备回调
			let interval;
			const deviceFoundCallBck = res => {
				console.log('查询附近蓝牙设备---', res);
				res.devices.forEach(device => {
					if (!device.name && !device.localName) return;
					//wh开头的是雷达设备
					if (device.name.startsWith("wh")) {
						// 停止扫描蓝牙设备
						uni.stopBluetoothDevicesDiscovery();
						interval && clearInterval(interval);
						console.log("设备name：" + device.name + " 设备id：" + device.deviceId)
						this.pageEventCall(303);
						resolve(device.deviceId);
					}
				})
			};
			this.pageEventCall(301);
			uni.startBluetoothDevicesDiscovery({
				allowDuplicatesKey: true,
				success: res => {
					this.timeouts.push(setTimeout(() => { // 延迟一秒去查询是为了让蓝牙先搜寻到设备
						uni.onBluetoothDeviceFound(deviceFoundCallBck);
						interval = setInterval(() => { // 每三秒查一次已有结果，实测中监听可能没有响应
							uni.getBluetoothDevices({
								success: deviceFoundCallBck
							})
						}, 3000);
						this.intervals.push(interval);
					}, 1000));
				},
				fail: err => {
					this.pageErrorCall(302, '错误码' + (err.code || -1));
				}
			})
		})
	}

	/**
	 * 中止流程
	 */
	shopSteps() {
		this.timeouts.forEach(ele => ele && clearTimeout(ele)); // 关闭所有定时器
		this.intervals.forEach(ele => ele && clearInterval(ele));
		uni.stopBluetoothDevicesDiscovery(); // 停止搜寻蓝牙
		uni.closeBluetoothAdapter(); // 关闭
		this.wifiCount = 1;
	}


	/**
	 * 连接雷达
	 */
	bindCreateBLEConnection(deviceId) {
		return new Promise(resolve => {
			this.pageEventCall(304);
			if (deviceId) {
				uni.createBLEConnection({
					deviceId,
					success: res => {
						this.pageEventCall(305);
						resolve(true)
					},
					fail: err => this.pageErrorCall(306, '错误码' + (err.code || -1))
				})
			}
		})
	}


	/**
	 * 选择设备服务
	 */
	getBLEDeviceServices(deviceId) {
		this.pageEventCall(307);
		return new Promise(resolve => {
			this.timeouts.push(setTimeout(() => { // 连接成功后需要延时一秒去读取服务
				uni.getBLEDeviceServices({
					deviceId,
					success: async data => {
						console.log(data, '查询到的服务--------');
						const service = data.services.find(ele => ele.uuid === this
							.baseData.serviceId);
						if (service) {
							console.log('搜寻主服务成功-----', service);
							resolve(service)
						}
					},
					fail: err => {
						fail: err => this.pageErrorCall(309, '错误码' + (err.code || -
							1))
					}
				})
			}, 1000));
		})
	}

	/**
	 * 获取要连接得设备属性
	 */
	getBLEDeviceCharacteristics(deviceId, uuId) {
		return new Promise(resolve => {
			this.pageEventCall(310);
			const serviceId = isIos() ? uuId.toUpperCase() : uuId.toLowerCase();
			uni.getBLEDeviceCharacteristics({
				deviceId,
				serviceId,
				success: res => {
					console.log(res.characteristics, 'res.characteristics');
					res.characteristics.forEach(item => {
						if (item.properties.notify || item.properties.indicate) {
							console.log("============notify==========");
							uni.notifyBLECharacteristicValueChange({
								deviceId,
								serviceId,
								characteristicId: isIos() ? item.uuid
									.toUpperCase() : item.uuid.toLowerCase(),
								state: true,
							})
						}

						if (item.properties.write) {
							console.log("============write==========");
							//this.baseData.WritecharacteristicId = item.uuid;
						}

						if (item.properties.read) {
							console.log("============read==========")
							//	this.baseData.ReadcharacteristicId = item.uuid;
							uni.readBLECharacteristicValue({
								deviceId,
								serviceId,
								characteristicId: isIos() ? item.uuid
									.toUpperCase() : item.uuid
									.toLowerCase()
							})
						}
					});
					this.timeouts.push(setTimeout(() => {
						resolve(); // 这里没有回调，因此写入和设置监听需要延迟触发
					}, 1000))
				},
				fail: err => {
					fail: err => this.pageErrorCall(311, '错误码' + (err.code || -
						1))
				}
			})
		});


	}

	/**
	 * 监听蓝牙设备推送信息
	 */
	onBLECharacteristicValueChange() {
		uni.onBLECharacteristicValueChange(characteristic => {
			console.log('监听蓝牙推送内容----------', ab2hex(characteristic.value));
			const value = ab2hex(characteristic.value);
			if (value.startsWith("10031a")) { // 设备连接wifi失败
				console.log("设备连接wifi失败");
				if (this.wifiCount < 3) {
					this.wifiCount++;
					this.wifiConnect(); // 重试
				} else {
					this.pageErrorCall(402);
					this.shopSteps();
				}
			} else if (value.startsWith("1a0e")) { // 设备连接wifi成功
				console.log("设备连接wifi连接成功");
				console.log('第一阶段---------------');
				this.pageEventCall(403);
				this.timeouts.push(setTimeout(() => {
					this.sendCloudDetails(); // 推送设备到云端
				}, 2000));
			} else if (value.startsWith("0801")) { // 开始用户配对
				console.log('第二阶段---------------');
				this.pageEventCall(404);
				this.pair();
			} else if (value.startsWith("08021a")) { // 激活设备
				console.log('第三阶段---------------');
				this.pageEventCall(405);
				this.activation(ToAppMessage.decode(characteristic.value));
			}
		})
	}

	/**
	 * 激活设备
	 */
	activation(decode) {
		try {
			console.log(decode, 'decode----')
			let code;
			try {
				const str = hexToString(ab2hex(decode.payload)).trim();
				code = str.replace(/\D/g, ''); // 报文中存在异常字符
			} catch (e) {
				code = hexToString(ab2hex(decode.payload)).trim().substr(1);
			}
			console.log(code, 'code');
			const token = this.token.idToken;
			if (code) {
				this.pairingComplete(code);
				this.timeouts.push(setTimeout(() => {
					this.pairEnd();
				}, 2000));
				this.pageEventCall(406);
				this.timeouts.push(setTimeout(async () => {
					const res = await this.putPairing(code);
					if (res) {
						console.log('激活成功', res.data.deviceId);
						this.callBack({
							type: 'success',
							data: res.data.deviceId
						});
						this.pageEventCall(501);
						this.shopSteps();
					} else {
						this.pageErrorCall(502);
					}
				}, 5000));
			} else {
				console.log('code异常');
			}
		} catch (e) {
			console.log('activation报错', e);
			//TODO handle the exception
		}
	}

	/**
	 * 连接wifi
	 */
	wifiConnect() {
		console.log('开始连接wifi');
		this.pageEventCall(401);
		try {
			const Payload = {
				ssid: this.wifiInfo.ssid.trim(),
				pass: this.wifiInfo.pwd.trim()
			};
			const messageWifiCred = WifiCred.create(Payload),
				bufferWifiCred = WifiCred.encode(messageWifiCred).finish(),
				bufferToDeviceMessage = {
					type: ToDeviceMessageType.CONNECT_WIFI,
					payload: bufferWifiCred
				},
				messageToDeviceMessage = ToDeviceMessage.create(bufferToDeviceMessage),
				bufferProto = ToDeviceMessage.encode(messageToDeviceMessage).finish();
			console.log("wifi配置 ab2hex", ab2hex(bufferProto));
			const buffer1 = hexToString(ab2hex(bufferProto)),
				buffer = stringToBytes(buffer1);
			this.sendData(buffer);
		} catch (e) {
			console.log(e, '连接wifi报错-----');
		}
	}


	/**
	 * 推送设备到云端
	 */
	sendCloudDetails() {
		console.log('推送云端开始');
		const cloudOptions = {
			projectId: "",
			httpUrl: "https://api.walabot-home.cn",
			mqttUri: "",
			mqttPort: "",
			ntpUrl: "ntp.aliyun.com",
			mqttClientId: "",
			mqttUsername: "",
			mqttPassword: "",
			cloudType: 3,
			cloudRegion: "",
			cloudRegistry: "",
		};
		const messageCloudDetails = CloudDetails.create(cloudOptions),
			bufferCloudOptions = CloudDetails.encode(messageCloudDetails).finish();
		const bufferToDeviceMessage2 = {
				type: ToDeviceMessageType.CLOUD_CONNECT,
				payload: bufferCloudOptions,
			},
			messageToDeviceMessage2 = ToDeviceMessage.create(
				bufferToDeviceMessage2
			),
			bufferProto2 = ToDeviceMessage.encode(messageToDeviceMessage2).finish();
		const buffer2 = hexToString(ab2hex(bufferProto2)),
			buffer = hex2ArrayBuffer(ab2hex(bufferProto2));
		console.log('推送云端完成');
		this.sendData(buffer);
	}

	/**
	 * 云配对
	 */
	pair() {
		try {
			console.log('云配对开始')
			const messagePair = Pair.create({
					uid: this.token.uid
				}),
				bufferPair = Pair.encode(messagePair).finish();

			const messageToDeviceMessage3 = ToDeviceMessage.create({
					type: ToDeviceMessageType.PAIR_TO_PHONE,
					payload: bufferPair,
				}),
				bufferProto3 = ToDeviceMessage.encode(messageToDeviceMessage3).finish();
			const buffer3 = hexToString(ab2hex(bufferProto3)),
				buffer = stringToBytes(buffer3);
			this.sendData(buffer);
		} catch (e) {
			console.log('云配异常', e);
			//TODO handle the exception
		}

	}

	/**
	 * 保存激活信息
	 */
	pairingComplete(code) {
		try {
			const messagePairCompl = PairingComplete.create({
					uid: this.token.uid,
					code: code
				}),
				bufferPairCompl = PairingComplete.encode(messagePairCompl).finish();

			const bufferToDeviceMessage4 = {
					type: ToDeviceMessageType.PAIR_TO_PHONE_COMPLETE,
					payload: bufferPairCompl,
				},
				messageToDeviceMessage4 = ToDeviceMessage.create(
					bufferToDeviceMessage4
				);
			const bufferProto4 = ToDeviceMessage.encode(messageToDeviceMessage4).finish(),
				buffer4 = hexToString(ab2hex(bufferProto4)),
				buffer = stringToBytes(buffer4);
			this.sendData(buffer);
		} catch (e) {
			console.log('pairingComplete报错', e);
			//TODO handle the exception
		}

	}

	pairEnd() {
		try {
			const bufferToDeviceMessage5 = {
					type: ToDeviceMessageType.DO_REBOOT_OPERATIONAL, // payload: ""
				},
				messageToDeviceMessage5 = ToDeviceMessage.create(
					bufferToDeviceMessage5
				),
				bufferProto5 = ToDeviceMessage.encode(messageToDeviceMessage5).finish();

			const buffer5 = hexToString(ab2hex(bufferProto5)),
				buffer = stringToBytes(buffer5);
			// console.log("Stage7: RebootDevice")
			// console.log("Stage7：", buffer)
			console.log("enMessage5-16hex", ab2hex(bufferProto5));
			this.sendData(buffer);
		} catch (e) {
			//TODO handle the exception
			console.log('pairEnd报错', e);
		}

	}

	/**
	 * 向设备推送信息
	 * @param {Object} buffer
	 */
	sendData(buffer) {
		console.log("发送的数据  buf2hex", buf2hex(buffer))
		wx.writeBLECharacteristicValue({
			deviceId: this.baseData.deviceId,
			serviceId: this.baseData.serviceId,
			characteristicId: this.baseData.WritecharacteristicId,
			value: buffer,
			success(res) {
				console.log("发送数据成功：", JSON.stringify(res))
			},
			fail(res) {
				console.log("发送数据失败", JSON.stringify(res))
			}
		})
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
	 * 失败消息回调
	 */
	pageErrorCall(code, msg = '') {
		this.callBack({
			type: 'fail',
			data: {
				msg: this.connentMap[code] + msg || '处理中...',
				code
			}
		});
		this.shopSteps(); // 失败需要停止流程
	}
}


let vp;
try{
	vp  = new VpActivation();
}catch(e){
	//TODO handle the exception
}
export const vpActivation = vp;
