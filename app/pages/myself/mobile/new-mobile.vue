<template>
	<app-body :bg="false">
		<view class="ui-body">
			<app-logo color="#353535" text="修改手机号"></app-logo>
			<view class="ui-form">
				<view class="ui-form-item">
					<view class="title">
						新手机号码
					</view>
					<u-input v-model="form.newPhone" :border="'none'" maxlength="11" type="number" fontSize="28rpx"
						clearable />
				</view>
				<view class="ui-form-item">
					<view class="title">
						图形验证码
					</view>
					<view style="flex: 1;">
						<figure-code ref="codeRefbySms"></figure-code>
					</view>
				</view>
				<view class="ui-form-item">
					<view class="title">
						短信验证码
					</view>
					<view style="flex: 1;">
						<verify-code ref="smsRef" @reset="newReset" :payload="smsPayload" @checked="checkedBySms">
						</verify-code>
					</view>
				</view>
				<view class="ui-btn">
					<button @click="handleNext" class="default">提交</button>
				</view>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		PostUpdatePhoneByToken,
	} from '@/common/http/api.js';
	import {
		phoneValidator
	} from '@/common/utils/util';
	export default {
		data() {
			return {
				form: {
					newPhone: '',
					newCode: '',
					newuuid: '',
				}
			}
		},
		mounted() {
			this.form.token = this.$getCache('phoneToken');
		},
		methods: {
			newReset() {
				this.$refs.codeRefbySms.handleGetCaptcha();
			},
			/**
			 * 新手机短信认证通过
			 */
			checkedBySms(smsInfo) {
				this.form.newuuid = smsInfo.uuid
				this.form.newCode = smsInfo.code
			},
			/**
			 * 手机获取短信请求参数
			 */
			smsPayload() {
				const {
					code,
					uuid
				} = this.$refs.codeRefbySms.returnCodeData();
				return {
					uuid,
					captcha: code,
					phone: this.form.newPhone
				};
			},
			handleNext() {
				const {
					newPhone,
					newCode
				} = this.form
				if (!phoneValidator(newPhone)) {
					return uni.$u.toast('请填写正确的手机号码')
				}
				if (newCode.length !== 4) {
					return uni.$u.toast('请填写正确的验证码')
				}
				PostUpdatePhoneByToken({
					...this.form,
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						uni.switchTab({
							url: '/pages/myself/myself'
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
				font-size: 34rpx;
				color: #353535;
				font-weight: 400;
				width: 240rpx;
			}
		}

		.ui-btn {
			margin-top: 80rpx;
		}
	}
</style>
