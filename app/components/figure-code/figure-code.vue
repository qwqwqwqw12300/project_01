<!--
* @Author: zhanch
* @Date: 2023-01-03 15:28:23
* @FilePath: /components/graphic-input/graphic-input.vue
* @Description: 图形验证码
-->

<template>
	<view class="wd-content">
		<text class="wd-font" v-if="leftText">{{leftText}}</text>
		<u-input :prefixIcon="prefixIcon" :prefixIconStyle="prefixIconStyle" :fontSize="fontSize" v-model="code"
			maxlength="4" placeholder="请输入图形验证码" :border="'none'" clearable>
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
		props: {
			prefixIcon: {
				default: ''
			},
			prefixIconStyle: {
				type: String | Object,
				default: ''
			},
			fontSize: {
				default: '28rpx'
			},
			leftText: {
				default: ''
			}
		},
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
	.wd-content{
		display: flex;
		align-items: center;
		
	}
	.wd-font{
		margin-right: 60rpx;
		font-family: PingFangSC-Regular;
		font-size: 34rpx;
		color: #353535;
		line-height: 34rpx;
		font-weight: 400;
	}
	.img {
		width: 140rpx;
		height: 70rpx;
	}
</style>
