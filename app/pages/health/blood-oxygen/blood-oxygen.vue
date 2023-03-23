<!-- 血氧 -->
<template>
	<app-body :bodyStyle="{background: '#F7F7F7' }">
		<app-logo text="血氧" top="36rpx" :isShare="true"></app-logo>
		<view class="ui-tab">
			<date-picker @onSelect="onSelect"></date-picker>
		</view>
		<view class="ui-show">
			<text>95%</text>
		</view>
		<view class="ui-echart">
			<app-echarts :option="options" id="myChart" class="echart-box"></app-echarts>
		</view>
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
	</app-body>
</template>

<script>
	export default {
		data() {
			const options = {
				tooltip: {
					trigger: 'axis'
				},
				grid: {
					left: '5%',
					right: '5%',
					bottom: '3%',
					containLabel: true
				},
				xAxis: [{
					type: 'category',
					boundaryGap: false,
					axisTick: { //坐标轴刻度相关设置。
						show: false,
					},
					axisLabel: {
						textStyle: {
							color: "#666"
						}
					},
					axisLine: {
						lineStyle: {
							color: 'rgb(238,238,238)',
							width: 1
						}
					},
					data: ['00:00', '06:00', '12:00', '18:00', '23:59']
				}, ],
				yAxis: [{
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
				}],
				series: [{
					type: 'line',
					showSymbol: false,
					itemStyle: {
						normal: {
							lineStyle: {
								color: "#36BFFF",
								width: 2
							},
						}
					},
					data: [1, 2, 3, 3, 5, 6, 5, 3, 6, 5, 5, 4]
				}]
			}
			return {
				totalList: [{
						num: 80,
						title: '日平均值'
					},
					{
						num: 60,
						title: '日最高值'
					},
					{
						num: 100,
						title: '日最低值'
					}
				],
				options,
			}
		},
		methods: {
			onSelect() {

			}
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

	.ui-tab {
		margin-top: 64rpx;
	}

	.ui-echart {
		// margin-top: 20rpx;

		.echart-box {
			width: 100%;
			height: 500rpx;
		}
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
