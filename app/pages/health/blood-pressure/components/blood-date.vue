<template>
	<view>
		<!-- <app-echarts :option="option" id="myChart" class="myChart"></app-echarts> -->
		<view class="ui-content">
			<view class="ui-title" v-for="(item,index) in list" :key="index">
				<view class="ui-title-content">
					<view class="ui-circle" :style="{backgroundColor:item.color}"></view>
					<view class="ui-title-font">{{item.title}}</view>
				</view>
				<view style="margin-top: 8rpx;">
					<text class="ui-font">{{item.value}}</text>
					<text class="ui-content-font">mmHg</text>
				</view>
			</view>
		</view>
		<app-echarts :option="option" id="myChart" class="myChart"></app-echarts>
		<view class="ui-statistics">
			<view class="ui-date-title">日统计</view> 
			<view class="ui-window">
				<view v-for="(item,index) in dateList" :key="index" class="ui-statistics-content">
					<view class="ui-statistics-title">{{item.title}}</view>
					<view style="margin-top: 8rpx;">
						<text class="ui-statistics-font">{{item.value}}</text>
						<text class="ui-statistics-fonts">mmHg</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import * as echarts from '@/static/js/echarts.js';
	import {
		GetListBloodPressureByDay
	} from '@/common/http/api';
	export default {
		props:{
			time:{
				default:'2023-03-23'
			}
		},
		data() {
			return {
				option: {},
				list: [{
						color: '#FF7E23',
						title: '收缩压',
						value: '126'
					},
					{
						color: '#63DDBA',
						title: '舒张压',
						value: '88'
					}
				],
				dateList: [{
						title: '平均收缩压',
						value: '126'
					},
					{
						title: '平均舒张压',
						value: '88'
					},
					{
						title: '最高收缩压',
						value: '126'
					},
					{
						title: '最高舒张压',
						value: '88'
					}
				]
			}
		},
		mounted() {
			
		},
		watch: {
			time: {
				handler(val) {
					console.log(val, '333333333')
					if (val) {
						this.logstatrt();
					}
				},
			}
		},
		methods: {
			logstatrt() {
				GetListBloodPressureByDay({
					deviceId:240,
					dayTime:this.time,
					humanId:'00000000000000000001'
				}).then(res=>{
					console.log(res,'res')
				})
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
						left: '0',
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
	.ui-content {
		width: 80%;
		margin: 0 auto;
		margin-top: 32rpx !important;
		display: flex;
		align-items: center;
		justify-content: space-between;

		.ui-title {
			display: flex;
			flex-direction: column;
			justify-content: space-between;

			.ui-title-content {
				width: 90%;
				display: flex;

				.ui-circle {
					width: 30rpx;
					height: 30rpx;
					margin-right: 10rpx;
					border-radius: 50rpx;
				}

				.ui-title-font {
					font-size: 26rpx;
					color: #353535;
					letter-spacing: 0;
					font-weight: 400;
				}
			}
		}

		.ui-font {
			font-size: 72rpx;
			color: #353535;
			letter-spacing: 0;
			font-weight: 700;
		}

		.ui-content-font {
			font-size: 26rpx;
			color: #888888;
			letter-spacing: 0;
			font-weight: 400;
		}
	}
	.myChart{
		width: 90%;
		height: 400rpx;
		margin: 64rpx 32rpx 20rpx;
	}
	.ui-statistics{
		margin-top: 32rpx;
		display: flex;
		flex-direction: column;
		border-radius: 16rpx;
		background-color: #FFF;
		.ui-date-title{
			margin-top: 32rpx;
			margin-left: 20rpx;
			font-size: 36rpx;
			color: #353535;
			letter-spacing: 0;
			font-weight: 500;
		}
		.ui-window{
			margin-bottom: 36rpx;
			display: flex;
			align-items: center;
			flex-wrap: wrap;
			.ui-statistics-content{
				width: 50%;
				margin-top: 48rpx;
				display: flex;
				flex-direction: column;
				align-items: center;
				.ui-content-title{
					font-size: 26rpx;
					color: #353535;
					letter-spacing: 0;
					font-weight: 400;
				}
				.ui-statistics-font{
					font-size: 72rpx;
					margin-right: 10rpx;
					color: #353535;
					letter-spacing: 0;
					font-weight: 700;
				}
				.ui-statistics-fonts{
					font-size: 26rpx;
					color: #888888;
					letter-spacing: 0;
					font-weight: 400;
				}
			}
		}
	}
</style>
