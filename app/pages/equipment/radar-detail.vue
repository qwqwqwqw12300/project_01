<template>
	<app-body>
		<view class="ui-detail">
			<image class="ui-detail-icon" src="../../static/images/device.png" mode=""></image>
			<view class="ui-detail-device">
				<text class="title">xx设备名称</text>
				<view class="info">
					<text class="position">位置</text>
					<view class="status">
						<image src="../../static/images/online.png"></image>
						<text>在线</text>
					</view>
				</view>
			</view>
		</view>
		<view class="ui-msg-list">
			<msg-list srollHeight="calc(100vh - var(--window-bottom) - 600rpx - var(--status-bar-height))"></msg-list>
		</view>
		<!-- <view class="ui-tabs">
			<view class="ui-tabs-content">
				<u-tabs :list="list1" lineColor="#FEC92E" lineWidth="50" :activeStyle="{
							color: '#FEC92E',
							fontWeight: 'bold',
							transform: 'scale(1.05)'
						}" />
			</view>
		</view>
		<view class="ui-clear">
			<u-button class="btn" color="white" iconColor="white" icon="checkmark-circle">全部标记为已读</u-button>
		</view>
		<view class="ui-list">
			<view class="ui-list-item">
				<view class="item-title">
					<image class="item-title-message" src="../../static/images/message.png" mode="" />
					<text class="item-title-time">2022/12/22 10:22</text>
					<text class="item-title-type">SOS</text>
				</view>
				<view class="item-content">
					<view class="item-content-header">
						<u-icon name="arrow-right" size="42rpx"></u-icon>
						<text>时间</text>
						<text>类型</text>
						<text>SOS</text>
						<text>内容</text>
					</view>
					<view class="item-content-card">
						<view class="card-title">
							<image class="card-title-icon" src="../../static/images/phone.png" mode="" />
							<text>紧急电话</text>
						</view>
						<view class="card-tel">
							13900001111
						</view>
					</view>
				</view>
			</view>
		</view> -->
	</app-body>
</template>

<script>
	export default {
		data() {
			return {
				list1: [{
					name: '未读',
				}, {
					name: '已读',
				}]
			}
		},
		methods: {
			handleBack() {
				uni.navigateBack()
			}
		}
	}
</script>

<style lang="scss" scoped>
	.wd-box {
		border-radius: 14rpx;
		margin: 0 46rpx;
		background-color: #EBEBEB;
	}

	.ui-msg-list {
		padding: 0 46rpx;
	}

	.ui-detail {
		@extend .wd-box;
		margin-top: 40rpx;
		padding: 40rpx;
		position: relative;
		display: flex;

		// justify-content: space-between;
		.ui-detail-icon {
			width: 180rpx;
			height: 180rpx;
		}

		.ui-detail-device {
			margin-left: 90rpx;

			.title {
				line-height: 44rpx;
				font-size: 36rpx;
				font-weight: 550;
			}

			.info {
				margin-top: 16rpx;
				width: 220rpx;
				display: flex;
				justify-content: space-between;

				.position {
					font-size: 32rpx;
					font-weight: 550;
				}

				.status {
					display: flex;

					image {
						width: 40rpx;
						height: 40rpx;
					}

					text {
						font-size: 32rpx;
						font-weight: 550;
						color: #1D8D28;
						margin-left: 6rpx;
					}
				}
			}
		}

		.ui-detail-map {
			position: absolute;
			width: 50rpx;
			height: 60rpx;
			right: 20rpx;
			bottom: 20rpx;
		}
	}

	.ui-tabs {
		@extend .wd-box;
		margin-top: 20rpx;

		.ui-tabs-content {
			padding: 12rpx 20rpx;
			width: 280rpx;
			margin: 0 auto;
		}
	}

	.ui-clear {
		margin-top: 10rpx;
		padding: 10rpx 46rpx;

		.btn {
			width: 100%;
			border-radius: 10rpx;
			background: #000000 !important;
			opacity: 0.3;
			border: none !important;
		}
	}

	.ui-list {
		margin-top: 10rpx;

		.ui-list-item {
			margin-bottom: 20rpx;

			.item-title {
				@extend .wd-box;
				padding: 26rpx 40rpx;
				display: flex;
				align-items: center;
				box-shadow: 3px 5px 15px #cac6c1;

				.item-title-message {
					width: 45rpx;
					height: 40rpx;
				}

				.item-title-time {
					margin-left: 16rpx;
					font-size: 36rpx;
					font-weight: bold;
				}

				.item-title-type {
					margin-left: 30rpx;
					font-size: 38rpx;
					font-weight: bold;
				}
			}

			.item-content {
				@extend .wd-box;
				margin-top: 30rpx;
				padding: 26rpx 40rpx;
				box-shadow: 3px 5px 15px #cac6c1;

				.item-content-header {
					height: 70rpx;
					display: flex;
					align-items: center;

					.content-header-icon {
						width: 30rpx;
						height: 40rpx;
					}

					text {
						margin-left: 20rpx;
						font-size: 36rpx;
						font-weight: bold;
					}
				}

				.item-content-card {
					margin: 20rpx 30rpx 60rpx 60rpx;
					padding: 24rpx;
					border-radius: 14rpx;
					// background-color: #000000;
					background: linear-gradient(190deg, #FFE383, #FEC92E);
					display: flex;
					flex-direction: column;
					align-items: center;
					justify-content: space-between;

					.card-title {
						display: flex;
						align-items: center;

						.card-title-icon {
							width: 35rpx;
							height: 30rpx;
						}

						text {
							margin-left: 10rpx;
							font-size: 32rpx;
							font-weight: bold;
						}
					}

					.card-tel {
						margin-top: 14rpx;
						font-size: 32rpx;
						font-weight: bold;
					}
				}
			}
		}
	}
</style>
