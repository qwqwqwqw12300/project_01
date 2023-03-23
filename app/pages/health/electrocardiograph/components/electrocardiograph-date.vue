<template>
	<view>
		<app-echarts :option="option" id="myChart" class="myChart"></app-echarts>
		<WatchDiv :text="'静息心率'" :content="'60'"></WatchDiv>
	</view>
</template>

<script>
	import WatchDiv from '@/components/watch-div/watch-div.vue'
	import * as echarts from '@/static/js/echarts.js';
	export default {
		components: {
			WatchDiv
		},
		data() {
			return {
				
			}
		},
		created() {
			this.logstatrt();
		},
		methods: {
			onSelect(val) {
				console.log(val, '000')
			},
			logstatrt() {
				this.option = {
					title: {
						text: ''
					},
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: []
					},
					grid: {
						left: '20',
						right: '20',
						bottom: '5',
						top:'0',
						containLabel: true
					},
					toolbox: {
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						data: ['00:00', '06:00', '12:00', '18:00', '23:59'],
						axisTick: {
							show: false
						},
					},
					yAxis: {
						type: 'category',
						data: ['0', '60', '120', '180', '240'],
						axisTick: {
							show: false
						},
						axisLabel: {
							show: false
						},
						axisLine: {
							show: false
						},
						splitLine: {
							show: true,
							lineStyle:{
								type:'disable'
							}
						},
					},
					series: [{
							name: 'Email',
							type: 'line',
							stack: 'Total',
							data: ['0', '60', '120', '180', '240'],
							showSymbol: false,
							itemStyle: {
								normal: {
									lineStyle: {
										color: "#FF7E23",
										width: 1
									}
								}
							},
						},
						{
							name: 'Union Ads',
							type: 'line',
							stack: 'Total',
							data: ['240', '180', '60', '120', '120'],
							showSymbol: false,
							itemStyle: {
								normal: {
									lineStyle: {
										color: "#63DDBA",
										width: 1
									}
								}
							},
						}
					]
				};
			},
		}
	}
</script>

<style lang="scss" scoped>
	.myChart{
		width: 90%;
		height: 400rpx;
		margin: 64rpx 32rpx 20rpx;
	}
	
</style>
