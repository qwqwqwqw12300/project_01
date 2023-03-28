<template>
	<app-body :bodyStyle="{background: '#F7F7F7' }">
		<app-logo text="心率" top="36rpx" iconUrl="/static/images/share@3x.png"></app-logo>
		<view class="ui-tab">
			<date-picker @onSelect="onSelect"></date-picker>
		</view>
		<view class="ui-echart">
			<!-- 	<rate-echarts :timeOption="timeOption"></rate-echarts> -->
			<app-echarts :option="options" id="myChart" class="echart-box"></app-echarts>
		</view>
		<view class="ui-total">
			<view class="total-item" v-for="(item,index) in totalList" :key="index">
				<view class="item-data">
					<text class="num">{{ item.num }}</text>
					<text class="unit">bpm</text>
				</view>
				<view class="item-title">
					{{ item.title }}
				</view>
			</view>
		</view>
		<view class="ui-detail">
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
		</view>
	</app-body>
</template>

<script>
	import {
		GetListHeartRateByDay,
		GetListHeartRateByWeek
	} from '@/common/http/api.js';
	export default {
		data() {
			const weekOptions = {
				tooltip: {
					trigger: 'axis'
				},
				grid: {
					left: '3%',
					right: '3%',
					bottom: '3%',
					containLabel: true
				},
				xAxis: [{
					type: 'time',
					boundaryGap: false,
					axisTick: { //坐标轴刻度相关设置。
						show: false,
					},
					axisLabel: {
						textStyle: {
							color: "#666"
						},
						formatter: function(val, index) {
							const weekArr = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
							return index === 7 ? '' : weekArr[new Date(val).getDay()]
							// return weekArr[new Date(val).getDay()]
							// return (uni.$u.timeFormat(new Date(val), 'hh:MM'))
						}
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
					min: '',
					max: '',
					// data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
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
					type: 'line',
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
					trigger: 'axis'
				},
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
					axisLabel: {
						textStyle: {
							color: "#666"
						},
						formatter: function(val) {
							return (uni.$u.timeFormat(new Date(val), 'hh:MM'))
						}
					},
					axisLine: {
						lineStyle: {
							color: 'rgb(238,238,238)',
							width: 1
						}
					},
					splitLine: {
						show: false
					},
					// data: ['00:00', '06:00', '12:00', '18:00', '23:59']
					// splitNumber: 6,
					interval: 6 * 3600 * 1000,
					min: '',
					max: '',
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
					type: 'line',
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
						num: 80,
						title: '平均'
					},
					{
						num: 60,
						title: '最低'
					},
					{
						num: 100,
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

				}
			}
		},
		mounted() {},
		methods: {
			onSelect(val) {
				val.type === 'date' ? this.handleDate(val) : this.handleWeek(val)
			},
			handleWeek(options) {
				GetListHeartRateByWeek({
					deviceId: 240,
					beginDate: options.value[0],
					endDate: options.value[6],
					humanId: '1',
				}).then(res => {
					const data = res.data.MapList.map(n => {
						return [new Date(n.time), n.value]
					})
					this.weekOptions.series[0].data = data
					this.weekOptions.xAxis[0].min = new Date(uni.$u.timeFormat(new Date(options.value[0]),
							'yyyy-mm-dd') +
						' 00:00:00')
					this.weekOptions.xAxis[0].max = new Date(uni.$u.timeFormat(new Date(options.value[6]),
							'yyyy-mm-dd') +
						' 23:59:59')
					this.options = this.weekOptions
				})
			},
			handleDate(options) {
				GetListHeartRateByDay({
					deviceId: 240,
					dayTime: options.value,
					humanId: '1',
				}).then(res => {
					const data = res.data.MapList.map(n => {
						return [new Date(n.time), n.value]
					})
					this.$nextTick(() => {
						this.dayOptions.xAxis[0].min = new Date(uni.$u.timeFormat(new Date(options.value),
								'yyyy-mm-dd') +
							' 00:00:00')
						this.dayOptions.xAxis[0].max = new Date(uni.$u.timeFormat(new Date(options.value),
								'yyyy-mm-dd') +
							' 23:59:59')
						this.dayOptions.series[0].data = data
						this.options = this.dayOptions
					})
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-tab {
		margin-top: 64rpx;
	}

	.ui-echart {
		// margin-top: 20rpx;

		.echart-box {
			width: 100%;
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