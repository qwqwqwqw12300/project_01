<!--
* @Author: zhanch
* @Date: 2023-03-08 17:57:24
* @FilePath: /pages/myself/device-manage/components/dzqgk-card.vue
* @Description: zhanch
-->


<template>
	<view class="wd-menu-item">
		<view class="item-box" @click="edit(device)">
			<view class="device-status">
				<text class="online" v-if="device.onlineFlag == 1">在线</text>
				<text class="offline" v-else>离线</text>
			</view>
			<view class="device-info">
				<image src="/static/images/dzqgk.png"></image>
				<view class="detail">
					<text class="name">{{ device.name || '未命名设备' }}</text>
					<text class="position">
						{{ (device.humanName || '未绑定' )}}
					</text>
				</view>
			</view>
			<view class="device-action">
				<text class="danger" @tap.stop="onDelete(device.deviceId)">删除</text>
				<text class="warn" v-if="!device.humanId" @click.stop="binding(device)">绑定</text>
				<text class="orange" v-else @click.stop="unbinding(device)">解绑</text>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		PostCareCardUnBind,
		PostDeviceDel,
		relDevice
	} from '../../../../common/http/api';
	export default {
		name: 'dzqgk-card',
		props: {
			device: Object
		},
		data() {
			return {

			};
		},
		methods: {
			/**
			 * 删除设备
			 */
			onDelete(deviceId) {
				uni.showModal({
					title: '提示',
					content: '是否确认删除该设备',
					success: res => {
						if (res.confirm) {
							PostDeviceDel({
								deviceId
							}).then(res => {
								uni.$u.toast('删除成功');
								setTimeout(() => {
									this.$emit('change');
								}, 1000);
							})
						}
					}
				});
			},
			/**
			 * 绑定
			 */
			binding(item) {
				const {
					deviceId
				} = item;
				this.$emit('bind', deviceId)
			},

			/**
			 * 解绑
			 */
			unbinding({
				deviceId,
				humanId
			}) {
				uni.showModal({
					title: '提示',
					content: '是否和人员解除绑定',
					success: res => {
						if (res.confirm) {
							PostCareCardUnBind({
								deviceId,
								humanId
							}).then(res => {
								uni.$u.toast(res.msg);
								setTimeout(() => {
									this.$emit('change');
								}, 100);
							})
						}
					}
				});
			},
			/**
			 * 跳转设备管理
			 */
			edit(item) {
				this.$store.commit('setDeviceInfo', item);
				// this.$setCache('setDevice', item);
				uni.navigateTo({
					url: '/pages/equipment/card-set'
				})
			},
		},
	}
</script>

<style lang="scss">
	.wd-menu-item {
		background: #FFFFFF;
		border-radius: 16px;
		width: 48%;
		margin: 0 1%;
		margin-bottom: 20rpx;
		position: relative;

		.item-box {
			padding: 30rpx 20rpx;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;

			.device-status {
				position: absolute;
				top: 12rpx;
				right: 12rpx;

				text {
					color: #FFFFFF;
					font-size: 20rpx;
					padding: 4rpx 8rpx;
					border-radius: 4px;
				}
			}

			.online {
				background-image: linear-gradient(90deg, #1EC862 0%, #13B98F 100%);
			}

			.offline {
				background: #D4D4D4 !important;
			}

			.device-info {
				// width: 100%;
				margin-top: 10rpx;
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
					display: inline-flex;
					flex-direction: column;

					text {
						display: inline-block;
					}

					.name {
						width: 150rpx;
						font-size: 32rpx;
						color: #353535;
						font-weight: 500;
					}

					.position {
						margin-top: 10rpx;
						font-size: 26rpx;
						color: #888888;
					}
				}
			}

			.device-action {
				width: 100%;
				margin-top: 20rpx;
				display: flex;
				justify-content: space-around;
				align-items: center;

				.danger {
					color: #E95656;
				}

				.warn {
					border: 1px solid rgba(254, 174, 67, 1);
					border-radius: 24px;
					color: #FEAE43;
					padding: 6rpx 20rpx;
					font-size: 26rpx;
				}

				.orange {
					padding: 6rpx 20rpx;
					font-size: 26rpx;
					color: #fff;
					background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
					border-radius: 24px;
				}

				text {
					font-size: 26rpx
				}
			}
		}
	}
</style>
