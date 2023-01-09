<!--
* @Author: zhanch
* @Date: 2022-12-28 11:01:47
* @FilePath: /pages/details/details.vue
* @Description: 信息详情
-->

<template>
	<app-body :hideTitle="true">
		<view id="details">
			<!-- 筛选框 -->
			<view class="ui-screen">
				<view class="ui-list">
					<text>我的家庭:</text>
					<view class="ui-select">
						<uni-data-select v-model="value" :clear="false" :localdata="range"></uni-data-select>
						<!-- 	<view class="ui-icon"><u-icon name="arrow-down-fill" color="#414141" size="30rpx"></u-icon></view> -->
					</view>
				</view>
				<view class="ui-list">
					<text>开始时间:</text>
					<view class="ui-date active" @click="openDate('start')">
						<text>2022/12/28</text>
						<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
					</view>
				</view>
				<view class="ui-list">
					<text>结束时间:</text>
					<view class="ui-date active" @click="openDate('end')">
						<text>2022/12/28</text>
						<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
					</view>
				</view>
				<view class="ui-list">
					<text>设备类型:</text>
					<view class="ui-select">
						<uni-data-select v-model="value" :clear="false" :localdata="range"></uni-data-select>

					</view>
				</view>
				<view class="ui-list">
					<text>事件类型:</text>
					<view class="ui-select">
						<uni-data-select v-model="value" :clear="false" :localdata="range"></uni-data-select>

					</view>
				</view>
				<view class="ui-list">
					<text>已读标识:</text>
					<view class="ui-select">
						<uni-data-select v-model="value" :clear="false" :localdata="range"></uni-data-select>

					</view>
				</view>
			</view>
			<!-- /筛选框 -->
			<view class="ui-msg-list">
				<msg-list :needNav="false"
					srollHeight="calc(100vh - var(--window-bottom) - 730rpx - var(--status-bar-height))"></msg-list>
			</view>
			<u-calendar @close="dateClose" :show="dateHandle.show" @confirm="dateConfirm"></u-calendar>
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {
				value: 0,
				range: [{
					value: 0,
					text: '设备一'
				}, {
					value: 1,
					text: '设备二'
				}, {
					value: 2,
					text: '设备三'
				}],
				/**日期控制**/
				dateHandle: {
					/**是否展示**/
					show: false,
					/**日期类型**/
					type: 'start'
				},

			};
		},
		methods: {
			/**
			 * 日期确认
			 */
			dateConfirm(date) {
				this.dateClose();
				console.log(date);
			},

			dateClose() {
				this.dateHandle.show = false;
			},
			/**
			 * 开启日期 
			 **/
			openDate(type) {
				Object.assign(this.dateHandle, {
					type,
					show: true
				});
			}
		}
	};
</script>

<style lang="scss">
	view {
		box-sizing: border-box;
	}

	#details {
		display: inline-block;
		overflow: hidden;
		box-sizing: border-box;
		// margin-top: 60rpx;
		padding: 60rpx 57rpx 0 57rpx;
		text-align: center;
		width: 100%;

		.ui-screen {
			position: relative;
			z-index: 5;
			height: 550rpx;
			width: 100%;
			padding: 30rpx 40rpx;
			border-radius: 10rpx;
			filter: drop-shadow(7.824px 10.382px 8px rgba(7, 5, 5, 0.08));
			background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);

			.ui-list {
				margin-top: 10rpx;
				width: 100%;
				display: inline-flex;
				justify-content: space-between;
				align-items: center;
				font-size: 28rpx;
				color: #414141;
			}

			.ui-select,
			.ui-date {
				position: relative;
				height: 70rpx;
				width: 68%;
				text-align: left;
				border-radius: 10px;
				background-color: #dcdcdc;
				display: flex;
			}

			.ui-date {
				align-items: center;
				justify-content: space-between;
				padding: 0 8rpx 0 20rpx;
			}

			.ui-icon {
				position: absolute;
				right: 10rpx;
				top: 50%;
				transform: translateY(-50%);
				pointer-events: none;
			}
		}

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
	}
</style>
