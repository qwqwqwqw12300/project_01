<template>
	<app-body :hideTitle="true">
		<view class="ui-banner">
			<swiper class="ui-swiper" circular :indicator-dots="true" :autoplay="false" @change="swiperChange"
				:current="current">
				<swiper-item>
					<swiper-device></swiper-device>
				</swiper-item>
			</swiper>
		</view>
		<view class="ui-body">
			<view class="ui-w-h-100">
				<view class="ui-f-center ui-box ui-white-bg ui-br-16">
					<image class="ui-img-size1" src="@/static/images/caihong.png"></image>
					<view class="ui-f-center ui-f-wrap ui-w-70 ui-mar-l-32">
						<view class="ui-w-60 ui-f-start ui-f-wrap">
							<image class="ui-img-size2" src="@/static/images/xiaohao.png"></image>
							<text class="ui-font-24 ui-mar-l-10">卡路里<千卡></text>
							<view class="ui-text-box ui-mar-t-15">
								<text class="ui-font-1">300</text>
								<text class="ui-font-2">/1000</text>
							</view>
						</view>
						<view class="ui-w-40 ui-f-start ui-f-wrap">
							<image class="ui-img-size2" src="@/static/images/timeclock.png"></image>
							<text class="ui-font-24 ui-mar-l-10">活动(分)</text>
							<view class="ui-text-box ui-mar-t-15">
								<text class="ui-font-1">16</text>
								<text class="ui-font-2">/30</text>
							</view>
						</view>
						<view class="ui-w-60 ui-f-start ui-mar-t-45 ui-f-wrap">
							<image class="ui-img-size2" src="@/static/images/walk.png"></image>
							<text class="ui-font-24 ui-mar-l-10">行走<步></text>
							<view class="ui-text-box ui-mar-t-15">
								<text class="ui-font-1">2000</text>
								<text class="ui-font-2">/10000</text>
							</view>
						</view>
						<view class="ui-w-40 ui-f-start ui-mar-t-45 ui-f-wrap">
							<image class="ui-img-size2" src="@/static/images/stand.png"></image>
							<text class="ui-font-24 ui-mar-l-10">站立<分></text>
							<view class="ui-text-box ui-mar-t-15">
								<text class="ui-font-1">20</text>
								<text class="ui-font-2">/30</text>
							</view>
						</view>
					</view>
				</view>
				<view class="ui-f-between ui-mar-t-20">
					<view class="ui-w-42 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20"
						@click="jumpUrl('/pages/health/sleep/sleep')">
						<view class="ui-f-start ui-f-wrap">
							<image class="ui-img-size3" src="../../static/images/yueliang.png"></image>
							<text class="ui-font-32 ui-mar-l-10">睡眠</text>
							<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
								2月16日
							</view>
							<view class="ui-w-h-100 ui-f-between ui-mar-t-20">
								<view class="ui-color-block1 ui-color-block1-width"></view>
								<view class="ui-color-block2 ui-color-block1-width"></view>
								<view class="ui-color-block3 ui-color-block1-width"></view>
								<view class="ui-color-block4 ui-color-block1-width"></view>
							</view>
							<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
								<text class="ui-font-22 ui-font-c-888">差</text>
								<text class="ui-font-22 ui-font-c-888">很好</text>
							</view>
						</view>
					</view>
					<view class="ui-w-42 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20">
						<view class="ui-f-start ui-f-wrap">
							<image class="ui-img-size3" src="../../static/images/xinlv.png"></image>
							<text class="ui-font-32 ui-mar-l-10">心率76次/分</text>
							<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
								2月16日
							</view>
							<view class="ui-w-h-100 ui-mar-t-20">
								<image class="ui-img-size4" src="../../static/images/xinlvLine.png"></image>
							</view>
							<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
								<text class="ui-font-22 ui-font-c-888">00:00</text>
								<text class="ui-font-22 ui-font-c-888">24:00</text>
							</view>
						</view>
					</view>
				</view>
				<view class="ui-f-between ui-mar-t-20">
					<view class="ui-w-42 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20">
						<view class="ui-f-start ui-f-wrap">
							<image class="ui-img-size3" src="../../static/images/xueya.png"></image>
							<text class="ui-font-32 ui-mar-l-10">血压</text>
							<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
								2月16日
							</view>
							<view class="ui-w-h-100 ui-f-between ui-mar-t-20">
								<image class="ui-img-size4" src="../../static/images/xueyaLine.png"></image>
							</view>
							<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
								<text class="ui-font-22 ui-font-c-888">00:00</text>
								<text class="ui-font-22 ui-font-c-888">24:00</text>
							</view>
						</view>
					</view>
					<view class="ui-w-42 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20">
						<view class="ui-f-start ui-f-wrap">
							<image class="ui-img-size3" src="../../static/images/xueyang.png"></image>
							<text class="ui-font-32 ui-mar-l-10">血氧</text>
							<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
								2月16日
							</view>
							<view class="ui-w-h-100 ui-mar-t-20">
								<image class="ui-img-size4" src="../../static/images/xueyangLine.png"></image>
							</view>
							<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
								<text class="ui-font-22 ui-font-c-888">00:00</text>
								<text class="ui-font-22 ui-font-c-888">24:00</text>
							</view>
						</view>
					</view>
				</view>
				<view class="ui-f-between ui-mar-t-20">
					<view class="ui-w-42 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20">
						<view class="ui-f-start ui-f-wrap">
							<image class="ui-img-size3" src="../../static/images/xindian.png"></image>
							<text class="ui-font-32 ui-mar-l-10">心电</text>
							<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
								2月16日
							</view>
							<view class="ui-w-h-100 ui-f-between ui-mar-t-20">
								<image class="ui-img-size4" src="../../static/images/xindianLine.png"></image>
							</view>
							<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
								<text class="ui-font-22 ui-font-c-888">00:00</text>
								<text class="ui-font-22 ui-font-c-888">24:00</text>
							</view>
						</view>
					</view>
					<view class="ui-w-42 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20">
						<view class="ui-f-start ui-f-wrap">
							<image class="ui-img-size3" src="../../static/images/xinzang.png"></image>
							<text class="ui-font-32 ui-mar-l-10">心脏</text>
							<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
								2月16日
							</view>
							<view class="ui-w-h-100 ui-mar-t-20">
								<image class="ui-img-size4" src="../../static/images/xinzangLine.png"></image>
							</view>
							<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
								<text class="ui-font-22 ui-font-c-888">00:00</text>
								<text class="ui-font-22 ui-font-c-888">24:00</text>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</app-body>
</template>

<script>
	import SwiperDevice from './components/swiper-device.vue';
	export default {
		components: {
			SwiperDevice
		},
		data() {
			return {
				current: 0,
			}
		},
		methods: {
			jumpUrl(url) {
				uni.navigateTo({
					url: url
				})
			},
			swiperChange(val) {

			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-banner {
		margin-top: 60rpx;
		padding: 0 20rpx;
	}

	.ui-swiper {
		height: 320rpx;
	}

	.ui-body {
		padding: 32rpx;

		.ui-mar-l-20 {
			margin-left: 20rpx;
		}

		.ui-mar-t-10 {
			margin-top: 10rpx;
		}

		.ui-mar-t-15 {
			margin-top: 15rpx;
		}

		.ui-mar-t-20 {
			margin-top: 20rpx;
		}

		.ui-mar-t-45 {
			margin-top: 45rpx;
		}

		.ui-white-bg {
			background-color: #FFF;
		}

		.ui-w-h-100 {
			width: 100%;
			height: 100%;
		}

		.ui-br-16 {
			border-radius: 16rpx;
		}

		.ui-f-center {
			display: flex;
			justify-content: center;
			align-items: center;
		}

		.ui-f-between {
			display: flex;
			justify-content: space-between;
			align-items: center;
		}

		.ui-f-start {
			display: flex;
			justify-content: flex-start;
			align-items: center;
		}

		.ui-t-box {
			width: 50%;
		}

		.ui-f-wrap {
			flex-wrap: wrap;
		}

		.ui-img-size1 {
			width: 200rpx;
			height: 100rpx;
		}

		.ui-w-40 {
			width: 40%;
		}

		.ui-w-60 {
			width: 60%;
		}

		.ui-w-70 {
			width: 70%;
		}

		.ui-mar-l-10 {
			margin-left: 10rpx;
		}

		.ui-mar-l-32 {
			margin-left: 32rpx;
		}

		.ui-box {
			padding: 47rpx 32rpx 42rpx 32rpx;
		}

		.ui-w-45 {
			width: 45%;
		}

		.ui-w-42 {
			width: 42%;
		}

		.ui-img-size2 {
			width: 36rpx;
			height: 36rpx;
		}

		.ui-font-24 {
			font-size: 24rpx;
		}

		.ui-font-22 {
			font-size: 22rpx;
		}

		.ui-text-box {
			padding-left: 36rpx;

			.ui-font-1 {
				font-size: 36rpx;
				color: #353535;
				letter-spacing: 0;
				line-height: 36rpx;
				font-weight: 700;
			}

			.ui-font-2 {
				font-size: 36rpx;
				color: #D4D4D4;
				letter-spacing: 0;
				line-height: 36rpx;
				font-weight: 700;
			}
		}

		.ui-img-size3 {
			width: 48rpx;
			height: 48rpx;
		}

		.ui-img-size4 {
			width: 100%;
			height: 64rpx;
		}

		.ui-padding-20 {
			padding: 20rpx;
		}

		.ui-font-32 {
			font-size: 32rpx;
		}

		.ui-font-3 {
			font-size: 26rpx;
			color: #353535;
			line-height: 26rpx;
			font-weight: 400;
		}

		.ui-padding-l-58 {
			padding-left: 58rpx;
		}

		.ui-color-block1 {
			background: rgba(82, 136, 245, 0.25);
			height: 72rpx;
		}

		.ui-color-block2 {
			background: rgba(82, 136, 245, 0.50);
			height: 72rpx;
		}

		.ui-color-block3 {
			background: rgba(82, 136, 245, 0.75);
			height: 72rpx;
		}

		.ui-color-block4 {
			background: rgba(82, 136, 245, 1);
			height: 72rpx;
		}

		.ui-color-block1-width {
			width: 23%;
		}

		.ui-font-c-888 {
			color: #888888;
		}
	}

	.ui-img {
		width: 100%;
		height: calc(100vh - var(--window-bottom) - var(--status-bar-height));

	}
</style>
