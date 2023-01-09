<!--
* @Author: zhanch
* @Date: 2023-01-06 15:44:29
* @FilePath: /components/msg-list/msg-list.vue
* @Description: 消息列表
-->

<template>
	<view>
		<!-- 已读按钮 -->
		<view class="ui-read active">
			<u-text prefixIcon="checkmark-circle" align="center" :iconStyle="{fontSize: '30rpx', color: '#fff'}"
				color="#fff" size="24rpx" text="全部标记为已读"></u-text>
		</view>
		<!-- /已读按钮 -->
		<!-- 未读事件列表 -->
		<scroll-view scroll-y="true" class="ui-scroll">
			<view class="ui-message">
				<u-icon name="chat" color="#414141" size="40rpx"></u-icon>
				<text>2022/12/26 10:00:00</text>
				<text>xx设备</text>
				<text>人员离开</text>
			</view>
			<!-- /sos事件 -->
			<view class="ui-sos">
				<view>
					<u-icon name="arrow-right" color="#414141" size="40rpx"></u-icon>
					<text>2022/12/26 10:00:00</text>
					<text>xx设备</text>
					<text>人员摔倒</text>
				</view>
				<view class="ui-sos-btn active">
					<u-text prefixIcon="phone" align="center" :block="false" iconStyle="font-size: 40rpx" size="24rpx"
						text="紧急电话"></u-text>
					<text>132222222222</text>
				</view>

			</view>
			<!-- /sos事件 -->
			<view class="ui-message">
				<u-icon name="chat" color="#414141" size="40rpx"></u-icon>
				<text>2022/12/26 10:00:00</text>
				<text>xx设备</text>
				<text>人员离开</text>
			</view>
		</scroll-view>
		<!-- /未读事件列表 -->
	</view>
</template>

<script>
	export default {
		data() {
			return {

			};
		}
	}
</script>

<style lang="scss">
	.ui-read {
		background-color: rgba(0, 0, 0, 0.4);
		width: 100%;
		height: 60rpx;
		line-height: 60rpx;
		border-radius: 10rpx;
	}

	.ui-scroll {
		margin-top: 20rpx;
		height: calc(100vh - (var(--window-bottom) + 610rpx + 100rpx + var(--status-bar-height)));

		.ui-message,
		.ui-sos {
			margin-top: 20rpx;
			border-radius: 10rpx;
			padding: 0 20rpx;
			font-size: 28rpx;
			filter: drop-shadow(7.824rpx 10.382rpx 8rpx rgba(7, 5, 5, 0.08));
			background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
		}

		.ui-message {
			color: #414141;
			display: flex;
			align-items: center;
			min-height: 80rpx;

			&>* {
				margin-left: 15rpx;
				text-align: left;

				// min-width: 100rpx;
				&:nth-child(2) {
					margin-left: 2rpx;
				}
			}
		}

		.ui-sos {
			padding: 20rpx;
			min-height: 200rpx;

			&>view {
				display: flex;
				align-items: center;

				&>* {
					margin-left: 15rpx;
					text-align: left;

					// min-width: 100rpx;
					&:nth-child(2) {
						margin-left: 2rpx;
					}
				}

				&:nth-child(1) {
					margin-bottom: 30rpx;
				}
			}

			.ui-sos-btn {
				flex-direction: column;
				justify-content: center;
				margin: 0 auto;
				padding: 20rpx;
				width: 545rpx;
				height: 120rpx;
				border-radius: 10rpx;
				background-image: linear-gradient(-43deg, #fdc92d 0%, #ffe383 100%);

				&>* {
					flex: 1;
					font-size: 24rpx;
					color: #414141;
				}
			}
		}
	}
</style>
