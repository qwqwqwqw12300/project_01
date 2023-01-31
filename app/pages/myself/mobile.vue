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
				<u-text prefixIcon="phone" iconStyle="font-size: 30rpx" text="原手机号码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u--input v-model="form.oldPhone" placeholder="请输入手机号码" :border="'none'" fontSize="28rpx" clearable>
					</u--input>
				</view>
			</view>
			<view class="ui-form-item">
				<graphic-input ref="oldCodeRefbySms"></graphic-input>
			</view>
			<view class="ui-form-item">
				<sms-input ref="oldSmsRef" @reset="oldReset" :payload="oldSmsPayload" @checked="oldCheckedBySms">
				</sms-input>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="phone" iconStyle="font-size: 30rpx" text="新手机号码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u--input v-model="form.newPhone" placeholder="请输入手机号码" :border="'none'" fontSize="28rpx" clearable>
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
			<view class="wd-btn-gloup">
				<button type="default" @click="handleSubmit">重置</button>
				<button type="default" @click="handleBack">取消</button>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		PostUpdatePhone,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				form: {
					password: '',
					newPhone: '',
					oldPhone: '',
					oldCode: '',
					newCode: '',
				}
			};
		},
		methods: {

			/**
			 * 提交
			 */
			handleSubmit() {
				console.log(this.form, '99')
				PostUpdatePhone({
					...this.form
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						this.handleBack()
					}, 500)
				}, err => {
					this.$refs.oldSmsRef.reset();
					this.$refs.newSmsRef.reset();
				})
			},

			oldReset() {
				console.log(123);
				this.$refs.oldCodeRefbySms.handleGetCaptcha();
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
			 * 原手机短信认证通过
			 */
			oldCheckedBySms(smsInfo) {
				this.form.oldCode = smsInfo.code

			},
			/**
			 * 新手机短信认证通过
			 */
			newCheckedBySms(smsInfo) {
				this.form.newCode = smsInfo.code
			},
			/**
			 * 旧手机获取短信请求参数
			 */
			oldSmsPayload() {
				const {
					code,
					uuid
				} = this.$refs.oldCodeRefbySms.returnCodeData();
				return {
					uuid,
					captcha: code,
					phone: this.form.oldPhone
				};
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

		.wd-btn-gloup {
			margin: 80rpx 0;
		}
	}
</style>
