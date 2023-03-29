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
				type: Object,
				required: true
			},
			options2: {
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
