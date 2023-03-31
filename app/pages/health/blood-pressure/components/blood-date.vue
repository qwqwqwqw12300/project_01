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
		<app-echarts :option="option" @click="handleClick" id="myChart" class="myChart"></app-echarts>
		<view class="ui-statistics">
			<view class="statistics-box">
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
	</view>
</template>

<script>
	import * as echarts from '@/static/js/echarts.js';
	import {
		GetListBloodPressureByDay
	} from '@/common/http/api';
	import {
		mapState,
	} from 'vuex';
	export default {
		computed: {
			...mapState({
				/**所有家庭列表**/
				deviceInfo: state => state.deviceInfo
			}),

		},
		props: {
			time: {
				default: ''
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
				],
				spMapList: [], //收缩压
				dpMapList: [], //舒张压
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
				immediate: true //监听到数据立即执行
			}
		},
		methods: {
			handleClick(option){
				console.log(option,'option')
				
			},
			logstatrt() {
				this.spMapList = []
				this.dpMapList = []
				GetListBloodPressureByDay({
					deviceId: this.deviceInfo.deviceId,
					dayTime: this.time,
					humanId: this.deviceInfo.humanId,
					// humanId:'117'
				}).then(res => {
					console.log(res, 'res')
					this.list[0].value = res.data.spAvg
					this.list[1].value = res.data.dpAvg
					this.dateList[0].value = res.data.spAvg
					this.dateList[1].value = res.data.dpAvg
					this.dateList[2].value = res.data.maxSp
					this.dateList[3].value = res.data.maxDp
					for (let i = 0; i < res.data.spMapList.length; i++) {
						this.spMapList.push([
							res.data.spMapList[i].time,
							res.data.spMapList[i].value
						])
					}
					for (let i = 0; i < res.data.dpMapList.length; i++) {
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
						positionStatus: true,
						trigger: 'axis',
					},
					backgroundColor: '#fff',
					legend: {
						data: []
					},
					grid: {
						left: '20',
						right: '20',
						bottom: '5',
						top: '20',
						containLabel: true
					},
					toolbox: {},
					xAxis: {
						type: 'time',
						// interval: 6 * 3600 * 1000, // 间隔为6小时
						min: `${this.time + ' 00:00:00'}`, // x轴起始时间
						max: `${this.time + ' 23:04:00'}`, // x轴结束时间
						boundaryGap: false,
						axisTick: {
							show: false
						},
						axisLine: {
							lineStyle: {
								color: 'rgb(238,238,238)',
								width: 1
							}
						},
						axisLabel: {
							textStyle: {
								color: "#666"
							},
							formatter: function(val) {
								return (uni.$u.timeFormat(new Date(val), 'hh:MM'))
							}
						},
						splitLine: {
							show: false
						},
					},
					yAxis: {
						type: 'value',
						min: '0', 
						scale: true,
						splitArea: {
							show: true,
							areaStyle: {
								color: ['#f6f8fc', '#fff']
							}
						},
						axisLine: {
							show: false
						},
						axisTick: {
							show: false
						},
						splitLine: {
							lineStyle: {
								type: "dashed",
								color: "#E9E9E9"
							}
						},
					},
					series: [{
							name: '收缩压',
							data: this.spMapList,
							type: 'bar',
							showSymbol: false,
							itemStyle: {
								color: '#FF7E23'
							},
							barWidth: '10', //---柱形宽度
							barCategoryGap: '20%', //---柱形间距
						},
						{
							name: '舒张压',
							data: this.dpMapList,
							type: 'bar',
							showSymbol: false,
							itemStyle: {
								color: '#63DDBA'
							},
							barWidth: '10', //---柱形宽度
							barCategoryGap: '20%', //---柱形间距
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
				width: 100%;
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

	.myChart {
		width: 90%;
		height: 500rpx;
		margin: 64rpx 32rpx 20rpx;
	}

	.ui-statistics {
		margin: 32rpx 0;
		padding: 0 32rpx;

		.statistics-box {
			display: flex;
			flex-direction: column;
			border-radius: 16rpx;
			background-color: #FFF;

			.ui-date-title {
				margin-top: 32rpx;
				margin-left: 20rpx;
				font-size: 36rpx;
				color: #353535;
				letter-spacing: 0;
				font-weight: 500;
			}

			.ui-window {
				margin-bottom: 36rpx;
				display: flex;
				align-items: center;
				flex-wrap: wrap;

				.ui-statistics-content {
					width: 50%;
					margin-top: 48rpx;
					display: flex;
					flex-direction: column;
					align-items: center;

					.ui-content-title {
						font-size: 26rpx;
						color: #353535;
						letter-spacing: 0;
						font-weight: 400;
					}

					.ui-statistics-font {
						font-size: 72rpx;
						margin-right: 10rpx;
						color: #353535;
						letter-spacing: 0;
						font-weight: 700;
					}

					.ui-statistics-fonts {
						font-size: 26rpx;
						color: #888888;
						letter-spacing: 0;
						font-weight: 400;
					}
				}
			}
		}
	}
</style>