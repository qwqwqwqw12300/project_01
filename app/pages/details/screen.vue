<!--
* @Author: zhanch
* @Date: 2023-02-16 15:13:52
* @FilePath: /pages/details/screen/screen.vue
* @Description: 消息详情筛选
-->

<template>
	<app-body :bg="false">
		<view class="ui-body">
			<!-- 选择时间 -->
			<view class="ui-section">
				<view class="ui-tip">选择时间</view>
				<view class="ui-date-select">
					<view class="ui-text active" @click="openDate('start')">
						{{(screenInfo.startDate && $u.timeFormat(screenInfo.startDate, 'yyyy/mm/dd')) || '选择开始日期'}}
					</view>
					<text>至</text>
					<view class="ui-text active" @click="openDate('end')">
						{{(screenInfo.endDate && $u.timeFormat(screenInfo.endDate, 'yyyy/mm/dd')) || '选择结束日期'}}
					</view>
				</view>
			</view>
			<!-- /选择时间 -->
			<!-- 设备类型 -->
			<view class="ui-section">
				<view class="ui-tip">设备类型</view>
				<view class="ui-list">
					<view @click="select(item.value, 'deviceType')"
						:class="{select : screenInfo.deviceType == item.value}" v-for="(item, index) of typeRang"
						:key="'type' + index">
						<image v-if="item.icon" :src="'../../static/images/'+ item.icon +'.png'" mode=""></image>
						{{item.text}}
					</view>
				</view>
			</view>
			<!-- /设备类型 -->
			<!-- 事件类型 -->
			<view class="ui-section">
				<view class="ui-tip">事件类型</view>
				<view class="ui-list">
					<view @click="select(item.value, 'eventlevel')"
						:class="{select : screenInfo.eventlevel == item.value}" v-for="(item, index) of eventRang"
						:key="'event' + index">{{item.text}}</view>
				</view>
			</view>
			<!-- /事件类型 -->
			<!-- 处理标识 -->
			<view class="ui-section">
				<view class="ui-tip">处理标识</view>
				<view class="ui-list">
					<view @click="select(item.value, 'readFlag')" :class="{select : screenInfo.readFlag == item.value}"
						v-for="(item, index) of operateRang" :key="'operateRang' + index">{{item.text}}
					</view>
				</view>
			</view>
			<!-- /处理标识 -->
			<view class="ui-btn-list">
				<view class="ui-btn active" @click="reset">重置</view>
				<view class="ui-btn active" @click="save">确定</view>
			</view>
		</view>
		<u-calendar @close="dateClose" :monthNum="13" :maxDate="dateHandle.max" :minDate="dateHandle.min"
			:show="dateHandle.show" @confirm="dateConfirm"></u-calendar>
	</app-body>

</template>

<script>
	import {
		INIT_SELECT
	} from '../../config/db';
	export default {
		data() {
			return {
				/**筛选信息**/
				screenInfo: {
					...INIT_SELECT
				},
				/**设备列表**/
				typeRang: [{
						value: '',
						text: '全部'
					},
					{
						value: '0',
						icon: 'leida-nm',
						text: 'vayyar'
					},
					{
						value: '1',
						icon: 'dzqgk',
						text: '电子牵挂卡'
					},
					{
						value: '2',
						icon: 'watch',
						text: '4g智能手表'
					}

				],
				/**事件列表**/
				eventRang: [{
						value: '',
						text: '全部'
					},
					{
						value: 'urgent',
						text: '紧急事件'
					},
					{
						value: 'normal',
						text: '普通事件'
					}
				],
				/**已读列表**/
				operateRang: [{
						value: '',
						text: '全部'
					},
					{
						value: '1',
						text: '已处理'
					},
					{
						value: '0',
						text: '未处理'
					}
				],
				/**日期控制**/
				dateHandle: {
					/**是否展示**/
					show: false,
					/**日期类型**/
					type: 'start',
					max: new Date().getTime(),
					min: new Date(new Date().setFullYear(new Date().getFullYear() - 1)).getTime()
				},
			};
		},
		onLoad() {
			this.init();
		},
		methods: {
			/**
			 * 点击选中
			 */
			select(value, type) {
				this.screenInfo[type] = value;
			},

			/**
			 * 初始化
			 */
			init() {
				this.$setCache('detailsScreenResult', null);
				Object.assign(this.screenInfo, this.$getCache('detailsScreenInfo') || {
					...INIT_SELECT
				})
			},
			/**
			 * 重置
			 */
			reset() {
				this.screenInfo = {
					...INIT_SELECT
				};
			},
			/**
			 * 保存
			 */
			save() {
				const {
					startDate,
					endDate
				} = this.screenInfo;
				if (startDate > endDate) return uni.$u.toast('开始时间不能早于结束时间');
				uni.$emit('detailsScreenResult', this.screenInfo);
				uni.navigateBack();
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
			 * 日期确认
			 */
			dateConfirm(date) {
				this.dateClose();
				if (this.dateHandle.type === 'start') {
					this.screenInfo.startDate = new Date(date[0] + ' 00:00:00').getTime();
				} else {
					this.screenInfo.endDate = new Date(date[0] + ' 23:59:59').getTime();
				}
			},

			dateClose() {
				this.dateHandle.show = false;
			},

		},

		onBackPress(event) {
			uni.$off('detailsScreenResult');
		}
	}
</script>

<style lang="scss">
	.ui-body {
		padding-bottom: 100rpx;
	}

	.ui-section {
		margin-top: 20rpx;
		background: #fff;
		font-size: 28rpx;

		.ui-tip {
			padding: 18rpx 32rpx;
			font-size: 24rpx;
			border: 1rpx solid rgba(235, 235, 235, 1);
		}
	}

	.ui-date-select {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx 32rpx;

		.ui-text {
			width: 300rpx;
			height: 60rpx;
			line-height: 60rpx;
			text-align: center;
			background: #F7F7F7;
			border: 1rpx solid rgba(136, 136, 136, 1);
			border-radius: 8rpx;
		}
	}


	.ui-list {
		padding: 0 32rpx 30rpx 32rpx;
		min-height: 88rpx;
		zoom: 1;

		>view {
			margin-top: 20rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: row;
			float: left;
			background: #F7F7F7;
			border-radius: 8px;
			min-width: calc(33% - 22rpx);
			height: 88rpx;
			color: #353535;

			&:nth-child(2) {
				margin: 20rpx 30rpx 0 30rpx;
			}

			&.select {
				color: #fff;
				background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
			}

			image {
				width: 48rpx;
				height: 48rpx;
			}
		}

		&::after {
			content: '';
			display: block;
			height: 0;
			clear: both;
		}
	}

	.ui-btn-list {
		display: flex;
		width: 100%;
		position: fixed;
		bottom: 0;

		.ui-btn {
			height: 100rpx;
			flex: 1;
			display: flex;
			align-items: center;
			justify-content: center;

			&:nth-child(1) {

				background: #fff;
				color: #FEAE43;
			}

			&:nth-child(2) {

				background: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
				color: #fff;
			}
		}
	}
</style>
