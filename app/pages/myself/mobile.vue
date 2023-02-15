<!--
* @Author: zhanch
* @Date: 2022-12-29 11:27:28
* @FilePath: /pages/myself/mobile.vue
* @Description: 修改手机号
-->
<template>
	<app-body :bodyStyle="{backgroundPositionY: '-200rpx'}">
		<!-- <text class="ui-logo">修改手机号</text> -->
		<app-logo text="修改手机号"></app-logo>
		<view class="ui-form">
			<view class="ui-form-item">
				<u-text prefixIcon="phone" iconStyle="font-size: 30rpx" text="新手机号码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u--input v-model="form.newPhone" maxlength="11" type="number" placeholder="请输入手机号码"
						:border="'none'" fontSize="28rpx" clearable>
					</u--input>
				</view>
			</view>
			<view class="ui-form-item">
				<graphic-input ref="newCodeRefbySms"></graphic-input>
			</view>
			<view class="ui-form-item">
				<sms-input ref="newSmsRef" @reset="newReset" :payload="newSmsPayload" @checked="newCheckedBySms">
				</sms-input>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="lock-fill" iconStyle="font-size: 32rpx" text="密码" color="#444" size="28rpx">
				</u-text>
				<view class="ui-input">
					<u-input v-model="form.password" placeholder="请输入你的密码" :password="true" :border="'none'"
						fontSize="28rpx" clearable>
					</u-input>
				</view>
			</view>
			<view class="wd-btn-group">
				<button class="cancel" @click="handleBack">取消</button>
				<button class="default" @click="handleSubmit">重置</button>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		env
	} from "@/config/env.js";
	import jsencrypt from '@/common/utils/jsencrypt.js';
	import {
		PostUpdatePhone,
	} from '@/common/http/api.js';
	import {
		phoneValidator
	} from '../../common/utils/util';
	import {
		removeToken
	} from '@/common/utils/auth.js';
	export default {
		data() {
			return {
				form: {
					password: '',
					newPhone: '',
					newCode: '',
				}
			};
		},
		methods: {

			/**
			 * 提交
			 */
			handleSubmit() {
				const {
					password,
					newCode,
					newPhone
				} = this.form
				if (!phoneValidator(newPhone)) {
					return uni.$u.toast('请填写正确的手机号码')
				}
				if (!password) {
					return uni.$u.toast('请填写密码')
				}
				if (newCode.length !== 4) {
					return uni.$u.toast('请填写正确的验证码')
				}
				console.log(this.form.password, 'ppp')
				PostUpdatePhone({
					...this.form,
					password: jsencrypt.setEncrypt(env.publicKey, this.form.password)
				}).then(res => {
					uni.$u.toast(res.msg)
					removeToken()
					setTimeout(() => {
						uni.reLaunch({
							url: '/pages/login/login'
						})
					}, 500)
				}, err => {
					this.$refs.newSmsRef.reset();
				})
			},

			newReset() {
				this.$refs.newCodeRefbySms.handleGetCaptcha();
			},

			/**
			 * 取消
			 */
			handleBack() {
				uni.navigateBack()
			},
			/**
			 * 新手机短信认证通过
			 */
			newCheckedBySms(smsInfo) {
				console.log(smsInfo, 'ss')
				this.form.uuid = smsInfo.uuid
				this.form.newCode = smsInfo.code
			},
			/**
			 * 新手机获取短信请求参数
			 */
			newSmsPayload() {
				const {
					code,
					uuid
				} = this.$refs.newCodeRefbySms.returnCodeData();
				return {
					uuid,
					captcha: code,
					phone: this.form.newPhone
				};
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
		margin-top: 50rpx;
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

		.wd-btn-group {
			margin: 80rpx 0;

			button {
				color: #ffffff;
			}
		}
	}
</style>
