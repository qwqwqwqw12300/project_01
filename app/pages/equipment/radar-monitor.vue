<!-- 实时监控 -->
<template>
	<app-body>
		<view class="ui-monitor">
			<view class="ui-area" :style="getStyle">
				<view class="ui-roomZone" v-for="(item,index) in roomZone" :key="index" :style="getZonePosition(item)">

				</view>
				<view class="ui-device" :style="getPosition">
					<text class="ui-zone-name">设备</text>
				</view>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	export default {
		data() {
			return {
				baseLenght: '360',
				boxScale: 1,
				deviceScale: 1,
				minBox: 50,
				boxInfo: {
					width: 0,
					height: 0,
				},
				roomZone: [],
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
				let left = this.deviceScale * this.baseLenght
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
			}
		},
		onLoad() {
			console.log(this.deviceInfo.parameter.deviceLocation, 'deviceIfo')
			this.handleInit()
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
					this.boxInfo.height = this.boxScale > 1 ? this.baseLenght : this.boxScale * this.baseLenght
					this.boxInfo.width = this.boxScale < 1 ? this.baseLenght : this.boxScale * this.baseLenght
					this.roomZone = this.deviceInfo.parameter?.roomZones.map(n => {
						const {
							x1,
							x2,
							y1,
							y2
						} = n
						n.width = (x2 - x1) / (roomLeft + roomRight) * this.boxInfo.width
						n.height = (y2 - y1) / roomLength * this.boxInfo.height
						n.positionLeft = (roomLeft + x1) / (roomLeft + roomRight) * this.boxInfo.width
						n.positionTop = (roomLength - y2) / roomLength * this.boxInfo.height
						return n
					})
					console.log(this.roomZone, 'rrrr')
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-monitor {
		width: 100%;
		padding: 30rpx 20rpx;
		box-sizing: border-box;
		display: flex;
		align-items: center;
		justify-content: center;

		.ui-area {
			// height: 100rpx;
			background-color: #fff;
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

	.ui-zone-name {
		font-size: 20rpx;
		color: #fff;
	}

	.ui-roomZone {
		position: absolute;
		background-color: gray;
	}
</style>
