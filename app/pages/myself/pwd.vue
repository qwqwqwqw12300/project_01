<!--
* @Author: zhanch
* @Date: 2022-12-28 19:03:59
* @FilePath: /pages/myself/pwd.vue
* @Description: 修改密码
-->

<template>
	<app-body>
		<!-- 	<text class="ui-logo">修改密码</text> -->
		<app-logo text="修改密码"></app-logo>
		<view class="ui-form">
			<view class="ui-form-item">
				<u-input placeholder="请输入原密码" v-model="form.oldPassword" :password="eyes.old" :border="'none'"
					prefixIcon="/static/images/login/lock.png" :prefixIconStyle="{height: '48rpx', width: '48rpx'}"
					fontSize="40rpx" clearable>
					<template slot="suffix">
						<u-icon :name="eyes.old ? 'eye-off' : 'eye-fill'" color="rgb(144, 147, 153)" size="48rpx"
							@click="eyesChange('old')"></u-icon>
					</template>
				</u-input>
			</view>
			<view class="ui-form-item">
				<u-input placeholder="请输入新密码" v-model="form.newPassword" :password="eyes.new" :border="'none'"
					prefixIcon="lock" :prefixIconStyle="{fontSize: '48rpx', color: '#555'}" fontSize="40rpx" clearable>
					<template slot="suffix">
						<u-icon :name="eyes.new ? 'eye-off' : 'eye-fill'" @click="eyesChange('new')"
							color="rgb(144, 147, 153)" size="48rpx"></u-icon>
					</template>
				</u-input>
			</view>
			<view class="ui-form-item">
				<u-input placeholder="再次输入新密码" v-model="form.reNewPassword" :password="eyes.reNew" :border="'none'"
					prefixIcon="lock" :prefixIconStyle="{fontSize: '48rpx', color: '#555'}" fontSize="40rpx" clearable>
					<template slot="suffix">
						<u-icon :name="eyes.reNew ? 'eye-off' : 'eye-fill'" @click="eyesChange('reNew')"
							color="rgb(144, 147, 153)" size="48rpx"></u-icon>
					</template>
				</u-input>
			</view>
			<view class="wd-btn-group">
				<button @click="handleCancel" class="cancel">取消</button>
				<button @click="handleReset" class="default">重置</button>
			</view>
			<view class="ui-forget">
				<text class="active" @click="forgot">忘记密码？</text>
			</view>
		</view>

		<!-- <view class="wd-btn-group">
			<button @click="handleCancel" class="cancel">取消</button>
			<button @click="handleReset" class="default">重置</button>
		</view> -->
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
			},

			/**
			 * 忘记密码
			 */
			forgot() {
				uni.navigateTo({
					url: '/pages/login/forgot'
				})
			},
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

		.ui-form-item {
			padding: 44rpx 0;
			border-bottom: 1rpx solid #e2e2e2;
		}
	}

	.wd-btn-group {
		margin-top: 100rpx;
	}

	.ui-forget {
		margin-top: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 30rpx;
		color: #599FFF;
	}
</style>