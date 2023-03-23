<template>
	<app-body :bodyStyle="{background: '#F7F7F7' }">
		<app-logo text="心率" top="36rpx" :isShare="true"></app-logo>
		<view class="ui-tab">
			<date-picker @onSelect="onSelect"></date-picker>
		</view>
		<view class="ui-echart">
			<app-echarts :option="options" id="myChart" class="echart-box"></app-echarts>
		</view>
		<view class="ui-total">
			<view class="total-item" v-for="(item,index) in totalList" :key="index">
				<view class="item-data">
					<text class="num">{{ item.num }}</text>
					<text class="unit">bpm</text>
				</view>
				<view class="item-title">
					{{ item.title }}
				</view>
			</view>
		</view>
		<view class="ui-detail">
			<view class="title">
				运动心率
			</view>
			<view class="content">
				<view class="info-item" v-for="(item,index) in dataList" :key="index">
					<view class="process">
						<u-line-progress :percentage="item.value" :activeColor="item.activeColor"
							:inactiveColor="item.inactiveColor">
						</u-line-progress>
						<view class="sub">
							<text>{{ item.time }}</text>分钟
						</view>
					</view>
					<view class="data process">
						<text class="left">{{ item.title }}</text>
						<text class="right">{{ item.unit }}bpm</text>
					</view>
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
							color: 'rgba(255,89,89,1)',
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
					data: [1, 2, 3, 3, 5, 6, 5, 3, 6, 5, 5, 4]
				}]
			}
			return {
				totalList: [{
						num: 80,
						title: '平均'
					},
					{
						num: 60,
						title: '最低'
					},
					{
						num: 100,
						title: '最高'
					}
				],
				dataList: [{
						value: 30,
						title: '常规心率',
						time: '80',
						unit: '35-95',
						activeColor: '#cccccc',
						inactiveColor: "#F2F2F2"
					}, {
						value: 40,
						title: '热身运动',
						time: '80',
						unit: '96-144',
						activeColor: '#63CDA9',
						inactiveColor: "#A3EED4"
					}, {
						value: 50,
						title: '脂肪燃烧',
						time: '80',
						unit: '96-144',
						activeColor: '#75B2FF',
						inactiveColor: "#DDECFF"
					}, {
						value: 60,
						title: '有氧运动',
						time: '80',
						unit: '96-144',
						activeColor: '#F8DA68',
						inactiveColor: "#FFF5D0"
					}, {
						value: 70,
						title: '无氧运动',
						time: '80',
						unit: '96-144',
						activeColor: '#FF7DCB',
						inactiveColor: "#FFDCF1"
					},
					{
						value: 80,
						title: '极限心率',
						time: '80',
						unit: '96-144',
						activeColor: '#FF5A5A',
						inactiveColor: "#FFD5D5"
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
		display: flex;
		justify-content: space-between;

		.total-item {
			box-sizing: content-box;
			padding: 10rpx 20rpx;
			background: #FFFFFF;
			border-radius: 8px;

			.item-data {
				width: 150rpx;

				.num {
					font-size: 50rpx;
					color: #353535;
					font-weight: 700;
				}

				.unit {
					margin-left: 14rpx;
					font-size: 26rpx;
					color: #353535;
				}
			}

			.item-title {
				font-size: 24rpx;
				color: #888888;
			}
		}
	}

	.ui-detail {
		padding: 0 32rpx;
		// margin-top: 30rpx;

		.title {
			height: 100rpx;
			line-height: 100rpx;
			font-size: 32rpx;
			color: #353535;
			font-weight: 550;
		}

		.content {
			background: #FFFFFF;
			border-radius: 16rpx;
			padding: 10rpx;

			.info-item {
				padding: 0 10rpx;
				margin-bottom: 20rpx;

				.process {
					display: flex;
					align-items: center;
					justify-content: space-between;
					height: 60rpx;

					.sub {
						width: 200rpx;
						text-align: right;
						font-size: 26rpx;
						color: #888888;

						text {
							margin-right: 10rpx;
							font-size: 50rpx;
							font-weight: 700;
							color: #353535;
						}
					}
				}

				.data {
					height: 40rpx !important;

					.left {
						font-size: 26rpx;
						color: #353535;
					}

					.right {
						font-size: 26rpx;
						color: #888888;
					}
				}
			}
		}
	}
</style>
