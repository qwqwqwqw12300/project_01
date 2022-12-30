<!--
* @Author: zhanch
* @Date: 2022-12-28 19:03:59
* @FilePath: /pages/myself/pwd.vue
* @Description: 修改密码
-->

<template>
	<app-body>
		<u-navbar leftText="首页" @rightClick="rightClick" :autoBack="true" bgColor="transparent" leftIconColor="#fff" :fixed="false" />
		<text class="ui-logo">修改密码</text>
		<view class="ui-form">
			<view class="ui-form-item">
				<u-text prefixIcon="lock" iconStyle="font-size: 32rpx" text="原密码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u-input placeholder="请输入原密码" :password="true" :border="'none'" fontSize="28rpx" clearable>
						<template slot="suffix">
							<u-icon name="eye-fill" color="rgb(144, 147, 153)" size="36rpx"></u-icon>
						</template>
					</u-input>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="lock" iconStyle="font-size: 32rpx" text="密码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u-input placeholder="请输入新密码" :border="'none'" :password="true" fontSize="28rpx" clearable>
						<template slot="suffix">
							<u-icon name="eye-fill" color="rgb(144, 147, 153)" size="36rpx"></u-icon>
						</template>
					</u-input>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="lock" iconStyle="font-size: 32rpx" text="确认密码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u-input placeholder="再次输入新密码" :border="'none'" :password="true" fontSize="28rpx" clearable>
						<template slot="suffix">
							<u-icon name="eye-fill" color="rgb(144, 147, 153)" size="36rpx"></u-icon>
						</template>
					</u-input>
				</view>
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
