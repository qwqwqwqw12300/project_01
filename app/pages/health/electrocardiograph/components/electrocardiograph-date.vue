<template>
	<view>
		<app-echarts :option="option" id="myChart" class="myChart"></app-echarts>
		<view class="ui-content">
			<view class="ui-content-left">增长</view>
			<view class="ui-content-right">
				10mm/mv
				<u-icon name="arrow-right" size="28rpx" ></u-icon>
			</view>
		</view>
		<view class="ui-content" style="margin-top: 48rpx;">
			<view class="ui-content-left">移动速度</view>
			<view class="ui-content-right">
				25mm/s
				<u-icon name="arrow-right" size="28rpx" ></u-icon>
			</view>
		</view>
		<view class="ui-window" v-for="(item,index) in cellList" :key="index">
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
				<u-input v-model="item.vlaue" border="none" class="ui-input"></u-input>
			</view>
		</view>
	</view>
</template>

<script>
	import * as echarts from '@/static/js/echarts.js';
	export default {
		data() {
			return {
				cellList:[{
					time:'21:31',
					num:'90',
					value:'',
					input:false
				},
				{
					time:'21:30',
					num:'90',
					value:'',
					input:false
				},
				{
					time:'21:29',
					num:'90',
					value:'',
					input:false
				}]
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
		margin: 64rpx 32rpx 48rpx;
	}
	.ui-content{
		display: flex;
		align-items: center;
		.ui-content-left{
			margin-left: 48rpx;
		}
		.ui-content-right{
			margin-left:100rpx;
			display: flex;
			align-items: center;
			font-weight: bold;
		}
	}
	.ui-window{
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
		.ui-row{
			margin: 32rpx 20rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			.ui-row-num{
				font-size: 50rpx;
				font-weight: bold;
				margin-right: 10rpx;
			}
			.ui-row-right{
				font-size: 32rpx;
				padding: 5rpx 20rpx;
				border: 1px black solid;
				border-radius: 50rpx;
			}
		}
		.ui-input-font{
			margin-left: 20rpx;
			margin-bottom: 20rpx;
			font-size: 30rpx;
		}
		.ui-input{
			width: 90%;
			margin: 0 auto;
		}
	}
	
</style>
