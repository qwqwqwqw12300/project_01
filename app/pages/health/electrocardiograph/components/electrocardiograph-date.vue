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
				<u-input v-model="item.vlaue" border="none" class="ui-input"></u-input>
			</view>
		</view>
	</view>
</template>

<script>
	import * as echarts from '@/static/js/echarts.js';
	import {
		GetListElectrocardiogramByDay
	} from '@/common/http/api';
	export default {
		props:{
			time:{
				default:''
			}
		},
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
				}],
				option:{},//echart配置
				mapList:[],//echart series数组
				dataList:[],//erchart x轴数组
				lenList:[],//定时器处理数组
				count:90,//计数器
				allCount:0//总数组长度
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
				immediate:true //监听到数据立即执行
			}
		},
		methods: {
			logstatrt() {
				GetListElectrocardiogramByDay({
					deviceId:240,
					dayTime:this.time,
					humanId:'116'
				}).then(res=>{
					console.log(res,'res')
					let list = []
					this.cellList = res.data.MapList.map(item=>{
						return {
							time:uni.$u.timeFormat(item.time, 'hh:MM'),
							list:item.value.split(","),
							num:0,
							value:'',
							input:false
						}
					})

					console.log(this.cellList,'cellList')
					this.cellList[0].list.forEach(item=>{
						list.push(item)
					})
					// console.log(list.slice(90,list.length),'list')
					this.allCount = list.length
					for(let i =0;i<this.count;i++){
						this.mapList.push(list[i])
					}
					this.lenList = list.slice(this.count,list.length)
					let len = this.lenList.length
					// setInterval(()=>{
					// 	console.log('定时器----------------------')
					// 	if(this.count == this.allCount){
					// 		return
					// 	}else{
					// 		for(let i =0;i<this.lenList.length;i++){
					// 			this.count++
					// 			this.mapList.push(this.lenList[i])
					// 			this.mapList.shift()
					// 			return
					// 		}
					// 	}
					// 	this.dataList.push(len++)
					// 	this.dataList.shift()
					// },100)
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
							top:'20',
							containLabel: true
						},
						toolbox: {
						},
						xAxis: {
							type: 'category',
							boundaryGap: false,
							data:this.dataList,
							axisTick: {
								show: false
							},
							axisLabel: {
								show:false
							},
							axisLine: {
								show: false
							},
						},
						yAxis: {
							type: 'value',
							min:'-1000',
							max:'1000',
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
								data: this.mapList,
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
				})
			},
			handleChange(val){
				console.log(val,'val')
				let list = []
				this.mapList = []
				this.lenList = []
				this.count = 90
				this.allCount = 0
				val.list.forEach(item=>{
					list.push(item)
				})
				// console.log(list.slice(90,list.length),'list')
				this.allCount = list.length
				for(let i =0;i<this.count;i++){
					this.mapList.push(list[i])
				}
				this.lenList = list.slice(this.count,list.length)
				let len = this.lenList.length
				setInterval(()=>{
					console.log('定时器----------------------')
					if(this.count == this.allCount){
						return
					}else{
						for(let i =0;i<this.lenList.length;i++){
							this.count++
							this.mapList.push(this.lenList[i])
							this.mapList.shift()
							return
						}
					}
					this.dataList.push(len++)
					this.dataList.shift()
				},100)
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
						data:this.dataList,
						axisTick: {
							show: false
						},
						axisLabel: {
							show:false
						},
						axisLine: {
							show: false
						},
					},
					yAxis: {
						type: 'value',
						min:'-500',
						max:'500',
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
							data: this.mapList,
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
			}
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
