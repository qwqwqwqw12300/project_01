<template>
	<app-body>
		<app-logo text="消息推送设置"></app-logo>
		<view class="ui-form">
			<view class="ui-form-item">
				<u-icon name="/static/images/push.png" width="26" height="23" size="53rpx"></u-icon>
				<text class="text">消息推送</text>
				<u-switch activeColor="#5ac725" inactiveColor="rgb(138, 138, 138)" size="24"> ></u-switch>
			</view>
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {

		}
	}
</script>

<style lang="scss" scoped>
	.ui-form {
		margin-top: 256rpx;
		padding: 0 60rpx;

		.ui-form-item {
			padding: 30rpx;
			background-color: #f5f5f5;
			border-radius: 10rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.text {
				flex: 1;
				margin-left: 24rpx;
				font-size: 33rpx;
			}
		}
	}
</style>
