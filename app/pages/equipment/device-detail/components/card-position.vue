<template>
	<view class="ui-map">
		<template v-if="deviceInfo.status === '1'">
			<point-map :record="locationInfo"></point-map>
		</template>
		<div class="list-empty" v-else>
			<u-empty mode="wifi" text="设备已离线" icon="/static/images/no-wifi.png">
			</u-empty>
		</div>
		<map v-show="false"></map>
		<!-- 牵挂卡获取定位的页面的icon -->
		<view class="ui-float">
			<view class="float-item" @click="getNowLocation">
				<u-icon name="/static/images/map-position.png" size="36px"></u-icon>
					<text>刷新</text>
			</view>
			<view class="float-line"></view>
			<view class="float-item" @click="toJump">
				<u-icon name="/static/images/map-track.png" size="30px"></u-icon>
					<text>足迹</text>
			</view>
		</view>
		<!-- <touch-popup v-if="deviceInfo.status === '1'" :minHeight="0.12" :maxHeight="0.7" :touchHeight="64"
			radius="30rpx"> -->
			<view class="bottom-info">
				<view class="map-position">
					<text class="label">当前位置:</text>
					<view class="content">
						{{ addressInfo.address }}
					</view>
				</view>
				<view class="map-position border">
					<text class="label">更新时间:</text>
					<view class="content">
						{{ addressInfo.locateTimeFromCurrent }}
					</view>
				</view>
			</view>
			<!-- <template v-if="historyList.length">
				<view class="address-list">
					<view class="list-item" v-for="(n, index) in historyList" :key="index" @click="mapMarker(n)">
						<view class="list-item-label">
							<text class="main">
								{{ n.address }}
							</text>
							<text class="sub">
								{{ n.locateTime }}
							</text>
						</view>
						<u-icon :name="getMapIcon(n)" size="44rpx" style="margin-right: 6rpx;" />
					</view>
				</view>
			</template>
			<view class="list-empty" v-else>
				<u-empty mode="list" text="暂无数据"></u-empty>
			</view>
			<view class="ui-btn">
				<button class="default" @click="toJump">历史位置</button>
			</view> -->
		<!-- </touch-popup> -->
	</view>
</template>

<script>
	import {
		GetLastPoint,
		GetsetAddressBook,
		GetNowLocation
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
				covers: [],
				mapHeight: 0,
				loading: false,
				//最近更新信息
				addressInfo: {
					address: '',
					locateTimeFromCurrent: '',
				},
				//经纬度信息
				locationInfo: {
					latitude: '',
					longitude: '',
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
			if (this.deviceInfo.status !== '1') return
			this.getDeviceLocation('last')

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
					index,
					location: {
						latitude,
						longitude
					}
				} = data
				this.currentSelect = index
				this.locationInfo = {
					latitude,
					longitude,
				}
			},
			getNowLocation() {
				if (this.deviceInfo.onlineFlag !== '1') return uni.$u.toast('您的设备已离线,无法进行定位')
				this.getDeviceLocation('now')
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
									locateTime: n.locateTime,
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
				}).then(async res => {
					const list = res.data.map((n, i) => {
						n.locateTime = uni.$u.timeFormat(n.locateTime, 'yyyy-mm-dd hh:MM')
						return n
					}).slice(0, 3)
					const arr = []
					for (let i = 0; i < list.length; i++) {
						const res = await this.getLocation(list[i])
						res.index = i
						arr.push(res)
					}
					this.historyList = arr
				})
			},
			getDeviceLocation(type) {
				const interList = {
					last: GetLastPoint,
					now: GetNowLocation
				}
				uni.showLoading({
					title: '获取定位中'
				})
				interList[type]({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					const {
						latitude,
						longitude
					} = res.data.location
					if (!latitude || !longitude) {
						this.locationInfo = {
							latitude: '',
							longitude: '',
						}
						this.addressInfo = {
							address: '暂无数据',
							locateTimeFromCurrent: '暂无数据'
						}
						return uni.hideLoading()
					}
					this.getLocation(res.data).then(info => {
						try {
							const {
								location: {
									latitude,
									longitude
								},
								address,
								locateTime
							} = info
							this.locationInfo = {
								latitude,
								longitude,
							}
							this.addressInfo = {
								address,
								locateTimeFromCurrent: res.data?.locateTimeFromCurrent || uni.$u
									.timeFormat(res.data.locateTime, 'yyyy-mm-dd hh:MM')
							}
							this.getHistoryLocation()
						} catch (e) {
							console.log('报错-------', e);
							//TODO handle the exception
						}

						console.log('关闭弹窗----------', info);
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
			height: 100rpx;
			display: flex;
			align-items: center;
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

		.border {
			border-bottom: solid 2px #f7f7f7;
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
			padding: 24rpx 4rpx;
			// height: 128rpx;
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;
			border-bottom: solid 2px #f7f7f7;

			.list-item-label {
				font-size: 32rpx;
				color: #353535;
				display: flex;
				flex-direction: column;

				.sub {
					margin-top: 5px;
					font-size: 30rpx;
					color: #909193;
				}
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
	.bottom-info {
		position: fixed;
		bottom: 0;
		left: 0;
		width: 100%;
		padding: 0 20rpx;
		background-color: #ffffff;
		box-sizing: border-box;
	}	
</style>