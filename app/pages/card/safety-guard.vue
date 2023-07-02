<!-- 修改设备名称 -->
<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<app-logo color="#353535" text="安全守护"></app-logo>
		</view>
		<view class="app-main">
			<view class="tips">
				设置好安全守护位置，当被守护人离开或到达时，您都将收到通知（设定安全区范围过小，可能导致误报，请慎重选择设定！）
			</view>
			<view class="guard" v-if="guardList.length">
				<view class="guard-item" v-for="(item, index) in guardList" :key="index" @tap="toLocationGuard(item)">
					<image class="guard-item-img" src="@/static/images/guar-map.png" mode=""></image>
					<view class="guard-item-content">
						<view class="guard-item-content-info">
							<image class="guard-item-content-info-icon" src="@/static/images/add-home.png" mode=""></image>
							<view class="guard-item-content-info-text">
								<view class="guard-item-content-info-text-title">
									<view class="guard-item-content-info-text-title-name">{{ item.name }}</view>
									<view v-if="item.fenceType === 'circle'"
										class="guard-item-content-info-text-title-radius">方圆{{ item.radius }}米</view>
								</view>
								<view class="guard-item-content-info-text-address">
									<view v-if="item.fenceType === 'circle'">{{ item.address }}</view>
								</view>
							</view>
						</view>
						<view class="guard-item-content-switch" @tap.stop.prevent>
							<u-icon name="trash" size="30" @tap="delGuard(item)"></u-icon>
							<u-switch v-model="item.fenceSwitch" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)"
								asyncChange @change="handleSwitch($event, item)"></u-switch>
						</view>
					</view>
				</view>
			</view>
			<view v-else>
				<image class="guard-item-img" src="@/static/images/guar-map.png" mode=""></image>
			</view>
		</view>
		<view class="app-bottom">
			<button class="app-bottom-btn" @tap="toEnclosure">添加新的区域守护</button>
		</view>
	</app-body>
</template>

<script>
import {
	mapState
} from 'vuex';
import {
	GetFenceInfo,
	PostAddFence,
	remGuard
} from '@/common/http/api';
export default {
	data() {
		return {
			guardList: [],
			a: 1
		}
	},
	computed: {
		...mapState({
			deviceInfo: state => state.deviceInfo
		},)
	},
	// mounted() {
	// 	this.getGuardList()
	// },
	onShow() {
		this.getGuardList()
	},
	methods: {
		async getGuardList() {
			let res = await GetFenceInfo({
				deviceId: this.deviceInfo.deviceId
			})
			res.data.map((item, index) => {
				if (item.fenceSwitch) {
					item.fenceSwitch = true
				} else {
					item.fenceSwitch = false
				}
			})
			this.guardList = res.data
		},
		toEnclosure() {
			if (this.guardList.length >= 3) {
				uni.showModal({
					title: '提示',
					content: '最多只能添加三个'
				});
				return
			}
			this.$store.dispatch('setUrlLocation', {})
			uni.navigateTo({
				url: '/pages/card/enclosure/enclosure'
			})
		},
		toLocationGuard(item) {
			console.log('守护列表选择item', item)
			const {
				province,
				city,
				district,
				address,
				latitude,
				longitude,
				deviceFenceId,
				deviceId,
				fenceType,
				deviceNo,
				radius,
				points,
				name
			} = item
			this.$store.dispatch('setUrlLocation', {
				location: {
					latitude,
					longitude
				},
				province,
				city,
				district,
				address,
				deviceFenceId,
				deviceId,
				fenceType,
				deviceNo,
				radius: Number(radius),
				points,
				name
			})
			uni.navigateTo({
				url: `/pages/card/enclosure/enclosure`
			})
		},
		async delGuard(item) {
			const res = await remGuard({
				deviceId: this.deviceInfo.deviceId,
				deviceFenceId: item.deviceFenceId,
				fenceType: item.fenceType
			})
			uni.$u.toast('删除成功')
			this.getGuardList()
		},
		async handleSwitch(e, item) {
			item.fenceSwitch = e
			const res = await PostAddFence({
				deviceId: this.deviceInfo.deviceId,
				deviceFenceId: item.deviceFenceId,
				fenceType: item.fenceType,
				deviceNo: item.deviceNo,
				fenceSwitch: item.fenceSwitch ? 1 : 0,
				address: item.address,
				latitude: item.latitude,
				longitude: item.longitude,
				radius: item.radius,
				points: item.points
			})
			uni.$u.toast(e ? '开启成功' : '关闭成功')
			console.log(e, 'handleSwitcheeeeeee')
			console.log(e, 'handleSwitchitemitemitem')
		}
	}
}
</script>

<style lang="scss" scoped>
.ui-logo {
	background-color: #ffffff;
	padding-bottom: 34rpx;
}

.app-main {
	padding: 20rpx 32rpx;
	padding-bottom: 266rpx;
}

.tips {
	text-align: justify;
	font-size: 26rpx;
	color: #888888;
	font-weight: 400;
}

.guard {
	&-item {
		&-img {
			width: 100%;
			height: 300rpx;
			display: block;
			padding-top: 20rpx;
		}

		&-content {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 26rpx 32rpx 32rpx 32rpx;
			background-color: #ffffff;

			&-info {
				display: flex;
				align-items: center;
				padding-left: 20rpx;
				font-size: 32rpx;

				&-icon {
					width: 64rpx;
					height: 64rpx;
					min-width: 64rpx;
					min-height: 64rpx;
				}

				&-text {
					margin-left: 20rpx;
					&-title {
						width: 100%;
						height: 100%;
						padding-bottom: 10rpx;
						display: flex;

						&-name {
							font-size: 32rpx;
							color: #353535;
							font-weight: 400;
						}

						&-radius {
							white-space: nowrap;
							margin-left: 10rpx;
						}
					}

					&-address {
						view {
							font-size: 26rpx;
							color: #888888;
							font-weight: 400;
						}
					}
				}



			}

			&-switch {
				display: flex;
				align-items: center;
			}
		}
	}
}

.app-bottom {
	position: fixed;
	bottom: 0;
	padding-top: 20rpx;
	padding-bottom: 54rpx;
	z-index: 9;
	width: 100%;
	height: 88rpx;
	background-color: #f2f2f2;

	&-btn {
		width: 686rpx;
		background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
		border-radius: 44rpx;
		font-size: 36rpx;
		color: #FFFFFF;
		text-align: center;
		font-weight: 400;
	}
}

/deep/ .u-icon {
	margin: 0 20rpx;
}
</style>