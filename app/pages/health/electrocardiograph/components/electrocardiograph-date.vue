<template>
	<view>
		<view class="ui-echart">
			<app-echarts :option="option" id="myChart" class="myChart"></app-echarts>
		</view>
		<view class="ui-content">
			<view class="ui-content-left">增长</view>
			<view class="ui-content-right">
				10mm/mv
				<!-- <u-icon name="arrow-right" size="28rpx"></u-icon> -->
			</view>
		</view>
		<view class="ui-content" style="margin-top: 48rpx;">
			<view class="ui-content-left">移动速度</view>
			<view class="ui-content-right">
				25mm/s
				<!-- <u-icon name="arrow-right" size="28rpx"></u-icon> -->
			</view>
		</view>
		<view class="ui-window" v-for="(item,index) in cellList" :key="index" @click="handleChange(item)">
			<view class="ui-window-content">
				<view class="ui-circle"></view>
				<view class="ui-window-font">{{item.time}}</view>
			</view>
			<view class="ui-row">
				<view>
					<text class="ui-row-num">{{item.num || '--'}}</text>
					<text>bpm</text>
				</view>
				<view class="ui-row-right" @click="item.input = true">
					备注
				</view>
			</view>
			<view style="height: 150rpx;" v-if="item.input">
				<view class="ui-input-font">心电信号弱，请重新测量</view>
				<u-input v-model="item.value" border="none" class="ui-input"
					@blur="handleBlur(item.idElectrocardiogram,item.value)"></u-input>
			</view>
		</view>
		<view style="margin-top: 30rpx;width: 100%;height: 30rpx;"></view>
	</view>
</template>

<script>
	import * as echarts from '@/static/js/echarts.js';
	import {
		GetListElectrocardiogramByDay,
		PostAddElectrocardiogramReMake
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
				cellList: [{
						time: '21:31',
						num: '90',
						value: '',
						input: false
					},
					{
						time: '21:30',
						num: '90',
						value: '',
						input: false
					}
				],
				option: {}, //echart配置
				mapList: [], //echart series数组
				dataList: Array.from(Array(91).keys()),
				count: 90, //计数器
				allCount: 0, //总数组长度
				interval: null
			}
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
		onHide() {
			this.interval && clearInterval(this.interval)
		},
		destroyed() {
			this.interval && clearInterval(this.interval)
		},
		methods: {
			logstatrt() {
				GetListElectrocardiogramByDay({
					deviceId: this.deviceInfo.deviceId,
					dayTime: this.time,
					humanId: this.deviceInfo.humanId
				}).then(res => {
					console.log(res, 'res')
					this.cellList = res.data.MapList.map(item => {
						return {
							idElectrocardiogram: item.idElectrocardiogram,
							time: uni.$u.timeFormat(item.time, 'hh:MM'),
							list: item.value.split(","),
							num: 0,
							value: item.remark,
							input: item.remark != '' ? true : false
						}
					})
					clearInterval(this.interval)
					if (this.cellList.length == 0) return this.setOption(this.cellList)
					this.handleChange(this.cellList[0])
				})
			},
			setOption(data) {
				this.option = {
					title: {
						text: ''
					},
					backgroundColor: '#fff',
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
						top: '0',
						containLabel: true
					},
					toolbox: {},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						data: this.dataList,
						axisTick: {
							show: false
						},
						axisLabel: {
							show: false
						},
						axisLine: {
							show: false
						},
					},
					yAxis: {
						type: 'value',
						min: '-3000',
						max: '3000',
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
							lineStyle: {
								type: 'disable'
							}
						},
					},
					series: [{
						name: 'Union Ads',
						type: 'line',
						stack: 'Total',
						data,
						showSymbol: false,
						itemStyle: {
							normal: {
								lineStyle: {
									color: "#63DDBA",
									width: 1
								}
							}
						},
					}]
				};
			},
			handleChange(val) {
				clearInterval(this.interval)
				const list = [...val.list].filter(ele => Number(ele) || ele === 0); // 当前项列表
				this.count = 90; // 初始化展示数量
				this.allCount = list.length; //  当前项长度
				this.mapList = list.slice(0, this.count); // 实际渲染数组
				this.interval = setInterval(() => {
					if (this.count == this.allCount) {
						this.count = 90;
						this.mapList = list.slice(0, this.count);
						// return
					} else {
						this.mapList.shift()
						this.mapList.push(list[++this.count])
					}
					this.setOption(this.mapList);
				}, 30)
			},
			handleBlur(id, val) {
				PostAddElectrocardiogramReMake({
					idElectrocardiogram: parseInt(id),
					remake: val
				}).then(res => {
					console.log(res, 'res')
					uni.$u.toast(res.msg)
					setTimeout(() => {
						this.logstatrt()
					}, 500);
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-echart {
		padding: 0 32rpx;
		margin-top: 30rpx;

		.myChart {
			height: 500rpx;
			// margin: 64rpx 32rpx 48rpx;
		}
	}

	.ui-content {
		margin-top: 30rpx;
		display: flex;
		align-items: center;

		.ui-content-left {
			margin-left: 48rpx;
		}

		.ui-content-right {
			margin-left: 32rpx;
			display: flex;
			align-items: center;
			font-weight: bold;
		}
	}

	.ui-window {
		width: 90%;
		// height: 200rpx;
		margin: 0 auto;
		border-radius: 20rpx;
		margin-top: 20rpx !important;
		display: flex;
		flex-direction: column;
		background-color: #FFF;

		.ui-window-content {
			display: flex;
			margin-left: 20rpx;
			margin-top: 32rpx;

			.ui-circle {
				width: 30rpx;
				height: 30rpx;
				margin-right: 10rpx;
				border-radius: 50rpx;
				background-color: #fd993f;
			}

			.ui-window-font {
				font-size: 26rpx;
				color: #353535;
				letter-spacing: 0;
				font-weight: 400;
			}
		}

		.ui-row {
			margin: 32rpx 20rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.ui-row-num {
				font-size: 50rpx;
				font-weight: bold;
				margin-right: 10rpx;
			}

			.ui-row-right {
				font-size: 32rpx;
				padding: 5rpx 20rpx;
				border: 1px black solid;
				border-radius: 50rpx;
			}
		}

		.ui-input-font {
			margin-left: 20rpx;
			margin-bottom: 20rpx;
			font-size: 30rpx;
		}

		.ui-input {
			width: 90%;
			margin: 0 auto;
		}
	}

	.ui-window:hover {
		background-color: lightgray;
	}
</style>
