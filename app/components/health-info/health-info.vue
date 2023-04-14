<template>
	<view class="ui-more">
		<view class="box" @tap="handleMore">
			<text>了解{{ title }}</text>
			<u-icon name="arrow-right"></u-icon>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			title: {
				type: String
			}
		},
		data() {
			return {

			}
		},
		methods: {
			handleMore() {
				uni.navigateTo({
					url: `/pages/health/sleep/info?name=${this.title}`
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-more {
		margin-top: 40rpx;
		padding: 0 32rpx;
		padding-bottom: 30px;

		.box {
			height: 108rpx;
			border-radius: 20rpx;
			background: #FFFFFF;
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 0 20rpx;
			font-size: 26rpx;
		}
	}
</style>