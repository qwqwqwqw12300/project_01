<template>
	<view class="ui-detail">
		<view class="detail-box">
			<image class="device-img" src="/static/images/electron-card.png"></image>
			<view class="device-info">
				<view class="title">
					<text class="name">{{record.name || '未命名设备'}}</text>
					<text class="line"
						:class="[record.onlineFlag === '1' ? 'online' : 'offline']">{{record.onlineFlag === '1' ? '在线' : '离线'}}</text>
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
				<u-icon :name="type === 'set' ? '/static/images/device-set.png': '/static/images/reStart.png'"
					size="44rpx" style="margin-right: 6rpx;" />
				{{ type === 'set'? '设置':'重启设备' }}
			</view>
		</view>
	</view>
</template>

<script>
	import {
		PostRebootDevice
	} from '@/common/http/api';
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
			}
		},
		methods: {
			handleSet() {
				if (this.type === 'set') {
					this.$store.commit('setDeviceInfo', this.record);
					uni.navigateTo({
						url: '/pages/equipment/card-set'
					})
					return
				} else {
					uni.showModal({
						title: '提示',
						content: '是否确认重启设备',
						success: res => {
							if (res.confirm) {
								PostRebootDevice({
									deviceNo: this.record.no,
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
	.ui-detail {
		margin-top: 52rpx;
		padding: 0 32rpx;

		.detail-box {
			background-color: #fff;
			border-radius: 16rpx;
			padding: 26rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.device-img {
				width: 140rpx;
				height: 140rpx;
			}

			.device-info {
				flex: 1;
				align-self: flex-start !important;
				padding-left: 10rpx;
				padding-top: 10rpx;

				.title {
					color: #353535;
					font-size: 32rpx;
					font-weight: 500;
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
					margin-top: 4rpx;
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
