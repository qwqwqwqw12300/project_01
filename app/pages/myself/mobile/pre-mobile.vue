<template>
	<app-body :bg="false" title="修改手机号">
		<view class="ui-body">
			<!-- <app-logo color="#353535" text="修改手机号"></app-logo> -->
			<view class="ui-form">
				<view class="ui-form-item">
					<view class="title">
						当前手机号码
					</view>
					<u-input :value="userInfoPhone" disabled disabledColor="#fff" :border="'none'" maxlength="11"
						type="number" fontSize="28rpx" clearable />
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
					<button @click="handleNext" class="default">下一步</button>
				</view>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		PostUpdatePhoneBySms,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				form: {
					oldPhone: '',
					oldCode: '',
					uuid: '',
				}
			}
		},
		computed: {
			userInfoPhone() {
				return this.$store.getters.userInfo.phone
			}
		},
		methods: {
			newReset() {
				this.$refs.codeRefbySms.handleGetCaptcha();
			},
			/**
			 * 新手机短信认证通过
			 */
			checkedBySms(smsInfo) {
				console.log(smsInfo, 'ss')
				this.form.uuid = smsInfo.uuid
				this.form.oldCode = smsInfo.code
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
					phone: this.userInfoPhone
				};
			},
			handleNext() {
				const {
					oldCode
				} = this.form
				// if (oldCode.length !== 6) {
				// 	return uni.$u.toast('请填写正确的验证码')
				// }
				PostUpdatePhoneBySms({
					...this.form,
					oldPhone: this.userInfoPhone,
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
		margin-top: 20rpx;
		padding-bottom: 60rpx;
		background-color: #ffffff;
	}
	/deep/.wd-fixed{
		padding-top: 20px;
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
