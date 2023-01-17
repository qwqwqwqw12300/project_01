<!--
* @Author: zhanch
* @Date: 2022-12-28 19:03:59
* @FilePath: /pages/myself/pwd.vue
* @Description: 修改密码
-->

<template>
	<app-body :bodyStyle="{backgroundPositionY: '-100rpx'}">
		<!-- 	<text class="ui-logo">修改密码</text> -->
		<app-logo text="修改密码"></app-logo>
		<view class="ui-form">
			<view class="ui-form-item">
				<u-text prefixIcon="lock" iconStyle="font-size: 32rpx" text="原密码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u-input v-model="form.oldPassword" placeholder="请输入原密码" :password="eyes.old" :border="'none'"
						fontSize="28rpx" clearable>
						<template slot="suffix">
							<u-icon :name="eyes.old ? 'eye-off' : 'eye-fill'" color="rgb(144, 147, 153)" @click="eyesChange('old')" size="36rpx"></u-icon>
						</template>
					</u-input>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="lock" iconStyle="font-size: 32rpx" text="密码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u-input v-model="form.newPassword" placeholder="请输入新密码" :border="'none'" :password="eyes.new"
						fontSize="28rpx" clearable>
						<template slot="suffix">
							<u-icon :name="eyes.new ? 'eye-off' : 'eye-fill'" color="rgb(144, 147, 153)" @click="eyesChange('new')" size="36rpx"></u-icon>
						</template>
					</u-input>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="lock" iconStyle="font-size: 32rpx" text="确认密码" color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u-input v-model="form.reNewPassword" placeholder="再次输入新密码" :border="'none'" :password="eyes.reNew"
						fontSize="28rpx" clearable>
						<template slot="suffix">
							<u-icon :name="eyes.reNew ? 'eye-off' : 'eye-fill'" color="rgb(144, 147, 153)" @click="eyesChange('reNew')" size="36rpx"></u-icon>
						</template>
					</u-input>
				</view>
			</view>
			<view class="wd-btn-gloup">
				<button @click="handleReset">重置</button>
				<button @click="handleCancel">取消</button>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		PostUpdatePwd,
	} from '@/common/http/api.js';
	import jsencrypt from '@/common/utils/jsencrypt.js'
	import {
		env
	} from "@/config/env.js";
	export default {
		data() {
			return {
				form: {
					oldPassword: '',
					newPassword: '',
					reNewPassword: '',
				},
				eyes: {
					old: true,
					new: true,
					reNew: true,
				}
			};
		},
		methods: {
			/**
			 * 重置
			 */
			handleReset() {
				const {
					oldPassword,
					newPassword,
					reNewPassword
				} = this.form
				if (!oldPassword) {
					return uni.$u.toast('请填写原密码')
				}
				if (!newPassword || !reNewPassword) {
					return uni.$u.toast('请填写新密码')
				}
				if (newPassword !== reNewPassword) {
					return uni.$u.toast('两次密码不一致')
				}
				const submitForm = {
					newPassword: jsencrypt.setEncrypt(env.publicKey, newPassword),
					oldPassword: jsencrypt.setEncrypt(env.publicKey, oldPassword)
				}
				PostUpdatePwd({
					...submitForm
				}).then(res => {
					uni.$u.toast(res.msg)
					this.handleCancel()
				})
			},
			/**
			 * 取消
			 */
			handleCancel() {
				setTimeout(() => {
					uni.navigateBack()
				}, 500)
			},
			/**
			 * 密码隐藏显示
			 */
			eyesChange(type) {
				this.eyes[type] = !this.eyes[type];
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
