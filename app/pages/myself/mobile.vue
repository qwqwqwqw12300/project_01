<!--
* @Author: zhanch
* @Date: 2022-12-29 11:27:28
* @FilePath: /pages/myself/mobile.vue
* @Description: 修改手机号
-->
<template>
	<app-body :bodyStyle="{backgroundPositionY: '-100rpx'}">
		<!-- <text class="ui-logo">修改手机号</text> -->
		<app-logo text="修改手机号"></app-logo>
		<view class="ui-form">
			<view class="ui-form-item">
				<u-text prefixIcon="phone" iconStyle="font-size: 30rpx" text="原手机号码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u--input placeholder="请输入手机号码" :border="'none'" fontSize="28rpx" clearable></u--input>
				</view>
			</view>
			<view class="ui-form-item">
				<sms-input></sms-input>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="phone" iconStyle="font-size: 30rpx" text="新手机号码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u--input placeholder="请输入手机号码" :border="'none'" fontSize="28rpx" clearable></u--input>
				</view>
			</view>
			<view class="ui-form-item">
				<sms-input></sms-input>
			</view>
			<view class="wd-btn-gloup">
				<button type="default" @click="goLogin">重置</button>
				<button type="default" @click="goLogin">取消</button>
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
	.ui-logo {
		height: 69rpx;
		width: 366rpx;
		font-size: 69rpx;
		color: #fff;
		margin: 62rpx 72rpx;
		display: inline-block;
		font-weight: bold;
	}

	.ui-form {
		margin-top: 156rpx;
		padding: 0 80rpx;
		padding-bottom: 120rpx;

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

		.wd-btn-gloup {
			margin: 120rpx 0;
		}
	}
</style>
