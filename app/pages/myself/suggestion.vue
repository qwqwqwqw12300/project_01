<template>
	<app-body>
		<app-logo text="意见与建议"></app-logo>
		<view class="ui-form">
			<view class="ui-form-item">
				<u-text prefixIcon="file-text" iconStyle="font-size: 32rpx" text="您的问题与建议" color="#444" size="28rpx">
				</u-text>
				<view class="ui-textarea">
					<u--textarea v-model="content" height="100" placeholder="请输入您的问题或建议"></u--textarea>
				</view>
			</view>
			<view class="ui-form-item">
				<graphic-input ref="codeRef"></graphic-input>
			</view>
			<view class="wd-btn-gloup"><button class="ui-button" @click="submit">提交</button></view>
		</view>
	</app-body>
</template>

<script>
	import {
		PostAddAdvise,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				content: '',
			}
		},
		methods: {
			submit() {
				const {
					code: captcha,
					uuid
				} = this.$refs.codeRef.returnCodeData()
				PostAddAdvise({
					content: this.content,
					captcha,
					uuid,
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						uni.navigateBack()
					}, 500)
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-form {
		margin-top: 256rpx;
		padding: 0 80rpx;
		padding-bottom: 120rpx;

		.ui-input {
			margin: 34rpx 0 60rpx 0;
			border-bottom: 1px solid #e2e2e2;
		}

		.ui-textarea {
			margin: 34rpx 0 60rpx 0;
			border-bottom: 1px solid #e2e2e2;
		}
	}

	.wd-btn-gloup {
		margin: 120rpx 0;

		.ui-button {
			width: 100%;
			height: 80rpx;
		}
	}
</style>
