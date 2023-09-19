<template>
	<view>
		<!-- <view class="ui-totalTime">00:00</view> -->
		<view class="ui-time">
			<view>
				<!-- <text class="ui-font">{{ totalData.hour }}</text> -->
				<text class="ui-time-font" v-if="hour"> {{hour}} 时</text>

			</view>
			<view style="margin-left: 10rpx;">
				<!-- <text class="ui-font">{{ totalData.minutes }}</text> -->
				<text class="ui-time-font" v-if="hour">{{minute}}分</text>
			</view>
		</view>
		<!-- <view class="ui-title">
			<view class="ui-circle"></view>
			<view class="ui-title-font">睡眠时长</view>
		</view> -->
		<view class="ui-window">
			<!-- <view>
				<view class="ui-content" v-for="(item,index) in sleepList" :key="index">
					<view class="ui-content-title">
						<view class="ui-content-circle" :style="{backgroundColor:item.color}"></view>
						<view class="ui-content-font">{{item.title}}</view>
					</view>
				</view>
			</view> -->
			<app-echarts :option="option" id="myChart" class="myChart" @Cclick="clickEchart"></app-echarts>
		</view>
		<WatchDiv :text="'日均睡眠得分'" :content="sleepScore"></WatchDiv>
	</view>
</template>

<script>
	import WatchDiv from '@/components/watch-div/watch-div.vue'
	import {
		GetSleepWeekData,
	} from '@/common/http/api.js'
	import {
		mapState,
	} from 'vuex';
	export default {
		components: {
			WatchDiv
		},
		props: {
			dayTime: {
				type: Array,
				default: () => ([])
			}
		},
		data() {
			return {
				arr: ['1', '2', '3', '4'],
				option: {},
				sleepList: [{
						color: '#F1C44F',
						title: '清醒'
					},
					{
						color: '#EF7B8C',
						title: '快速眼动'
					},
					{
						color: '#BF47CA',
						title: '浅睡'
					},
					{
						color: '#8538E0',
						title: '深睡'
					}
				],
				sleepData: {
					1: [], //清醒
					2: [], //浅睡
					3: [], //深睡
					4: [] //眼动
				},
				totalData: {
					hour: 0,
					minutes: 0,
				},
				sleepScore: 0,
				Data: [],
				spMapList: [],
				hour: '',
				minute: ''
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}),

		},
		created() {
			// this.handleInit()
			// this.logstatrt();
		},
		watch: {
			dayTime: {
				handler(value) {
					if (value.length) {
						this.handleInit()
					}
				},
				immediate: true,
				deep: true,
			}
		},
		methods: {
			clickEchart(val) {
				// console.log(val, 'val');
				const res = this.Data.find(item => {
					return item[0] == val.value
				})

				this.hour = Math.floor(res[1] / 60)
				console.log(this.hour, 'aaaaaaaaaa');
				this.minute = res[1] % 60
				// console.log(this.minute);
			},
			handleInit() {
				const {
					deviceId,
					humanId,
				} = this.deviceInfo
				GetSleepWeekData({
					deviceId,
					humanId,
					beginDate: this.dayTime[0],
					endDate: this.dayTime[this.dayTime.length - 1]
				}).then(res => {
					// const {
					// 	sleepSegmentationDataList,
					// 	sleepScore,
					// 	countTime
					// } = res.data
					// this.sleepScore = sleepScore
					// this.totalData.hour = countTime[0]
					// this.totalData.minutes = countTime[1] % 60
					// const data = sleepSegmentationDataList.map(n => {
					// 	n.differTime = Number(this.getDifferTime(n.st, n.et).toFixed(2))
					// 	n.time = uni.$u.timeFormat(n.st, 'yyyy-mm-dd')
					// 	return n
					// })
					this.Data = res.data.MapList.map(n => { //获取当前周的所有max值
						return [n.time, n.slt]
					})
					this.spMapList = []
					for (let i = 0; i < res.data.MapList.length; i++) {
						this.spMapList.push([
							res.data.MapList[i].time,
							res.data.MapList[i].slt
						])
					}
					console.log(this.spMapList, 'a啊啊啊啊啊啊啊啊啊啊');
					// this.dayTime.forEach((n, index) => {
					// 	const dayData = data.filter(item => {
					// 		return item.time === n
					// 	})
					// 	this.arr.forEach(e => {
					// 		this.sleepData[e][index] = dayData.reduce((p, c) => {
					// 			return p + ((c.slt === e) ? c.differTime : 0)
					// 		}, 0).toFixed(2)
					// 	})
					// })

					this.logstatrt()

				})
			},
			logstatrt() {
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
					xAxis: [{
						type: 'time',
						boundaryGap: false,
						interval: 24 * 3600 * 1000,
						data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日', ],
						axisTick: {
							show: false
						},
						axisPointer: {
							handle: {
								show: false,
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
					}],
					yAxis: {
						type: "value",
						min: '0',
						max: '720',
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
						barWidth: '25%', //柱形图宽度
						type: 'bar',
						stack: 'Total',
						// label: {
						// 	show: true,
						// 	position: 'top'
						// },
						showSymbol: false,
						itemStyle: {
							normal: {
								color: 'rgba(255,89,89,1)',
								barBorderRadius: 30,
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
						data: this.spMapList
					}]
				};
			},
			getDifferTime(start, end) {
				const st = new Date(start)
				const et = new Date(end)
				return (et - st) / 1000 / 60 / 60
			}

		}
	}
</script>

<style lang="scss" scoped>
	.ui-time {
		margin-top: 20rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 42rpx;
		color: #353535;
		letter-spacing: 0;
		font-weight: 550;
	}

	.ui-title {
		margin-top: 20rpx;
		margin-bottom: 58rpx;
		display: flex;
		align-items: center;
		justify-content: center;

		.ui-circle {
			width: 30rpx;
			height: 30rpx;
			margin-right: 10rpx;
			border-radius: 50rpx;
			background-color: #68D688;
		}

		.ui-left-title {
			font-size: 34rpx;
			color: #353535;
			letter-spacing: 0;
			font-weight: 400;
		}
	}

	.ui-window {
		margin-bottom: 64rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 16px;

		.ui-content {
			width: 48%;
			margin-left: 56rpx;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;

			.ui-content-title {
				margin-top: 20rpx;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;

				.ui-content-circle {
					width: 30rpx;
					height: 30rpx;
					margin-bottom: 10rpx;
					border-radius: 50rpx;
				}

				.ui-content-font {
					font-size: 34rpx;
					color: #353535;
					letter-spacing: 0;
					font-weight: 400;
				}
			}
		}

		.myChart {
			width: 90%;
			height: 400rpx;
			padding: 64rpx 32rpx 20rpx;
			background-color: #FFFFFF;
		}
	}
</style>