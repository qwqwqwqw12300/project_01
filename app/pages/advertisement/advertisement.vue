<template>
	<app-body :hideTitle="true" :needService="false"
		:bodyStyle="{backgroundImage: 'linear-gradient(180deg, #FFE7B5 0%, #FFFFFF 24%)' }">
		<view class="ui-ad" @click="goGuide">
			<image src="../../static/images/logo@2x.png"></image>
		</view>
		<view class="ui-tip">
			<text>艾吉通</text>
		</view>
		<view class="ui-title">
			<text>守护您每一分钟</text>
			<!-- <image src="../../static/images/ad-text.png"></image> -->
		</view>
		<view class="ui-btn" :animation="animationData">
			<button class="default" @click="goRegister">马上体验</button>
			<text @click="goLogin">老朋友? &nbsp;点此登录</text>
		</view>
	</app-body>
</template>

<script>
	import {
		GetDict,
		PostVersionInfo,
	} from '@/common/http/api.js';
	import upload from '../../common/utils/upload';
	import {
		versionCompare,
		isIos,
		isApp
	} from '../../common/utils/util';
	import {
		env
	} from '../../config/env';
	export default {
		data() {
			return {
				animationData: {}
			};
		},
		mounted() {
			this.initInfo();
			// upload.check().then(res => { // 检查更新，上架后开启
			// 	if (res.status) {
			// 		this.$store.dispatch('GetContactsList');
			// 	}
			// });
		},
		methods: {
			/**
			 * 跳转首页
			 */
			goMain() {
				uni.switchTab({
					url: '/pages/index/index'
				});
			},
			/**
			 * 跳转注册
			 */
			goRegister() {
				uni.navigateTo({
					url: '/pages/login/register'
				});
			},
			/**
			 * 跳转登录
			 */
			goLogin() {
				uni.navigateTo({
					url: '/pages/login/login'
				});
			},
			goGuide() {
				if (env.mode === 'dev')
					uni.navigateTo({
						url: '/pages/guide/guide'
					});
			},

			/**
			 * 获取用户信息
			 */
			getUserInfo() {
				return new Promise(resolve => {
					this.$store.dispatch('getPushMsgState').then(res => {
						resolve(res);
					});
				});
			},
			initInfo() {
				GetDict().then(res => {
					console.log(res, '字典信息');
				})
				this.getUserInfo().then(res => {
					if (!res) {
						const animation = uni.createAnimation({
							duration: 3000,
							timingFunction: 'ease'
						});
						animation.opacity(10).step();
						this.animationData = animation.export();
					} else {
						this.goMain();
					}

				});
			}
		},

	};
</script>

<style lang="scss">
	// .ui-logo {
	// 	padding: 64rpx 0 0 26rpx;
	// 	height: 53rpx;
	// 	width: 227rpx;

	// 	image {
	// 		max-width: 100%;
	// 		max-height: 100%;
	// 	}
	// }

	.ui-ad {
		height: 260rpx;
		width: 260rpx;
		margin: 0 auto;
		padding: 300rpx 0 0;
		text-align: center;

		image {
			max-width: 100%;
			max-height: 100%;
		}
	}

	.ui-tip {
		margin-top: 40rpx;
		font-size: 54rpx;
		color: #353535;
		font-weight: 500;
		text-align: center;

		text {
			margin-left: 36rpx;

			&:nth-child(1) {
				margin-left: 0rpx;
			}
		}
	}

	.ui-title {
		margin-top: 20rpx;
		font-size: 34rpx;
		color: #353535;
		font-weight: 400;
		text-align: center;
	}

	.ui-btn {
		opacity: 0;
		position: absolute;
		width: 579rpx;
		margin: 0 auto;
		bottom: 100rpx;
		left: 50%;
		transform: translateX(-50%);
		text-align: center;

		text {
			font-size: 30rpx;
			color: #0094ff;
		}

		&>* {
			&:active {
				opacity: 0.8;
			}
		}

		&>*:nth-child(1) {
			margin-bottom: 50rpx;
		}
	}
</style>