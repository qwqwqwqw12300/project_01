<!-- 增加雷达设备 -->
<template>
	<app-body>
		<!-- <view class="ui-title">
			<text>增加雷达波设备</text>
		</view> -->
		<app-logo text="增加雷达波设备"></app-logo>
		<view class="ui-step">
			<view class="ui-step-icon step1_bg"></view>
			<view class="ui-step-title">
				<text>请确保手机蓝牙</text>
				<text>定位权限</text>
				<text>WIFI处于开启状态</text>

			</view>
		</view>
		<view class="ui-satellite" @click="isEditShow = true">
			<image src="../../static/images/satellite.png" mode="heightFix"></image>
		</view>
		<view class="ui-step">
			<view class="ui-step-icon step2_bg"></view>
			<view class="ui-step-title bg">
				<text>长按智能设备开关,</text>
				<text>等待蓝灯亮起后点击下一步</text>
			</view>
		</view>
		<view class="ui-bluetooth">
			<image src="../../static/images/bluetooth.png" mode="heightFix"></image>
		</view>
		<view class="ui-btn" v-if="connectStatic === 'init'">
			<button @click="next">下一步</button>
		</view>
		<view class="ui-btn" v-if="connectStatic === 'connect'">
			<u-loading-icon :text="eventMsg"></u-loading-icon>
		</view>
		<!-- 修改名称 -->
		<u-popup :closeable="true" :round="10" :show="isEditShow" mode="center" @close="eidtClose">
			<view class="wd-add ui-change">
				<u-text prefixIcon="edit-pen" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
					size="30rpx" text="设备设置"></u-text>
				<view class="ui-add-box">
					<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="设备名称"></u-text>
					<u--input placeholder="请输入设备名称" :maxlength="6" v-model="addForm.deviceName" border="bottom"
						clearable>
					</u--input>
					<u-text size="28rpx" prefixIcon="map" iconStyle="font-size: 36rpx" text="设备位置"></u-text>
					<u--input placeholder="请输入设备位置" :maxlength="6" v-model="addForm.location" border="bottom" clearable>
					</u--input>
				</view>
				<view class="wd-btn-gloup"><button @click="add">确定</button></view>
			</view>
		</u-popup>
		<!-- /修改名称 -->
		<connect-wifi :list="wifiList" ref="wifiRef" @confirm="wifiConfirm"></connect-wifi>
	</app-body>
</template>

<script>
	import {
		PostcreDevice
	} from '../../common/http/api';

	import {
		vpsdk
	} from '../../common/sdk/vpsdk.js';

	export default {
		data() {
			return {
				/**创建设备信息**/
				addForm: {
					deviceName: '',
					deviceNo: uni.$u.random(1, 3),
					deviceType: '0',
					location: ''
				},
				/**编辑展示**/
				isEditShow: false,
				/**事件说明**/
				eventMsg: '启动中...',
				/**设备连接状态 init connect success**/
				connectStatic: 'init',
				wifiList: [{
						ssid: 'nisdfgdfdgddfdfdfg'
					},
					{
						ssid: 'nisdfgdfdgdg2'
					},
					{
						ssid: 'nisdfgdfdgdg3'
					},
					{
						ssid: 'nisdfgdfdgdg4'
					},
					{
						ssid: 'nisdfgdfdgdg5'
					}
				]
			}
		},
		methods: {
			/**
			 * 关闭编辑弹窗
			 */
			eidtClose() {
				this.isEditShow = false;
			},
			handleBack() {
				uni.navigateBack()
			},

			add() {
				if (this.addForm.location && this.addForm.deviceName) {
					PostcreDevice({
						...this.addForm
					}).then(res => {
						uni.showToast({
							icon: 'none',
							title: '添加成功'
						});
						setTimeout(() => {
							uni.navigateBack();
						}, 2000)

					})
				} else {
					uni.showToast({
						icon: 'none',
						title: '请完整填写信息'
					})
				}


			},

			/**
			 * 添加设备
			 */
			next() {
				this.connectStatic = 'connect';
				vpsdk.connect(res => {
					console.log(res, '回弹结果');
					const {
						type,
						data
					} = res;
					switch (type) {
						case 'event': // 事件监听
							this.eventMsg = data;
							break;
						case 'wifi': // 选择wifi
							this.openWifi(data);
							break;
						case 'success': // 连接成功
							this.addForm.deviceNo = data;
							this.isEditShow = true;
							break;
						default:
							uni.showModal({
								title: '设备添加失败，请重试'
							});
							this.connectStatic = 'init';
							break;
					}
				});
			},

			/**
			 * wifi确认
			 */
			wifiConfirm(info) {
				const {
					ssid,
					bssid,
					rssi
				} = info.wifi;
				vpsdk.connectWifi(ssid, bssid, rssi, info.pwd);
			},

			/**
			 * 打开wifi
			 */
			openWifi(data) {
				this.wifiList = data;
				this.$refs.wifiRef.open();
			}
		}
	}
</script>

<style lang="scss" scoped>
	.step1_bg {
		background-image: url('../../static/images/step1.png');
	}

	.step2_bg {
		background-image: url('../../static/images/step2.png');
	}

	.bg {
		background-color: rgb(227, 252, 255);
	}

	.active {
		background-color: rgb(220, 251, 255);
	}


	.ui-navbar {
		padding-top: calc(var(--status-bar-height) + 26rpx);
		padding-left: 20rpx;
		display: flex;
		align-items: center;

		.ui-navbar-text {
			font-size: 36rpx;
			color: #fff;
		}
	}

	.ui-title {
		margin-top: 10rpx;
		padding-left: 50rpx;

		text {
			font-size: 72rpx;
			font-weight: bold;
			color: #ffffff;
		}
	}

	.ui-step {
		height: 110rpx;
		display: flex;
		align-items: center;
		margin-top: 30rpx;
		padding: 0 40rpx;

		.ui-step-icon {
			width: 110rpx;
			height: 96rpx;
			background-size: cover;
			background-position: center;
			background-repeat: no-repeat;
		}

		.ui-step-title {
			display: flex;
			flex-direction: column;
			margin-left: 20rpx;
			padding: 10rpx 20rpx;
			border-radius: 10rpx;

			text {
				color: #0e6bc0;
				font-size: 28rpx;
				font-weight: bold;
				text-shadow: 0 0 3px #fff, 0 0 3px #5acbff;
			}
		}
	}

	.ui-satellite {
		margin-top: 30rpx;
		width: 100%;
		height: 300rpx;
		display: flex;
		align-items: center;
		justify-content: center;

		image {
			// width: 400rpx;
			height: 300rpx;
		}
	}

	.ui-bluetooth {
		margin-top: 20rpx;
		width: 100%;
		height: 300rpx;
		display: flex;
		align-items: center;
		justify-content: center;

		image {
			// width: 380rpx;
			height: 300rpx;
		}
	}



	.ui-btn {
		position: absolute;
		width: 450rpx;
		margin: 0 auto;
		margin-top: 50rpx;
		// bottom: 20rpx;
		left: 50%;
		transform: translateX(-50%);
		text-align: center;

		text {
			font-size: 30rpx;
			color: #0094ff;
		}

		&>* {
			&:active {
				opacity: 0.8;
			}
		}

		&>*:nth-child(1) {
			margin-bottom: 50rpx;
		}
	}

	.wd-add {
		width: 582rpx;
		min-height: 500rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		&.ui-change {
			.ui-add-box {
				border-bottom: 1px solid #e4e4e4;
			}
		}

		&>view {
			margin-top: 52rpx;

			&.ui-add-box {
				padding: 10rpx 20rpx;

				&>* {
					margin-top: 30rpx;
				}
			}
		}

		.wd-btn-gloup {
			text-align: center;
			margin-top: 70rpx;

			button {
				width: 237rpx;
				height: 71rpx;
				font-size: 28rpx;
				color: #ffffff;
			}
		}
	}
</style>
