<template>
	<view class="ui-map">
		<point-map :record="addressInfo"></point-map>
		<touch-popup :minHeight="0.1" :maxHeight="0.7" :touchHeight="64" radius="30rpx">
			<view class="map-position">
				<text class="label">当前位置:</text>
				<view class="content">
					{{ addressInfo.address }}
				</view>
			</view>
			<view class="address-list">
				<view class="list-item" v-for="(n, index) in historyList" :key="index" @click="mapMarker(n)">
					<text class="list-item-label">
						{{ n.address }}
					</text>
					<u-icon :name="getMapIcon(n)" size="44rpx" style="margin-right: 6rpx;" />
				</view>
			</view>
			<view class="ui-btn">
				<button class="default" @click="toJump">历史位置</button>
			</view>
		</touch-popup>
	</view>
</template>

<script>
	import {
		GetLastPoint,
		GetsetAddressBook
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
				loading: false,
				addressInfo: {
					latitude: '',
					longitude: '',
					address: '福建省福州市新大陆科技园',
				},
				currentSelect: '',
				historyList: [],

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
		computed: {
			getMapIcon() {
				return function(item) {
					return this.currentSelect === item.index ? '/static/images/map-select.png' :
						'/static/images/position.png'
				}
			},
		},
		methods: {
			toJump() {
				uni.navigateTo({
					url: '/pages/equipment/historical-location'
				})
			},
			mapMarker(data) {
				const {
					address,
					index,
					location: {
						latitude,
						longitude
					}
				} = data
				this.currentSelect = index
				this.addressInfo = {
					latitude,
					longitude,
					address
				}
			},
			getLocation(n) {
				const {
					latitude,
					longitude
				} = n.location
				return new Promise((resolve, reject) => {
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
							n.address = province + city + district
							n.subAddress = street + streetNum + poiName
							resolve(n)
							// this.addressInfo.latitude = latitude
							// this.addressInfo.longitude = longitude
							// this.addressInfo.address = province + city + district + street + streetNum + poiName
						},
						false: (res) => {
							reject(res)
							uni.hideLoading()
						}
					})
				})
			},
			getHistoryLocation() {
				const dateData = uni.$u.timeFormat(new Date(), 'yyyy-mm-dd')
				GetsetAddressBook({
					startTime: dateData + " " + '00:00:00',
					endTime: dateData + " " + '23:59:59',
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					const list = res.data.map(n => {
						n.locateTime = uni.$u.timeFormat(n.locateTime, 'yyyy-mm-dd hh:MM')
						return n
					}).slice(0, 3)
					const promises = list.map(n => {
						return this.getLocation(n)
					})
					Promise.all(promises).then(res => {
						// console.log(res, '9999-----------------')
						this.historyList = res.map((n, i) => {
							n.index = i
							return n
						})
					}).catch(res => {}).finally(() => {
						// uni.hideLoading()
					})
				})
			},
			getDeviceLocation() {
				uni.showLoading({
					title: '加载中'
				})
				GetLastPoint({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					this.getLocation(res.data).then(res => {
						const {
							location: {
								latitude,
								longitude
							},
							address
						} = res
						this.addressInfo = {
							latitude,
							longitude,
							address
						}
						this.getHistoryLocation()
						// console.log(res, 'ddiididi')
						uni.hideLoading()
					})
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
			// margin-top: 30rpx;
			height: 128rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			border-bottom: solid 2px #f7f7f7;

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
