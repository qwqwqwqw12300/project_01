<template>
	<app-body :bg="false">
		<view class="ui-body">
			<app-logo color="#353535" text="修改手机号"></app-logo>
			<view class="ui-form">
				<view class="ui-form-item">
					<view class="title">
						登录密码
					</view>
					<u-input v-model="password" :border="'none'" fontSize="28rpx" clearable />
				</view>
				<view class="ui-btn">
					<button @click="handleNext" class="default">下一步</button>
				</view>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		env
	} from "@/config/env.js";
	import {
		PostUpdatePhoneByPwd,
	} from '@/common/http/api.js';
	import jsencrypt from '@/common/utils/jsencrypt.js';
	export default {
		data() {
			return {
				password: '',
			}
		},
		computed: {
			userInfoPhone() {
				return this.$store.getters.userInfo.phone
			}
		},
		methods: {
			handleNext() {
				if (!this.password) {
					return uni.$u.toast('请填写密码')
				}
				PostUpdatePhoneByPwd({
					oldPhone: this.userInfoPhone,
					password: jsencrypt.setEncrypt(env.publicKey, this.password)
				}).then(res => {
					uni.$u.toast(res.msg)
					this.$setCache('phoneToken', res.token);
					setTimeout(() => {
						uni.navigateTo({
							url: '/pages/myself/mobile/new-mobile',
						})
					}, 500)
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-body {
		// padding: 0 44rpx;
		margin-top: 60rpx;
		padding-bottom: 60rpx;
		background-color: #ffffff;
	}

	.ui-form {
		margin-top: 80rpx;
		padding: 0 44rpx;

		.ui-form-item {
			display: flex;
			align-items: center;
			justify-content: space-between;
			min-height: 130rpx;
			border-bottom: solid 2px #f9f9f9;

			.title {
				font-size: 36rpx;
				color: #353535;
				font-weight: 400;
				width: 200rpx;
			}

		}

		.ui-btn {
			margin-top: 80rpx;
		}
	}
</style>
