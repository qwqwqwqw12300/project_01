/*
 * @Author: white_queen
 * @Date: 2022-12-16 15:00:48
 * @FilePath: /common/start-up/push.js
 * @Description: 推送管理
 */

import {
	env
} from '@/config/env.js';

class Push {
	/**推送模块**/
	jpushModule;
	/**注册id(设备唯一编号)**/
	registrationID = '';

	constructor() {

	}

	init() {
		return new Promise(resolve => {
			console.log('极光推送开始注册');
			this.jpushModule = uni.requireNativePlugin("JG-JPush");
			if (uni.getSystemInfoSync().platform == "ios") {
				// 请求定位权限
				const locationServicesEnabled = this.jpushModule.locationServicesEnabled(); // 获取系统定位服务是否开启
				const locationAuthorizationStatus = jpushModule
					.getLocationAuthorizationStatus(); // 获取用户定位权限状态
				console.log('locationAuthorizationStatus', locationAuthorizationStatus);
				if (locationServicesEnabled === true && locationAuthorizationStatus < 3) { // 定位已开启 
					this.jpushModule.requestLocationAuthorization((result) => { // 请求定位权限
						console.log('定位权限', result.status);
					});
				}
				this.jpushModule.requestNotificationAuthorization((result) => { // 获取通知的授权状态
					const status = result.status;
					if (status < 2) {
						uni.showToast({
							icon: 'none',
							title: '您还没有打开通知权限，可能会错过消息推送',
							duration: 3000
						});
					}
				})
			}
			this.jpushModule.initJPushService(); // 初始化推送
			this.jpushModule.setLoggerEnable(env.mode === 'dev'); // 开启debug开关
			this.addConnectEventListener();
			this.messageListener();
			console.log('极光推送注册完成');
			resolve();
		});
	}

	/**
	 * 获取设备注册id，用于与登录用户绑定
	 */
	getRegistrationID() {
		return new Promise(resolve => {
			this.jpushModule.getRegistrationID(result => {
				resolve(result.registerID);
			})
		});
	}


	/**
	 * 监听连接状态
	 */
	addConnectEventListener() {
		this.jpushModule.addConnectEventListener(result => {
			const connectEnable = result.connectEnable;
			if (!connectEnable) {
				uni.showToast({
					icon: 'none',
					title: '当前网络异常，请检查网络后重启应用',
					duration: 3000
				});
			}
		});
	}

	/**
	 * 消息监听
	 */
	messageListener() {
		const listenerHandle = result => {
			const {
				extras
			} = result;
			console.log(extras, '应用消息回调');
		};
		// 监听推送点击事件
		this.jpushModule.addNotificationListener(listenerHandle);
		this.jpushModule.addCustomMessageListener(listenerHandle); // 监听 Portal 发送的自定义消息
		this.jpushModule.addLocalNotificationListener(listenerHandle);
		// jpushModule.addNotificationListener(result => {
		// 	const {
		// 		notificationEventType, // 分为notificationArrived和notificationOpened两种
		// 		messageID, // 唯一标识通知消息的 ID
		// 		title, // 对应 Portal 推送通知界面上的“通知标题”字段
		// 		content, // 对应 Portal 推送通知界面上的“通知内容”字段
		// 		extras // 对应 Portal 推送消息界面上的“可选设置”里的附加字段
		// 	} = result;
		// 	console.log(extras, 'extras接收到的通知');
		// });
	}

	/**
	 * 设置用户手机号码
	 */
	setMobileNumber() {
		this.jpushModule.setMobileNumber({
			mobileNumber: '15889230000'
		});
	}

	/**
	 * 添加一个本地通知
	 */
	addLocalNotification() {
		console.log('本地推送');
		this.jpushModule.addLocalNotification({
			messageID: '123',
			title: 'title',
			content: 'content',
			extras: {
				name: 'Cindy',
				age: '16'
			}
		});
	}
}

export const push = new Push();
