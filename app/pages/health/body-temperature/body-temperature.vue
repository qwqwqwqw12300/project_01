<!-- 体温 -->
<template>
	<app-body titleColor="#F7F7F7" :bodyStyle="{background: '#F7F7F7' }" title="体温">
		<!-- <app-logo text="体温" top="36rpx" iconUrl="/static/images/share@3x.png"></app-logo> -->
		<view class="ui-tab">
			<date-picker @onSelect="onSelect" @month="monthChange" :lightDot="monthData"></date-picker>
		</view>
		<view class="ui-show" v-if="WEEK">
			<text>{{nowDataMin}}
				<text v-if="nowDataMax">-</text>
				{{nowDataMax}}°C</text>
		</view>
		<view class="ui-echart">
			<app-echarts @click="handleClick" :option="options" id="myChart" class="echart-box"
				@Cclick="clickEchart"></app-echarts>
		</view>
		<!-- <view class="ui-total">
			<view class="total-box">
				<view class="cell">
					<text class="title">体温</text>
					<text class="value"></text>
				</view>
				<view class="cell" v-for="(item,index) in totalList" :key="index">
					<text class="text">{{ item.title }}</text>
					<text class="value">{{ item.num }}℃</text>
				</view>
			</view>
		</view> -->
		<health-info title="体温"></health-info>
	</app-body>
</template>

<script>
	import * as echarts from '@/static/js/echarts.js';
	import {
		GetMonthDataFlag,
		GetListBoayTmperatureByDay,
		// GetListBoayTmperatureByWeek
		GetMTemperature
	} from '@/common/http/api';
	import {
		mapState,
	} from 'vuex';
	export default {
		computed: {
			...mapState({
				/**所有家庭列表**/
				deviceInfo: state => state.deviceInfo
			}),

		},
		data() {
			return {
				totalList: [{
						num: 36,
						title: '日平均值'
					},
					{
						num: 36.5,
						title: '日最高值'
					},
					{
						num: 36,
						title: '日最低值'
					}
				],
				options: {},
				dataList: [], //周体温min
				dataList2: [], //周体温max-min
				text: '0',
				monthData: [],
				nowDataMax: '', //实时数据
				nowDataMin: '',
				Data: [],
				WEEK: false
			}
		},
		mounted() {
			const month = uni.$u.timeFormat(new Date(), 'yyyy-mm')
			this.getMonthData(month)
		},
		methods: {
			clickEchart(val) {
				console.log(val, 'val');
				const res = this.Data.find(item => {
					return item[0] == val.value
				})
				this.nowDataMin = res[1]
				this.nowDataMax = res[2]
			},
			monthChange(date) {
				const month = uni.$u.timeFormat(date, 'yyyy-mm')
				this.getMonthData(month)
			},

			getMonthData(month) {
				const {
					deviceId,
					humanId,
				} = this.deviceInfo
				GetMonthDataFlag({
					deviceId,
					yearMonth: month,
					type: '8', //（ 1卡路里 2活动时长 3步数 4睡眠 5站立 6心率 7血压 8血氧 9心电）
				}).then(res => {
					this.monthData = res.data.filter(n => {
						return n.flag === '1'
					}).map(item => {
						return item.date
					})
				})
			},
			handleClick(option) {
				console.log(option, 'option')
				this.text = option.value[1]
			},
			dateFun(option) {
				console.log(option, 'option');
				console.log(this.dataList, 'this.dataList');
				this.options = {
					notMerge: true,
					tooltip: {
						textStyle: {
							// color: '#188df0'
						},
						trigger: 'axis',
						triggerOn: 'click',
						axisPointer: {
							type: 'line',
							lineStyle: {
								type: 'dashed', //指示线为虚线
							},
						},
					},
					backgroundColor: '#fff',
					grid: {
						left: '5%',
						right: '5%',
						bottom: '3%',
						top: '10%',
						containLabel: true,
					},
					xAxis: [{
						type: 'time',
						// interval: 6 * 60 * 60 * 1000, // 设置x轴间隔为6小时
						min: `${option.value + ' 00:00:00'}`, // x轴起始时间
						max: `${option.value + ' 23:49:00'}`, // x轴结束时间
						// boundaryGap: false,
						axisTick: { //坐标轴刻度相关设置。
							show: false,
						},
						//手柄
						axisPointer: {
							value: '',
							handle: {
								show: true,
								size: 18,
								margin: 2,
								throttle: 5,
								color: '#ffac4a'
							}
						},
						axisLabel: {
							margin: 13,
							textStyle: {
								color: "#666"
							},
							formatter: 'date'
						},
						splitLine: {
							show: false
						},
						axisLine: {
							lineStyle: {
								color: 'rgb(238,238,238)',
								width: 1
							}
						},
					}, ],
					yAxis: [{
						type: "value",
						min: '35',
						max: '39',
						scale: true,
						splitArea: {
							show: true,
							areaStyle: {
								color: ['#f6f8fc', '#fff']
							}
						},
						axisLabel: {
							textStyle: {
								color: "#666"
							}
						},
						nameTextStyle: {
							color: "#666",
							fontSize: 12,
							lineHeight: 40
						},
						// 分割线
						splitLine: {
							lineStyle: {
								type: "dashed",
								color: "#E9E9E9"
							}
						},
						axisLine: {
							show: false
						},
						axisTick: {
							show: false
						}
					}],
					series: [{
						type: 'line',
						showSymbol: false,
						itemStyle: {
							// color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
							// 		offset: 0,
							// 		color: '#83bff6'
							// 	},
							// 	{
							// 		offset: 0.5,
							// 		color: '#188df0'
							// 	},
							// 	{
							// 		offset: 1,
							// 		color: '#188df0'
							// 	}
							// ]),
							normal: {
								color: 'rgba(54, 191, 255,1)',
								lineStyle: {
									color: "rgba(54, 191, 255,1)",
									width: 1,
									// type: 'dashed'
								},
								areaStyle: {
									color: {
										type: 'linear',
										x: 0,
										y: 0,
										x2: 0,
										y2: 1,
										colorStops: [{
											offset: 0,
											color: 'rgba(54, 191, 255,0.90)'
										}, {
											offset: 1,
											color: 'rgba(54, 191, 255,0.00)'
										}],
										global: false
									}
								}
							}
						},
						barWidth: '5', //---柱形宽度
						barCategoryGap: '20%', //---柱形间距
						data: this.dataList
					}]
				}
			},
			weekFun(option) {
				this.options = {
					tooltip: {
						trigger: 'axis',
						showContent: false,
						formatter: (params) => {
							console.log(params, 'paramsparamsparamsparamsparams');
							return `最高:${Number(params[0].data[1])+Number(params[1].data[1])}</br>最低:${params[0].data[1]}`
						},
						axisPointer: {
							type: 'line',
							lineStyle: {
								type: 'dashed', //指示线为虚线
							},
						},
					},
					backgroundColor: '#fff',
					grid: {
						left: '5%',
						right: '5%',
						bottom: '3%',
						containLabel: true
					},
					xAxis: [{
						type: 'time',
						interval: 24 * 3600 * 1000,
						min: new Date(`${option.value[0] + ' 00:00:00'}`), // x轴起始时间
						max: new Date(`${option.value[6] + ' 00:00:00'}`), // x轴结束时间
						boundaryGap: false,
						axisTick: { //坐标轴刻度相关设置。
							show: false,
						},

						axisLabel: {
							textStyle: {
								color: "#666"
							},
							formatter: 'week'
						},
						axisLine: {
							margin: 13,
							lineStyle: {
								color: 'rgb(238,238,238)',
								width: 1
							}
						},
					}, ],
					yAxis: [{
						type: "value",
						axisLabel: {
							textStyle: {
								color: "#666"
							}
						},
						nameTextStyle: {
							color: "#666",
							fontSize: 12,
							lineHeight: 40
						},
						// 分割线
						splitLine: {
							lineStyle: {
								type: "dashed",
								color: "#E9E9E9"
							}
						},
						axisLine: {
							show: false
						},
						axisTick: {
							show: false
						}
					}],
					series: [{
						barWidth: '25%',
						type: 'bar',
						stack: 'Total',
						showSymbol: false,
						itemStyle: {
							// normal: {
							// 	lineStyle: {
							// 		color: "#FF7E23",
							// 		width: 1
							// 	}
							// },
							borderColor: 'transparent',
							color: 'transparent',
						},
						data: this.dataList
					}, {
						type: 'bar',
						stack: 'Total',
						data: this.dataList2,
						showSymbol: false,
						itemStyle: {
							normal: {
								barBorderRadius: 30,
								lineStyle: {
									color: "#63DDBA",
									width: 1
								}
							}
						},
					}]
				}
			},
			handleDate(option) {
				this.WEEK = false
				console.log(option, 'option')
				this.dataList = []
				GetListBoayTmperatureByDay({
					deviceId: this.deviceInfo.deviceId,
					dayTime: option.value,
					humanId: this.deviceInfo.humanId
				}).then(res => {
					this.totalList[0].num = res.data.avg
					this.totalList[1].num = res.data.max
					this.totalList[2].num = res.data.min
					for (let i = 0; i < res.data.MapList.length; i++) {
						this.dataList.push([
							res.data.MapList[i].time,
							res.data.MapList[i].value
						])
					}
					console.log(this.options);
					const length = res.data.MapList.length
					this.options.xAxis[0].axisPointer.value = res.data.MapList[length - 1].time
					this.text = this.dataList[0] ? this.dataList[0][1] : 0;
					console.log(this.dataList, 'dataList')
				})
				this.dateFun(option)
				console.log('aawwwww', this.options);
			},
			handleWeek(option) {
				this.WEEK = true
				this.dataList = []
				this.dataList2 = []
				GetMTemperature({
					deviceId: this.deviceInfo.deviceId,
					beginDate: option.value[0],
					endDate: option.value[6],
					humanId: this.deviceInfo.humanId
				}).then(res => {
					// this.totalList[0].num = res.data.avg
					// this.totalList[1].num = res.data.max
					// this.totalList[2].num = res.data.min
					this.Data = res.data.MapList.map(n => { //获取当前周的所有max值
						return [n.time, n.valueMin, n.valueMax]
					})
					for (let i = 0; i < res.data.MapList.length; i++) {
						this.dataList.push([
							res.data.MapList[i].time,
							res.data.MapList[i].valueMin
						])
					}
					for (let i = 0; i < res.data.MapList.length; i++) {
						this.dataList2.push([
							res.data.MapList[i].time,
							res.data.MapList[i].valueMax - res.data.MapList[i].valueMin
						])
					}
					// console.log(this.dataList, 'this.dataList')
				})
				this.weekFun(option)
			},
			onSelect(val) {
				console.log(val, '000')
				val.type === 'date' ? this.handleDate(val) : this.handleWeek(val)
			},
		},
	}
</script>

<style lang="scss" scoped>
	.ui-show {
		margin-top: 20rpx;
		text-align: center;
		font-size: 72rpx;
		color: #353535;
		font-weight: 700;

	}

	.ui-tab {
		margin-top: 64rpx;
	}

	.ui-echart {
		// margin-top: 20rpx;
		padding: 0 32rpx;
		// height: 1000rpx;

		.echart-box {
			height: 500rpx;
		}
	}

	.ui-total {
		padding: 0 32rpx;
		margin-top: 50rpx;

		.total-box {
			background: #FFFFFF;
			border-radius: 16rpx;

			.cell {
				height: 110rpx;
				padding: 0 20rpx;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.title {
					font-size: 36rpx;
					color: #353535;
					font-weight: 550;
				}

				.text {
					font-size: 34rpx;
					color: #353535;
				}

				.value {
					font-size: 56rpx;
					color: #353535;
					font-weight: 700;
				}
			}
		}
	}
</style>