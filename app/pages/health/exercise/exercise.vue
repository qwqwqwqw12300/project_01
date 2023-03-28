<template>
	<app-body :bodyStyle="{background: '#F7F7F7' }">
		<app-logo text="活动" top="36rpx" iconUrl="/static/images/question.png"></app-logo>
		<view class="ui-date">
			<date-picker @onSelect="onSelect"></date-picker>
		</view>
		<view class="ui-echart">
			<exexcise-echart :type="type"></exexcise-echart>
		</view>
		<view class="day-title">
			<text>日平均数据</text>
		</view>
		<view class="ui-tab">
			<scroll-view class="scroll" scroll-x>
				<block v-for="(item,index) in list" :key="index">
					<view class="scroll-item" @click="handleTab(item.key)"
						:style="{ 'background': key === item.key ? item.selectedBg : '#FFFFFF'}">
						<view class="icon">
							<u-icon size="40rpx" :name="key === item.key ? item.selectedIconPath : item.iconPath ">
							</u-icon>
						</view>
						<view class="num" :style="{ 'color': key === item.key ?  '#FFFFFF':'#353535'}">
							{{ item.num }}
						</view>
					</view>
				</block>
			</scroll-view>
		</view>
		<view class="detail-title">
			详情
		</view>
		<view class="ui-detail">
			<view class="detail-box" v-for="item in 3">
				<view class="box-cell">
					<view class="title">
						20:37 - 20:53 步行
					</view>
					<view class="content">
						<view class="c">
							<u-icon size="30" name="/static/images/bushu.png" />
							<text>97</text>步
						</view>
						<view class="c">
							<u-icon size="40rpx" name="arrow-left" />
							<text>0.1</text>公里
						</view>
						<view class="c">
							<text>0.1</text>千卡
						</view>
					</view>
				</view>
			</view>
		</view>
	</app-body>
</template>

<script>
	import exexciseEchart from './components/erexcise-echart'
	import {
		GetListStepNumByDay,
		GetListStepNumByWeek,
		GetListCalorieByDay,
		GetListCalorieByWeek,
		GetListExerciseDurationByDay,
		GetListExerciseDurationByWeek
	} from '@/common/http/api.js'
	export default {
		components: {
			exexciseEchart,
		},
		data() {
			return {
				key: 'step',
				type: 'date',
				options: {},
				queryData: {
					key: 'step', // 步数 时间 卡路里
					type: '', // week day
					date: '',
					beginDate: '',
					endDate: ''
				},
				list: [{
					key: 'step',
					iconPath: "/static/images/bushu.png",
					selectedIconPath: "/static/images/bushu_white.png",
					selectedBg: "#61AAF7",
					num: 3000
				}, {
					key: 'calories',
					iconPath: "/static/images/xiaohao.png",
					selectedIconPath: "/static/images/xiaohao_white.png",
					selectedBg: "#E43552",
					num: 3077
				}, {
					key: 'time',
					iconPath: "/static/images/timeclock.png",
					selectedIconPath: "/static/images/timeclock_white.png",
					selectedBg: "#69CA5F",
					num: 303
				}]
			}
		},
		created() {

		},
		watch: {
			queryData: {
				handler(newVal, oldVal) {
					if (this.queryData.type === 'date') {
						switch (this.queryData.key) {
							case 'step':
								this.fetchStepNumByDay()
								break;
							case 'calories':
								this.fetchCalorieByDay()
								break;
							case 'time':
								this.fetchExerciseDurationByDay()
								break;
						}
					} else if (this.queryData.type === 'week') {
						switch (this.queryData.key) {
							case 'step':
								this.fetchStepNumByWeek()
								break;
							case 'calories':
								this.fetchCalorieByWeek()
								break;
							case 'time':
								this.fetchExerciseDurationByWeek()
								break;
						}
					}
				},
				deep: true,
				immediate: true
			}
		},
		methods: {
			onSelect(val) {
				console.log(val)
				this.queryData.type = val.type;
				if (val.type === 'date') {
					this.queryData.date = val.value;
				} else if (val.type === 'week') {
					this.queryData.beginDate = val.value[0];
					this.queryData.endDate = val.value[6];
				}
				// val.type === 'date' ? this.handleDate(val) : this.handleWeek(val)
			},
			handleTab(key) {
				console.log(key, 'kkkkkkkk')
				this.key = key;
				this.queryData.key = key;
			},
			handleDate(val) {
				this.dataList = []

				this.dealDay(option)
			},
			dealDay(res) {
				this.options = {
					notMerge: true,
					tooltip: {
						trigger: ''
					},
					grid: {
						left: '0',
						right: '0',
						bottom: '0',
						top: '10',
						containLabel: true
					},
					xAxis: [{
						type: 'time',
						interval: 6 * 3600 * 1000, // 间隔为6小时
						min: new Date(`${res.value + ' 00:00:00'}`), // x轴起始时间
						max: new Date(`${res.value + ' 23:59:59'}`), // x轴结束时间
						boundaryGap: false,
						axisTick: { //坐标轴刻度相关设置。
							show: false,
						},
					}],
					yAxis: [{
						type: "value",
						axisLine: {
							show: false
						},
						axisTick: {
							show: false
						},
						data: [0, 25, 50, 70, 100],
					}],
					series: [{
						data: [
							[new Date('2023-3-28 00:00:00'), '1'],
							[new Date('2023-3-28 06:00:00'), '2'],
							[new Date('2023-3-28 12:00:00'), '3'],
							[new Date('2023-3-28 18:00:00'), '4'],
							[new Date('2023-3-28 23:59:59'), '5'],
						],
						type: 'bar',
						itemStyle: { //---图形形状
							color: '#61AAF7',
							barBorderRadius: [18, 18, 0, 0],
						},
						barWidth: '10', //---柱形宽度
						barCategoryGap: '20%', //---柱形间距
					}, ]
				}
			},
			handleWeek(val) {
				this.dataList = []
				GetListBloodOxygenByWeek({
					deviceId: 240,
					beginDate: val.value[0],
					endDate: val.value[6],
					humanId: '101'
				}).then(res => {
					console.log(res, 'res')
					this.totalList[0].num = res.data.oxMap.avgOx
					this.totalList[1].num = res.data.oxMap.maxOx
					this.totalList[2].num = res.data.oxMap.minOx
					for (let i = 0; i < res.data.oxMap.dataList.length; i++) {
						this.dataList.push([
							res.data.oxMap.dataList[i].time,
							res.data.oxMap.dataList[i].value
						])
					}
					console.log(this.dataList, 'this.dataList')
				})
				this.dealWeek(val)
			},
			dealWeek(res) {
				this.options = {
					notMerge: true,
					tooltip: {
						trigger: ''
					},
					grid: {
						left: '0',
						right: '0',
						bottom: '0',
						top: '10',
						containLabel: true
					},
					xAxis: {
						type: 'category',
						data: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
						axisTick: {
							show: false
						},
					},
					yAxis: {
						type: 'value',
						axisTick: {
							show: false
						},
						axisLine: {
							show: false
						},
						data: [0, 25, 50, 70, 100],
					},
					series: [{
						data: [10, 25, 50, 70, 100, 120, 140],
						type: 'bar',
						itemStyle: { //---图形形状
							color: '#61AAF7',
							barBorderRadius: [0, 0, 0, 0],
						},
						barWidth: '30', //---柱形宽度
					}]
				}
			},
			fetchStepNumByDay() {
				console.log(1)
				const params = {
					deviceId: 240,
					dayTime: this.queryData.date,
					humanId: '1'
				}
				GetListStepNumByDay(params).then(res => {
					console.log(res, 'res')
					// this.dealDay(res.data);
				});
			},
			fetchStepNumByWeek() {
				console.log(2)
				const params = {
					deviceId: 240,
					beginDate: this.queryData.beginDate,
					endDate: this.queryData.endDate,
					humanId: '1'
				}
				GetListStepNumByWeek(params).then(res => {
					console.log(res, 'res')
				});
			},
			fetchCalorieByDay() {
				console.log(3)
				const params = {
					deviceId: 240,
					dayTime: this.queryData.date,
					humanId: '1'
				}
				GetListCalorieByDay(params).then(res => {
					console.log(res, 'res')
				});;
			},
			fetchCalorieByWeek() {
				console.log(4)
				const params = {
					deviceId: 240,
					beginDate: this.queryData.beginDate,
					endDate: this.queryData.endDate,
					humanId: '1'
				}
				GetListStepNumByWeek(params).then(res => {
					console.log(res, 'res')
				});
				GetListCalorieByWeek();
			},
			fetchExerciseDurationByDay() {
				console.log(5)
				const params = {
					deviceId: 240,
					dayTime: this.queryData.date,
					humanId: '1'
				}
				GetListExerciseDurationByDay(params).then(res => {
					console.log(res, 'res')
				});
			},
			fetchExerciseDurationByWeek() {
				console.log(6)
				const params = {
					deviceId: 240,
					beginDate: this.queryData.beginDate,
					endDate: this.queryData.endDate,
					humanId: '1'
				}
				GetListStepNumByWeek(params).then(res => {
					console.log(res, 'res')
				});
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-date {
		margin-top: 64rpx;
	}

	.ui-echart {}

	.day-title {
		margin-top: 30rpx;
		height: 50rpx;
		line-height: 50rpx;
		padding: 0 32rpx;
		font-size: 32rpx;
		color: #353535;
		font-weight: 550;
	}

	.detail-title {
		margin-top: 30rpx;
		height: 50rpx;
		line-height: 50rpx;
		padding: 0 32rpx;
		font-size: 32rpx;
		color: #353535;
		font-weight: 550;
	}

	.ui-tab {
		margin-top: 30rpx;
		width: 100%;
		/* 		padding: 10rpx 0; */
		display: flex;
		justify-content: space-between;

		.scroll {
			white-space: nowrap;
			width: 100%;

			.scroll-item {
				display: inline-block;
				height: 128rpx;
				width: 200rpx;
				background: #FFFFFF;
				border-radius: 16rpx;
				padding: 14rpx;
				margin: 0 10rpx;

				.icon {
					margin-top: 4rpx;
					margin-left: 4rpx;
				}

				.num {
					margin-top: 12rpx;
					font-size: 50rpx;
					color: #353535;
					font-weight: 700;
				}

				&:first-child {
					margin-left: 27.27rpx;
				}

				&:last-child {
					margin-right: 27.27rpx;
				}
			}
		}
	}

	scroll-view ::-webkit-scrollbar {
		display: none !important;
		width: 0 !important;
		height: 0 !important;
		-webkit-appearance: none;
		background: transparent;
	}

	.detail-box:not(:first-child){
		margin-top: 20rpx;
	}

	.ui-detail {
		padding: 0 32rpx;
		margin-top: 20rpx;
		.detail-box {
			background: #FFFFFF;
			border-radius: 16rpx;
			padding: 30rpx 20rpx;

			.box-cell {
				.title {
					height: 50rpx;
					line-height: 50rpx;
					font-size: 30rpx;
					color: #353535;
				}

				.content {
					height: 120rpx;
					display: flex;
					align-items: center;
					justify-content: space-between;
					border-bottom: solid 2px #f7f7f7;

					&:last-child {
						border: none !important;
					}

					.c {

						display: flex;
						align-items: center;
						font-size: 26rpx;
						color: #888888;

						text {
							margin-left: 10rpx;
							font-size: 40rpx;
							color: #353535;
							font-weight: 600;
						}
					}
				}

			}
		}
	}
</style>
