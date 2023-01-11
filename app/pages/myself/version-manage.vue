<template>
	<app-body>
		<view class="ui-main">
			<image class="ui-main-img" src="../../static/images/logo.png"></image>
			<text class="ui-main-name">艾吉通</text>
			<text class="ui-main-version">版本：2.1.0</text>

			<view class="ui-main-button">
				<button class="ui-button">检查更新</button>
			</view>
		</view>
	</app-body>
</template>

<script>
	export default {

	}
</script>

<style lang="scss" scoped>
	.ui-main {
		height: 1000rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		padding-top: 150rpx;

		// justify-content: center;
		.ui-main-img {
			width: 180rpx;
			height: 180rpx;
		}

		.ui-main-name {
			font-size: 36rpx;
			color: #333333;
			margin-top: 20rpx;
		}

		.ui-main-version {
			font-size: 32rpx;
			color: #000;
			margin-top: 10rpx;
		}

		.ui-main-button {
			margin-top: 300px;
			width: 600rpx;
			.ui-button{
				width: 100%;
				height: 80rpx;
				line-height: 80rpx;
			}
		}
	}
</style>
