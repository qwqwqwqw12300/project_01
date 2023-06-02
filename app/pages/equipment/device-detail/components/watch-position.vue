<template>
	<view class="ui-map">
		<point-map :record="addressInfo"></point-map>
		<map v-show="false"></map>

		<view class="ui-float">
			<view class="float-item" @click="getNowLocation">
				<u-icon name="/static/images/map-position.png" size="36px"></u-icon>
				<!-- <text>更新位置</text> -->
			</view>
			<!-- 	<view class="float-line"></view> -->
		</view>
		<view class="map-popup">
			<view class="map-position">
				<text class="label">当前位置:</text>
				<view class="content">
					{{ addressInfo.address }}
				</view>
			</view>
			<view class="map-position">
				<text class="label">更新时间:</text>
				<view class="content">
					{{ addressInfo.locateTimeFromCurrent }}前
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		GetWatchTrack,
		GetWatchLocation
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
				isLoading: false,
				time: 1,
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
			getNowLocation() {
				if (this.deviceInfo.onlineFlag !== '1') return uni.$u.toast('您的设备已离线,无法进行定位')
				if (this.isLoading) return
				uni.showLoading({
					title: '获取定位中'
				})
				GetWatchLocation({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					setTimeout(() => {
						this.isLoading = true
						this.time = 1
						this.getDeviceLocation()
					}, 3000)
				}, err => {
					uni.hideLoading()
				})
				// this.getDeviceLocation('now')
			},
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
				!this.isLoading && uni.showLoading({
					title: '获取定位中'
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
						const preTime = this.addressInfo.locateTimeFromCurrent
						const {
							latitude,
							longitude,
							address,
							// locateTimeFromCurrent
						} = info
						this.addressInfo = {
							latitude,
							longitude,
							address,
							locateTimeFromCurrent: res.data?.locateTimeFromCurrent,
						}
						console.log(this.isLoading, '455555555----')
						
						if (!this.isLoading) return uni.hideLoading()
						console.log(this.addressInfo.locateTimeFromCurrent, res.data.locateTimeFromCurrent,
							'00000000000000')
						console.log(this.addressInfo.locateTimeFromCurrent == res.data
							.locateTimeFromCurrent, 'oo----------')
						if (preTime == res.data.locateTimeFromCurrent &&
							this.time <=
							3) {
							console.log('进来了----------')
							setTimeout(() => {
								this.time += 1
								this.getDeviceLocation()
							}, 3000)

						} else {
							this.time = 1
							this.isLoading = false
							uni.hideLoading()
						}
					})
					// if (this.loading && this.addressInfo.locateTimeFromCurrent != res.data.locateTimeFromCurrent) {
					// setTimeout(() => {
					// 	this.isLoading = true
					// 	this.getDeviceLocation()
					// }, 3000)
					// }else{
					// 	this.isLoading = false
					// }
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
			// border-radius: 30rpx 30rpx 0 0;
			height: 260rpx;
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
				width: 180rpx;
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

	.ui-float {
		position: fixed;
		right: 32rpx;
		bottom: 330rpx;
		padding: 20rpx 20rpx;
		background-color: #FFFFFF;
		box-sizing: border-box;
		border-radius: 15rpx;
		z-index: 999;

		.float-item {
			display: flex;
			flex-direction: column;
			align-items: center;

			text {
				margin-top: 4rpx;
				font-size: 26rpx;
			}
		}

		.float-line {
			width: 100%;
			border-bottom: 2rpx solid #353535;
			margin: 20rpx 0;
		}

	}
</style>