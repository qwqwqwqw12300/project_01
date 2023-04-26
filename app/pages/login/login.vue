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
			<view class="ui-logo">
				<image src="../../static/images/login-logo.png"></image>
				<text>艾吉通</text>
			</view>
			<!-- <view class="ui-tip">艾吉通·守护您每一分钟!</view> -->
			<view class="ui-nav">
				<u-tabs lineWidth="160rpx" lineColor="#353535" :itemStyle="{ width: '250rpx', height: '90rpx' }"
					:activeStyle="{ color: '#353535', fontSize: '36rpx' }"
					:inactiveStyle="{ color: '#888888', fontSize: '36rpx' }" lineHeight="4.5rpx" :list="navList"
					@click="navClick"></u-tabs>
			</view>
			<!-- 账号登录 -->
			<u-transition :mode="initFlag ? '' : 'fade-left'" :show="navActive === 0 && delay === 0">
				<view class="ui-form">
					<view class="ui-form-item">
						<u--input prefixIcon="../../static/images/login/phone.png"
							:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx"
							v-model="loginForm.phone" maxlength="11" type="number" placeholder="请输入手机号码"
							:border="'none'" clearable></u--input>
					</view>
					<view class="ui-form-item">
						<figure-code prefixIcon="../../static/images/login/code.png"
							:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" size="4"
							ref="codeRef"></figure-code>
					</view>
					<view class="ui-form-item">
						<u-input prefixIcon="../../static/images/login/lock.png"
							:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx"
							v-model="loginForm.password" placeholder="请输入你的密码" :password="true" :border="'none'"
							clearable>
						</u-input>
					</view>
					<view class="ui-bot">
						<text class="active" @click="register">免费注册</text>
						<text class="active" @click="forgot">忘记密码？</text>
					</view>
					<view class="ui-btn"><button @click="login" class="default">立即登录</button></view>
				</view>
			</u-transition>
			<!-- /账号登录 -->
			<!-- 验证码登录 -->
			<u-transition mode="fade-right" :show="navActive !== 0 && delay !== 0">
				<view class="ui-form">
					<view class="ui-form-item">
						<!-- 	<u-text prefixIcon="phone-fill" iconStyle="font-size: 30rpx" text="手机号码" color="#444"
							size="28rpx">
						</u-text> -->
						<u-input prefixIcon="../../static/images/login/phone.png"
							:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx"
							v-model="smsLoginForm.phone" type="number" placeholder="请输入手机号码" maxlength="11"
							:border="'none'" clearable></u-input>
					</view>
					<view class="ui-form-item">
						<figure-code prefixIcon="../../static/images/login/code.png"
							:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" size="4"
							ref="codeRefbySms"></figure-code>
					</view>
					<view class="ui-form-item">
						<verify-code prefixIcon="../../static/images/login/sms.png"
							:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" ref="sms"
							@reset="smsReset" :payload="smsPayload" smsType="1" @checked="checkedBySms">
						</verify-code>
					</view>
					<view class="ui-bot">
						<text class="active" @click="register">免费注册</text>
						<text class="active" @click="forgot">忘记密码？</text>
					</view>
					<view class="ui-btn"><button class="default" @click="loginBySms">立即登录</button></view>
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
		PsotSetJGInfo
	} from '@/common/http/api.js';
	import {
		isApp,
		isIos,
		phoneValidator
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
					phone: '',
					code: '',
					password: '',
					uuid: '',
				},
				/**短信登录参数**/
				smsLoginForm: {
					phone: '',
					uuid: '',
					code: ''
				},
			};
		},
		mounted() {
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
				if (!phoneValidator(phone)) {
					return uni.$u.toast('请填写正确的手机号码')
				}
				if (!password) {
					return uni.$u.toast('请填写密码')
				}
				// if (code.length !== 6) {
				// 	return uni.$u.toast('请填写正确的验证码')
				// }
				PostLoginByPwd({
					phone,
					password: rsaPassword,
					code,
					uuid,
					registrationType: '1',
					registrationId: 'dfdfdfdnufbgudbgud'
				}).then(res => {
					setToken(res.token);
					this.$store.dispatch('setJGInfo'); // 设置推送id
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
				// console.log(this.smsLoginForm.phone, 'ooo')
				// if (!uni.$u.test.mobile(this.smsLoginForm.phone)) {
				// 	return uni.$u.toast('请填写正确的手机号码')
				// }
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
				console.log(this.smsLoginForm, 'this.smsLoginForm.uuid');
				if (this.smsLoginForm.uuid) {
					const {
						phone,
						code
					} = this.smsLoginForm
					if (!phoneValidator(phone)) {
						return uni.$u.toast('请填写正确的手机号码')
					}
					// if (code.length !== 6) {
					// 	return uni.$u.toast('请填写正确的验证码')
					// }
					loginBySms({
						...this.smsLoginForm
					}).then(res => {
						setToken(res.token);
						this.$store.dispatch('getPushMsgState');
						this.$store.dispatch('setJGInfo'); // 设置极光id
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
			}
		}
	};
</script>

<style lang="scss">
	.ui-body {
		// height: 100vh;
		text-align: center;
		padding-bottom: 20rpx;
		box-sizing: border-box;
		// background-position: 0 -100rpx;
	}

	.ui-logo {
		width: 100%;
		margin: 121rpx 0 60rpx 0;
		display: inline-flex;
		font-weight: bold;
		align-items: center;
		flex-direction: column;

		image {
			height: 144rpx;
			width: 144rpx;
		}

		text {
			font-size: 40rpx;
			color: #353535;
		}

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
		margin-top: 50rpx;
		padding: 0 80rpx;

		.ui-form-item {
			padding: 44rpx 0;
			border-bottom: 1rpx solid #e2e2e2;
		}

		.ui-input {
			margin: 34rpx 0 60rpx 0;
			border-bottom: 1px solid #e2e2e2;
		}

		.ui-bot {
			margin-top: 49rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			font-size: 30rpx;
			color: #599FFF;
		}

		.ui-btn {
			margin-top: 59rpx;
		}

		.wd-sms {
			width: 160rpx;
			// height: 60rpx;
		}
	}

	.ui-nav {
		background: unset !important;
	}
</style>
