<template>
	<view class="ui-map">
		<view class="map-box">
			<map id="map" ref="map" style=" width: 100%; height: 560rpx;border-radius: 20rpx;" :latitude="latitude"
				:longitude="longitude" />
		</view>
		<view class="map-position">
			<text class="label">当前位置:</text>
			<view class="content">
				福州市马尾区儒江西路1号新大陆科技园B座
			</view>
		</view>
		<view class="map-address">
			<view class="address-box">
				<view class="list-item">

				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				latitude: 39.909,
				longitude: 116.39742,
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-map {
		padding: 32rpx;

		.map-box {
			width: 100%;
			height: 560rpx;
			border: 2rpx solid black;
			border-radius: 16rpx;
		}

		.map-position {
			margin-top: 30rpx;
			display: flex;
			justify-content: space-between;

			.label {
				width: 160rpx;
				font-size: 32rpx;
				color: #888888;
			}

			.content {
				white-space: wrap;
				font-size: 32rpx;
				color: #353535;
				font-weight: 600;
			}
		}

		.map-address {
			margin-top: 30rpx;
			padding: 32rpx;
			background-color: #fff;
			border-radius: 16rpx;

			.address-box {
				display: flex;
				flex-direction: column;
				border-bottom: solid 2px #f7f7f7;

				.list-item {
					padding: 48rpx 32rpx;
				}
			}
		}
	}
</style>
