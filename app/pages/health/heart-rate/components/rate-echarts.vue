<template>
	<view class="">
		<app-echarts :option="options" id="myChart" class="echart-box"></app-echarts>
	</view>
</template>

<script>
	import {
		GetListHeartRateByDay,
		GetListHeartRateByWeek
	} from '@/common/http/api.js';
	export default {
		props: {
			timeOption: {
				type: Object,
				default: () => {}
			}
		},
		data() {
			const weekOptions = {
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
					type: 'category',
					boundaryGap: false,
					axisTick: { //坐标轴刻度相关设置。
						show: false,
					},
					axisLabel: {
						textStyle: {
							color: "#666"
						}
					},
					axisLine: {
						lineStyle: {
							color: 'rgb(238,238,238)',
							width: 1
						}
					},
					data: ['00:00', '06:00', '12:00', '18:00', '23:59']
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
					data: [1, 2, 3, 3, 5, 6, 5, 3, 6, 5, 5, 4]
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
						[new Date('2023-3-27 00:00:00'), '1'],
						[new Date('2023-3-27 06:00:00'), '2'],
						[new Date('2023-3-27 12:00:00'), '3'],
						[new Date('2023-3-27 18:00:00'), '4'],
						[new Date('2023-3-27 23:59:59'), '5'],
					]
				}]
			}
			return {
				dayOptions,
				weekOptions,
				options: {

				}
			}
		},
		watch: {
			timeOption: {
				handler(val) {
					console.log()
					val.type === 'date' ? this.handleDate(val) : this.handleWeek(val)
				},
				deep: true
			}
		},
		methods: {
			handleWeek() {
				this.options = this.weekOptions
			},
			handleDate(options) {
				console.log(options, 'op')
				GetListHeartRateByDay({
					deviceId: 243,
					dayTime: options.value,
					humanId: '1',
				}).then(res => {
					const data = res.data.MapList.map(n => {
						return [new Date(n.time), n.value]
					})
					console.log(data, 'dddddddddddddd')
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
	.echart-box {
		width: 100%;
		height: 500rpx;
	}
</style>