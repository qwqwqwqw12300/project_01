<template>
	<view class="ui-map">
		<point-map :record="addressInfo"></point-map>
		<map v-show="false"></map>
		<view class="map-popup">
			<view class="map-position">
				<text class="label">当前位置:</text>
				<view class="content">
					{{ addressInfo.address }}
				</view>
			</view>
			<view class="map-position">
				<text class="label">距上次更新时间:</text>
				<view class="content">
					{{ addressInfo.locateTimeFromCurrent }}
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		GetWatchTrack,
	} from '@/common/http/api'
	import {
		isIos,
		isApp
	} from '@/common/utils/util';
	let mapSearch;
	if (isApp()) mapSearch = weex.requireModule('mapSearch');
	export default {
		props: {
			deviceInfo: {
				type: Object,
				default: () => {},
			}
		},
		data() {
			return {
				covers: [],
				mapHeight: 0,
				loading: false,
				addressInfo: {
					latitude: '',
					longitude: '',
					address: '',
				},
			}
		},
		created() {
			const sysHeight = uni.getSystemInfoSync().windowHeight
			const saveHeight = uni.getSystemInfoSync().statusBarHeight
			//44 + 167 + 47 + 130
			this.mapHeight = sysHeight - saveHeight - 400
		},
		mounted() {
			this.getDeviceLocation()
		},
		methods: {
			getLocation(n) {
				const {
					latitude,
					longitude
				} = n.location
				return new Promise((resolve, reject) => {
					mapSearch && mapSearch.reverseGeocode({
							point: {
								latitude,
								longitude
							}
						},
						e => {
							resolve({
								latitude,
								longitude,
								address: e.address,
								locateTimeFromCurrent: n.locateTimeFromCurrent,
							})
						})
				})
			},
			getDeviceLocation() {
				uni.showLoading({
					title: '加载中'
				})
				GetWatchTrack({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					if (!res.data.location?.latitude) {
						this.addressInfo = {
							latitude: '',
							longitude: '',
							address: '暂无数据',
							locateTimeFromCurrent: '暂无数据',
						}
						return uni.hideLoading()
					}
					this.getLocation(res.data).then(info => {
						console.log(info, 'ffffff------------')
						const {
							latitude,
							longitude,
							address,
							locateTimeFromCurrent
						} = info
						this.addressInfo = {
							latitude,
							longitude,
							address,
							locateTimeFromCurrent,
						}
						uni.hideLoading()
					})
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-map {

		.map-popup {
			position: fixed;
			left: 0;
			bottom: 0;
			background-color: #FFFFFF;
			box-sizing: border-box;
			padding: 0 12px;
			padding-top: 20rpx;
			width: 100%;
			z-index: 9999;
			border-radius: 30rpx 30rpx 0 0;
			height: 240rpx;
		}

		// padding: 32rpx;
		.list-empty {
			height: 600rpx;
			display: flex;
			justify-content: center;
			align-items: center;
		}

		.map-box {}

		.map-position {
			// margin-top: 30rpx;
			height: 100rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			// border-bottom: solid 2px #f7f7f7;

			.label {
				width: 240rpx;
				font-size: 32rpx;
				color: #888888;
			}

			.content {
				flex: 1;
				// white-space: wrap;
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

	.address-list {
		// margin-top: 20rpx;
		background-color: #fff;
		// padding: 0 32rpx;

		.address-list-title {
			height: 100rpx;
			display: flex;
			flex-direction: row;
			align-items: center;
			font-size: 36rpx;
			color: #FEAE43;
			font-weight: 600;
			border-bottom: solid 2px #f7f7f7;

			.title-icon {
				height: 50rpx;
				border-left: solid 10rpx #FEAE43;
				// border-radius: 4rpx;
				margin-right: 20rpx;
			}
		}

		.list-item {
			// padding: 48rpx 32rpx;
			height: 128rpx;
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;
			border-bottom: solid 2px #f7f7f7;

			.list-item-label {
				font-size: 34rpx;
				color: #353535;
			}
		}
	}
</style>