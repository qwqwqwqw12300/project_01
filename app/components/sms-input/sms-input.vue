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
			<u-input placeholder="请输入短信认证码" :readonly="status === 'checked'" @input="check" :border="'none'"
				fontSize="28rpx" clearable>
				<template slot="suffix">
					<button class="wd-sms ui-mini" @click="sendCode" size="mini">{{suffix}}</button>
				</template>
			</u-input>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			/**请求与验证地址**/
			// urls: {
			// 	default: {}
			// }
		},
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
				/**验证码**/
				code: ''
			};
		},
		methods: {
			/**
			 * 发送短信验证码
			 */
			sendCode() {
				if (this.status !== 'init') return;
				this.suffix = '发送中..';
				this.status = 'send';
				this.timer = setInterval(() => {
					this.count--;
					if (this.count <= 0) {
						this.reset();
					} else {
						this.suffix = this.count + 's';
					}
				}, 1000);
			},

			/**
			 * 重置
			 */
			reset() {
				this.count = 60;
				this.suffix = '重新发送';
				this.status = 'init';
				this.display = false;
				this.timer && clearInterval(this.timer);
			},
			/**
			 * 校验
			 */
			check(code) {
				if (this.status === 'send' && code.length === 6) {
					this.status = 'checked';
					this.suffix = '已认证';
					this.timer && clearInterval(this.timer);
				}
			}
		}
	}
</script>

<style lang="scss">
	.ui-input {
		margin: 34rpx 0 60rpx 0;
		border-bottom: 1px solid #e2e2e2;
	}

	.ui-mini {
		width: 200rpx;
	}
</style>
