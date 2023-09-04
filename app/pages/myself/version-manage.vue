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
		<view class="renew_bg" v-show="hide" @click="hiderenew">
			<view class="renew">
				<image src="/static/images/renew.png" style="width: 300px;" mode="widthFix"></image>
				<view class="renew_title">
					发现新版本
				</view>
				<view class="renew_content">
					发现新版本，请及时更新，避免影响使用
				</view>
				<view class="Progress_box">
					<view class="Progress" v-show="Progress">
						<view class="Progress_bar" :style="{ width: Progress}"></view>
					</view>
				</view>
				<button @click.stop="download" :disabled="disabled">
					立即更新
				</button>
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
				versionType: 0,
				Progress:0,
				downloadUrl:'',
				hide:false,
				disabled:false
			}
		},
		methods: {
			handleQuery() {
				plus.runtime.getProperty(plus.runtime.appid, (info) => {
					this.appVersion = info.version
				})
				this.versionType = isIos ? '0' : '1'
			},
			handleUpdate() {
				PostVersionInfo({
					versionType: isIos ? '0' : '1',
				}).then(res => {
					const curVersion = res.data.content
					const result = versionCompare(this.appVersion, curVersion)
					if (!result) {
						this.downloadUrl = res.data.downloadAddress
						this.hide = true
					} else {
						uni.showModal({
							title: '',
							content: '已经是最新版本了',
						});
					}
				})
			},
			download() {
				const downloadTask = uni.downloadFile({
					url: this.downloadUrl,
					success: (res) => {
						console.log(res)
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
				downloadTask.onProgressUpdate((res) => {
					this.disabled = true
					this.Progress = res.progress*3+'px'
					// console.log('下载进度' + res.progress);
					// console.log('已经下载的数据长度' + res.totalBytesWritten);
					// console.log('预期需要下载的数据总长度' + res.totalBytesExpectedToWrite);
					// 满足测试条件，取消下载任务。
					// if (res.progress == 100) {
					// 	console.log('下载完啦！！！！！')
					// }
				});
			},
			hiderenew(){
				if(this.Progress != 0){
					return
				}
				this.hide = false
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
	.renew_bg{
		width: 100%;
		height: 100vh;
		background-color: rgba(120, 120, 120, 0.6);
		position: fixed;
		left: 0;
		top: 0;
		.renew{
			width: 300px;
			height: 240rpx;
			background-color: #FFFFFF;
			border-radius: 10px;
			position: absolute;
			left: calc(50% - 150px);
			top: calc(50vh - 200rpx);
			padding: 30rpx 0;
			padding-top: 110px;
			image{
				position: absolute !important;
				top: -67px;
				left: 0;
			}
			.renew_title{
				text-align: center;
				font-family: Inter;
				font-size: 32rpx;
				font-style: normal;
				font-weight: 700;
				line-height: normal;
				margin: 0 20rpx;
				margin-bottom: 30rpx;
				background: -webkit-linear-gradient(#FF7602, #F8CFA9);
				-webkit-background-clip: text;
				-webkit-text-fill-color: transparent;
			}
			.renew_content{
				color: #000000;
				font-family: PingFang SC;
				font-size: 24rpx;
				font-style: normal;
				font-weight: 400;
				text-align: center;
				margin: 0 20rpx;
				margin-bottom: 30rpx;
			}
			button{
				margin: 0 auto;
				width: 260rpx;
				height: 75rpx;
				background: #FE9740;
				color: #FFF;
				text-align: center;
				font-size: 24rpx;
				font-style: normal;
				font-weight: 400;
				line-height: 75rpx;
				border-radius: 20px !important;
			}
			button[disabled]{
			 background: rgb(179, 179, 179) !important;
			}
			.Progress_box{
				margin-bottom: 20rpx;
				height: 2px;
			}
			.Progress{
				margin-bottom: 10rpx;
				height: 2px;
				background-color: rgba(95, 95, 95, 0.2);
				.Progress_bar{
					background-color: #FEAE43;
					height: 2px;
				}
			}
		}
	}
</style>