<!--
* @Author: zhanch
* @Date: 2022-12-27 12:46:17
* @FilePath: /pages/login/login.vue
* @Description: 登录页面
-->
<template>
	<app-body :needService="false" :hideTitle="true">
		<view class="ui-body">
			<!-- <image class="ui-logo" src="../../static/images/logo.png"></image> -->
			<text class="ui-logo">艾吉通</text>
			<view class="ui-tip">艾吉通·守护您每一分钟!</view>
			<view class="ui-nav">
				<u-tabs lineWidth="160rpx" lineColor="#fdbd2d" :itemStyle="{ width: '200rpx', height: '90rpx' }"
					:activeStyle="{ color: '#fdbd2d', fontSize: '28rpx' }"
					:inactiveStyle="{ color: '#bfbfbf', fontSize: '28rpx' }" lineHeight="4.5rpx" :list="navList"
					@click="navClick"></u-tabs>
			</view>
			<!-- 账号登录 -->
			<u-transition :mode="initFlag ? '' : 'fade-left'" :show="navActive === 0 && delay === 0">
				<view class="ui-form">
					<view class="ui-form-item">
						<u-text prefixIcon="phone-fill" iconStyle="font-size: 30rpx" text="手机号码" color="#444"
							size="28rpx">
						</u-text>
						<view class="ui-input">
							<u--input v-model="loginForm.phone" type="number" placeholder="请输入手机号码" :border="'none'"
								fontSize="28rpx" clearable></u--input>
						</view>
					</view>
					<view class="ui-form-item">
						<graphic-input ref="codeRef"></graphic-input>
					</view>
					<view class="ui-form-item">
						<u-text prefixIcon="lock-fill" iconStyle="font-size: 32rpx" text="密码" color="#444" size="28rpx">
						</u-text>
						<view class="ui-input">
							<u-input v-model="loginForm.password" placeholder="请输入你的密码" :password="true"
								:border="'none'" fontSize="28rpx" clearable>
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
						<u-text prefixIcon="phone-fill" iconStyle="font-size: 30rpx" text="手机号码" color="#444"
							size="28rpx">
						</u-text>
						<view class="ui-input">
							<u-input v-model="smsLoginForm.phone" type="number" placeholder="请输入手机号码" :border="'none'"
								fontSize="28rpx" clearable></u-input>
						</view>
					</view>
					<view class="ui-form-item">
						<graphic-input ref="codeRefbySms"></graphic-input>
					</view>
					<view class="ui-form-item">
						<sms-input ref="sms" @reset="smsReset" :payload="smsPayload" @checked="checkedBySms">
						</sms-input>
					</view>
					<view class="ui-bot">
						<text class="active" @click="register">免费注册</text>
						<text class="active" @click="forgot">忘记密码？</text>
					</view>
					<view class="ui-btn"><button @click="loginBySms">立即登录</button></view>
				</view>
			</u-transition>

			<!-- /验证码登录 -->
		</view>
	</app-body>

</template>

<script>
	import {
		loginBySms,
		PostLoginByPwd,
	} from '@/common/http/api.js';
	import {
		push
	} from '@/common/sdk/push.js';
	import {
		isApp,
		isIos,
	} from '../../common/utils/util';
	import {
		env
	} from "@/config/env.js";
	import {
		setToken
	} from '@/common/utils/auth.js';
	import jsencrypt from '@/common/utils/jsencrypt.js';
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
				/**页面初始化标志**/
				initFlag: true,
				loginForm: {
					phone: '13205907108',
					code: '',
					password: '1111',
					uuid: '',
				},
				/**短信登录参数**/
				smsLoginForm: {
					phone: '',
					uuid: '',
					code: ''
				},
				//会员登录设备注册号
				registrationId: uni.$u.guid(20),
			};
		},
		mounted() {
			this.getRegistrationID();
		},
		computed: {
			registrationType() {
				return isIos() ? '1' : '0'
			}
		},
		methods: {
			navClick({
				index
			}) {
				this.initFlag = false;
				this.navActive = index;
				setTimeout(() => {
					this.delay = index;
				}, 200);
			},
			/**
			 * 登录
			 */
			login() {
				const {
					phone,
					password
				} = this.loginForm
				const rsaPassword = jsencrypt.setEncrypt(env.publicKey, password)
				const {
					code,
					uuid
				} = this.$refs.codeRef.returnCodeData()
				PostLoginByPwd({
					phone,
					password: rsaPassword,
					code,
					uuid,
					registrationType: this.registrationType,
					registrationId: this.registrationId
				}).then(res => {
					setToken(res.token);
					this.$store.dispatch('getPushMsgState');
					uni.switchTab({
						url: '/pages/index/index'
					});
				}, err => {
					this.$refs.codeRef.handleGetCaptcha();
				})
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
			},

			/**
			 * 获取短信请求参数
			 */
			smsPayload() {
				const {
					code,
					uuid
				} = this.$refs.codeRefbySms.returnCodeData();
				return {
					uuid,
					captcha: code,
					phone: this.smsLoginForm.phone
				};
			},

			/**
			 * 短信登录
			 */
			loginBySms() {
				console.log(this.smsLoginForm.uuid, 'this.smsLoginForm.uuid');
				if (this.smsLoginForm.uuid) {
					loginBySms({
						...this.smsLoginForm,
						registrationType: this.registrationType,
						registrationId: this.registrationId
					}).then(res => {
						setToken(res.token);
						this.$store.dispatch('getPushMsgState');
						uni.switchTab({
							url: '/pages/index/index'
						});
					}, () => {
						this.$refs.sms.reset();
					});
				}

			},

			/**
			 * 重置短信
			 */
			smsReset() {
				this.$refs.codeRefbySms.handleGetCaptcha();
			},

			/**
			 * 短信认证通过
			 */
			checkedBySms(smsInfo) {
				Object.assign(this.smsLoginForm, smsInfo);
			},
			/**
			 * 获取登录设备注册号
			 */
			getRegistrationID() {
				isApp() && push.getRegistrationID().then(res => {
					console.log(res, 'lllllllllllll')
					this.registrationId = res
				});
			},
		}
	};
</script>

<style lang="scss">
	.ui-body {
		// height: 100vh;
		text-align: center;
		padding-bottom: 50rpx;
		// background-position: 0 -100rpx;
	}

	.ui-logo {
		height: 69rpx;
		width: 366rpx;
		font-size: 69rpx;
		color: #fff;
		margin-top: 100rpx;
		display: inline-block;
		font-weight: bold;
	}

	.ui-tip {
		margin-top: 30rpx;
		color: #fff;
		font-size: 40rpx;
	}

	.ui-nav {
		margin-top: 30rpx;
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
