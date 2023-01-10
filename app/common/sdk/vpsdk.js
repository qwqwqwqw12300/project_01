/*
 * @Author: zhanch
 * @Date: 2023-01-09 16:01:39
 * @FilePath: /common/sdk/vpsdk.js
 * @Description: 设备连接sdk
 */

class Vpsdk {
	/**推送模块**/
	vpModule;

	constructor() {
		if (isApp()) this.vpModule = uni.requireNativePlugin("VPairSDKModule");

	}

	init() {
		this.vpModule.initSDK();
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

export const vpsdk = new Vpsdk();
