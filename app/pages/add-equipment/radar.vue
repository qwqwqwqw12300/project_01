<!-- 增加雷达设备 -->
<template>
	<app-body>
	<!-- 	<view class="ui-navbar" @tap="handleBack">
			<u-icon name="arrow-left" color="#fff" size="23"></u-icon>
			<text class="ui-navbar-text">首页</text>
		</view> -->
		<u-navbar leftText="首页" @rightClick="handleBack" :autoBack="true" bgColor="transparent" leftIconColor="#fff"
			:fixed="false" />
		<view class="ui-title">
			<text>增加雷达波设备</text>
		</view>
		<view class="ui-step">
			<view class="ui-step-icon step1_bg"></view>
			<view class="ui-step-title">
				<text>请确保手机蓝牙</text>
				<text>WIFI处于开启状态</text>
			</view>
		</view>
		<view class="ui-satellite">
			<image src="../../static/images/satellite.png"></image>
		</view>
		<view class="ui-step">
			<view class="ui-step-icon step2_bg"></view>
			<view class="ui-step-title bg">
				<text style="font-size: 60rpx;">正在扫描</text>
			</view>
		</view>
		<view class="ui-bluetooth">
			<image src="../../static/images/bluetooth.png"></image>
		</view>
		<view class="ui-btn">
			<button>下一步</button>
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			const deviceList = [{
					key: 0,
					name: '设备1 device-01',
					active: true
				},
				{
					key: 1,
					name: '设备2 device-03',
					active: false
				},
				{
					key: 2,
					name: '设备2 device-03',
					active: false
				}
			]
			return {
				deviceList,
			}
		},
		methods: {
			handleSelect(item) {
				this.deviceList = this.deviceList.map(n => {
					n.active = n.key === item.key
					return n
				})
			},
			handleBack() {
				uni.navigateBack()
			}
		}
	}
</script>

<style lang="scss" scoped>
	.step1_bg {
		background-image: url('../../static/images/step1.png');
	}

	.step2_bg {
		background-image: url('../../static/images/step2.png');
	}

	.bg {
		background-color: rgb(227, 252, 255);
	}

	.active {
		background-color: rgb(220, 251, 255);
	}

	.ui-navbar {
		padding-top: calc(var(--status-bar-height) + 26rpx);
		padding-left: 20rpx;
		display: flex;
		align-items: center;

		.ui-navbar-text {
			font-size: 36rpx;
			color: #fff;
		}
	}

	.ui-title {
		margin-top: 10rpx;
		padding-left: 50rpx;

		text {
			font-size: 72rpx;
			font-weight: bold;
			color: #ffffff;
		}
	}

	.ui-step {
		height: 148rpx;
		display: flex;
		align-items: center;
		margin-top: 50rpx;
		padding: 0 40rpx;

		.ui-step-icon {
			width: 150rpx;
			height: 120rpx;
			background-size: cover;
			background-position: center;
			background-repeat: no-repeat;
		}

		.ui-step-title {
			display: flex;
			flex-direction: column;
			margin-left: 20rpx;
			padding: 10rpx 20rpx;
			border-radius: 10rpx;

			text {
				color: #0e6bc0;
				font-size: 40rpx;
				font-weight: bold;
				text-shadow: 0 0 3px #fff, 0 0 3px #5acbff;
			}
		}
	}

	.ui-satellite {
		margin-top: 30rpx;
		width: 100%;
		height: 500rpx;
		display: flex;
		align-items: center;
		justify-content: center;

		image {
			width: 500rpx;
			height: 480rpx;
		}
	}

	.ui-bluetooth {
		margin-top: 30rpx;
		width: 100%;
		height: 400rpx;
		display: flex;
		align-items: center;
		justify-content: center;

		image {
			width: 380rpx;
			height: 360rpx;
		}
	}


	.ui-btn {
		position: absolute;
		width: 450rpx;
		margin: 0 auto;
		margin-top: 50rpx;
		// bottom: 20rpx;
		left: 50%;
		transform: translateX(-50%);
		text-align: center;

		text {
			font-size: 30rpx;
			color: #0094ff;
		}

		&>* {
			&:active {
				opacity: 0.8;
			}
		}

		&>*:nth-child(1) {
			margin-bottom: 50rpx;
		}
	}
</style>
