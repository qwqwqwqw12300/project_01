<!--
* @Author: zhanch
* @Date: 2022-12-27 12:46:17
* @FilePath: /pages/login/login.vue
* @Description: 登录页面
-->
<template>
	<app-body :needService="false" :hideTitle="true">
		<view class="ui-body">
			<!-- <image class="ui-logo" src="../../static/images/logo.png"></image> -->
			<view class="ui-logo">
				<image src="../../static/images/login-logo.png"></image>
				<text>艾吉通</text>
			</view>
			<!-- <view class="ui-tip">艾吉通·守护您每一分钟!</view> -->
			<view class="ui-nav">
				<u-tabs lineWidth="160rpx" lineColor="#353535" :itemStyle="{ width: '250rpx', height: '90rpx' }"
					:activeStyle="{ color: '#353535', fontSize: '36rpx' }"
					:inactiveStyle="{ color: '#888888', fontSize: '36rpx' }" lineHeight="4.5rpx" :list="navList"
					@click="navClick"></u-tabs>
			</view>
			<!-- 账号登录 -->
			<u-transition :mode="initFlag ? '' : 'fade-left'" :show="navActive === 0 && delay === 0">
				<view class="ui-form">
					<view class="ui-form-item">
						<u--input prefixIcon="../../static/images/login/phone.png"
							:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx"
							v-model="loginForm.phone" maxlength="11" type="number" placeholder="请输入手机号码"
							:border="'none'" clearable></u--input>
					</view>
					<view class="ui-form-item">
						<u-input prefixIcon="../../static/images/login/lock.png"
							:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx"
							v-model="loginForm.password" placeholder="请输入你的密码" :password="true" :border="'none'"
							clearable>
						</u-input>
					</view>
					<view class="ui-form-item">
						<figure-code prefixIcon="../../static/images/login/code.png"
							:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" size="4"
							ref="codeRef"></figure-code>
					</view>
					<view class="ui-bot">
						<text class="active" @click="register">免费注册</text>
						<text class="active" @click="forgot">忘记密码？</text>
					</view>
					<view class="ui-btn"><button @click="login" class="default">立即登录</button></view>
					<view class="ui-agreement">
						<u-checkbox-group v-model="radiovalue" @change="handleShowModal">
							<u-checkbox :customStyle="{ marginRight: '8rpx' }" shape="circle" activeColor="#fdbc2b"
								size="30rpx" name="ag"></u-checkbox>
						</u-checkbox-group>
						<text @tap="userAgreement">我已阅读并同意《用户协议与隐私政策》</text>
					</view>
				</view>
			</u-transition>
			<!-- /账号登录 -->
			<!-- 验证码登录 -->
			<u-transition mode="fade-right" :show="navActive !== 0 && delay !== 0">
				<view class="ui-form">
					<view class="ui-form-item">
						<!-- 	<u-text prefixIcon="phone-fill" iconStyle="font-size: 30rpx" text="手机号码" color="#444"
							size="28rpx">
						</u-text> -->
						<u-input prefixIcon="../../static/images/login/phone.png"
							:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx"
							v-model="smsLoginForm.phone" type="number" placeholder="请输入手机号码" maxlength="11"
							:border="'none'" clearable></u-input>
					</view>
					<view class="ui-form-item">
						<figure-code prefixIcon="../../static/images/login/code.png"
							:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" size="4"
							ref="codeRefbySms"></figure-code>
					</view>
					<view class="ui-form-item">
						<verify-code prefixIcon="../../static/images/login/sms.png"
							:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" ref="sms"
							@reset="smsReset" :payload="smsPayload" smsType="1" @checked="checkedBySms">
						</verify-code>
					</view>
					<view class="ui-bot">
						<text class="active" @click="register">免费注册</text>
						<text class="active" @click="forgot">忘记密码？</text>
					</view>
					<view class="ui-btn"><button class="default" @tap="loginBySms">立即登录</button></view>
					<view class="ui-agreement">
						<u-checkbox-group v-model="radiovalue" @change="handleShowModal">
							<u-checkbox :customStyle="{ marginRight: '8rpx' }" shape="circle" activeColor="#fdbc2b"
								size="30rpx" name="ag"></u-checkbox>
						</u-checkbox-group>
						<text @tap="userAgreement">我已阅读并同意《用户协议与隐私政策》</text>
					</view>
				</view>
			</u-transition>
			<!-- /验证码登录 -->
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
		loginBySms,
		PostLoginByPwd,
		PsotSetJGInfo,
		PostUserAgreement
	} from '@/common/http/api.js';
	import {
		isApp,
		isIos,
		phoneValidator
	} from '../../common/utils/util';
	import {
		env
	} from "@/config/env.js";
	import {
		setToken
	} from '@/common/utils/auth.js';
	import {
		push
	} from '@/common/sdk/push.js';
	import jsencrypt from '@/common/utils/jsencrypt.js';
	export default {
		data() {
			return {
				/**登录模式列表**/
				navList: [{
						name: '账号密码登录'
					},
					{
						name: '短信登录'
					}
				],
				/**选中的登录模式**/
				navActive: 0,
				/**切换动画延时效果**/
				delay: 0,
				/**页面初始化标志**/
				initFlag: true,
				radiovalue: [],
				showVisible: false,
				content: '',
				loginForm: {
					phone: '',
					code: '',
					password: '',
					uuid: '',
				},
				/**短信登录参数**/
				smsLoginForm: {
					phone: '',
					uuid: '',
					code: ''
				},
			};
		},
		mounted() {
		},
		methods: {
			navClick({
				index
			}) {
				this.initFlag = false;
				this.navActive = index;
				setTimeout(() => {
					this.delay = index;
				}, 200);
			},
			/**
			 * 登录
			 */
			login() {
				const {
					phone,
					password
				} = this.loginForm
				const rsaPassword = jsencrypt.setEncrypt(env.publicKey, password)
				const {
					code,
					uuid
				} = this.$refs.codeRef.returnCodeData()
				if (!phoneValidator(phone)) {
					return uni.$u.toast('请填写正确的手机号码')
				}
				if (!password) {
					return uni.$u.toast('请填写密码')
				}
				console.log(this.loginForm.code, 'uuid')
				console.log(this.$refs.codeRef.returnCodeData(), 'this.$refs.codeRef.returnCodeData()')
				if(code.length !== 4) return uni.$u.toast('请输入四位图形验证码')
				// if (code.length !== 6) {
				// 	return uni.$u.toast('请填写正确的验证码')
				// }
				if (!this.radiovalue.includes('ag')) {
					uni.showToast({
						icon: 'none',
						title: '为保障您的合法权益，请阅读并同意《用户协议与隐私政策》',
						duration: 2000
					});
					return;
				}
				PostLoginByPwd({
					phone,
					password: rsaPassword,
					code,
					uuid,
					registrationType: '1',
					registrationId: 'dfdfdfdnufbgudbgud'
				}).then(res => {
					if (isApp()) {
						push.init(); // 推送服务初始化
					}
					setToken(res.token);
					this.$store.dispatch('setJGInfo'); // 设置推送id
					this.$store.dispatch('getPushMsgState');
					uni.switchTab({
						url: '/pages/index/index'
					});
				}, err => {
					this.$refs.codeRef.handleGetCaptcha();
				})
			},
			/**
			 * 取消用户协议弹窗
			 */
			handleCancle(){
				this.radiovalue = []
				this.radiovalue.push('ag')
				this.showVisible = false
			},
			/**
			 * 打开用户协议弹窗
			 */
			userAgreement() {
				PostUserAgreement({}).then(res=>{
					this.content = res.data.content
				})
				this.showVisible = true
			},
			/**
			 * 点击单选框用户协议弹窗
			 */
			handleShowModal(e){
				if (!this.radiovalue.includes('ag')) {
					this.userAgreement()
				}
			},
			/**
			 * 注册
			 */
			register() {
				uni.navigateTo({
					url: '/pages/login/register'
				})
			},
			/**
			 * 忘记密码
			 */
			forgot() {
				uni.navigateTo({
					url: '/pages/login/forgot'
				})
			},

			/**
			 * 获取短信请求参数
			 */
			smsPayload() {
				// console.log(this.smsLoginForm.phone, 'ooo')
				// if (!uni.$u.test.mobile(this.smsLoginForm.phone)) {
				// 	return uni.$u.toast('请填写正确的手机号码')
				// }
				const {
					code,
					uuid
				} = this.$refs.codeRefbySms.returnCodeData();
				return {
					uuid,
					captcha: code,
					phone: this.smsLoginForm.phone
				};
			},

			/**
			 * 短信登录
			 */
			loginBySms() {
				console.log(this.smsLoginForm, 'this.smsLoginForm.uuid');
				if (this.smsLoginForm.uuid) {
					const {
						phone,
						code
					} = this.smsLoginForm
					if (!phoneValidator(phone)) {
						return uni.$u.toast('请填写正确的手机号码')
					}
					if (!this.radiovalue.includes('ag')) {
						uni.showToast({
							icon: 'none',
							title: '为保障您的合法权益，请阅读并同意《用户协议与隐私政策》',
							duration: 2000
						});
						return;
					}
					// if (code.length !== 6) {
					// 	return uni.$u.toast('请填写正确的验证码')
					// }
					loginBySms({
						...this.smsLoginForm
					}).then(res => {
						if (isApp()) {
							push.init(); // 推送服务初始化
						}
						setToken(res.token);
						this.$store.dispatch('getPushMsgState');
						this.$store.dispatch('setJGInfo'); // 设置极光id
						uni.switchTab({
							url: '/pages/index/index'
						});
					}, () => {
						this.$refs.sms.reset();
					});
				}

			},

			/**
			 * 重置短信
			 */
			smsReset() {
				this.$refs.codeRefbySms.handleGetCaptcha();
			},

			/**
			 * 短信认证通过
			 */
			checkedBySms(smsInfo) {
				Object.assign(this.smsLoginForm, smsInfo);
			},
			agreementTap(type) {
				this.showVisible = false
				if(!type) return this.radiovalue = []
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
		// height: 100vh;
		text-align: center;
		padding-bottom: 20rpx;
		box-sizing: border-box;
		// background-position: 0 -100rpx;
	}

	.ui-logo {
		width: 100%;
		margin: 121rpx 0 60rpx 0;
		display: inline-flex;
		font-weight: bold;
		align-items: center;
		flex-direction: column;

		image {
			height: 144rpx;
			width: 144rpx;
		}

		text {
			font-size: 40rpx;
			color: #353535;
		}

	}

	.ui-tip {
		margin-top: 30rpx;
		color: #fff;
		font-size: 40rpx;
	}

	.ui-nav {
		margin-top: 30rpx;
		display: inline-flex;
		justify-content: center;
		height: 109rpx;
		width: 597rpx;
		background: #fff;
		border-radius: 10px;
	}

	.ui-form {
		margin-top: 50rpx;
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
			margin-top: 49rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			font-size: 30rpx;
			color: #599FFF;
		}

		.ui-btn {
			margin-top: 59rpx;
		}

		.wd-sms {
			width: 160rpx;
			// height: 60rpx;
		}
	}

	.ui-nav {
		background: unset !important;
	}
	.ui-agreement {
		margin-top: 38rpx;
		display: inline-flex;
		align-items: center;
		justify-content: center;
	
		text {
			font-size: 30rpx;
			color: #fdbc2b;
		}
	}
	.ui-content{
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
			&-cancel {
				
			}
			&-confirm {
				border-left: 1px solid #e8e8e8;
				color: #599FFF;
			}
		}
		.uni-scroll-content{
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
