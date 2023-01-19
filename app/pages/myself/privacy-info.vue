<!-- 隐私协议 -->
<template>
	<app-body>
		<app-logo text="隐私协议"></app-logo>
		<view class="uni-agreement">
			<scroll-view scroll-y class="uni-agreement-scroll">
				{{ content }}
			</scroll-view>
		</view>
	</app-body>
</template>

<script>
	import {
		PostSelectAgreement,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				content: '',
			}
		},
		methods: {
			handleInit() {
				PostSelectAgreement({
					type: '0'
				}).then(res => {
					this.content = res.data.content
				})
			}
		},
		mounted() {
			this.handleInit()
		}
	}
</script>

<style lang="scss" scoped>
	.ui-title {
		margin-top: 10rpx;
		padding-left: 40rpx;

		text {
			font-size: 72rpx;
			font-weight: bold;
			color: #ffffff;
		}
	}

	.uni-agreement {
		margin-top: 30rpx;
		padding: 0px 20rpx 20rpx 20rpx;
		box-sizing: content-box;
		word-wrap: break-word;
		word-break: normal;
		text-indent: 2em;

		.agreement-title {
			font-size: 19px;
			font-weight: bold;
			margin-top: 11px;
		}

		.agreement-text {
			font-size: 16px;
			line-height: 27px;
			color: #333333;
		}

		.uni-agreement-scroll {
			height: calc(100vh - 250rpx);
		}
	}
</style>
