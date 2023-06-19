<template>
	<app-body>
		<view class="ui-card">
			<DeviceSwiper :record="deviceInfo" type="reset"></DeviceSwiper>
		</view>
		<view class="ui-cell">
			<view class="cell-box">
				<u-cell-group>
					<u-cell v-for="(item,index) in cellList" :key="index" size="large" :title="item.title"
						@tap="handleJump(item.url)" :isLink="true" titleStyle="font-size: 15px;color: #303133;">
						<!-- <u-icon slot="icon" size="25" :color="item.icon.color" :name="item.icon.name"></u-icon> -->
					</u-cell>
				</u-cell-group>
			</view>
		</view>
		<view class="ui-button">
			<button @click="unBind">
				解绑
			</button>
		</view>
	</app-body>

</template>

<script>
	import {
		mapState,
	} from 'vuex';
	import {
		PostCareCardUnBind
	} from '@/common/http/api';
	import DeviceSwiper from '@/pages/equipment/device-detail/components/card-swiper.vue'
	export default {
		components: {
			DeviceSwiper,
		},
		data() {
			return {
				cellList: [{
					title: '设备名称',
					url: '/pages/card/name-edit',
				}, {
					title: '通讯录白名单',
					url: '/pages/card/tel-books',
				}, {
					title: '亲情号码',
					url: '/pages/card/kinship-number'
				}, {
					title: '定位频率',
					url: '/pages/card/location-frequency'
				}, {
					title: '设置电子围栏',
					url: '/pages/card/enclosure/enclosure?type=set'
				}, {
					title: '位置守护',
					url: '/pages/card/guard'
				}, {
					title: '时段禁用',
					url: '/pages/card/time-interval'
				}, {
					title: '远程监听',
					url: '/pages/card/monitor'
				}]
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, ),
		},
		mounted() {},
		methods: {
			handleJump(url) {
				uni.navigateTo({
					url
				})
			},
			unBind() {
				uni.showModal({
					title: '提示',
					content: `是否确认解绑？`,
					success: res => {
						if (res.confirm) {
							const {
								humanId,
								deviceId: deviceId
							} = this.deviceInfo
							PostCareCardUnBind({
								humanId,
								deviceId
							}).then(res => {
								uni.$u.toast(res.msg)
								setTimeout(() => {
									uni.switchTab({
										url: '/pages/index/index'
									})
								}, 1000)
							})
						}
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	::v-deep {
		.u-cell .u-line {
			margin: 0px !important;
		}
	}

	.ui-card {
		margin-top: 50rpx;
		padding: 0 26rpx;
	}

	.ui-detail {
		margin-top: 52rpx;
		padding: 0 32rpx;

		.detail-box {
			background-color: #fff;
			border-radius: 16rpx;
			padding: 32rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.device-img {
				width: 150rpx;
				height: 150rpx;
			}

			.device-info {
				flex: 1;
				align-self: flex-start !important;
				padding-left: 10rpx;
				padding-top: 10rpx;

				.title {
					color: #353535;
					font-size: 34rpx;
					font-weight: 500;
					padding-left: 4rpx;
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
				width: 180rpx;
				display: flex;
				color: #FEAE43;
				font-size: 30rpx;
				font-weight: 400;
			}
		}
	}

	.ui-cell {
		margin-top: 20rpx;
		padding: 0 32rpx;

		.cell-box {
			background-color: #fff;
			border-radius: 16rpx;
		}
	}

	.ui-button {
		margin-top: 30rpx;
		padding: 0 32rpx;

		button {
			border-radius: 18rpx;
			color: #FF4645;
			font-size: 32rpx;
			background: #FFFFFF;
		}
	}

	.online {
		background-image: linear-gradient(90deg, #1EC862 0%, #13B98F 100%);
	}

	.offline {
		background-image: linear-gradient(90deg, #ff4800 0%, #FF7E00 100%);
	}
</style>