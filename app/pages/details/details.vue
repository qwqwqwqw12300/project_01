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
					<view class="ui-date active" @click="handleSelect('family')">
						<text>{{ eventInfo.familyName || '全部'}}</text>
						<u-icon name="arrow-down" size="40rpx"></u-icon>
						<!-- 	<uni-data-select v-model="eventInfo.familyId" :clear="false" :localdata="getFamilyList">
						</uni-data-select> -->
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
					<view class="ui-date active" @click="handleSelect('device')">
						<text>{{ eventInfo.deviceTypeName || '全部'}}</text>
						<u-icon name="arrow-down" size="40rpx"></u-icon>
						<!-- 		<uni-data-select v-model="eventInfo.deviceType" :clear="false" :localdata="typeRang">
						</uni-data-select> -->
					</view>
				</view>
				<view class="ui-list">
					<text>事件类型:</text>
					<view class="ui-date active" @click="handleSelect('event')">
						<text>{{ eventInfo.eventlevelName || '全部'}}</text>
						<u-icon name="arrow-down" size="40rpx"></u-icon>
						<!-- 	<uni-data-select v-model="eventInfo.eventlevel" :clear="false" :localdata="eventRang">
						</uni-data-select> -->
					</view>
				</view>
				<view class="ui-list">
					<text>已读标识:</text>
					<view class="ui-date active" @click="handleSelect('flag')">
						<text>{{ eventInfo.readFlagName || '全部'}}</text>
						<u-icon name="arrow-down" size="40rpx"></u-icon>
						<!-- 	<uni-data-select v-model="eventInfo.readFlag" :clear="false" :localdata="operateRang">
						</uni-data-select> -->
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
			<u-picker :key="index" @cancel="selectShow = false" @confirm="onConfirm" :show="selectShow"
				:columns="selectObj[currentSelect]" keyName="text"></u-picker>
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
			const typeRang = [
				[{
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
				]
			]
			const eventRang = [
				[{
						value: '',
						text: '全部'
					},
					{
						value: 'urgent',
						text: '重要事件'
					},
					{
						value: 'normal',
						text: '普通事件'
					}
				]
			]

			const operateRang = [
				[{
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
				]
			]
			return {

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
					/**家庭名称**/
					familyName: '',
					/**开始时间**/
					startDate: '',
					/**结束时间**/
					endDate: '',
					/**事件等级**/
					eventlevel: '',
					/**事件等级名称**/
					eventlevelName: '',
					/**是否已读**/
					readFlag: '',
					/**是否已读名称**/
					readFlagName: '',
					/**设备类型**/
					deviceType: '',
					/**设备类型名称**/
					deviceTypeName: ''
				},
				/**消息列表**/
				messageList: [],
				/* 下拉框visible */
				selectShow: false,
				selectObj: {
					family: [],
					device: typeRang,
					event: eventRang,
					flag: operateRang,
				},
				currentSelect: '',
				operateRang,
				index: 0,
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
			eventInfo: {
				handler: function(val) {
					this.selectEventInfo();
				},
				deep: true
			}
		},
		mounted() {
			const date = new Date();
			Object.assign(this.eventInfo, {
				endDate: uni.$u.timeFormat(date, 'yyyy-mm-dd'),
				startDate: uni.$u.timeFormat(date.setDate(date.getDate() - 3), 'yyyy-mm-dd'),
				familyId: this.getFamilyList[0].value
			});
			this.selectObj.family = [
				[{
					text: '全部',
					value: ''
				}, ...this.getFamilyList]
			]
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
				return new Promise(resolve => {
					const {
						startDate,
						endDate
					} = this.eventInfo;
					getMessage({
						...this.eventInfo,
						startDate: new Date(startDate + ' 00:00:00').getTime(),
						endDate: new Date(endDate + ' 23:59:59').getTime(),
					}).then(res => {
						this.messageList = res.rows || [];
						resolve(this.messageList);
					}, err => resolve());
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

			/**
			 * 开启下拉选择
			 */
			handleSelect(type) {
				this.index += 1
				this.currentSelect = type
				this.selectShow = true
			},

			/**
			 * 下拉框选择
			 */
			onConfirm(val) {
				const {
					value,
					text
				} = val.value[0]
				switch (this.currentSelect) {
					case 'family':
						this.eventInfo.familyId = value
						this.eventInfo.familyName = text
						break
					case 'device':
						this.eventInfo.deviceType = value
						this.eventInfo.deviceTypeName = text
						break
					case 'event':
						this.eventInfo.eventlevel = value
						this.eventInfo.eventlevelName = text
						break
					case 'flag':
						this.eventInfo.readFlag = value
						this.eventInfo.readFlagName = text
						break
				}
				this.selectShow = false
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
				padding: 0 20rpx 0 20rpx;
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
		}
	}
</style>
