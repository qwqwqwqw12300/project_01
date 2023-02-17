<template>
	<view class="swiper-item">
		<image class="img" src="/static/images/leida-nm.png"></image>
		<view class="device-info">
			<view class="title">
				{{record.name || '未命名设备'}}<text
					:class="[record.onlineFlag === '1' ? 'online' : 'offline']">{{record.onlineFlag === '1' ? '在线' : '离线'}}</text>
			</view>
			<view class="position">
				{{ record.location || '--'}}
			</view>
		</view>
		<view class="device-set">
			<u-icon name="/static/images/device-set.png" size="44rpx" style="margin-right: 4rpx;">
			</u-icon>设备配置
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			record: {
				type: Object,
				default: () => {},
			},
		},
		data() {
			return {

			}
		},
		methods: {

		},
	}
</script>

<style lang="scss" scoped>
	.swiper-item {
		display: block;
		border-radius: 20rpx;
		height: 250rpx;
		margin: 0 6rpx;
		background-color: #fff;
		padding: 30rpx 20rpx;
		box-sizing: border-box;
		display: flex;
		align-items: center;
		justify-content: space-between;

		.img {
			width: 160rpx;
			height: 160rpx;
		}

		.device-info {
			flex: 1;
			align-self: flex-start !important;
			padding-left: 10rpx;
			padding-top: 30rpx;

			.title {
				color: #353535;
				font-size: 34rpx;
				font-weight: 500;

				text {
					margin-left: 30rpx;
					color: #fff;
					font-size: 22rpx;
					padding: 6rpx 12rpx;
					// background-image: linear-gradient(90deg, #1EC862 0%, #13B98F 100%);
					border-radius: 4px;
				}
			}

			.position {
				margin-top: 10rpx;
				font-size: 26rpx;
				color: #888888;
				line-height: 26px;
				font-weight: 400;
			}

		}

		.device-set {
			width: 180rpx;
			display: flex;
			color: #FEAE43;
			font-size: 30rpx;
			font-weight: 400;
		}
	}

	.online {
		background-image: linear-gradient(90deg, #1EC862 0%, #13B98F 100%);
	}

	.offline {
		background-image: linear-gradient(90deg, #ff4800 0%, #FF7E00 100%);
	}
</style>
