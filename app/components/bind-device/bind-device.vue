<!--
* @Author: zhanch
* @Date: 2022-12-29 18:05:30
* @FilePath: /components/bind-device/bind-device.vue
* @Description: 绑定设备
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close" @open="open">
		<view class="ui-add">
			<view class="ui-title">
				<text class="ui-title-font">绑定设备</text>
			</view>
			<view class="ui-add-box">
				<text class="ui-input-font">选择设备</text>
				<view class="ui-select">
					<uni-data-select v-model="deviceId" :clear="false" :localdata="devices"></uni-data-select>
				</view>
			</view>
			<view class="ui-btn-group">
				<view class="plain" @click="goManager">管理设备</view>
				<view class="ui-btn-hr"></view>
				<view class="plain" @click="next">确定</view>
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
	import {
		INIT_DEIVCE_SET
	} from '../../config/db';
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
					return state.devicesList.filter(ele => !ele.roomId).map(ele => ({
						text: ele.name,
						value: ele.deviceId
					}));
				}
			})
		},
		mounted(options) {},
		methods: {
			close() {
				this.show = false;
			},
			open() {
				this.show = true;
			},
			next() {
				if (this.deviceId) {
					setDevice({
						...INIT_DEIVCE_SET,
						...this.payload,
						deviceName: this.payload.name,
						deviceId: this.deviceId,
						flag: '1'
					}).then(res => {
						uni.showToast({
							title: '绑定成功',
							icon: 'none'
						});
						this.close();
						this.$emit('next')
					});
				} else {
					uni.$u.toast('请选择设备');
				}

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
		height: 352rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		border: 1px solid #ffcb3d;
		padding: 20rpx 31rpx;
		.ui-title{
			display: flex;
			align-items: center;
			justify-content: center;
			.ui-title-font{
				font-family: PingFangSC-Medium;
				font-size: 36rpx;
				color: #333333;
				letter-spacing: 0;
				text-align: center;
				line-height: 46rpx;
				font-weight: 500;
			}
		}
		&>view {
			margin-top: 10rpx;

			&.ui-add-box {
				padding: 10rpx 20rpx;

				&>* {
					margin-top: 30rpx;
				}
			}
		}
		.ui-input-font{
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
			width: 506rpx;
			border-radius: 20rpx;
			background-color: #bfbfbf;
		}

		.ui-btn-group {
			width: 100%;
			position: absolute;
			left: 0rpx;
			bottom: 0rpx;
			border-radius:0rpx 0rpx 20rpx 20rpx;
			border-top: 1rpx solid black;
			border-color: rgba(0,0,0,0.1);
			display: flex;
			align-items: center;
			justify-content: space-between;
			.plain {
				width: 49.5%;
				height: 100rpx;
				display: flex;
				align-items: center;
				justify-content: center;
			}
			.ui-btn-hr{
				width: 1rpx;
				height: 100rpx;
				opacity: 0.1;
				background-color: black;
			}
		}
	}
</style>
