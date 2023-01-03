<!--
* @Author: zhanch
* @Date: 2022-12-27 15:02:25
* @FilePath: /pages/login/forgot.vue
* @Description:找回密码
-->

<template>
	<view class="wd-bg ui-body">
		<text class="ui-logo">找回密码</text>
		<view class="ui-form">
			<view class="ui-form-item">
				<u-text prefixIcon="phone" iconStyle="font-size: 30rpx" text="手机号码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u--input placeholder="请输入手机号码" :border="'none'" fontSize="28rpx" clearable></u--input>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="photo" iconStyle="font-size: 32rpx" text="验证码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u-input placeholder="请输入图形验证码" :border="'none'" fontSize="28rpx" clearable>
						<template slot="suffix">
							<button class="ui-mini" size="mini">验证码</button>
						</template>
					</u-input>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="email" iconStyle="font-size: 32rpx" text="短信码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u-input placeholder="请输入短信认证码" :border="'none'" fontSize="28rpx" clearable>
						<template slot="suffix">
							<button class="wd-sms ui-mini" size="mini">验证码</button>
						</template>
					</u-input>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="lock" iconStyle="font-size: 32rpx" text="密码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u-input placeholder="请输入你的密码" :password="true" :border="'none'" fontSize="28rpx" clearable>
						<template slot="suffix">
							<u-icon name="eye-fill" color="rgb(144, 147, 153)" size="36rpx"></u-icon>
						</template>
					</u-input>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="lock-fill" iconStyle="font-size: 32rpx" text="确认密码" color="#444" size="28rpx">
				</u-text>
				<view class="ui-input">
					<u-input placeholder="请输入你的密码" :password="true" :border="'none'" fontSize="28rpx" clearable>
						<template slot="suffix">
							<u-icon name="eye-fill" color="rgb(144, 147, 153)" size="36rpx"></u-icon>
						</template>
					</u-input>
				</view>
			</view>
			<view class="ui-btn"><button @click="goMain">重置密码</button></view>
			<text class="ui-link active" @click="goLogin">老朋友？点此登录</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {};
		},
		methods: {
			/**
			 * 勾选协议
			 */
			radioChange() {},

			/**
			 * 跳转登录
			 */
			goLogin() {
				uni.navigateTo({
					url: '/pages/login/login'
				});
			},

			/**
			 * 跳转首页
			 */
			goMain() {
				uni.switchTab({
					url: '/pages/index/index'
				});
			}
		}
	};
</script>

<style lang="scss">
	.ui-body {
		text-align: center;
		// background-position: 0 -100rpx;
		background-position: 0 -230rpx;
	}

	.ui-logo {
		height: 69rpx;
		width: 366rpx;
		font-size: 69rpx;
		color: #fff;
		margin-top: 150rpx;
		display: inline-block;
		font-weight: bold;
	}

	.ui-form {
		// margin-top: 316rpx;
		margin-top: 160rpx;
		padding: 0 80rpx;

		.ui-input {
			margin: 34rpx 0 60rpx 0;
			border-bottom: 1px solid #e2e2e2;
		}

		.ui-bot {
			display: flex;
			align-items: center;
			justify-content: space-between;
			font-size: 26rpx;
			color: #484848;
		}

		.ui-mini {
			width: 160rpx;
			// height: 60rpx;
		}

		.ui-agreement {
			display: inline-flex;
			align-items: center;
			justify-content: center;

			text {
				font-size: 24rpx;
				color: #fdbc2b;
			}
		}

		.ui-btn {
			margin: 58rpx 0 0 0;
		}

		.ui-link {
			display: inline-block;
			margin: 52rpx 30rpx;
			font-size: 24rpx;
			color: #0094ff;
		}
	}
</style>
