<template>
	<view class="">
		<app-echarts :option="options" id="myChart" class="myChart"></app-echarts>
	</view>
</template>

<script>
	export default {
		props: {
			type: {
				default: 'date'
			},
			options: {
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
				},
			optionWeek: {
				default: function() {
					return {
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
				}
			}
		},
		data() {
			return {
				// optionDate,
				// optionWeek
			}
		},
		created() {

		}
	}
</script>

<style lang="scss" scoped>
	.myChart {
		width: 90%;
		height: 400rpx;
		margin: 64rpx 32rpx 48rpx;
	}
</style>
