<template>
	<view class="ui-min-h-121 ui-padding-20"
		@click="jumpUrl('/pages/health/body-temperature/body-temperature')" style="position: relative;">
		<image v-if="!item.isShow && isEdit" class="ui-img-size2 ui-pos-right" src="../../../../static/images/add.png" @click.stop="iconClick()"></image>
		<image v-if="item.isShow && isEdit" class="ui-img-size2 ui-pos-right" src="../../../../static/images/minus.png" @click.stop="iconClick()"></image>
		<view class="ui-f-start ui-f-wrap">
			<image class="ui-img-size3" src="/static/images/xueya.png"></image>
			<text class="ui-font-32 ui-mar-l-10">体温</text>
			<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
				{{date}}
			</view>
			<view class="ui-w-h-100 ui-mar-t-20">
				<app-echarts class="ui-echarts-size" :option="option" id="xueYangChart">
				</app-echarts>
				<!-- <image class="ui-img-size4" src="../../static/images/xueyangLine.png"></image> -->
			</view>
			<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
				<text class="ui-font-22 ui-font-c-888">00:00</text>
				<text class="ui-font-22 ui-font-c-888">24:00</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
		props: {
			fetchRes: {
				type: Object,
				default: () => {
					return {
						spMapList: [],
						tWatchBloodOxygen: [],
						HeartRateList: [],
						electrocardiogramMapList: [],
						sleepMap: ''
					}
				}
			},
			date:{
				type:String
			},
			option:{
				type:Object
			},
			item:{
				type: Object
			},
			isEdit:{
				type: Boolean,
				default: false
			}
		},
		data() {
			return {

			}
		},
		methods:{
			jumpUrl(url) {
				uni.navigateTo({
					url: url
				})
			},
			iconClick() {
				this.$emit('iconClick',this.item.type);
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-pos-right{
		position: absolute;
		right: 20rpx;
		top: 20rpx;
	}
	
	.max {
		width: 500rpx;
		height: 500rpx;
	}

	.ui-banner {
		padding: 0 20rpx;
		padding-top: 60rpx;
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

		.ui-w-47 {
			width: 47%;
		}

		.ui-w-52 {
			width: 52%;
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

		.ui-w-43 {
			width: 43%;
		}

		.ui-img-size2 {
			width: 36rpx;
			height: 36rpx;
		}

		.ui-img-size5 {
			width: 46rpx;
			height: 46rpx;
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

		.ui-img-size6 {
			width: 40rpx;
			height: 40rpx;
		}

		.ui-echarts-size {
			width: 100%;
			height: 72rpx;
		}

		.ui-padding-20 {
			padding: 20rpx;
		}

		.ui-padding-r-40 {
			padding-right: 40rpx;
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

		.ui-min-h-121 {
			min-height: 242rpx;
		}
	}

	.ui-img {
		width: 100%;
		height: calc(100vh - var(--window-bottom) - var(--status-bar-height));
	}

	.ui-noData-font {
		width: 100%;
		height: 134rpx;
		display: flex;
		align-items: center;
		justify-content: center
	}

	.ui-scroll {
		height: calc(100vh - (var(--window-bottom) + 300rpx + 50px + var(--status-bar-height)))
	}

	.ui-p-center {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
	}
</style>