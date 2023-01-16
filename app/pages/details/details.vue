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
						<uni-data-select v-model="eventInfo.familyId" :clear="false" :localdata="getFamilyList">
						</uni-data-select>
						<!-- 	<view class="ui-icon"><u-icon name="arrow-down-fill" color="#414141" size="30rpx"></u-icon></view> -->
					</view>
				</view>
				<view class="ui-list">
					<text>开始时间:</text>
					<view class="ui-date active" @click="openDate('start')">
						<text>{{ eventInfo.startDate }}</text>
						<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
					</view>
				</view>
				<view class="ui-list">
					<text>结束时间:</text>
					<view class="ui-date active" @click="openDate('end')">
						<text>{{ eventInfo.endDate }}</text>
						<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
					</view>
				</view>
				<view class="ui-list">
					<text>设备类型:</text>
					<view class="ui-select">
						<uni-data-select v-model="eventInfo.deviceType" :clear="false" :localdata="typeRang">
						</uni-data-select>
					</view>
				</view>
				<view class="ui-list">
					<text>事件类型:</text>
					<view class="ui-select">
						<uni-data-select v-model="eventInfo.eventlevel" :clear="false" :localdata="eventRang">
						</uni-data-select>
					</view>
				</view>
				<view class="ui-list">
					<text>已读标识:</text>
					<view class="ui-select">
						<uni-data-select v-model="eventInfo.readFlag" :clear="false" :localdata="operateRang">
						</uni-data-select>
					</view>
				</view>
			</view>
			<!-- /筛选框 -->
			<view class="ui-msg-list">
				<msg-list @onRefresh="onRefresh" :needNav="false" :list="messageList"
					srollHeight="calc(100vh - var(--window-bottom) - 730rpx - var(--status-bar-height))"></msg-list>
			</view>
			<u-calendar @close="dateClose" :monthNum="13" :maxDate="dateHandle.max" :minDate="dateHandle.min"
				:show="dateHandle.show" @confirm="dateConfirm"></u-calendar>
		</view>
	</app-body>
</template>

<script>
	import {
		getEventList,
		getFamilgetMessage,
		getMessage,
		selectEventInfo
	} from '../../common/http/api';
	import {
		mapState
	} from 'vuex';
	import {
		rejects
	} from 'assert';
	const dateTime = new Date();
	export default {
		data() {
			return {
				familyRange: [{
					value: '',
					text: '全部',
				}],
				typeRang: [{
						value: '',
						text: '全部'
					},
					{
						value: 0,
						text: '雷达波'
					},
					{
						value: 1,
						text: '监控设备'
					}
				],
				eventRang: [{
						value: '',
						text: '全部'
					},
					{
						value: 0,
						text: '重要事件'
					},
					{
						value: 1,
						text: '普通事件'
					}
				],
				operateRang: [{
						value: '',
						text: '全部'
					},
					{
						value: 0,
						text: '已读'
					},
					{
						value: 1,
						text: '未读'
					}
				],
				/**日期控制**/
				dateHandle: {
					/**是否展示**/
					show: false,
					/**日期类型**/
					type: 'start',
					max: dateTime.getTime(),
					min: new Date(new Date().setFullYear(dateTime.getFullYear() - 1)).getTime()
				},
				/**事件信息**/
				eventInfo: {
					/**家庭id**/
					familyId: '',
					/**开始时间**/
					startDate: '',
					/**结束时间**/
					endDate: '',
					/**事件等级**/
					eventlevel: '',
					/**是否已读**/
					readFlag: '',
					/**设备类型**/
					deviceType: ''
				},
				/**消息列表**/
				messageList: []
			};
		},
		computed: {
			...mapState({
				getFamilyList: state => {
					const list = state.familyList;
					return list.map(ele => ({
						value: ele.familyId,
						text: ele.name
					}));
				}
			})
		},
		watch: {
			eventInfo: function(val) {
				console.log(1212);
				this.selectEventInfo();
			},
		},
		mounted() {
			const date = new Date();
			Object.assign(this.eventInfo, {
				endDate: uni.$u.timeFormat(date, 'yyyy-mm-dd'),
				startDate: uni.$u.timeFormat(date.setDate(date.getDate() - 3), 'yyyy-mm-dd'),
				familyId: this.getFamilyList[0].value
			});
			this.selectEventInfo();
		},
		methods: {
			/**
			 * 日期确认
			 */
			dateConfirm(date) {
				this.dateClose();
				if (this.dateHandle.type === 'start') {
					this.eventInfo.startDate = date[0];
				} else {
					this.eventInfo.endDate = date[0];
				}
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
			},
			/**
			 * 查询设备消息
			 */
			selectEventInfo() {
				return new Promise((resolve) => {
					const {
						startDate,
						endDate
					} = this.eventInfo;
					getMessage({
						...this.eventInfo,
						startDate: new Date(startDate).getTime(),
						endDate: new Date(endDate).getTime(),
					}).then(res => {
						this.messageList = res.rows || [];
						resolve(this.messageList);
					}, err => resolve())
				});
			},
			/**
			 * 上拉刷新
			 * @param {Object} $e
			 */
			async onRefresh(cb) {
				await this.selectEventInfo();
				cb();
			},
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
