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
			<view class="ui-date-title">周统计</view> 
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
		GetListBloodPressureByWeek
	} from '@/common/http/api';
	export default {
		props:{
			time:{
				default:''
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
					}
				],
				spMapList:[],//收缩压
				dpMapList:[],//舒张压
			}
		},
		watch: {
			time: {
				handler(val) {
					console.log(val, '44444444444')
					if (val) {
						this.logstatrt();
					}
				},
				immediate:true //监听到数据立即执行
			}
		},
		methods: {
			logstatrt() {
				this.spMapList = []
				this.dpMapList = []
				GetListBloodPressureByWeek({
					beginDate:this.time[0],
					endDate:this.time[6],
					deviceId:240,
					humanId:'00000000000000000001'
				}).then(res=>{
					console.log(res,'res')
					this.list[0].value = res.data.spAvg
					this.list[1].value = res.data.dpAvg
					this.dateList[0].value = res.data.spAvg
					this.dateList[1].value = res.data.dpAvg
					for(let i =0;i<res.data.spMapList.length;i++){
						this.spMapList.push([
							res.data.spMapList[i].time,
							res.data.spMapList[i].value
						])
					}
					for(let i =0;i<res.data.dpMapList.length;i++){
						this.dpMapList.push([
							res.data.dpMapList[i].time,
							res.data.dpMapList[i].value
						])
					}
				})
				this.option = {
					title: {
						text: ''
					},
					tooltip: {
						positionStatus:true,
						trigger: 'axis',
					},
					legend: {
						data: []
					},
					grid: {
						left: '20',
						right: '20',
						bottom: '5',
						top:'20',
						containLabel: true
					},
					toolbox: {
					},
					xAxis: {
						type: 'time',
						boundaryGap: false,
						interval: 24 * 3600 * 1000,
						min: new Date(`${this.time[0] + ' 00:00:00'}`), // x轴起始时间
						max: new Date(`${this.time[6] + ' 00:00:00'}`), // x轴结束时间
						axisTick: {
							show: false
						},
						axisLabel: {
							textStyle: {
								color: "#666"
							},
							formatter: function(val,index) {
								const weekArr = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
								return index == 7 ? '' : weekArr[new Date(val).getDay()]
							}
						},
					},
					yAxis: {
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
					},
					series: [{
							name: '收缩压',
							type: 'line',
							stack: 'Total',
							data:this.spMapList,
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
							name: '舒张压',
							type: 'line',
							stack: 'Total',
							data: this.dpMapList,
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
			margin-bottom: 84rpx;
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
