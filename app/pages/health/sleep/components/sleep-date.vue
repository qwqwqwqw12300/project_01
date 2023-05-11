<template>
	<view>
		<view class="ui-legend">
			<view class="item" v-for="(item,key) in lengedList" :key="key">
				<text :style="{background: item.color }"></text>{{ item.text }}
			</view>
			<!-- 	<text class="item">深睡</text>
			<text class="item">快速眼动</text> -->
		</view>
		<view class="ui-echart">
			<app-echarts :option="option" id="myChart" class="echart-box"></app-echarts>
		</view>
		<view class="ui-time">
			<text>00:00</text>
			<text>06:00</text>
			<text>12:00</text>
			<text>18:00</text>
			<text>23:59</text>
		</view>
		<WatchDiv :text="'睡眠得分'" :content="'0'"></WatchDiv>
		<view class="ui-content">
			<view class="ui-title" v-for="(item,index) in sleepList" :key="index">
				<view class="ui-title-content">
					<view class="ui-left">
						<view class="ui-circle" :style="{backgroundColor:item.color}"></view>
						<view class="ui-left-title">{{item.title}}</view>
					</view>
					<view class="ui-right" @click="jumpUrl(item.text)">
						<view style="margin-right: 20rpx;">参考值:{{item.value}}</view>
						<u-icon name="arrow-right" size="22rpx"></u-icon>
					</view>
				</view>
				<view class="ui-time">
					<view>
						<text class="ui-font">0</text>
						<text class="ui-time-font">小时</text>
					</view>
					<view style="margin-left: 10rpx;">
						<text class="ui-font">0</text>
						<text class="ui-time-font">分钟</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import WatchDiv from '@/components/watch-div/watch-div.vue'
	import {
		GetSleepDayData,
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
				type: String
			}
		},
		data() {
			return {
				lengedList: [{
					text: '浅睡',
					color: '#EF7B8C'
				}, {
					text: '深睡',
					color: '#BF47CA'
				}, {
					text: '快速眼动',
					color: '#8538E0'
				}],
				option: {},
				sleepList: [{
						color: '#68D688',
						title: '总睡眠时长',
						text: '夜间睡眠',
						value: '6-10h'
					},
					{
						color: '#8437DA',
						title: '深睡 0%',
						text: '深睡',
						value: '10-40%'
					},
					{
						color: '#C145C9',
						title: '浅睡 0%',
						text: '浅睡',
						value: '45-80%'
					},
					{
						color: '#EFC356',
						title: '清醒时长 0%',
						text: '清醒',
						value: '<10%'
					}
				]
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
					if (value) {
						this.handleInit()
					}
				},
				immediate:true
			}
		},
		methods: {
			jumpUrl(name) {
				uni.navigateTo({
					url: `/pages/health/sleep/info?name=${name}`
				})
			},
			handleInit() {
				const {
					deviceId,
					humanId,
				} = this.deviceInfo
				GetSleepDayData({
					deviceId,
					humanId,
					dayTime: this.dayTime
				}).then(res => {
					const series = [
						[],
						[],
						[]
					]
					res.data.segSleepList.forEach(n => {
						const st = uni.$u.timeFormat(n.st, 'hh:MM')
						const et = uni.$u.timeFormat(n.et, 'hh:MM')
						const index = {
							'2': 0,
							'3': 1,
							'4': 2
						} [n.slt]
						series[index].push(['', st, et])
					})
					console.log(series, 'ssssssss--------------')
					this.logstatrt(series)
				})
			},
			logstatrt(series) {
				const types = ['浅睡', '深睡', '快速眼动']
				// const series = [
				// 	[
				// 		["", "00:00", "01:35"],
				// 		["", "02:00", "04:35"],
				// 		["", "18:00", "20:35"],
				// 	],
				// 	[
				// 		["00:00~04:45", "08:35", "12:35"],
				// 		["06:00~07:35", "14:00", "18:00"],
				// 	],
				// 	[
				// 		["09:01~09:35", "04:35", "08:35"],
				// 		["05:09~07:35", "16:00", "20:00"],
				// 	],
				// ]
				const colors = ['#EF7B8C', '#BF47CA', '#8538E0']
				const dataSeries = types.map((item, index) => {
					return {
						type: 'custom',
						name: item,
						label: {
							show: true
						},
						encode: {
							x: [1, 2],
							// label: [0]
						},
						itemStyle: {
							color: colors[index]
						},
						data: series[index],
						renderItem: function(params, api) {
							const start = api.coord([api.value(1), params.seriesName])
							const end = api.coord([api.value(2), params.seriesName])
							const height = api.size([1, 1])[1]
							const width = end[0] - start[0]

							var rectShape = echarts.graphic.clipRectByRect({
								x: start[0],
								y: start[1] - height / 2,
								width: width,
								height: height
							}, {
								x: params.coordSys.x,
								y: params.coordSys.y,
								width: params.coordSys.width,
								height: params.coordSys.height
							});

							return rectShape && {
								type: 'rect',
								shape: rectShape,
								style: api.style()
							}
						}
					}
				})
				this.option = {
					title: {
						text: ''
					},
					tooltip: {
						// trigger: 'axis'
					},
					isRender: true,
					backgroundColor: '#fff',
					//取消坐标轴
					grid: {
						left: '0',
						right: '0',
						bottom: '5',
						top: '0',
						// containLabel:false
					},
					xAxis: {
						type: 'category',
						// boundaryGap: false,
						axisLabel: {
							show: true
						},
						axisTick: {
							show: false
						},
						axisLine: {
							show: false
						},
						data: this.makeXAxis()
					},
					yAxis: {
						type: 'category',
						splitLine: {
							show: true
						},
						axisLabel: {
							show: true
						},
						axisTick: {
							show: false
						},
						axisLine: {
							show: false
						},

						data: types
					},
					series: dataSeries,
				}
			},
			makeXAxis() {
				const axis = []
				for (let i = 0; i < 24; i++) {
					for (let j = 0; j < 60; j++) {
						const str = (i >= 10 ? i : ('0' + i)) + ':' + (j >= 10 ? j : ('0' + j))
						axis.push(str)
					}
				}
				return axis
			}
		}
	}
</script>

<style lang="scss" scoped>
	.box {
		margin-top: 68rpx;
	}

	.ui-icon {
		margin-left: 546rpx;
	}

	.myChart {
		width: 90%;
		height: 200rpx;
		margin: 64rpx 32rpx 20rpx;
	}

	.ui-legend {
		width: 360rpx;
		margin: 0 auto;
		margin-top: 30rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;

		.item {
			display: flex;
			align-items: center;
			font-size: 24rpx;

			text {
				width: 20rpx;
				height: 20rpx;
				border-radius: 100rpx;
				// background: #EF7B8C;
				margin-right: 10rpx;
			}

			// width: 80rpx;
			// height: 40rpx;
			// padding: 8rpx 20rpx;
			// text-align: center;
			// background: #EF7B8C;
			// font-size: 22rpx;
			// color: #fff;
			// border-radius: 10rpx;
		}
	}

	.ui-echart {
		margin-top: 20rpx;
		padding: 0 32rpx;

		.echart-box {
			// width: 100%;
			height: 300rpx;
		}
	}

	.ui-time {
		width: 90%;
		font-size: 24rpx;
		color: #888;
		margin: 0 auto;
		margin-bottom: 32rpx;
		margin-top: 10rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		letter-spacing: 0;
		font-weight: 400;
	}

	.ui-content {
		width: 90%;
		height: 728rpx;
		margin: 0 auto;
		margin-top: 20rpx !important;
		background-color: #FFFFFF;
		border-radius: 16rpx;

		.ui-title {
			width: 92%;
			height: 136rpx;
			margin: 0rpx 20rpx;
			margin-bottom: 48rpx !important;
			display: flex;
			align-items: center;
			flex-direction: column;
			justify-content: space-between;

			.ui-title-content {
				width: 100%;
				margin-top: 32rpx;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.ui-left {
					display: flex;
					align-items: center;
					justify-content: center;

					.ui-circle {
						width: 30rpx;
						height: 30rpx;
						margin-right: 10rpx;
						border-radius: 50rpx;
					}

					.ui-left-title {
						font-size: 34rpx;
						color: #353535;
						letter-spacing: 0;
						font-weight: 400;
					}
				}

				.ui-right {
					font-size: 34rpx;
					color: #888888;
					font-weight: 400;
					display: flex;
					align-items: center;
					justify-content: center;
				}
			}

			.ui-time {
				margin-top: 20rpx;
				// background-color: pink;
				display: flex;
				align-items: center;
				justify-content: flex-start;

				.ui-font {
					font-size: 56rpx;
					color: #353535;
					letter-spacing: 0;
					font-weight: 700;
				}

				.ui-time-font {
					font-size: 26rpx;
					color: #888888;
					letter-spacing: 0;
					font-weight: 400;
				}
			}
		}
	}
</style>