<!--
* @Author: zhanch
* @Date: 2023-01-03 14:17:10
* @FilePath: /components/sms-input/sms-input.vue
* @Description: 短信输入框
-->

<template>
	<view>
		<u-text prefixIcon="email" iconStyle="font-size: 32rpx" text="短信码" color="#444" size="28rpx">
		</u-text>
		<view class="ui-input">
			<u-input placeholder="请输入短信认证码" v-model="checkPayLoad.code" type="number" maxlength="4"
				:readonly="status === 'checked'" @input="checkSms" :border="'none'" fontSize="28rpx" clearable>
				<template slot="suffix">
					<button class="wd-sms ui-mini" @click="sendCode" size="mini">{{suffix}}</button>
				</template>
			</u-input>
		</view>
	</view>
</template>

<script>
	import {
		sendSms,
		checkSms,
		applyLoginBySms,
	} from '@/common/http/api.js';
	import {
		phoneValidator
	} from '../../common/utils/util';
	export default {
		props: {
			/**请求与验证地址**/
			urls: {
				default: () => {
					return {
						sendSms,
						applyLoginBySms,
						checkSms
					}
				}
			},
			/**发送短信时需要携带的验证参数**/
			payload: {
				default: () => {
					return {}
				}
			},
			/**发送短信类型、区分验证码登录短信及其它*/
			smsType: {
				type: String,
				default: '0', //0: 普通短信验证，1: 登录短信验证
			}

		},
		emit: ['checked'],
		data() {
			return {
				/**短信倒计时**/
				count: 60,
				/**发送状态**/
				status: 'init',
				/**发送按钮文字**/
				suffix: '验证码',
				/**按钮是否允许点击**/
				display: false,
				/**倒计时实例**/
				timer: null,
				/**校验参数**/
				checkPayLoad: {
					code: ''
				}

			};
		},
		methods: {
			/**
			 * 发送短信验证码
			 */
			async sendCode() {
				if (this.status !== 'init') return;
				const payload = typeof this.payload === 'function' ? await this.payload() : this.payload;
				if (!phoneValidator(payload.phone)) {
					return uni.$u.toast('请填写正确的手机号码')
				}
				const Interface = this.smsType === '0' ? this.urls.sendSms : this.urls.applyLoginBySms
				if (this.verification(payload)) {
					this.suffix = '发送中..';
					this.status = 'send';
					Interface && Interface(payload).then(res => {
						Object.assign(this.checkPayLoad, {
							uuid: res.smsUuid,
							phone: payload.phone
						});
						this.timer = setInterval(() => {
							this.count--;
							if (this.count <= 0) {
								this.reset();
							} else {
								this.suffix = this.count + 's';
							}
						}, 1000);
					}, () => { // 发送失败
						this.reset();
					})
				}

			},

			/**
			 * 重置
			 */
			reset(text = '重新发送') {
				this.count = 60;
				this.suffix = text;
				this.status = 'init';
				this.display = false;
				this.timer && clearInterval(this.timer);
				this.checkPayLoad.code = '';
				this.$emit('reset');
			},
			/**
			 * 校验
			 */
			checkSms(code) {
				if (this.status === 'send' && code.length === 4) {
					this.urls.checkSms({
						...this.checkPayLoad,
						captcha: code
					}).then(res => {
						this.status = 'checked';
						this.suffix = '已认证';
						this.timer && clearInterval(this.timer);
						this.$emit('checked', {
							...this.checkPayLoad
						});
					});
				}
			},

			/**
			 * 发送状态校验 
			 **/
			verification(payload) {
				if (payload) {
					if (!payload.phone) { // 无uuid
						uni.showToast({
							icon: 'none',
							title: '请输入手机号码',
							duration: 2000
						});
						return false;
					}
					if (!payload.uuid) { // 无uuid
						uni.showToast({
							icon: 'none',
							title: '请先输入图片验证码',
							duration: 2000
						});
						return false;
					}
				}
				return true;
			}
		}
	}
</script>

<style lang="scss">
	.ui-input {
		box-sizing: border-box;
		margin: 34rpx 0 60rpx 0;
		border-bottom: 1px solid #e2e2e2;
	}

	.ui-mini {
		width: unset !important;
		min-width: 200rpx !important;
	}
</style>
