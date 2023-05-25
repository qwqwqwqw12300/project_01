<template>
	<view class="ui-box">
		<view style="padding: 20rpx 30rpx">
			<view class="box-title">
				<text class="device-type">{{ deviceName(msgDetail.deviceType) }}</text>
				<text class="event-date">{{
          $u.timeFormat(msgDetail.createTime, "mm/dd hh:MM:ss") || "--"
        }}</text>
				<view class="event-status">
					<text class="urgent common" v-if="msgDetail.eventLevel == 'urgent'">紧急事件</text>
					<text class="nourgent common" v-else>一般事件</text>
					<text class="nodeal common" v-if="msgDetail.operateFlag == '0'">未处理</text>
					<text class="deal common" v-else>已处理</text>
				</view>
			</view>
			<text class="device-own">
				{{ msgDetail.deviceName || "未命名设备" }}
			</text>
			<view class="content">
				<text>
					{{msgDetail.content || '--'}}
				</text>
	<!-- 			<u-read-more :showHeight="60" :toggle="true" closeText="展开" openText="关闭"
					@open="handleRead(msgDetail.msgId)">
					<rich-text :nodes="$u.trim(msgDetail.content, 'all') || '--'"></rich-text>
				</u-read-more> -->
				<!-- 设备张三的雷达监测到有人摔倒、请及时处理 -->
			</view>
		</view>
		<view class="action">
			<template v-if="msgDetail.eventLevel == 'urgent'">
				<view class="detail" v-if="msgDetail.operateFlag == '0'" @click="handleRead(msgDetail.msgId)">我来处理
				</view>
				<view class="detail disabled" v-else>我已处理</view>
				<view class="phone" @click="handleCall(msgDetail.phone)" v-if="msgDetail.operateFlag == '0'">
					<u-icon name="phone" color="white" size="22"></u-icon><text style="margin-left: 8rpx">他人处理</text>
				</view>
				<view class="phone opacity" v-else>
					<u-icon name="phone" color="white" size="22"></u-icon><text style="margin-left: 8rpx">他人处理</text>
				</view>
			</template>
			<template v-else>
				<view class="detail" v-if="msgDetail.operateFlag == '0'" @click="handleRead(msgDetail.msgId)">暂未阅读
				</view>
				<view class="detail disabled" v-else>我已阅读</view>
			</template>
		</view>
	</view>
</template>

<script>
	import {
		PostSetMsgInfo
	} from '@/common/http/api';
	export default {
		props: {
			msgInfo: {
				type: Object,
				default: () => {},
			},
		},
		data() {
			return {
				msgDetail: this.msgInfo,
			}
		},
		computed: {
			deviceName() {
				return (type) => {
					return {
						0: '跌倒检测器',
						1: '电子牵挂卡',
						2: '4G健康手表'
					} [type]
				}
			}
		},
		mounted() {},
		methods: {
			/**
			 * 单条用户信息已读
			 */
			handleRead(msgId) {
				uni.showModal({
					title: '提示',
					content: `是否确认处理？`,
					success: res => {
						if (res.confirm) {
							PostSetMsgInfo({
								msgId,
								msgFlag: '1'
							}).then(res => {
								uni.$u.toast('已处理登记成功')
								this.msgDetail.operateFlag = '1'
							})
						}
					}
				});
			},
			/**
			 * 点击联系人
			 */
			handleCall(phoneNumber) {
				// if (!this.contactsList.length) return uni.$u.toast('您暂未设置联系人')
				this.$emit('call')
			},
		}
	}
</script>

<style lang="scss" scoped>
	.ui-box {
		background: #ffffff;
		box-shadow: 0px 2px 20px 0px rgba(220, 205, 180, 0.5);
		border-radius: 20rpx;
		margin-bottom: 30rpx;
		// padding: 20rpx 30rpx;

		.box-title {
			height: 60rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.device-type {
				font-size: 30rpx;
				color: #888888;
			}

			.event-date {
				flex: 1;
				padding-left: 30rpx;
				font-size: 28rpx;
				color: #888888;
			}

			.event-status {
				display: flex;
				align-items: center;

				.common {
					font-size: 22rpx;
					color: #ffffff;
					padding: 4rpx 12rpx;
					border-radius: 4px;
					margin: 0 6rpx;
				}

				.urgent {
					background-color: #ef3b45;
				}

				.nourgent {
					background-color: #1dc666;
				}

				.nodeal {
					background-color: #f8e6e6;
					color: #ac0000 !important;
				}

				.deal {
					background-color: #e6f8ee;
					color: #00ac55 !important;
				}
			}
		}

		.device-own {
			margin-top: 10rpx;
			font-size: 36rpx;
			color: #353535;
			font-weight: 550;
			line-height: 50rpx;
		}

		.content {
			padding: 14rpx 0;
			font-size: 28rpx;
			color: #353535;
			// text{
			// 	text-indent: 20rpx;
			// }
			// letter-spacing: 0;
			// text-align: left;
			// font-weight: 500;
			// overflow: hidden;
			// text-overflow: ellipsis;
			// display: -webkit-box;
			// -webkit-line-clamp: 2;
			// -webkit-box-orient: vertical;
		}

		.action {
			border-top: 1px solid rgba(0, 0, 0, 0.1);
			margin-top: 30rpx;
			height: 90rpx;
			line-height: 90rpx;
			display: flex;

			.detail {
				flex: 1;
				background-color: #fff;
				color: #5e85f8;
				text-align: center;
				border-radius: 0 0 0 20rpx;

				&.disabled {
					color: #888888;
				}
			}

			.opacity {
				opacity: 0.4;
			}

			.phone {
				flex: 1;
				display: flex;
				justify-content: center;
				align-items: center;
				background-image: linear-gradient(90deg, #ffb24d 0%, #fd913b 100%);
				// text-align: center;
				border-radius: 0 0 20rpx 0 !important;
				color: #fff;
			}
		}
	}
</style>