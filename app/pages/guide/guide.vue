<template>
	<view class="ui-container">
		<view class="ui-img">
			<!-- <image src="../../static/componentIndex.png" mode="aspectFit"></image> -->
		</view>
		<!-- 	<u-cell-group>
			<u-cell v-for="(item, index) in list" :key="item.id" :title="item.name" isLink :url="item.url"></u-cell>
		</u-cell-group> -->
		<button @click="localPush">本地推送</button>
		<button @click="getRegistrationID">用户id</button>
		<button @click="initSDK">初始化</button>
		<button @click="connect">连接</button>
		<button @click="getwifiInfo">获取wifi信息</button>
		<button @click="checkPermission">检查权限</button>
		<button @click="requestPermission">获取权限</button>
		<button @click="getDemo">get请求测试</button>
		<button @click="postDemo">post请求测试</button>
		<button @click="getToken">token获取</button>
		<button @click="goExt">设备连接</button>
	</view>
</template>
<script>
	const sdkModule = uni.requireNativePlugin("VPairSDKModule");
	import {
		push
	} from '@/common/sdk/push.js';
	import {
		getDemo,
		postDemo
	} from '../../common/http/api';
	import {
		vpsdk
	} from '../../common/sdk/vpsdk';
	export default {
		data() {
			return {
				list: [{
					id: 'range',
					url: '/pages/guide/range',
					name: '检测范围'
				}, ]
			}
		},
		methods: {
			localPush() {
				try {
					push.addLocalNotification();
				} catch (e) {
					console.log(e);
				}
			},
			getRegistrationID() {
				push.getRegistrationID().then(res => {
					console.log(res, 'getRegistrationID');
				});
			},
			initSDK() {
				sdkModule.initSDK(); //初始化调用
			},
			checkPermission() {
				var result = sdkModule.checkPermission() //检查权限，返回Map<Integer，string>错误码与对应的错误信息，map为空说明正常
				console.log(result, 'checkPermission', typeof result);
			},
			requestPermission() {
				sdkModule.requestPermission(3) //根据错误码请求功能
			},
			getwifiInfo() {
				var info = sdkModule.getWifiInfo() //获取当前连接的wifi的SSID和BSSID
				console.log(info, 'getwifiInfo', typeof info);
			},
			connect() {
				var ssid = "";
				var bssid = "";
				var password = "";
				sdkModule.connect(ssid, bssid, password); //用wifi的SSID和BSSID和wifi密码连接
			},
			getDemo() {
				getDemo().then(res => {
					console.log(res, 'getDemo');
				})
			},

			postDemo() {
				postDemo({
					"uuid": "c8916a767204456fa970c675c71048e5",
					"captcha": "6d23"
				}).then(res => {
					console.log(res, 'postDemo');
				})
			},

			getToken() {
				const info = vpsdk.getToken()
			},

			goExt() {
				uni.navigateTo({
					url: '/pages/guide/ext'
				})
			}
		},
	}
</script>

<style lang="scss">
	.ui-img {
		height: 150rpx;
		width: 100%;
		text-align: center;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.ui-container {
		height: 100%;
		width: 100%;
		background: #fff;
	}

	button {
		float: left;
		width: 300rpx;
		margin: 30rpx;
	}
</style>
