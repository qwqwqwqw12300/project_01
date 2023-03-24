<template>
	<view class="">
		<app-echarts :option="type == 'date' ? optionDate: optionWeek" id="myChart" class="myChart"></app-echarts>
	</view>
</template>

<script>
	export default {
		props:{
			type:{
				default:'date'
			}
		},
		data() {
			const optionDate = {
				notMerge:true,
				tooltip: {
					trigger: ''
				},
				grid: {
					left: '0',
					right: '0',
					bottom: '0',
					top:'10',
					containLabel: true
				},
				xAxis: {
					type: 'category',
					data: ['00:00', '06:00', '12:00', '18:00', '23:59'],
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
						data: [10, 25, 50, 70, 100],
						type: 'bar',
						itemStyle: { //---图形形状
							color: '#61AAF7',
							barBorderRadius: [18, 18, 0, 0],
						},
						barWidth: '10', //---柱形宽度
						barCategoryGap: '20%', //---柱形间距
					},
					{
						data: [50, 100, 50, 70, 100],
						type: 'bar',
						itemStyle: { //---图形形状
							color: '#61AAF7',
							barBorderRadius: [18, 18, 0, 0],
						},
						barWidth: '10', //---柱形宽度
						barCategoryGap: '20%', //---柱形间距
					},
					{
						data: [25, 50, 50, 70, 100],
						type: 'bar',
						itemStyle: { //---图形形状
							color: '#61AAF7',
							barBorderRadius: [18, 18, 0, 0],
						},
						barWidth: '10', //---柱形宽度
						barCategoryGap: '20%', //---柱形间距
					}
				]
			}
			const optionWeek = {
				notMerge:true,
				tooltip: {
					trigger: ''
				},
				grid: {
					left: '0',
					right: '0',
					bottom: '0',
					top:'10',
					containLabel: true
				},
				xAxis: {
					type: 'category',
					data: ['周日','周一','周二','周三','周四','周五','周六'],
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
						data: [10, 25, 50, 70, 100,120,140],
						type: 'bar',
						itemStyle: { //---图形形状
							color: '#61AAF7',
							barBorderRadius: [0, 0, 0, 0],
						},
						barWidth: '30', //---柱形宽度
					}
				]
			}
			return {
				optionDate,
				optionWeek
			}
		},
		created() {
			
		}
	}
</script>

<style lang="scss" scoped>
	.myChart{
		width: 90%;
		height: 400rpx;
		margin: 64rpx 32rpx 48rpx;
	}
</style>
