<!--
* @Author: zhanch
* @Date: 2022-12-27 15:02:25
* @FilePath: /pages/login/forgot.vue
* @Description:找回密码
-->

<template>
	<app-body :needService="false" :hideTitle="false" title="找回密码">
		<view class="ui-body">
			<!-- <text class="ui-logo">找回密码</text> -->
			<view class="ui-form">
				<view class="ui-form-item">
					<!-- <u-text prefixIcon="phone" iconStyle="font-size: 30rpx" text="手机号码" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input">
						<u-input v-model="formParams.phone" placeholder="请输入手机号码" type="number" maxlength="11"
							:border="'none'" fontSize="28rpx" clearable></u-input>
					</view> -->
					<u-input v-model="formParams.phone" placeholder="请输入手机号码" type="number" maxlength="11"
						:border="'none'" prefixIcon="../../static/images/login/phone.png"
						:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" clearable></u-input>
				</view>
				<view class="ui-form-item">
					<figure-code prefixIcon="../../static/images/login/code.png"
						:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" size="4" ref="codeRef">
					</figure-code>
					<!-- <graphic-input ref="codeRef"></graphic-input> -->
				</view>
				<view class="ui-form-item">
					<!-- <sms-input ref="sms" :payload="smsPayload" @reset="codeReset" @checked="smsChecked"></sms-input> -->
					<verify-code prefixIcon="../../static/images/login/sms.png"
						:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" ref="sms"
						@reset="codeReset" :payload="smsPayload" @checked="smsChecked">
					</verify-code>
				</view>
				<view class="ui-form-item">
					<!-- <u-text prefixIcon="lock" iconStyle="font-size: 32rpx" text="密码" color="#444" size="28rpx"></u-text> -->
					<!-- <view class="ui-input" prop="formParams.pwd">
						<u-input placeholder="请输入你的密码" v-model="formParams.pwd" :password="eyes.pwd" :border="'none'"
							fontSize="28rpx" clearable>
							<template slot="suffix">
								<u-icon :name="eyes.pwd ? 'eye-off' : 'eye-fill'" color="rgb(144, 147, 153)"
									size="36rpx" @click="eyesChange('pwd')"></u-icon>
							</template>
						</u-input>
					</view> -->
					<u-input placeholder="请输入你的密码" v-model="formParams.pwd" :password="eyes.pwd" :border="'none'"
						prefixIcon="../../static/images/login/lock.png"
						:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" clearable>
						<template slot="suffix">
							<u-icon :name="eyes.pwd ? 'eye-off' : 'eye-fill'" color="rgb(144, 147, 153)" size="48rpx"
								@click="eyesChange('pwd')"></u-icon>
						</template>
					</u-input>
				</view>
				<view class="ui-form-item">
					<!-- <u-text prefixIcon="lock-fill" iconStyle="font-size: 32rpx" text="确认密码" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input" prop="formParams.confirm">
						<u-input placeholder="请再次输入您的密码" v-model="formParams.confirm" :password="eyes.confirm"
							:border="'none'" fontSize="28rpx" clearable>
							<template slot="suffix">
								<u-icon :name="eyes.confirm ? 'eye-off' : 'eye-fill'" @click="eyesChange('confirm')"
									color="rgb(144, 147, 153)" size="36rpx"></u-icon>
							</template>
						</u-input>
					</view> -->
					<u-input placeholder="请再次输入您的密码" v-model="formParams.confirm" :password="eyes.confirm"
						:border="'none'" prefixIcon="lock" :prefixIconStyle="{fontSize: '48rpx', color: '#555'}"
						fontSize="40rpx" clearable>
						<template slot="suffix">
							<u-icon :name="eyes.confirm ? 'eye-off' : 'eye-fill'" @click="eyesChange('confirm')"
								color="rgb(144, 147, 153)" size="48rpx"></u-icon>
						</template>
					</u-input>
				</view>
				<view class="ui-btn"><button class="default" @click="reset">重置密码</button></view>
				<!-- <text class="ui-link active" @click="goLogin">老朋友？点此登录</text> -->
			</view>
		</view>
	</app-body>

</template>

<script>
	import {
		resetMemberPwd
	} from '../../common/http/api';
	import {
		phoneValidator
	} from '../../common/utils/util';
	import jsencrypt from '@/common/utils/jsencrypt.js'
	import {
		env
	} from '../../config/env';
	import {
		removeToken
	} from '@/common/utils/auth.js';
	export default {
		data() {
			return {
				/**表单参数**/
				formParams: {
					phone: '',
					code: '',
					pwd: '',
					confirm: '',
					uuid: ''
				},
				/**密码显隐**/
				eyes: {
					pwd: true,
					confirm: true
				},
			};
		},
		methods: {

			/**
			 * 跳转登录
			 */
			goLogin() {
				uni.reLaunch({
					url: '/pages/login/login'
				});
				removeToken()
			},

			/**
			 * 重置密码
			 */
			reset() {
				const {
					phone,
					pwd,
					confirm,
					code
				} = this.formParams
				if (!phoneValidator(phone)) {
					return uni.$u.toast('请填写正确的手机号码')
				}
				// if (code.length !== 4) {
				// 	return uni.$u.toast('请填写正确的短信码')
				// }
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
					resetMemberPwd({
						phone: this.formParams.phone,
						password: jsencrypt.setEncrypt(env.publicKey, this.formParams.pwd),
						code: this.formParams.code,
						uuid: this.formParams.uuid
					}).then(res => {
						uni.showModal({
							title: '',
							content: '重置密码成功，是否立即前往登录？',
							success: res => {
								if (res.confirm) {
									this.goLogin();
								}
							}
						});
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
			 * 重置验证码
			 */
			codeReset() {
				this.$refs.codeRef.handleGetCaptcha();
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
				console.log(this.formParams, 'this.formParams');
				const {
					code,
					uuid
				} = this.$refs.codeRef.returnCodeData();
				return {
					uuid,
					captcha: code,
					phone: this.formParams.phone
				}
			},

			/**
			 * 短信验证成功
			 */
			smsChecked(smsInfo) {
				Object.assign(this.formParams, smsInfo);
			}
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
		color: #353535;
		margin-top: 130rpx;
		display: inline-block;
		font-weight: bold;
	}

	.ui-form {
		// margin-top: 316rpx;
		margin-top: 100rpx;
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
			display: flex;
			align-items: center;
			justify-content: space-between;
			font-size: 36rpx;
			color: #484848;
		}

		.ui-mini {
			width: 200rpx;
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
			font-size: 30rpx;
			color: #0094ff;
		}
	}
</style>