<!--
* @FilePath: /components/bind-device/bind-device.vue
* @Description: 绑定设备
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close" @open="open">
		<view class="ui-add">
			<view class="ui-title">
				<text class="ui-title-font">选择绑定设备</text>
			</view>

			<scroll-view :scroll-y="true" class="scroll">
				<view class="ui-add-box">
					<view class="device-list" v-for="(n,index) in devices" :key="index">
						<image :src="getImage(n)"></image>
						<view class="detail">
							<text class="name">{{ n.name }}</text>
							<text class="action" @click="next(n)">绑定</text>
						</view>
					</view>
				</view>
			</scroll-view>
			<view class="ui-btn-group">
				<text @click="goManager">前往管理设备</text>
			</view>
		</view>
	</u-popup>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		PostUpdateCareCardBind,
		setDevice,
		PostUpdateWatchBind
	} from '../../common/http/api';
	export default {
		props: {
			payload: {
				default: () => ({})
			}
		},
		data() {
			return {
				show: false,
				sheetRoom: false,
			};
		},
		computed: {
			...mapState({
				devices: function(state) {
					let devices = [];
					switch (this.payload.type) {
						case 'room': // 绑定房间
							devices = state.devicesList.filter(ele => (!ele.roomId) && ele.type === '0'); // 跌倒设备
							break;
						case 'human': // 绑定房间
							devices = state.devicesList.filter(ele => (!ele.humanId) && (ele.type === '1' || ele
								.type === '2')); // 跌倒设备
							break;
						default:
							break;
					}
					return devices.map((ele, index) => ({
						name: ele.name,
						value: index,
						deviceId: ele.deviceId,
						type: ele.type
					}));
				}
			}),
			getImage() {
				return function(val) {
					return {
						0: '/static/images/leida-nm.png',
						1: '/static/images/dzqkg.png',
						2: '/static/images/watch-device.png',
					} [val.type]
				}
			}
		},
		mounted(options) {
			console.log(this.devices, '444444---------')
		},
		methods: {
			demo() {
				console.log(this.devices, 'devices')
				this.sheetRoom = true
			},
			close() {
				this.show = false;
			},
			open() {
				this.show = true;
			},
			next(item) {
				console.log(item, '999998898')
				const {
					deviceId
				} = item
				if (!deviceId && deviceId !== 0) return uni.$u.toast('请选择设备');
				const {
					familyId,
					id,
					type
				} = this.payload;
				const device = this.devices[deviceId];
				console.log(device, 'device');
				let handle;
				console.log(type, 'type');
				switch (type) {
					case 'room':
						console.log('绑定到房间');
						handle = setDevice({
							familyId,
							roomId: id,
							deviceId: device.deviceId,
							flag: '1'
						})
						break;
					case 'human':
						console.log('绑定到人');
						handle = device.type === '1' ? PostUpdateCareCardBind({
							familyId,
							deviceId: device.deviceId,
							humanId: id
						}) : PostUpdateWatchBind({
							familyId,
							deviceId: device.deviceId,
							humanId: id
						})
						break;
					default:
						break;
				}
				handle && handle.then(res => {
					uni.showToast({
						title: '绑定成功',
						icon: 'none'
					});
					this.close();
					this.$emit('next')
				});
			},
			/**
			 * 跳转管理
			 */
			goManager() {
				this.close();
				uni.navigateTo({
					url: '/pages/myself/device-manage/device-manage'
				});
			},
		}
	};
</script>

<style lang="scss">
	.ui-add {
		width: 600rpx;
		height: 352rpx;
		border-radius: 20rpx;
		padding: 20rpx 20rpx;
		background: #F7F7F7;

		.ui-title {
			// height: 80rpx;
			display: flex;
			align-items: center;
			justify-content: center;

			.ui-title-font {
				font-family: PingFangSC-Medium;
				font-size: 36rpx;
				color: #333333;
				letter-spacing: 0;
				text-align: center;
				line-height: 46rpx;
				font-weight: 550;
			}
		}

		.scroll {
			height: 440rpx;
		}

		.ui-add-box {
			margin-top: 30rpx;
			// padding: 10rpx 20rpx;
			display: flex;
			flex-direction: row;
			flex-wrap: wrap;

			.device-list {
				height: 160rpx;
				width: calc(50% - 50rpx);
				padding: 0 16rpx;
				margin: 0 8rpx;
				margin-bottom: 20rpx;
				background: #fff;
				border-radius: 32rpx;
				display: flex;
				align-items: center;
				justify-content: space-between;

				image {
					height: 120rpx;
					width: 120rpx;
				}

				.detail {
					flex: 1;
					margin-left: 10rpx;
					display: flex;
					align-items: center;
					flex-direction: column;

					.name {
						font-size: 30rpx;
						color: #353535;
						font-weight: 550;
					}

					.action {
						margin-top: 16rpx;
						border-radius: 24px;
						color: #fff;
						padding: 6rpx 24rpx;
						font-size: 26rpx;
						color: #fff;
						background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
					}
				}

			}
		}

		.ui-input-font {
			font-family: PingFangSC-Medium;
			font-size: 30rpx;
			color: #333333;
			letter-spacing: 0;
			line-height: 30rpx;
			font-weight: 500;
		}

		.ui-btn {
			text-align: center;
			margin-top: 70rpx;

			button {
				width: 237rpx;
				height: 71rpx;
				font-size: 28rpx;
				color: #ffffff;
			}

			;

		}

		.ui-select {
			// width: 506rpx;
			border-radius: 20rpx;
			background-color: #F8F8F8;
		}

		.ui-btn-group {
			width: 100%;
			height: 98rpx;
			position: absolute;
			left: 0rpx;
			bottom: 0rpx;
			border-radius: 0rpx 0rpx 20rpx 20rpx;
			border-top: 1rpx solid black;
			border-color: rgba(0, 0, 0, 0.1);
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: 30rpx;
			color: #599FFF;

			// .plain {
			// 	width: 49.5%;
			// 	height: 100rpx;
			// 	display: flex;
			// 	align-items: center;
			// 	justify-content: center;
			// }

			// .ui-btn-hr {
			// 	width: 1rpx;
			// 	height: 100rpx;
			// 	opacity: 0.1;
			// 	background-color: black;
			// }
		}
	}
</style>