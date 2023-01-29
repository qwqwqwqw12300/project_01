<template>
	<app-body>
		<view class="ui-main">
			<image class="ui-main-img" src="../../static/images/logo.png"></image>
			<text class="ui-main-name">艾吉通</text>
			<text class="ui-main-version">当前版本：{{ appVersion }}</text>

			<view class="ui-main-button">
				<button class="ui-button" @click="handleUpdate">检查更新</button>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		PostVersionInfo,
	} from '@/common/http/api.js';
	import {
		versionCompare,
		isIos
	} from '../../common/utils/util';
	export default {
		data() {
			return {
				appVersion: ''
			}
		},
		methods: {
			handleQuery() {
				plus.runtime.getProperty(plus.runtime.appid, (info) => {
					this.appVersion = info.version
				})
				// PostVersionInfo({
				// 	versionType: isIos ? '0' : '1',
				// }).then(res => {
				// 	this.version = res.data.content
				// })
			},
			handleUpdate() {
				PostVersionInfo({
					versionType: isIos ? '0' : '1',
				}).then(res => {
					const curVersion = res.data.content
					const result = versionCompare(this.appVersion, curVersion)
					if (!result) {
						uni.showModal({
							title: '',
							content: `发现新版本${curVersion}、是否更新？`,
							success: res => {
								if (res.confirm) {
									let appurl = "https://sj.qq.com/"
									plus.runtime.openURL(appurl)
								}
							}
						});
					} else {
						uni.showModal({
							title: '',
							content: '已经是最新版本了',
						});
					}
				})
			}
		},
		mounted() {
			this.handleQuery()
		}
	}
</script>

<style lang="scss" scoped>
	.ui-main {
		height: 1000rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		padding-top: 150rpx;

		// justify-content: center;
		.ui-main-img {
			width: 180rpx;
			height: 180rpx;
		}

		.ui-main-name {
			font-size: 36rpx;
			color: #333333;
			margin-top: 20rpx;
		}

		.ui-main-version {
			font-size: 32rpx;
			color: #000;
			margin-top: 10rpx;
		}

		.ui-main-button {
			margin-top: 300px;
			width: 600rpx;

			.ui-button {
				width: 100%;
				height: 80rpx;
				line-height: 80rpx;
			}
		}
	}
</style>
