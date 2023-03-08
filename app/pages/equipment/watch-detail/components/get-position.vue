<template>
	<view class="ui-map" id="ui-map">
		<view class="map-box">
			<map id="map" ref="map" style="width: 100%; height: 480rpx" :latitude="latitude" :longitude="longitude" />
		</view>
		<view class="map-position">
			<text class="label">当前位置:</text>
			<view class="content">
				福州市马尾区儒江西路1号新大陆科技园B座
			</view>
		</view>
		<view class="ui-btn">
			<button class="default" @click="toJump">历史位置</button>
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
		},
		mounted(){
			const query = uni.createSelectorQuery().in(this)
			query
				.select('#ui-map')
				.boundingClientRect(data => {
					console.log(data.height, '3333')
					// if (data.height > 52) {
					// 	this.isOpen = false;
					// 	this.isShowMoreBtn = true;
					// }
				})
				.exec();
		},
		methods: {
			toJump() {
				uni.navigateTo({
					url: '/pages/equipment/historical-location'
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-map {
		padding: 32rpx;

		.map-box {}

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
			padding: 0 32rpx;
			background-color: #fff;
			border-radius: 16rpx;

			.address-box {
				display: flex;
				flex-direction: column;


				.list-item {
					// padding: 48rpx 32rpx;
					height: 128rpx;
					display: flex;
					align-items: center;
					justify-content: space-between;
					border-bottom: solid 2px #f7f7f7;
				}
			}
		}

		.ui-btn {
			margin-top: 50rpx;
		}
	}
</style>
