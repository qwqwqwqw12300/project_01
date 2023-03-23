<template>
	<view>
		<app-echarts :option="option" id="myChart" class="myChart"></app-echarts>
		<WatchDiv :text="'静息心率'" :content="'60'"></WatchDiv>
		<view class="ui-total">
			<view class="total-box">
				<view class="cell">
					<text class="title">运动心率</text>
					<text class="value"></text>
				</view>
				<view class="cell" v-for="(item,index) in totalList" :key="index">
					<text class="text">{{ item.title }}</text>
					<text class="value">{{ item.num }}</text>
				</view>
			</view>
		</view>
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
				totalList: [{
						num: 80,
						title: '平均心率'
					},
					{
						num: 100,
						title: '最高心率'
					},
					{
						num: 60,
						title: '最低心率'
					}
				],
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
						data: ['周日','周一','周二','周三','周四','周五','周六'],
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
					series: [
						{
							name: 'Union Ads',
							type: 'line',
							stack: 'Total',
							data: ['240', '180', '60', '120', '120','60','60'],
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
		margin: 64rpx 32rpx 32rpx;
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
