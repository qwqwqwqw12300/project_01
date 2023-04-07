<template>
	<view class="ui-map">
		<point-map :record="addressInfo"></point-map>
		<map v-show="false"></map>
		<view class="ui-float">
			<view class="float-item" @click="getDeviceLocation">
				<u-icon name="/static/images/map-position.png" size="36px"></u-icon>
				<!-- 	<text>定位</text> -->
			</view>
			<view class="float-line"></view>
			<view class="float-item" @click="toJump">
				<u-icon name="/static/images/map-track.png" size="30px"></u-icon>
				<!-- 	<text>轨迹</text> -->
			</view>
		</view>
		<touch-popup :minHeight="0.12" :maxHeight="0.7" :touchHeight="64" radius="30rpx">
			<view class="map-position">
				<text class="label">当前位置:</text>
				<view class="content">
					{{ addressInfo.address }}
				</view>
			</view>
			<template v-if="historyList.length">
				<view class="address-list">
					<view class="list-item" v-for="(n, index) in historyList" :key="index" @click="mapMarker(n)">
						<text class="list-item-label">
							{{ n.address }}
						</text>
						<u-icon :name="getMapIcon(n)" size="44rpx" style="margin-right: 6rpx;" />
					</view>
				</view>
			</template>
			<view class="list-empty" v-else>
				<u-empty mode="list" text="暂无数据"></u-empty>
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
				siteInfo: '',
				latitude: 39.909,
				longitude: 116.39742,
				covers: [],
				mapHeight: 0,
				loading: false,
				addressInfo: {
					latitude: '',
					longitude: '',
					address: '',
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
					url: '/pages/card/historical-location'
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
				try {
					const {
						latitude,
						longitude
					} = n.location
					return new Promise((resolve, reject) => {
						if (!latitude || !longitude) {
							return reject()
						}
						mapSearch && mapSearch.reverseGeocode({
								point: {
									latitude,
									longitude
								},
							},
							e => {
								if (e.type === 'fail') return reject()
								resolve({
									location: {
										latitude,
										longitude,
									},
									address: e.address
								})
							})
					})
				} catch (err) {
					console.log(err, '报错-------------------------------------')
				}

			},
			getHistoryLocation() {
				const dateData = uni.$u.timeFormat(new Date(), 'yyyy-mm-dd')
				GetsetAddressBook({
					startTime: dateData + " " + '00:00:00',
					endTime: dateData + " " + '23:59:59',
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					const list = res.data.map((n, i) => {
						n.index = i
						n.locateTime = uni.$u.timeFormat(n.locateTime, 'yyyy-mm-dd hh:MM')
						return n
					}).slice(0, 3)
					const promises = list.map(n => {
						return this.getLocation(n)
					})
					Promise.allSettled(promises).then(res => {
						this.historyList = res.filter(n => {
							return n.status = 'fulfilled'
						}).map(item => {
							return item.value
						})
						console.log(this.historyList, 'pppppppp------------')
					}).catch(res => {}).finally(() => {})
				})
			},
			getDeviceLocation() {
				uni.showLoading({
					title: '加载中'
				})
				GetLastPoint({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {

					if (!res.data.location?.latitude) {
						this.addressInfo = {
							latitude: '',
							longitude: '',
							address: '暂无数据'
						}
						return uni.hideLoading()
					}
					this.getLocation(res.data).then(info => {

						const {
							location: {
								latitude,
								longitude
							},
							address
						} = info
						this.addressInfo = {
							latitude,
							longitude,
							address
						}
						this.getHistoryLocation()
						uni.hideLoading()
						// console.log(res, 'ddiididi')
					})
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-map {

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

	.ui-float {
		position: fixed;
		right: 32rpx;
		bottom: 300rpx;
		padding: 25rpx 20rpx;
		background-color: #FFFFFF;
		box-sizing: border-box;
		border-radius: 15rpx;
		z-index: 999;

		// .border {
		// 	border-bottom: 2rpx solid #353535;
		// }

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

		// :nth-child(1) {
		// 	border-bottom: 2rpx solid #353535;
		// }
	}
</style>