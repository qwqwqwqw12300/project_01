<!--
* @Author: zhanch
* @Date: 2023-01-03 15:28:23
* @FilePath: /components/graphic-input/graphic-input.vue
* @Description: 图形验证码
-->

<template>
	<view>
		<u-input v-model="code" maxlength="4" placeholder="请输入图形验证码" :border="'none'" fontSize="28rpx" clearable>
			<template slot="suffix">
				<image @tap="handleGetCaptcha" class="img" :src="codeUrl"></image>
			</template>
		</u-input>
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
	.img {
		width: 140rpx;
		height: 70rpx;
	}
</style>
