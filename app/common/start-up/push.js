/*
 * @Author: white_queen
 * @Date: 2022-12-16 15:00:48
 * @FilePath: /common/start-up/push.js
 * @Description: 阿里推送管理
 */
class AliPush {
	channel = uni.requireNativePlugin('Aliyun-Push-NotificationChannel');
	aliyunPush = uni.requireNativePlugin('Aliyun-Push');

	constructor() {

	}

	init() {
		this.channel.createChannel({
			id: 'aaa',
			name: '测试通道A',
			desc: '测试创建通知通道',
			importance: 3,
		});
	}

	/**
	 * 注册app推送
	 */
	registerPush() {
		this.aliyunPush.registerPush({}, result => {
			const event = result.event;
			if (event === 'registerPush') {
				if (result.code === 'success') {
					console.log("注册推送 成功 ");
				} else {
					console.log("注册推送 " + result.code + " " + result.msg);
				}
			} else {
				const data = JSON.stringify(result.data);
				console.log("receive push event : " + event);
				console.log("receive push data : " + data);
			}
		});
	}
}
