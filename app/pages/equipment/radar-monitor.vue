<!-- 实时监控 -->
<template>
	<app-body>
		<view class="ui-monitor">
			<view class="ui-area" :style="getStyle">
				<view class="ui-roomZone" v-for="(item,index) in roomZoneList" :key="index"
					:style="getZonePosition(item)">
				</view>

				<view class="ui-person" v-for="(item,index) in personStateList" :key="index"
					:style="getPeisonPosition(item)">

				</view>

				<view class="ui-device" :style="getPosition">
					<text class="ui-zone-name">设备</text>
				</view>
			</view>
		</view>
		<view class="ui-tip">
			<view class="ui-item">
				<view class="span"></view>
				<text>隐私区域</text>
			</view>
			<view class="ui-item">
				<view class="span1"></view>
				<text>监控区域</text>
			</view>
			<view class="ui-item">
				<u-icon name="../../static/images/person.png" color="#fff" size="50rpx"></u-icon>
				<text>人员</text>
			</view>
			<view class="ui-item">
				<u-icon name="../../static/images/person-down.png" color="#fff" size="44rpx"></u-icon>
				<text>人员</text>
			</view>
		</view>
		<view class="ui-set">
			<button @click="handleDetail">查看明细</button>
			<button @click="handleSet">设置</button>
		</view>
	</app-body>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		GetStartDevice,
		GetEndDevice,
		GetNowInfo,
		GetRoomZone,
	} from '../../common/http/api';
	export default {
		data() {
			return {
				baseLenght: '330',
				boxScale: 1,
				deviceScale: 1,
				minBox: 50,
				boxInfo: {
					width: 350,
					height: 0,
				},
				roomZoneList: [],
				personStateList: []
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}),
			getStyle() {
				const height = this.boxInfo.height > this.minBox ? this.boxInfo.height : this.minBox
				const width = this.boxInfo.width > this.minBox ? this.boxInfo.width : this.minBox
				return {
					height: `${height}px`,
					width: `${width}px`
				}
			},
			getPosition() {
				let left = this.deviceScale * this.boxInfo.width
				left = left > this.minBox ? left : this.minBox
				return {
					left: `${left}px`
				}
			},
			getZonePosition() {
				return function(data) {
					return {
						width: `${data.width}px`,
						height: `${data.height}px`,
						left: `${data.positionLeft}px`,
						top: `${data.positionTop}px`
					}
				}
			},
			getPeisonPosition() {
				return function(data) {
					return {
						left: `${data.positionLeft}px`,
						top: `${data.positionTop}px`,
						backgroundImage: `url(${data.url})`,
					}
				}
			}
		},
		async onLoad() {
			console.log(this.deviceInfo, 'deviceIfo')
			const data = await GetRoomZone({
				deviceId: this.deviceInfo.deviceId
			})
			this.roomZoneList = data.rows
			this.handleInit()
			this.handleStart()
		},
		onUnload() {
			GetEndDevice({
				deviceId: this.deviceInfo.deviceId
			})
		},
		mounted() {
			const timer = setInterval(() => {
				this.handleQuery()
			}, 4000);
			this.$once('hook:beforeDestroy', () => {
				clearInterval(timer);
			})
		},
		methods: {
			handleInit() {
				const {
					parameter
				} = this.deviceInfo
				if (parameter?.deviceLocation) {
					const {
						roomLeft,
						roomRight,
						roomLength
					} = parameter.deviceLocation
					this.boxScale = roomLength / (roomLeft + roomRight)
					this.deviceScale = roomLeft / (roomLeft + roomRight)
					this.boxInfo.height = this.boxScale * this.boxInfo.width
					// this.boxInfo.width = this.boxScale < 1 ? this.baseLenght : this.boxScale * this.baseLenght
					this.roomZoneList = this.roomZoneList.map(n => {
						const {
							x1,
							x2,
							y1,
							y2
						} = n
						n.width = Math.abs((x2 - x1) / (roomLeft + roomRight) * this.boxInfo.width)
						n.height = Math.abs((y2 - y1) / roomLength * this.boxInfo.height)
						n.positionLeft = (roomLeft + x1) / (roomLeft + roomRight) * this.boxInfo.width
						n.positionTop = (roomLength - y2) / roomLength * this.boxInfo.height
						return n
					})
					console.log(this.roomZoneList, 'roro')
				}
			},
			handleStart() {
				GetStartDevice({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					this.handleQuery()
				})
			},
			handleQuery() {
				const {
					roomLeft,
					roomRight,
					roomLength
				} = this.deviceInfo.parameter.deviceLocation
				GetNowInfo({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					console.log(res, 'now')
					const urlObj = {
						0: '../../static/images/person.png',
						1: '../../static/images/person-down.png',
					}
					this.personStateList = res.data.map(n => {
						n.positionLeft = (roomLeft + ((+n.xPosCm) / 100)) / (roomLeft + roomRight) * this
							.boxInfo.width
						n.positionTop = (roomLength - ((+n.yPosCm) / 100)) / roomLength * this.boxInfo
							.height
						n.url = urlObj[n.state]
						return n
					})

					console.log(this.personStateList, 'sssppp')
				})
			},

			handleDetail() {

			},

			handleSet() {

			}
		}
	}
</script>

<style lang="scss" scoped>
	// ::v-deep {
	// 	uni-button{

	// 	}
	// }

	.ui-monitor {
		width: 100%;
		padding: 30rpx 20rpx;
		box-sizing: border-box;
		display: flex;
		align-items: center;
		justify-content: center;

		.ui-area {
			// height: 100rpx;
			background-color: #008fff;
			// border: 2rpx solid gray;
			position: relative;
		}
	}

	.ui-device {
		height: 26px;
		width: 26px;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
		text-align: center;
		background-color: #00eaff;
		border-radius: 50% 50%;
		position: absolute;
		bottom: -12px;
		margin-left: -13px;
		// left: 50%;
	}

	.ui-person {
		position: absolute;
		background-repeat: no-repeat;
		background-size: cover;
		background-color: #fff;
		width: 40rpx;
		height: 50rpx;
		z-index: 1000;
	}

	.ui-zone-name {
		font-size: 20rpx;
		color: #fff;
	}

	.ui-roomZone {
		position: absolute;
		background-color: gray;
	}

	.ui-tip {
		display: flex;
		align-items: center;
		justify-content: space-around;
		padding: 0rpx 70rpx;
		margin-top: 20rpx;
	}

	.ui-item {
		display: flex;
		flex-direction: row;
		align-items: center;

		text {
			font-size: 24rpx;
		}

		.span {
			width: 50rpx;
			height: 50rpx;
			background-color: gray;
			margin-right: 10rpx;
		}

		.span1 {
			width: 50rpx;
			height: 50rpx;
			background-color: #008fff;
			margin-right: 14rpx;
		}
	}

	.ui-set {
		display: flex;
		margin-top: 90rpx;

		button {
			padding-left: 100rpx;
			padding-right: 100rpx;
			font-size: 28rpx;
			border-radius: 8px !important;
		}
	}
</style>
