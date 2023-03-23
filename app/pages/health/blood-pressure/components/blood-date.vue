<template>
	<view >
		<!-- <app-echarts :option="option" id="myChart" class="myChart"></app-echarts> -->
		<view class="ui-title" v-for="(item,index) in list" :key="index">
			<view class="ui-title-content">
				<view class="ui-circle" :style="{backgroundColor:item.color}"></view>
				<view class="ui-title-font">{{item.title}}</view>
			</view>
			<view>
				<text class="ui-font">{{item.value}}</text>
				<text class="ui-content-font">mmHg</text>
			</view>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				option: {},
				list:[{
					color:'#FF7E23',
					title:'收缩压',
					value:'126'
				},
				{
					color:'#63DDBA',
					title:'舒张压',
					value:'88'
				}],
				dateList:[{
					color:'#68D688',
					title:'总睡眠时长',
					value:'6-10h'
				},
				{
					color:'#8437DA',
					title:'深睡 0%',
					value:'10-40%'
				},
				{
					color:'#C145C9',
					title:'浅睡 0%',
					value:'45-80%'
				},
				{
					color:'#EFC356',
					title:'清醒时长 0%',
					value:'<10%'
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
			logstatrt(){
				const types = ['任务一', '任务二', '任务三']
				const series = [
				    [
				        ["00:01~00:35", "00:00", "04:35"],
				        ["08:00~10:35", "12:00", "16:35"],
				    ],
				    [
				        ["09:01~09:35", "04:35", "08:35"],
					    ["06:00~07:35", "14:00", "18:00"],
				    ],
				    [
				        ["00:00~04:45", "08:35", "12:35"],
				        ["05:09~07:35", "16:00", "20:00"],
				    ],
				]
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
				        renderItem:function(params,api){
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
				this.option= {
					title: {
						text: ''
					},
					//取消坐标轴
					grid:{
						left:'0',
						right:'0',
						bottom:'5',
						top:'0',
						// containLabel:false
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						axisLabel: {
							show: false
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
							show: false
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
			makeXAxis(){
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
	.ui-title{
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		.ui-title-content{
			display: flex;
			align-items: center;
			justify-content: center;
			.ui-circle{
				width: 30rpx;
				height: 30rpx;
				margin-right: 10rpx;
				border-radius: 50rpx;
			}
			.ui-title-font{
				font-size: 26rpx;
				color: #353535;
				letter-spacing: 0;
				font-weight: 400;
			}
		}
	}
	.ui-font{
		font-size: 72rpx;
		color: #353535;
		letter-spacing: 0;
		font-weight: 700;
	}
	.ui-content-font{
		font-size: 26rpx;
		color: #888888;
		letter-spacing: 0;
		font-weight: 400;
	}
</style>
