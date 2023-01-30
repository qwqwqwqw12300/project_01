<!--
* @Author: zhanch
* @Date: 2023-01-03 15:28:23
* @FilePath: /components/graphic-input/graphic-input.vue
* @Description: 图形验证码
-->

<template>
	<view>
		<u-text prefixIcon="photo" iconStyle="font-size: 32rpx" text="验证码" color="#444" size="28rpx">
		</u-text>
		<view class="ui-input">
			<u-input v-model="code" maxlength="4" placeholder="请输入图形验证码" :border="'none'" fontSize="28rpx" clearable>
				<template slot="suffix">
					<image @tap="handleGetCaptcha" class="img" :src="codeUrl"></image>
				</template>
			</u-input>
		</view>
	</view>
</template>

<script>
	import {
		GetCaptchaImage,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				codeUrl: '',
				uuid: '',
				code: '',
			};
		},
		methods: {
			handleGetCaptcha() {
				GetCaptchaImage({}).then(res => {
					this.codeUrl = `data:image/jpeg;base64,${res.img}`
					this.uuid = res.uuid;
					this.code = '';
				})
			},
			returnCodeData() {
				return {
					code: this.code,
					uuid: this.uuid,
				}
			}
		},
		mounted() {
			this.handleGetCaptcha()
		}
	}
</script>

<style lang="scss">
	.ui-input {
		margin: 34rpx 0 60rpx 0;
		border-bottom: 1px solid #e2e2e2;

		.img {
			width: 140rpx;
			height: 70rpx;
		}
	}

	.ui-mini {
		width: 200rpx;
	}
</style>
