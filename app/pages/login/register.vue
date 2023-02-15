<!--
* @Author: zhanch
* @Date: 2022-12-27 12:47:07
* @LastEditors: zhanch
* @Description: 注册页面
-->

<template>
	<app-body :needService="false" :hideTitle="true" :bodyStyle="{backgroundPositionY: '-300rpx'}">
		<view class="ui-body">
			<text class="ui-logo">注册账号</text>
			<u-form class="ui-form">
				<view class="ui-form-item">
					<u-text prefixIcon="phone" iconStyle="font-size: 30rpx" text="手机号码" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input" prop="formParams.phone">
						<u-input placeholder="请输入手机号码" type="number" v-model="formParams.phone" maxlength="11"
							:border="'none'" fontSize="28rpx" clearable></u-input>
					</view>
				</view>
				<view class="ui-form-item">
					<graphic-input ref="codeRef"></graphic-input>
				</view>
				<view class="ui-form-item">
					<sms-input ref="sms" @checked="smsChecked" @reset="codeReset" :payload="smsPayload"></sms-input>
				</view>
				<view class="ui-form-item">
					<u-text prefixIcon="lock" iconStyle="font-size: 32rpx" text="密码" color="#444" size="28rpx"></u-text>
					<view class="ui-input" prop="formParams.pwd">
						<u-input placeholder="请输入你的密码" v-model="formParams.pwd" :password="eyes.pwd" :border="'none'"
							fontSize="28rpx" clearable>
							<template slot="suffix">
								<u-icon :name="eyes.pwd ? 'eye-off' : 'eye-fill'" color="rgb(144, 147, 153)"
									size="36rpx" @click="eyesChange('pwd')"></u-icon>
							</template>
						</u-input>
					</view>
				</view>
				<view class="ui-form-item">
					<u-text prefixIcon="lock-fill" iconStyle="font-size: 32rpx" text="确认密码" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input" prop="formParams.confirm">
						<u-input placeholder="请再次输入您的密码" v-model="formParams.confirm" :password="eyes.confirm"
							:border="'none'" fontSize="28rpx" clearable>
							<template slot="suffix">
								<u-icon :name="eyes.confirm ? 'eye-off' : 'eye-fill'" @click="eyesChange('confirm')"
									color="rgb(144, 147, 153)" size="36rpx"></u-icon>
							</template>
						</u-input>
					</view>
				</view>
				<view class="ui-agreement">
					<u-checkbox-group v-model="radiovalue">
						<u-checkbox :customStyle="{ marginRight: '8rpx' }" shape="square" activeColor="#fdbc2b"
							size="30rpx" name="ag"></u-checkbox>
					</u-checkbox-group>
					<text @tap="userAgreement">同意《用户协议》</text>
				</view>
				<view class="ui-btn"><button class="default" @click="register">注册</button></view>
				<text class="ui-link active" @click="goLogin">老朋友？点此登录</text>
			</u-form>
		</view>
	</app-body>
</template>

<script>
	import {
		regMember
	} from '../../common/http/api';
	import jsencrypt from '@/common/utils/jsencrypt.js'
	import {
		env
	} from '../../config/env';
	import {
		push
	} from '@/common/sdk/push.js';
	import {
		isApp,
		isIos,
		phoneValidator
	} from '../../common/utils/util';
	import {
		setToken
	} from '@/common/utils/auth.js';
	export default {
		data() {
			return {
				eyes: {
					pwd: true,
					confirm: true
				},
				formParams: {
					phone: '',
					pwd: '',
					confirm: '',
					uuid: '',
					code: ''
				},
				radiovalue: ['ag'],
				registrationId: '',
			};
		},
		computed: {
			registrationType() {
				return isIos() ? '1' : '0'
			}
		},
		mounted() {
			this.getRegistrationID();
		},
		methods: {
			/**
			 * 跳转用户协议
			 */
			userAgreement() {
				uni.navigateTo({
					url: '/pages/myself/user-agreement'
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

			/**
			 * 注册
			 */
			register() {
				console.log(this.formParams, 'this.formParams');
				const {
					phone,
					pwd,
					confirm,
					code
				} = this.formParams
				if (!phoneValidator(phone)) {
					return uni.$u.toast('请填写正确的手机号码')
				}
				if (code.length !== 4) {
					return uni.$u.toast('请填写正确的短信码')
				}
				if (!pwd) {
					return uni.$u.toast('请填写密码')
				}
				if (!confirm) {
					return uni.$u.toast('请填写确认密码')
				}

				if (pwd !== confirm) {
					return uni.$u.toast('两次密码不一致')
				}
				if (this.formParams.uuid && this.formParams.pwd && this.formParams.confirm) {
					if (!this.radiovalue.includes('ag')) {
						uni.showToast({
							icon: 'none',
							title: '请先勾选协议',
							duration: 2000
						});
						return;
					}
					regMember({
						phone: this.formParams.phone,
						password: jsencrypt.setEncrypt(env.publicKey, this.formParams.pwd),
						code: this.formParams.code,
						smsUuid: this.formParams.uuid,
						registrationType: this.registrationType,
						registrationId: this.registrationId
					}).then(res => {
						setToken(res.token);
						this.$store.dispatch('getPushMsgState');
						uni.$u.toast('注册成功')
						setTimeout(() => {
							uni.switchTab({
								url: '/pages/index/index'
							});
						}, 500);
					}, err => {
						this.$refs.sms.reset();
					});
				} else {
					uni.showToast({
						icon: 'none',
						title: '请先完善输入框信息',
						duration: 2000
					});
				}
			},

			/**
			 * 点击密码眼睛
			 * @param {Object} 
			 */
			eyesChange(type) {
				this.eyes[type] = !this.eyes[type];
			},

			/**
			 * 获取短信请求参数
			 */
			smsPayload() {
				const {
					code,
					uuid
				} = this.$refs.codeRef.returnCodeData();
				console.log(this.formParams.phone, ' this.formParams.phone')
				return {
					uuid,
					captcha: code,
					phone: this.formParams.phone
				}
			},

			/**
			 * 短信验证成功
			 */
			smsChecked(uuid) {
				this.formParams = {
					...this.formParams,
					...uuid
				};
			},

			/**
			 * 重置验证码
			 */
			codeReset() {
				console.log(12121);
				this.$refs.codeRef.handleGetCaptcha();
			},
			/**
			 * 获取登录设备注册号
			 */
			getRegistrationID() {
				isApp() && push.getRegistrationID().then(res => {
					this.registrationId = res
				});
			},
		}
	};
</script>

<style lang="scss">
	.ui-body {
		text-align: center;
	}

	.ui-logo {
		height: 69rpx;
		width: 366rpx;
		font-size: 69rpx;
		color: #fff;
		margin-top: 60rpx;
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
