<!--
* @Author: zhanch
* @Date: 2023-01-05 14:58:17
* @FilePath: /pages/service/message-details.vue
* @Description: 系统信息详情
-->
<!-- 隐私协议 -->
<template>
	<app-body leftText="返回">
		<view class="uni-agreement">
			<scroll-view scroll-y class="uni-agreement-scroll">
				<text>内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容</text>
			</scroll-view>
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {}
		},
		methods: {

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
		padding: 0px 32rpx 32rpx 32rpx;
		box-sizing: content-box;

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
