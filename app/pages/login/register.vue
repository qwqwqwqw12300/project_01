<!--
* @Author: zhanch
* @Date: 2022-12-27 12:47:07
* @LastEditors: zhanch
* @Description: 注册页面
-->

<template>
	<app-body :needService="false" :hideTitle="true" :bodyStyle="{backgroundPositionY: '-200rpx'}">
		<view class="ui-body">
			<text class="ui-logo">注册账号</text>
			<view class="ui-form">
				<view class="ui-form-item">
					<u-text prefixIcon="phone" iconStyle="font-size: 30rpx" text="手机号码" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input">
						<u--input placeholder="请输入手机号码" :border="'none'" fontSize="28rpx" clearable></u--input>
					</view>
				</view>
				<view class="ui-form-item">
					<graphic-input></graphic-input>
				</view>
				<view class="ui-form-item">
					<sms-input></sms-input>
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
				<view class="ui-agreement">
					<u-radio :customStyle="{ marginRight: '8rpx' }" @change="radioChange" shape="square"
						activeColor="#fdbc2b" size="24rpx"></u-radio>
					<text>同意</text>
					<text>用户协议</text>
				</view>
				<view class="ui-btn"><button @click="goLogin">注册</button></view>
				<text class="ui-link active">老朋友？点此登录</text>
			</view>
		</view>
	</app-body>

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
