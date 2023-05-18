<!-- 电子牵挂卡卡片 -->
<template>
	<view class="swiper-item">
		<view class="item-box">
			<image class="img" src="/static/images/electron-card.png"></image>
			<view class="device-info">
				<view class="title">
					<text class="name">{{record.name || '未命名设备'}}</text>
					<text class="line"
						:class="[record.onlineFlag === '1' ? 'online' : 'offline']">{{getOnlineStatus}}</text>
				</view>
				<view class="status">
					<u-icon name="/static/images/run-status.png" size="44rpx" style="margin-right: 6rpx;" />
					<text>运行状态: {{ getStatus }}</text>
				</view>
				<view class="status">
					<u-icon name="/static/images/card-power.png" size="44rpx" style="margin-right: 6rpx;" />
					<text>剩余电量: {{ record.currentPower || '暂无信息'}}</text>
				</view>
			</view>
			<view class="device-set" :style="{width: type === 'set' ? '130rpx': '170rpx'}" @click="handleSet">
				<template v-if="record.shareFlag  == '2'">
					<u-icon :name="type === 'set' ? '/static/images/device-set.png': '/static/images/reStart.png'"
						size="44rpx" style="margin-right: 6rpx;" />
					{{ type === 'set'? '设置':'重启设备' }}
				</template>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		PostRebootDevice
	} from '@/common/http/api';
	import {
		env
	} from "@/config/env.js";
	export default {
		props: {
			record: {
				type: Object,
				default: () => {},
			},
			type: {
				type: String,
				default: 'set', //set: '配置',reset: '重置设备'
			}
		},
		data() {
			return {

			}
		},
		computed: {
			getStatus() {
				return {
					0: '正常',
					1: '低电量',
					2: '关机',
				} [this.record.currentStatus]
			},
			getOnlineStatus() {
				return {
					0: '离线',
					1: '在线',
					2: '休眠',
				} [this.record.onlineFlag]
			}
		},
		methods: {
			handleSet() {
				if (this.type === 'set') {
					if (this.record.shareFlag == 0) return uni.$u.toast('当前设备属于分享设备，不能配置')
					if (this.record.onlineFlag != 1 && env.mode === 'prod') return uni.$u.toast('您的设备已离线，暂时无法进行配置')
					this.$store.commit('setDeviceInfo', this.record);
					uni.navigateTo({
						url: '/pages/card/card-set'
					})
					return
				} else {
					uni.showModal({
						title: '提示',
						content: '是否确认重启设备',
						success: res => {
							if (res.confirm) {
								PostRebootDevice({
									deviceId: this.record.deviceId,
									initialization: "0"
								}).then(res => {
									uni.$u.toast(res.msg)
								})
							}
						}
					});
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.swiper-item {
		display: block;
		height: 250rpx;
		// margin: 0 6rpx;
		background-color: #fff;
		padding: 30rpx 10rpx;
		border-radius: 20rpx;
		box-sizing: border-box;
		box-shadow: 0px 2px 20px 0px rgba(220, 205, 180, 0.5);

		.item-box {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.img {
				width: 190rpx;
				height: 190rpx;
			}

			.device-info {
				flex: 1;
				align-self: flex-start !important;
				padding-left: 10rpx;
				padding-top: 10rpx;

				.title {
					color: #353535;
					font-size: 36rpx;
					font-weight: 550;
					padding-left: 4rpx;
					display: flex;
					align-items: center;

					.name {
						max-width: 200rpx;
						white-space: nowrap;
						text-overflow: ellipsis;
						overflow: hidden;
					}

					.line {
						margin-left: 20rpx;
						color: #fff;
						font-size: 22rpx;
						padding: 6rpx 12rpx;
						// background-image: linear-gradient(90deg, #1EC862 0%, #13B98F 100%);
						border-radius: 4px;
					}
				}

				.status {
					// height: 60rpx;
					margin-top: 14rpx;
					display: flex;
					align-items: center;

					text {
						font-size: 26rpx;
						color: #888888;
					}
				}
			}

			.device-set {
				white-space: nowrap;
				text-overflow: ellipsis;
				overflow: hidden;
				display: flex;
				color: #FEAE43;
				font-size: 30rpx;
				font-weight: 400;
			}

			.set-width {
				width: 130rpx;
			}

			.reset-width {
				width: 170rpx;
			}
		}
	}

	.online {
		background-image: linear-gradient(90deg, #1EC862 0%, #13B98F 100%);
	}

	.offline {
		background-image: linear-gradient(90deg, #ff4800 0%, #FF7E00 100%);
	}
</style>