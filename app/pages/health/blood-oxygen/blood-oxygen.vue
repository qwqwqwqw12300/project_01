<!-- 血氧 -->
<template>
	<app-body :bodyStyle="{background: '#F7F7F7' }">
		<app-logo text="血氧" top="36rpx" iconUrl="/static/images/share@3x.png"></app-logo>
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
	import * as echarts from '@/static/js/echarts.js';
	import {
		GetListBloodOxygenByDay,
		GetListBloodOxygenByWeek
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
		data() {
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
				options:{},
				dataList:[]
			}
		},
		methods: {
			dateFun(option){
				this.options = {
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
						type: 'time',
						interval: 6 * 3600 * 1000, // 间隔为6小时
						min: new Date(`${option.value + ' 00:00:00'}`), // x轴起始时间
						max: new Date(`${option.value + ' 23:59:59'}`), // x轴结束时间
						boundaryGap: false,
						axisTick: { //坐标轴刻度相关设置。
							show: false,
						},
						axisLabel: {
							textStyle: {
								color: "#666"
							},
							formatter: function(val) {
								return (uni.$u.timeFormat(new Date(val), 'hh:MM'))
							}
						},
						axisLine: {
							lineStyle: {
								color: 'rgb(238,238,238)',
								width: 1
							}
						},
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
						data: this.dataList
					}]
				}
			},
			weekFun(option){
				this.options = {
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
						type: 'time',
						interval: 24 * 3600 * 1000,
						min: new Date(`${option.value[0] + ' 00:00:00'}`), // x轴起始时间
						max: new Date(`${option.value[6] + ' 00:00:00'}`), // x轴结束时间
						boundaryGap: false,
						axisTick: { //坐标轴刻度相关设置。
							show: false,
						},
						axisLabel: {
							textStyle: {
								color: "#666"
							},
							formatter: function(val,index) {
								console.log(val,index, 'dddd----------')
								const weekArr = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
								return index == 7 ? '' : weekArr[new Date(val).getDay()]
								// return (uni.$u.timeFormat(new Date(val), 'hh:MM'))
							}
						},
						axisLine: {
							lineStyle: {
								color: 'rgb(238,238,238)',
								width: 1
							}
						},
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
						data: this.dataList
					}]
				}
			},
			handleDate(option){
				console.log(option,'option')
				this.dataList = []
				GetListBloodOxygenByDay({
					deviceId:this.deviceInfo.deviceId,
					queryDate:option.value,
					humanId:this.deviceInfo.familyId
				}).then(res=>{
					console.log(res,'res')
					this.totalList[0].num = res.data.oxMap.avgOx
					this.totalList[1].num = res.data.oxMap.maxOx
					this.totalList[2].num = res.data.oxMap.minOx
					for(let i =0;i<res.data.oxMap.dataList.length;i++){
						this.dataList.push([
							res.data.oxMap.dataList[i].time,
							res.data.oxMap.dataList[i].value
						])
					}
				})
				this.dateFun(option)
			},
			handleWeek(option){
				this.dataList = []
				GetListBloodOxygenByWeek({
					deviceId:this.deviceInfo.deviceId,
					beginDate:option.value[0],
					endDate:option.value[6],
					humanId:this.deviceInfo.familyId
				}).then(res=>{
					console.log(res,'res')
					this.totalList[0].num = res.data.oxMap.avgOx
					this.totalList[1].num = res.data.oxMap.maxOx
					this.totalList[2].num = res.data.oxMap.minOx
					for(let i =0;i<res.data.oxMap.dataList.length;i++){
						this.dataList.push([
							res.data.oxMap.dataList[i].time,
							res.data.oxMap.dataList[i].value
						])
					}
					console.log(this.dataList,'this.dataList')
				})
				this.weekFun(option)
			},
			onSelect(val) {
				console.log(val, '000')
				val.type === 'date' ? this.handleDate(val) : this.handleWeek(val)
			},
		},
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
