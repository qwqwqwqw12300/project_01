<template>
	<app-body title="关于软件" :bodyStyle="{backgroundImage: 'linear-gradient(179deg, #FFE7B5 0%, #FFF 20%)'}">
		<!-- <app-logo color="#353535" text="关于软件" ></app-logo> -->
		<view class="ui-main">
			<view>
				<image class="ui-main-imageFir" src="@/static/images/login-logo.png" mode=""></image>
			</view>
			<!-- <view>
				<image class="ui-main-imageSec" src="@/static/images/H&V@3x.png" mode=""></image>
			</view> -->
			<view class="ui-main-font">
				艾吉通，守护您每一分钟！
			</view>
			<view class="ui-main-button">
				<button class="ui-button default" @click="handleUpdate">检查更新</button>
			</view>
			<view class="Version">
				福建易联智慧科技有限公司
			</view>
			<view class="Version" style="margin-top: 32rpx;">
				Version {{appVersion}}
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
		isIos,
		isApp
	} from '../../common/utils/util';
	export default {
		data() {
			return {
				appVersion: '',
				versionType: 0
			}
		},
		methods: {
			handleQuery() {
				plus.runtime.getProperty(plus.runtime.appid, (info) => {
					this.appVersion = info.version
				})
				this.versionType = isIos ? '0' : '1'
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
					console.log(res)
					const curVersion = res.data.content
					console.log('this.versionType', this.versionType)
					const result = versionCompare(this.appVersion, curVersion)
					if (!result) {
						if (this.versionType == 1) {
							uni.showModal({
								title: '发现新版本 ' + curVersion,
								content: '请到App store进行升级',
								showCancel: false
							})
							return
						}
						uni.showModal({
							title: '',
							content: `发现新版本${curVersion}是否更新？`,
							success: resl => {
								if (resl.confirm) {
									let downloadUrl = res.data.downloadAddress
									this.download(downloadUrl)
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
			},
			download(downloadUrl) {
				console.log(downloadUrl)
				uni.showLoading({
					title:'下载中'
				})
				uni.downloadFile({
					url: downloadUrl,
					success: (res) => {
						console.log(res)
						uni.hideLoading()
						if (res.statusCode === 200) {
							plus.runtime.install(res.tempFilePath, {}, () => {
								plus.runtime.restart();
							}, function(error) {
								uni.showToast({
									title: '安装失败',
									duration: 1500,
									icon: "none"
								});
							})
						}else{
							uni.showToast({
								title: '下载失败，请重试',
								duration: 1500,
								icon: "none"
							});
						}
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
		display: flex;
		flex-direction: column;
		align-items: center;
		padding-top: 110rpx;

		// justify-content: center;
		.ui-main-imageFir {
			width: 300rpx;
			height: 300rpx;
		}

		.ui-main-imageSec {
			width: 200rpx;
			height: 200rpx;
		}

		.ui-main-font {
			margin-top: 132rpx;
			font-family: PingFangSC-Regular;
			font-size: 36rpx;
			color: #353535;
			letter-spacing: 0;
			text-align: center;
			font-weight: 600;
		}

		.Version {
			margin-top: 132rpx;
			font-family: PingFangSC-Regular;
			font-size: 28rpx;
			color: #353535;
			letter-spacing: 0;
			text-align: center;
			font-weight: 400;
		}

		.ui-main-button {
			margin-top: 130rpx;
			// margin-bottom: 163rpx;
			width: 600rpx;

			.ui-button {
				width: 100%;
				height: 80rpx;
				line-height: 80rpx;
			}
		}
	}
</style>