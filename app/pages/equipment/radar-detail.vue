<template>
	<app-body>
		<view class="ui-detail">
			<image class="ui-detail-icon" src="../../static/images/leida-nm.png" mode=""></image>
			<view class="ui-detail-device">
				<text class="title">{{deviceInfo.name || '未命名设备'}}</text>
				<view class="info">
					<text class="position">{{deviceInfo.location || '--'}}</text>
					<view class="status">
						<u-icon :name="deviceInfo.onlineFlag === '1' ? 'wifi' : 'wifi-off'"
							:color="deviceInfo.onlineFlag === '1' ? '#0dab1c' : '#ff4800'" size="48rpx"></u-icon>
						<!-- <image src="../../static/images/online.png"></image> -->
						<text
							:style="{color: deviceInfo.onlineFlag === '1' ? '#0dab1c' : '#ff4800'}">{{deviceInfo.onlineFlag === '1' ? '在线' : '离线'}}</text>
					</view>
				</view>
			</view>
			<view class="ui-icon">
				<u-icon name="setting-fill" @click="goSet" size="30" color="#FF7E00" class="ui-detail-map">
				</u-icon>
			</view>

		</view>
		<view class="ui-msg-list">
			<msg-list @navClick="onNavClick" @onRefresh="onRefresh" :list="messageList"
				srollHeight="calc(100vh - var(--window-bottom) - 600rpx - var(--status-bar-height))"></msg-list>
		</view>
	</app-body>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		getMessage,
		PostMessageDeatil
	} from '../../common/http/api';
	export default {
		data() {
			return {
				list1: [{
					name: '未读',
				}, {
					name: '已读',
				}],
				/**消息列表**/
				messageList: [],
				navActive: 0,
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			})
		},
		mounted() {
			this.getMessage();
		},
		methods: {
			getMessage() {
				return new Promise(resolve => {
					const {
						type,
						familyId,
						deviceId
					} = this.deviceInfo;
					getMessage({
						deviceType: type,
						familyId,
						deviceId,
						readFlag: this.navActive,
						eventlevel: ''
					}).then(res => {
						this.messageList = res.rows || [];
						resolve(this.messageList);
					})
				});
			},

			/**
			 * 上拉刷新
			 * @param {Object} $e
			 */
			onRefresh(cb) {
				this.getMessage().then(res => cb());
			},

			/**
			 * tab切换
			 */
			onNavClick(index) {
				this.navActive = index;
				this.getMessage();
			},

			goSet() {
				uni.navigateTo({
					url: '/pages/myself/position-setting'
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.wd-box {
		border-radius: 14rpx;
		margin: 0 46rpx;
		background-color: #EBEBEB;
	}

	.ui-msg-list {
		padding: 0 46rpx;
	}

	.ui-detail {
		@extend .wd-box;
		margin-top: 40rpx;
		padding: 40rpx;
		position: relative;
		display: flex;

		.ui-icon {
			// width: 200rpx;
			display: flex;
			flex-direction: row;

			.ui-detail-map {
				position: absolute;
				right: 40rpx;
				bottom: 30rpx;
			}
		}

		// justify-content: space-between;
		.ui-detail-icon {
			width: 180rpx;
			height: 180rpx;
		}

		.ui-detail-device {
			margin-left: 40rpx;
			display: inline-block;
			vertical-align: top;

			.title {
				line-height: 44rpx;
				font-size: 36rpx;
				font-weight: 550;
			}

			.info {
				margin-top: 16rpx;
				// width: 220rpx;
				display: flex;
				justify-content: space-between;

				.position {
					font-size: 32rpx;
					font-weight: 550;
				}

				.status {
					display: flex;

					image {
						width: 40rpx;
						height: 40rpx;
					}

					text {
						font-size: 32rpx;
						font-weight: 550;
						color: #1D8D28;
						margin-left: 6rpx;
					}
				}
			}
		}

		.ui-detail-map {
			position: absolute;
			width: 50rpx;
			height: 60rpx;
			right: 20rpx;
			bottom: 20rpx;
		}
	}
</style>
