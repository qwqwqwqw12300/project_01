<!-- 增加监护设备 -->
<template>
	<app-body>
		<!-- <view class="ui-title">
			<text>增加监护设备</text>
		</view> -->
		<app-logo text="增加电子牵挂卡"></app-logo>
		<view class="ui-step">
			<view class="ui-step-icon step1_bg"></view>
			<view class="ui-step-title">
				<text>请确保手机蓝牙</text>
				<text>WIFI处于开启状态</text>
			</view>
		</view>
		<view class="ui-bluetooth">
			<image src="../../static/images/bluetooth.png" mode="heightFix"></image>
		</view>
		<view class="ui-step">
			<view class="ui-step-icon step2_bg"></view>
			<view class="ui-step-title">
				<text>长按智能设备开关,</text>
				<text>等待蓝灯亮起</text>
			</view>
		</view>
		<view class="ui-list">
			<view v-for="item in deviceList" :key="item.key" @tap="handleSelect(item)" class="ui-list-item"
				:class="{'active': item.active}">
				<image class="device" src="../../static/images/device.png" mode="" />
				<text class="text">设备1 device-01</text>
				<image v-show="item.active" class="tick" src="../../static/images/tick.png" />
			</view>
		</view>
		<view class="ui-btn">
			<button @click="next" class="default">下一步</button>
		</view>
		<!-- 修改名称 -->
		<u-popup :closeable="true" :round="10" :show="isEditShow" mode="center" @close="eidtClose">
			<view class="wd-add ui-change">
				<u-text prefixIcon="edit-pen" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
					size="30rpx" text="设备设置"></u-text>
				<view class="ui-add-box">
					<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="设备名称"></u-text>
					<u--input placeholder="请输入设备名称" v-model="addForm.deviceName" border="bottom" clearable>
					</u--input>
					<u-text size="28rpx" prefixIcon="map" iconStyle="font-size: 36rpx" text="设备位置"></u-text>
					<u--input placeholder="请输入设备位置" v-model="addForm.location" border="bottom" clearable>
					</u--input>
				</view>
				<view class="wd-btn-group"><button class="default" @click="add">确定</button></view>
			</view>
		</u-popup>
		<!-- /修改名称 -->
	</app-body>
</template>

<script>
	import {
		PostcreDevice
	} from '../../common/http/api'
	export default {
		data() {
			const deviceList = [{
					key: 0,
					name: '设备1 device-01',
					active: true
				},
				{
					key: 1,
					name: '设备2 device-03',
					active: false
				},
				{
					key: 2,
					name: '设备2 device-03',
					active: false
				}
			]
			return {
				deviceList,
				/**创建设备信息**/
				addForm: {
					deviceName: '',
					deviceNo: uni.$u.random(1, 100),
					deviceType: '1',
					location: ''
				},
				/**编辑展示**/
				isEditShow: false,
			}
		},
		methods: {
			handleSelect(item) {
				this.deviceList = this.deviceList.map(n => {
					n.active = n.key === item.key
					return n
				})
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
						}, 2000);

					})
				} else {
					uni.showToast({
						icon: 'none',
						title: '请完整填写信息'
					})
				}
			},
			eidtClose() {
				this.isEditShow = false
			},
			/**
			 * 添加设备
			 */
			next() {
				this.isEditShow = true;
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
		margin-top: 50rpx;
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
			padding: 10rpx;
			border-radius: 10rpx;

			text {
				color: #0e6bc0;
				font-size: 28rpx;
				font-weight: bold;
				text-shadow: 0 0 3px #fff, 0 0 3px #5acbff;
			}
		}
	}

	.ui-bluetooth {
		margin-top: 30rpx;
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

	.ui-list {
		margin-top: 20rpx;
		padding: 20rpx 60rpx 20rpx 20rpx;
		display: flex;
		flex-direction: column;

		.ui-list-item {
			margin-left: 80rpx;
			padding: 14rpx 34rpx;
			border-radius: 40rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.text {
				flex: 1;
				font-size: 36rpx;
				margin-left: 20rpx;
				color: #000000;
				font-weight: 550;
			}

			.device {
				width: 50rpx;
				height: 50rpx;
			}

			.tick {
				width: 35rpx;
				height: 30rpx;
			}
		}
	}

	.ui-btn {
		// position: absolute;
		width: 100%;

		button {
			width: 450rpx;
		}

		margin: 0 auto;
		// bottom: 80rpx;
		// left: 50%;
		// transform: translateX(-50%);
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
		height: 606rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		&.ui-change {
			height: 500rpx;

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

		.wd-btn-group {
			text-align: center;
			margin-top: 70rpx;

			button {
				width: 237rpx;
				height: 71rpx;
				line-height: 71rpx;
				font-size: 28rpx;
			}
		}
	}
</style>
