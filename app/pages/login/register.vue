<!--
* @Author: zhanch
* @Date: 2022-12-27 12:47:07
 * @LastEditors: Please set LastEditors
* @Description: 注册页面
-->

<template>
	<app-body :needService="false" title="注册账号">
		<view class="ui-body">
			<!-- <text class="ui-logo">注册账号</text> -->
			<u-form class="ui-form">
				<view class="ui-form-item">
					<u-input placeholder="请输入手机号码" type="number" v-model="formParams.phone" maxlength="11"
						:border="'none'" prefixIcon="../../static/images/login/phone.png"
						:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" clearable></u-input>
				</view>

				<view class="ui-form-item">
					<figure-code prefixIcon="../../static/images/login/code.png"
						:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" size="4" ref="codeRef">
					</figure-code>
				</view>
				<view class="ui-form-item">
					<verify-code prefixIcon="../../static/images/login/sms.png"
						:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" ref="sms"
						@reset="codeReset" :payload="smsPayload" @checked="smsChecked">
					</verify-code>
				</view>
				<view class="ui-form-item">
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
					<u-input placeholder="请再次输入您的密码" v-model="formParams.confirm" :password="eyes.confirm"
						:border="'none'" prefixIcon="lock" :prefixIconStyle="{fontSize: '48rpx', color: '#555'}"
						fontSize="40rpx" clearable>
						<template slot="suffix">
							<u-icon :name="eyes.confirm ? 'eye-off' : 'eye-fill'" @click="eyesChange('confirm')"
								color="rgb(144, 147, 153)" size="48rpx"></u-icon>
						</template>
					</u-input>
				</view>
				<view class="ui-btn"><button class="default" @click="register" style="margin-bottom: 10px;">注册</button>
				</view>
				<!-- <text class="ui-link active" @click="goLogin">老朋友？点此登录</text> -->
				<view class="ui-agreement">
					<u-checkbox-group v-model="radiovalue" @change="handleShowModal">
						<u-checkbox :customStyle="{ marginRight: '8rpx' }" shape="circle" activeColor="#fdbc2b"
							size="30rpx" name="ag"></u-checkbox>
					</u-checkbox-group>
					<text @tap="userAgreement">我已阅读并同意《用户协议与隐私政策》</text>
				</view><strong></strong>
			</u-form>
		</view>
		<u-popup :closeable="true" zIndex="99" :round="10" :show="showVisible" mode="center" duration="0"
			@close="closeChange">
			<view class="ui-content">
				<view class="ui-content-title">艾吉通用户协议与隐私政策</view>
				<scroll-view scroll-y class="uni-scroll-content">
					<view v-html="content"></view>
				</scroll-view>
				<view class="ui-content-bottom">
					<button class="ui-content-bottom-cancel" @tap="agreementTap(false)">不同意</button>
					<button class="ui-content-bottom-confirm" @tap="agreementTap(true)">同意</button>
				</view>
			</view>
		</u-popup>
	</app-body>
</template>

<script>
	import {
		regMember,
		PostUserAgreement
	} from '../../common/http/api';
	import jsencrypt from '@/common/utils/jsencrypt.js'
	import {
		env
	} from '../../config/env';
	import {
		push
	} from '@/common/sdk/push.js';
	import {
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
				radiovalue: [],
				showVisible: false,
				content: '',
			};
		},
		mounted() {},
		methods: {
			/**
			 * 打开用户协议弹窗
			 */
			userAgreement() {
				PostUserAgreement({}).then(res => {
					this.content = res.data.content
				})
				// uni.navigateTo({
				// 	url: '/pages/myself/agreement/agreement-detail'
				// });
				this.showVisible = true
			},
			/**
			 * 取消用户协议弹窗
			 */
			handleCancle() {
				this.radiovalue = []
				this.radiovalue.push('ag')
				this.showVisible = false
			},
			/**
			 * 点击单选框用户协议弹窗
			 */
			handleShowModal() {
				if (!this.radiovalue.includes('ag')) {
					this.userAgreement()
				}
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
				console.log(this.radiovalue, 'this.formParams');
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
					if (!this.radiovalue.includes('ag')) {
						uni.showToast({
							icon: 'none',
							title: '为保障您的合法权益，请阅读并同意《用户协议与隐私政策》',
							duration: 2000
						});
						return;
					}
					regMember({
						phone: this.formParams.phone,
						password: jsencrypt.setEncrypt(env.publicKey, this.formParams.pwd),
						code: this.formParams.code,
						smsUuid: this.formParams.uuid
					}).then(res => {
						setToken(res.token);
						this.$store.dispatch('getPushMsgState');
						this.interVal = setInterval(() => {
							this.$store.dispatch('setJGInfo')
							if (this.$store.getters.interSwitch) clearInterval(this.interVal);
						}, 1000)
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
				this.$refs.codeRef.handleGetCaptcha();
			},
			agreementTap(type) {
				this.showVisible = false
				if (!type) return this.radiovalue = []
				this.radiovalue = ['ag']
			},
			closeChange() {
				this.showVisible = false
				this.radiovalue = []
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
			width: 160rpx;
			// height: 60rpx;
		}

		.ui-agreement {
			display: inline-flex;
			align-items: center;
			justify-content: center;

			text {
				font-size: 30rpx;
				color: #fdbc2b;
			}
		}

		.ui-btn {
			margin: 30rpx 0 0 0;
		}

		.ui-link {
			display: inline-block;
			margin: 30rpx 30rpx;
			font-size: 30rpx;
			color: #0094ff;
		}
	}

	.ui-content {
		width: 660rpx;
		height: 900rpx;
		border-radius: 20rpx;
		border-bottom-left-radius: 0;
		border-bottom-right-radius: 0;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: #fff;

		&-title {
			padding: 22rpx 0;
			font-weight: bold;
			text-align: center;
			font-size: 36rpx;
		}

		&-bottom {
			width: 660rpx;
			display: flex;
			align-items: center;
			line-height: 0;

			button {
				width: 50%;
				border-radius: 0;
			}

			&-cancel {}

			&-confirm {
				border-left: 1px solid #e8e8e8;
				color: #599FFF;
			}
		}

		.uni-scroll-content {
			width: 100%;
			height: 808rpx;
			word-wrap: break-word;
			word-break: normal;
			text-indent: 1em;
			box-sizing: border-box;
			padding: 0 31rpx 0 31rpx;
		}
	}

	/deep/ .u-popup__content {
		border-bottom-left-radius: 0 !important;
		border-bottom-right-radius: 0 !important;
	}
</style>