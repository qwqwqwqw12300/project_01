<!--
* @Author: zhanch
* @Date: 2022-12-27 12:46:17
* @FilePath: /pages/login/login.vue
* @Description: 登录页面
-->
<template>
	<view class="wd-bg ui-body">
		<!-- <image class="ui-logo" src="../../static/images/logo.png"></image> -->
		<text class="ui-logo">新大陆米家</text>
		<view class="ui-nav">
			<u-tabs lineWidth="160rpx" lineColor="#fdbd2d" :itemStyle="{ width: '200rpx', height: '90rpx' }"
				:activeStyle="{ color: '#fdbd2d', fontSize: '28rpx' }"
				:inactiveStyle="{ color: '#bfbfbf', fontSize: '28rpx' }" lineHeight="4.5rpx" :list="navList"
				@click="navClick"></u-tabs>
		</view>
		<!-- 账号登录 -->
		<u-transition mode="fade-left" :show="navActive === 0 && delay === 0">
			<view class="ui-form">
				<view class="ui-form-item">
					<u-text prefixIcon="phone-fill" iconStyle="font-size: 30rpx" text="手机号码" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input">
						<u--input placeholder="请输入手机号码" :border="'none'" fontSize="28rpx" clearable></u--input>
					</view>
				</view>
				<view class="ui-form-item">
					<u-text prefixIcon="lock-fill" iconStyle="font-size: 32rpx" text="密码" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input">
						<u-input placeholder="请输入你的密码" :password="true" :border="'none'" fontSize="28rpx" clearable>
						</u-input>
					</view>
				</view>
				<view class="ui-bot">
					<text class="active" @click="register">免费注册</text>
					<text class="active" @click="forgot">忘记密码？</text>
				</view>
				<view class="ui-btn"><button @click="login">立即登录</button></view>
			</view>
		</u-transition>
		<!-- /账号登录 -->
		<!-- 验证码登录 -->
		<u-transition mode="fade-right" :show="navActive !== 0 && delay !== 0">
			<view class="ui-form">
				<view class="ui-form-item">
					<u-text prefixIcon="phone-fill" iconStyle="font-size: 30rpx" text="手机号码" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input">
						<u-input placeholder="请输入手机号码" :border="'none'" fontSize="28rpx" clearable></u-input>
					</view>
				</view>
				<view class="ui-form-item">
					<u-text prefixIcon="email-fill" iconStyle="font-size: 32rpx" text="验证码" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input">
						<u-input placeholder="请输入短信认证码" :border="'none'" fontSize="28rpx" clearable>
							<template slot="suffix">
								<button class="wd-sms" size="mini">验证码</button>
							</template>
						</u-input>
					</view>
				</view>
				<view class="ui-bot">
					<text class="active" @click="register">免费注册</text>
					<text class="active" @click="forgot">忘记密码？</text>
				</view>
				<view class="ui-btn"><button @click="login">立即登录</button></view>
			</view>
		</u-transition>

		<!-- /验证码登录 -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				/**登录模式列表**/
				navList: [{
						name: '账号密码登录'
					},
					{
						name: '短信登录'
					}
				],
				/**选中的登录模式**/
				navActive: 0,
				/**切换动画延时效果**/
				delay: 0,
			};
		},
		methods: {
			navClick({
				index
			}) {
				this.navActive = index;
				setTimeout(() => {
					this.delay = index
				}, 200);

			},
			/**
			 * 登录
			 */
			login() {
				uni.switchTab({
					url: '/pages/index/index'
				});
			},

			/**
			 * 注册
			 */
			register() {
				uni.navigateTo({
					url: '/pages/login/register'
				})
			},
			/**
			 * 忘记密码
			 */
			forgot() {
				uni.navigateTo({
					url: '/pages/login/forgot'
				})
			}
		}
	};
</script>

<style lang="scss">
	.ui-body {
		height: 100vh;
		text-align: center;
		background-position: 0 -100rpx;
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

	.ui-nav {
		margin-top: 71rpx;
		display: inline-flex;
		justify-content: center;
		height: 109rpx;
		width: 597rpx;
		background: #fff;
		border-radius: 10px;
	}

	.ui-form {
		margin-top: 136rpx;
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

		.ui-btn {
			margin-top: 59rpx;
		}

		.wd-sms {
			width: 160rpx;
			// height: 60rpx;
		}
	}
</style>
