<!--  手表设备设置卡片 -->
<template>
	<view class="swiper-item">
		<view class="item-box">
			<image class="img" src="/static/images/watch-device.png"></image>
			<view class="device-info">
				<view class="title">
					<text class="name">{{record.name || '未命名设备'}}</text>
					<text class="line"
						:class="[record.onlineFlag === '1' ? 'online' : 'offline']">{{ getOnlineStatus }}</text>
				</view>
				<view class="status">
					<view class="status-block">
						<template v-if="record.currentStatus === '0'">
							<u-icon name="/static/images/link-sucess.png" size="20" style="margin-right: 6rpx;" />
							<text>已连接</text>
						</template>
						<template v-else>
							<u-icon name="/static/images/link-false.png" size="20" style="margin-right: 6rpx;" />
							<text>未连接</text>
						</template>
					</view>
					<view class="status-block" style="margin-left:14rpx">
						<u-icon name="/static/images/electricity.png" size="20" style="margin-right: 4rpx;" />
						<text>电量:{{ record.currentPower || '--' }}</text>
					</view>
				</view>
				<view class="time">
					最近更新时间：{{ record.updateTime || '----' }}
				</view>
			</view>
			<view class="device-set" @click="jumpUrl">
				<u-icon name="/static/images/device-set.png" size="44rpx" style="margin-right: 4rpx;">
				</u-icon>配置
			</view>
		</view>
	</view>
</template>

<script>
	import {
		env
	} from "@/config/env.js";
	export default {
		props: {
			record: {
				type: Object,
				default: () => {
					return {}
				}
			},
		},
		computed: {
			getOnlineStatus() {
				return this.record.onlineFlag ? {
					0: '离线',
					1: '在线',
					2: '休眠',
				} [this.record.onlineFlag] : '离线'
			}
		},
		data() {
			return {

			}
		},
		methods: {
			jumpUrl() {
				if (this.record.onlineFlag === '') {
					return uni.navigateTo({
						url: '/pages/watch/add-watch'
					})
				}
				if (this.record.shareFlag == 0 && env.mode === 'prod') return uni.$u.toast('当前设备属于分享设备，不能配置')
				if (this.record.onlineFlag != 1) return uni.$u.toast('您的设备已离线，暂时无法进行配置')
				this.$store.commit('setDeviceInfo', this.record)
				uni.navigateTo({
					url: '/pages/watch/watch-set/watch-set'
				})
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
				padding-top: 18rpx;

				.title {
					color: #353535;
					font-size: 36rpx;
					font-weight: 550;
					display: flex;
					align-items: center;

					.name {
						max-width: 240rpx;
						white-space: nowrap;
						text-overflow: ellipsis;
						overflow: hidden;
					}

					.line {
						margin-left: 30rpx;
						color: #fff;
						font-size: 22rpx;
						padding: 6rpx 12rpx;
						border-radius: 4px;
					}

					.online {
						background-image: linear-gradient(90deg, #1EC862 0%, #13B98F 100%);
					}

					.offline {
						background-image: linear-gradient(90deg, #ff4800 0%, #FF7E00 100%);
					}
				}

				.status {
					// height: 60rpx;
					margin-top: 14rpx;
					display: flex;
					// align-items: center;
					font-size: 26rpx;
					color: #888888;

					.status-block {
						display: flex;
						align-items: center;
					}
				}

				.time {
					position: absolute;
					margin-top: 14rpx;
					font-size: 24rpx;
					color: #888888;
				}
			}

			.device-set {
				width: 120rpx;
				display: flex;
				color: #FEAE43;
				font-size: 30rpx;
				font-weight: 400;
			}
		}
	}
</style>