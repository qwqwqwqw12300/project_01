<template>
	<view>
		<!-- <app-echarts :option="option" id="myChart" class="myChart"></app-echarts> -->
		<!-- <view class="ui-content">
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
		</view> -->
		<view class="ui-show">
			<text>{{nowDataMin}}
				<text v-if="nowDataMax">-</text>
				{{nowDataMax}}mmHg</text>
		</view>
		<app-echarts :option="option" @click="handleClick" id="myChart" class="myChart"
			@Cclick="clickEchart"></app-echarts>
		<!-- <view class="ui-statistics">
			<view class="statistics-box">
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
		</view> -->
	</view>
</template>

<script>
	import * as echarts from '@/static/js/echarts.js';
	import {
		// GetListBloodPressureByWeek
		GetMBloodPressure
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
						value: '0'
					},
					{
						color: '#63DDBA',
						title: '舒张压',
						value: '0'
					}
				],
				dateList: [{
						title: '平均收缩压',
						value: '0'
					},
					{
						title: '平均舒张压',
						value: '0'
					}
				],
				spMapList: [], //收缩压
				dpMapList: [], //舒张压,
				nowDataMax: '', //实时数据
				nowDataMin: '',
				Data: [],
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
				immediate: true //监听到数据立即执行
			}
		},
		methods: {
			clickEchart(val) {
				console.log(val, 'val');
				const res = this.Data.find(item => {
					return item[0] == val.value
				})
				this.nowDataMin = res[1]
				this.nowDataMax = res[2]
			},
			handleClick(option) {
				console.log(option, 'option')
				option.seriesName == '收缩压' ? this.list[0].value = option.value[1] : this.list[1].value = option.value[1]
			},
			logstatrt() {
				this.spMapList = [] //血压min值
				this.dpMapList = [] //血压max-min值
				GetMBloodPressure({
					beginDate: this.time[0],
					endDate: this.time[6],
					deviceId: this.deviceInfo.deviceId,
					humanId: this.deviceInfo.humanId
				}).then(res => {
					console.log(res, 'res')
					// this.list[0].value = res.data.spAvg
					// this.list[1].value = res.data.dpAvg
					// this.dateList[0].value = res.data.spAvg
					// this.dateList[1].value = res.data.dpAvg
					this.Data = res.data.MapList.map(n => { //获取当前周的所有max值
						return [n.time, n.valueMin, n.valueMax]
					})
					for (let i = 0; i < res.data.MapList.length; i++) {
						this.spMapList.push([
							res.data.MapList[i].time,
							res.data.MapList[i].valueMin
						])
					}
					for (let i = 0; i < res.data.MapList.length; i++) {
						this.dpMapList.push([
							res.data.MapList[i].time,
							res.data.MapList[i].valueMax - res.data.MapList[i].valueMin
						])
					}
				})
				this.option = {
					title: {
						text: ''
					},
					tooltip: {
						positionStatus: true,
						showContent: false,
						trigger: 'axis',
						axisPointer: {
							type: 'line',
							lineStyle: {
								type: 'dashed', //指示线为虚线
							},
						},
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
						boundaryGap: false,
						interval: 24 * 3600 * 1000,
						min: new Date(`${this.time[0] + ' 00:00:00'}`), // x轴起始时间
						max: new Date(`${this.time[6] + ' 00:00:00'}`), // x轴结束时间
						axisTick: {
							show: false
						},
						axisPointer: {
							handle: {
								show: true,
								size: 18,
								margin: 2,
								throttle: 5,
								color: '#ffac4a'
							}
						},
						axisLine: {
							lineStyle: {
								color: 'rgb(238,238,238)',
								width: 1
							}
						},
						axisLabel: {
							margin: 13,
							textStyle: {
								color: "#666"
							},
							formatter: 'week'
						},
						splitLine: {
							show: false
						},
					},
					yAxis: {
						type: "value",
						min: '0',
						max: '200',
						scale: true,
						splitArea: {
							show: true,
							areaStyle: {
								color: ['#f6f8fc', '#fff']
							}
						},
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
							barWidth: '25%',
							name: '收缩压',
							type: 'bar',
							stack: 'Total',
							data: this.spMapList,
							showSymbol: false,
							itemStyle: {
								// normal: {
								// 	lineStyle: {
								// 		color: "#FF7E23",
								// 		width: 1
								// 	}
								// },
								borderColor: 'transparent',
								color: 'transparent',
							},
						},
						{
							name: '舒张压',
							type: 'bar',
							stack: 'Total',
							data: this.dpMapList,
							showSymbol: false,
							itemStyle: {
								normal: {
									barBorderRadius: 30,
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
	.ui-show {
		margin-top: 20rpx;
		text-align: center;
		font-size: 72rpx;
		color: #353535;
		font-weight: 700;

	}

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
				margin-bottom: 84rpx;
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