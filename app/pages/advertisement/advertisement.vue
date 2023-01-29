<template>
	<app-body :hideTitle="true" :needService="false">
		<view class="ui-ad" @click="goGuide">
			<image src="../../static/images/ad.png"></image>
		</view>
		<view class="ui-tip">
			<text>艾吉通</text>
		</view>
		<view class="ui-title">
			<image src="../../static/images/ad-text.png"></image>
		</view>
		<view class="ui-btn" :animation="animationData">
			<button @click="goRegister">马上体验</button>
			<text @click="goLogin">老朋友? &nbsp;点此登录</text>
		</view>
	</app-body>
</template>

<script>
	import {
		PostVersionInfo,
	} from '@/common/http/api.js';
	import {
		versionUpdate
	} from '../../common/utils/util';
	export default {
		data() {
			return {
				animationData: {}
			};
		},
		mounted() {
			this.queryVersion()
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
			/**
			 * 查询当前版本
			 */
			queryVersion() {
				const isIOS = (uni.getSystemInfoSync().platform === 'ios')
				PostVersionInfo({
					versionType: isIOS ? '0' : '1',
				}).then(res => {
					const curVersion = res.data.content
					versionUpdate(curVersion)
				})
			},
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
		height: 447rpx;
		width: 628rpx;
		margin: 0 auto;
		padding: 160rpx 0 0 44rpx;
		text-align: center;

		image {
			max-width: 100%;
			max-height: 100%;
		}
	}

	.ui-tip {
		margin-top: 80rpx;
		font-size: 48rpx;
		color: #fdbc2b;
		text-align: center;

		text {
			margin-left: 36rpx;

			&:nth-child(1) {
				margin-left: 0rpx;
			}
		}
	}

	.ui-title {
		height: 48rpx;
		width: 383rpx;
		margin: 0 auto;
		padding-top: 31rpx;

		image {
			max-width: 100%;
			max-height: 100%;
		}
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
