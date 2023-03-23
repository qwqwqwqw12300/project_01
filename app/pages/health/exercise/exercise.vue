<template>
	<view>
		<app-body :bg="false" :hideTitle="true">
			<view class="ui-body">
				<scroll-view :scroll-x="true" class="scrollview-box">
					<view class="ui-scroll-block">
						<view class="ui-block">
							<u-icon size="40rpx" name="/static/images/bushu.png"></u-icon>
							<view class="ui-font-50 ui-mar-l-10">5000</view>
						</view>
						<view class="ui-block">
							<u-icon size="40rpx" name="/static/images/xiaohao.png"></u-icon>
							<view class="ui-font-50 ui-mar-l-10">300</view>
						</view>
						<view class="ui-block">
							<u-icon size="40rpx" name="/static/images/timeclock.png"></u-icon>
							<view class="ui-font-50 ui-mar-l-10">30</view>
						</view>
						<view class="ui-block">
							<u-icon size="40rpx" name="/static/images/stand.png"></u-icon>
							<view class="ui-font-50 ui-mar-l-10">10</view>
						</view>
					</view>
				</scroll-view>
			</view>
		</app-body>
	</view>
</template>

<script>
	export default {
		data() {
			return {
			}
		},
		created() {

		},
		methods: {

		}
	}
</script>

<style>
	.scrollview-box {
		white-space: nowrap; // 滚动必须加的属性
		width: 100%;
		padding: 20rpx 20rpx 20rpx 20rpx;
	}

	.ui-body {
		padding: 32rpx;
	}

	.ui-block {
		display: inline-block;
		height: 128rpx;
		width: 200rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		padding: 10rpx;
	}

	.ui-flex-center {
		display: flex;
		justify-content: center;
		flex-wrap: nowrap;
		align-items: center;
	}

	.ui-scroll-block .ui-block~.ui-block {
		margin-left: 20rpx;
	}

	.ui-font-50 {
		font-size: 50rpx;
		color: #353535;
		text-align: left;
		font-weight: 700;
	}

	.ui-img-size-40 {
		width: 40rpx;
		height: 40rpx;
	}

	.ui-mar-l-10 {
		margin-left: 10rpx;
	}
</style>
