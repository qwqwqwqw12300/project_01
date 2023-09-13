<template>
	<app-body titleColor="#F7F7F7" :bodyStyle="{background: '#F7F7F7' }" title="心率">
		<!-- <app-logo text="心率" top="36rpx" iconUrl="/static/images/share@3x.png"></app-logo> -->
		<view class="ui-tab">
			<date-picker @onSelect="onSelect" @month="monthChange" :lightDot="monthData"></date-picker>
		</view>
		<view class="ui-show">
			<text>{{nowDataMin}}
				<text v-if="nowDataMax">-</text>
				{{nowDataMax}}bpm</text>
		</view>
		<!-- <view>{{slideDataMin}}</view> -->
		<view class="ui-echart">
			<!-- <rate-echarts :timeOption="timeOption"></rate-echarts> -->
			<app-echarts :option="options" id="myChart" class="echart-box" @click="clickEchart"></app-echarts>
		</view>
		<!-- <view class="ui-total">
			<view class="total-item" v-for="(item,index) in totalList" :key="index">
				<view class="item-data">
					<text class="num">{{ item.num }}</text>
					<text class="unit">bpm</text>
				</view>
				<view class="item-title">
					{{ item.title }}
				</view>
			</view>
		</view> -->
		<!-- <view class="ui-detail">
			<view class="title">
				运动心率
			</view>
			<view class="content">
				<view class="info-item" v-for="(item,index) in dataList" :key="index">
					<view class="process">
						<u-line-progress :percentage="item.value" :activeColor="item.activeColor"
							:inactiveColor="item.inactiveColor">
						</u-line-progress>
						<view class="sub">
							<text>{{ item.time }}</text>分钟
						</view>
					</view>
					<view class="data process">
						<text class="left">{{ item.title }}</text>
						<text class="right">{{ item.unit }}bpm</text>
					</view>
				</view>
			</view>
		</view> -->

		<health-info title="心率"></health-info>
	</app-body>
</template>

<script>
	import {
		GetMonthDataFlag,
		GetListHeartRateByDay,
		GetListHeartRateByWeek,
		GetMHeartRate
	} from '@/common/http/api.js';
	import {
		mapState,
	} from 'vuex';
	export default {
		data() {
			const weekOptions = {
				tooltip: {
					// show: false,
					trigger: 'axis',
					showContent: false, //隐藏提示框
					// axisPointer: {
					// 	type: 'shadow'
					// },
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
					boundaryGap: false,
					axisTick: { //坐标轴刻度相关设置。
						show: false,
					},
					axisPointer: {
						handle: {
							show: true,
							size: 18,
							margin: 2,
							throttle: 5,
							color: '#ffac4a'
						},
						label: {
							show: false,
							formatter: (params) => { //滑动拿到实时的值
								this.slideTime = echarts.format.formatTime('yyyy-MM-dd', params.value);
								return echarts.format.formatTime('yyyy-MM-dd', params.value);
							},
							// backgroundColor: '#7581BD'
						}
					},
					axisLabel: {
						textStyle: {
							color: "#666"
						},
						// formatter: 'week'
						formatter: 'week'
					},
					// nameLocation: 'end',
					// nameTextStyle: {
					// 	align: "center",
					// 	padding: [33, 13, 12, 35]
					// },
					axisLine: {
						lineStyle: {
							color: 'rgb(238,238,238)',
							width: 1
						}
					},
					interval: 24 * 3600 * 1000,
					// data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
				}, ],
				yAxis: [{
					type: "value",
					interval: 25,
					scale: true,
					splitArea: {
						show: false,
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
					},
					data: []
				}],
				series: [{
					type: 'bar',
					stack: 'Total',
					silent: true,

					showSymbol: false,
					itemStyle: {

						// normal: {
						// 	color: 'rgba(255,89,89,1)',
						// 	lineStyle: {
						// 		color: "rgba(255,89,89,1)",
						// 		width: 1
						// 	},
						// 	areaStyle: {
						// 		color: {
						// 			type: 'linear',
						// 			x: 0,
						// 			y: 0,
						// 			x2: 0,
						// 			y2: 1,
						// 			colorStops: [{
						// 				offset: 0,
						// 				color: 'rgba(255,89,89,0.90)'
						// 			}, {
						// 				offset: 1,
						// 				color: 'rgba(255,89,89,0.00)'
						// 			}],
						// 			global: false
						// 		}
						// 	}
						// },
						borderColor: 'transparent',
						color: 'transparent',
					},
					emphasis: {
						itemStyle: {
							borderColor: 'transparent',
							color: 'transparent'
						}
					},
					data: []
				}, {
					type: 'bar',
					stack: 'Total',
					// label: {
					// 	show: true,
					// 	position: 'top'
					// },
					showSymbol: false,
					itemStyle: {
						normal: {
							color: 'rgba(255,89,89,1)',
							lineStyle: {
								color: "rgba(255,89,89,1)",
								width: 1
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
										color: 'rgba(255,89,89,0.90)'
									}, {
										offset: 1,
										color: 'rgba(255,89,89,0.00)'
									}],
									global: false
								}
							}
						}
					},
					data: []
				}]
			}
			const dayOptions = {
				tooltip: {
					showContent:false,
					trigger: 'axis',
					// triggerOn: 'click',
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
					boundaryGap: false,
					axisTick: { //坐标轴刻度相关设置。
						show: false,
					},
					//手柄
					axisPointer: {
						// label: {
						// 	show: false,
						// 	formatter: (params) => { //滑动拿到实时的值
						// 		this.slideTime = echarts.format.formatTime('yyyy-MM-dd', params.value);
						// 		return echarts.format.formatTime('yyyy-MM-dd', params.value);
						// 	},
						// 	// backgroundColor: '#7581BD'
						// },
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
					axisLine: {
						lineStyle: {
							// type: 'solid',
							color: 'rgb(238,238,238)',
							// color: '#ffac4a',
							width: 1
						}
					},
					splitLine: {
						show: false
					},
					// data: ['00:00', '06:00', '12:00', '18:00', '23:59'],
					// min: `${option.value + ' 00:00:00'}`, // x轴起始时间
					// max: `${option.value + ' 23:49:00'}`, // x轴结束时间
					// splitNumber: 6,
					interval: 6 * 3600 * 1000, //时间间隔
				}, ],
				yAxis: [{
					type: "value",
					interval: 25,
					scale: true,
					splitArea: {
						show: false,
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
					symbolSize: 0.000001,
					itemStyle: {
						normal: {
							color: 'rgba(255,89,89,1)',
							lineStyle: {
								color: "rgba(255,89,89,1)",
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
										color: 'rgba(255,89,89,0.90)'
									}, {
										offset: 1,
										color: 'rgba(255,89,89,0.00)'
									}],
									global: false
								}
							}
						}
					},
					data: [
						// [new Date('2023-3-27 00:00:00'), '1'],
						// [new Date('2023-3-27 06:00:00'), '2'],
						// [new Date('2023-3-27 12:00:00'), '3'],
						// [new Date('2023-3-27 18:00:00'), '4'],
						// [new Date('2023-3-27 23:59:59'), '5'],


					]
				}]
			}
			return {
				totalList: [{
						num: 0,
						title: '平均'
					},
					{
						num: 0,
						title: '最低'
					},
					{
						num: 0,
						title: '最高'
					}
				],
				dataList: [{
						value: 0,
						title: '常规心率',
						time: '0',
						unit: '0',
						activeColor: '#cccccc',
						inactiveColor: "#F2F2F2"
					}, {
						value: 0,
						title: '热身运动',
						time: '0',
						unit: '0',
						activeColor: '#63CDA9',
						inactiveColor: "#A3EED4"
					}, {
						value: 0,
						title: '脂肪燃烧',
						time: '0',
						unit: '0',
						activeColor: '#75B2FF',
						inactiveColor: "#DDECFF"
					}, {
						value: 0,
						title: '有氧运动',
						time: '0',
						unit: '0',
						activeColor: '#F8DA68',
						inactiveColor: "#FFF5D0"
					}, {
						value: 0,
						title: '无氧运动',
						time: '0',
						unit: '0',
						activeColor: '#FF7DCB',
						inactiveColor: "#FFDCF1"
					},
					{
						value: 0,
						title: '极限心率',
						time: '0',
						unit: '0',
						activeColor: '#FF5A5A',
						inactiveColor: "#FFD5D5"
					}
				],
				dayOptions,
				weekOptions,
				options: {

				},
				monthData: [],
				nowDataMax: '', //实时数据
				nowDataMin: '',
				Data: [],
				slideTime: '' //手指滑动得到的数据（时间）
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}),
			// slideDataMin: () => { //滑动获取到的最小值
			// 	setTimeout(() => {
			// 		const res = this.Data.find(item => {
			// 			return item[0] == this.slideTime
			// 		})
			// 		return res[1]
			// 	}, 3000)
			// }

		},
		mounted() {
			const month = uni.$u.timeFormat(new Date(), 'yyyy-mm')
			this.getMonthData(month)
			// this.handleDate(options)
			// this.onSelect(val)
		},
		methods: {
			//点击图表获取值
			clickEchart(val) {
				console.log(val, 'val');
				const res = this.Data.find(item => {
					return item[0] == val.value
				})
				this.nowDataMin = res[1]
				this.nowDataMax = res[2]
			},
			onSelect(val) {
				val.type === 'date' ? this.handleDate(val) : this.handleWeek(val)
			},
			monthChange(date) {
				const month = uni.$u.timeFormat(date, 'yyyy-mm')
				this.getMonthData(month)
			},

			getMonthData(month) {
				const {
					deviceId,
					humanId
				} = this.deviceInfo
				GetMonthDataFlag({
					deviceId,
					humanId,
					yearMonth: month,
					type: '6', //（ 1卡路里 2活动时长 3步数 4睡眠 5站立 6心率 7血压 8血氧 9心电）
				}).then(res => {
					this.monthData = res.data.filter(n => {
						return n.flag === '1'
					}).map(item => {
						return item.date
					})
					console.log(this.monthData, 'mmm-----------')
				})
			},
			handleWeek(options) {
				this.nowDataMax = ''
				this.nowDataMin = ''
				const {
					deviceId,
					humanId
				} = this.deviceInfo
				GetMHeartRate({
					deviceId,
					humanId,
					beginDate: options.value[0],
					endDate: options.value[6],
				}).then(res => {
					const data1 = res.data.MapList.map(n => {
						return [n.time, n.valueMin]
					})
					const data2 = res.data.MapList.map(n => {
						return [n.time, n.valueMax - n.valueMin]
					})
					this.Data = res.data.MapList.map(n => { //获取当前周的所有max值
						return [n.time, n.valueMin, n.valueMax]
					})
					console.log(this.Data, 'Data');
					// console.log(res.data.MapList[2], 'data');
					this.weekOptions.series[0].data = data1
					this.weekOptions.series[1].data = data2
					this.weekOptions.xAxis[0].min = uni.$u.timeFormat(new Date(options.value[0]),
							'yyyy-mm-dd') +
						' 00:00:00'
					this.weekOptions.xAxis[0].max = uni.$u.timeFormat(new Date(options.value[6]),
							'yyyy-mm-dd') +
						' 00:00:00'
					this.weekOptions.yAxis[0].min = '40'
					this.weekOptions.yAxis[0].max = '165'
					this.options = this.weekOptions
					// console.log('dataaaaa', data);
					console.log(this.weekOptions, 'this.weekOptions');
				})
				// GetListHeartRateByWeek({
				// 	deviceId,
				// 	humanId,
				// 	beginDate: options.value[0],
				// 	endDate: options.value[6],
				// }).then(res => {
				// 	// 给假数据，使得没数据时不报错
				// 	if (!res.data.MapList.length) {
				// 		this.weekOptions.series[0].data = [
				// 			[
				// 				"2023-08-14 08:41:00",
				// 				"78"
				// 			],
				// 			[
				// 				"2023-08-14 09:41:00",
				// 				"88"
				// 			],
				// 			[
				// 				"2023-08-15 08:41:00",
				// 				"78"
				// 			],
				// 			[
				// 				"2023-08-15 09:41:00",
				// 				"88"
				// 			],
				// 		]
				// 		this.options = this.weekOptions
				// 		console.log(this.weekOptions, 'this.weekOptions');
				// 		return
				// 	}
				// 	const {
				// 		avg,
				// 		max,
				// 		min
				// 	} = res.data
				// 	this.totalList[0].num = avg
				// 	this.totalList[1].num = min
				// 	this.totalList[2].num = max

				// })
			},
			handleDate(options) {
				const {
					deviceId,
					humanId
				} = this.deviceInfo
				GetListHeartRateByDay({
					deviceId,
					humanId,
					dayTime: options.value,
				}).then(res => {
					// console.log('GetListHeartRateByDay', res)
					// console.log(options.value, 'options.value');
					if (!res.data.MapList) {
						this.dayOptions.series[0].data = []
						this.dayOptions.xAxis[0].min = uni.$u.timeFormat(new Date(options.value),
							'yyyy-mm-dd') + ' 00:00:00'
						this.dayOptions.xAxis[0].max = uni.$u.timeFormat(new Date(options.value),
							'yyyy-mm-dd') + ' 23:59:59'
						this.dayOptions.yAxis[0].min = '40'
						this.dayOptions.yAxis[0].max = '165'
						this.options = this.dayOptions
						return
					}
					const {
						avg,
						max,
						min
					} = res.data
					console.log('wdwdwdwdwdwdwdwdwdwdwd');
					this.totalList[0].num = avg
					this.totalList[1].num = min
					this.totalList[2].num = max
					const data = res.data.MapList.map((n, index) => {
						// if (index == 7) {
						// 	return [n.time, '-']
						// }
						return [n.time, n.value]
					})
					this.$nextTick(() => {
						this.dayOptions.xAxis[0].min = uni.$u.timeFormat(new Date(options.value),
							'yyyy-mm-dd') + ' 00:00:00'
						this.dayOptions.xAxis[0].max = uni.$u.timeFormat(new Date(options.value),
							'yyyy-mm-dd') + ' 23:59:59'
						this.dayOptions.yAxis[0].min = '40'
						this.dayOptions.yAxis[0].max = '165'
						this.dayOptions.series[0].data = data
						this.options = this.dayOptions
						// console.log('打印啦啊啊啊啊啊啊啊啊啊啊啊啊啊', this.dayOptions);
						console.log(data);
					})
				})
			}
		}
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
		margin-top: 20rpx;
		padding: 0 32rpx;

		.echart-box {
			// width: 100%;
			height: 500rpx;
		}
	}

	.ui-total {
		padding: 0 32rpx;
		margin-top: 50rpx;
		display: flex;
		justify-content: space-between;

		.total-item {
			box-sizing: content-box;
			padding: 10rpx 20rpx;
			background: #FFFFFF;
			border-radius: 8px;

			.item-data {
				width: 150rpx;

				.num {
					font-size: 50rpx;
					color: #353535;
					font-weight: 700;
				}

				.unit {
					margin-left: 14rpx;
					font-size: 26rpx;
					color: #353535;
				}
			}

			.item-title {
				font-size: 24rpx;
				color: #888888;
			}
		}
	}

	.ui-detail {
		padding: 0 32rpx;
		// margin-top: 30rpx;

		.title {
			height: 100rpx;
			line-height: 100rpx;
			font-size: 32rpx;
			color: #353535;
			font-weight: 550;
		}

		.content {
			background: #FFFFFF;
			border-radius: 16rpx;
			padding: 10rpx;

			.info-item {
				padding: 0 10rpx;
				margin-bottom: 20rpx;

				.process {
					display: flex;
					align-items: center;
					justify-content: space-between;
					height: 60rpx;

					.sub {
						width: 200rpx;
						text-align: right;
						font-size: 26rpx;
						color: #888888;

						text {
							margin-right: 10rpx;
							font-size: 50rpx;
							font-weight: 700;
							color: #353535;
						}
					}
				}

				.data {
					height: 40rpx !important;

					.left {
						font-size: 26rpx;
						color: #353535;
					}

					.right {
						font-size: 26rpx;
						color: #888888;
					}
				}
			}
		}
	}
</style>