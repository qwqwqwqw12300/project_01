<template>
	<view class="ui-map">
		<point-map :latitude="latitude" :longitude="longitude" :record="addressInfo"></point-map>
	</view>
</template>

<script>
	import {
		GetLastPoint
	} from '@/common/http/api';
	export default {
		props: {
			deviceInfo: {
				type: Object,
				default: () => {},
			}
		},
		data() {
			return {
				siteInfo: '',
				latitude: 39.909,
				longitude: 116.39742,
				covers: [],
				mapHeight: 0,
				addressInfo: {
					latitude: 26.03001,
					longitude: 119.39139,
					address: '福建省福州市新大陆科技园',
				}

			}
		},
		created() {
			const sysHeight = uni.getSystemInfoSync().windowHeight
			const saveHeight = uni.getSystemInfoSync().statusBarHeight
			//44 + 167 + 47 + 130
			this.mapHeight = sysHeight - saveHeight - 400
		},
		mounted() {
			// this.getDeviceLocation()

		},
		methods: {
			toJump() {
				uni.navigateTo({
					url: '/pages/equipment/historical-location'
				})
			},
			getLocation(latitude, longitude) {
				uni.getLocation({
					geocode: true,
					type: 'gcj02',
					latitude,
					longitude,
					success: (res) => {
						const {
							address: {
								province,
								city,
								district,
								street,
								streetNum,
								poiName
							},
						} = res
						this.addressInfo.latitude = latitude
						this.addressInfo.longitude = longitude
						this.addressInfo.address = province + city + district + street + streetNum + poiName
					},
					false: (res) => {
						console.log(res, 'error')
						uni.hideLoading()
					}
				})
			},
			getDeviceLocation() {
				GetLastPoint({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					const {
						latitude,
						longitude
					} = res.data.location
					this.latitude = latitude
					this.longitude = longitude
					this.getLocation(latitude, longitude)
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-map {
		// padding: 32rpx;

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
