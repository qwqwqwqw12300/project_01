<!--
* @Author: zhanch
* @Date: 2022-12-29 18:05:30
* @FilePath: /components/bind-device/bind-device.vue
* @Description: 绑定设备
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close" @open="open">
		<view class="ui-add">
			<u-text prefixIcon="plus-circle" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
				size="30rpx" text="绑定设备"></u-text>
			<view class="ui-add-box">
				<u-text size="28rpx" prefixIcon="../../static/images/set-form.png" iconStyle="font-size: 25rpx"
					text="选择设备"></u-text>
				<view class="ui-select">
					<uni-data-select v-model="deviceId" :clear="false" :localdata="devices"></uni-data-select>
				</view>
			</view>
			<view class="ui-btn-group">
				<button @click="next">确定</button>
				<button @click="close">取消</button>
				<button @click="goManager">管理设备</button>
			</view>
		</view>
	</u-popup>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		setDevice
	} from '../../common/http/api';
	const INIT_BINDFORM = {
		familyId: '',
		roomId: '',
		deviceId: '',
		deviceName: '',
		deviceType: '',
		deviceNo: '',
		deviceId: '',
		roomLeft: 100,
		roomHeight: 100,
		roomRight: 100,
		roomLength: 100,
		existFlag: 0,
		fallFlag: 0,
		entryTime: 0,
		departureTime: 0,
		startTime: 0,
		endTime: 0,
		inMonitorFlag: 0,
		outMonitorFlag: 0,
	};
	export default {
		props: {
			payload: {
				default: () => ({})
			}
		},
		data() {
			return {
				show: false,
				deviceId: ''
			};
		},
		computed: {
			...mapState({
				devices: state => {
					return state.devicesList.map(ele => ({
						text: ele.name,
						value: ele.deviceId
					}));
				}
			})
		},
		mounted(options) {

		},
		methods: {
			close() {
				this.show = false;
			},
			open() {
				this.show = true;
			},
			next() {
				setDevice({
					...INIT_BINDFORM,
					...this.payload,
					deviceId: this.deviceId
				}).then(res => {
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
					url: '/pages/myself/device-manage'
				});
			}
		}
	};
</script>

<style lang="scss">
	.ui-add {
		width: 582rpx;
		height: 406rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		border: 1px solid #ffcb3d;
		padding: 53rpx 31rpx;

		&>view {
			margin-top: 52rpx;

			&.ui-add-box {
				padding: 10rpx 20rpx;

				&>* {
					margin-top: 30rpx;
				}
			}
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
			width: 506rpx;
			border-radius: 20rpx;
			background-color: #bfbfbf;
		}

		.ui-btn-group {
			display: flex;

			button {
				width: 135rpx;
				height: 70rpx;
				border-radius: 35rpx;
				font-size: 28rpx;
				color: #ffffff;

				&:nth-child(1) {
					background-image: linear-gradient(96deg, #e8bf00 0%);
				}

				&:nth-child(2) {
					background-image: linear-gradient(96deg, #898989 0%, #b4b4b4 100%);
				}

				&:nth-child(3) {
					width: 206rpx;
					height: 70rpx;
					background-image: linear-gradient(96deg, #e8bf00 0%, #00eaff 100%);
				}
			}
		}
	}
</style>
