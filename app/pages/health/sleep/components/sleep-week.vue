<template>
	<view>
		<view class="ui-totalTime">00:00</view>
		<view class="ui-title">
			<view class="ui-circle"></view>
			<view class="ui-title-font">睡眠时长</view>
		</view>
		<view class="ui-window">
			<view>
				<view class="ui-content" v-for="(item,index) in sleepList" :key="index">
					<view class="ui-content-title">
						<view class="ui-content-circle" :style="{backgroundColor:item.color}"></view>
						<view class="ui-content-font">{{item.title}}</view>
					</view>
				</view>
			</view>
			<app-echarts :option="option" id="myChart" class="myChart"></app-echarts>
		</view>
		<WatchDiv :text="'日均睡眠得分'" :content="'0'"></WatchDiv>
	</view>
</template>

<script>
	import WatchDiv from '@/components/watch-div/watch-div.vue'
	export default {
		components: {
			WatchDiv
		},
		data() {
			return {
				option:{},
				sleepList:[{
					color:'#F1C44F',
					title:'清醒'
				},
				{
					color:'#EF7B8C',
					title:'快速眼动'
				},
				{
					color:'#BF47CA',
					title:'浅睡'
				},
				{
					color:'#8538E0',
					title:'深睡'
				}]
			}
		},
		created() {
			this.logstatrt();
		},
		methods: {
			logstatrt(){
				this.option = {

				    color: ["#F1C44F", "#EF7B8C","#BF47CA","#8538E0"],
				    legend: {
				        data: ['', '']
				
				    },
				    tooltip: {
				        trigger: 'axis',
				        axisPointer: {
				            type: 'shadow'
				        }
				    },
				    grid: {
						left:'0',
						right:'0',
						bottom:'5',
						top:'0',
				        containLabel: true
				    },
				    xAxis: [{
				        type: 'category',
				        data: ['周日', '周一', '周二', '周三', '周四', '周五'],
				        axisTick: {
				            alignWithLabel: true
				        }
				    }],
				    yAxis: [{
				        type: 'value',
						axisLabel: {
							show: false
						},
						axisTick: {
							show: false
						},
						axisLine: {
							show: false
						},
				    }],
				    series: [{
				        name: '清醒',
				        type: 'bar',
				        stack: '总量',
				        barWidth: '70%',
				        data: [14, 10, 16, 7, 15, 9]
				    }, {
				        name: '快速眼动',
				        type: 'bar',
				        stack: '总量',
				        barWidth: '70%',
				        data: [12, 14, 6, 13, 4, 9]
				    },
					{
					    name: '浅睡',
					    type: 'bar',
					    stack: '总量',
					    barWidth: '70%',
					    data: [12, 14, 6, 13, 4, 9]
					}, {
				        name: '深睡',
				        type: 'bar',
				        stack: '总量',
				        barWidth: '70%',
				        data: [12, 14, 6, 13, 4, 9]
				    }]
				};
			}
			
		}
	}
</script>

<style lang="scss" scoped>
	.ui-totalTime{
		margin-top: 20rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 56rpx;
		color: #353535;
		letter-spacing: 0;
		font-weight: 700;
	}
	.ui-title{
		margin-top: 20rpx;
		margin-bottom: 58rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		.ui-circle{
			width: 30rpx;
			height: 30rpx;
			margin-right: 10rpx;
			border-radius: 50rpx;
			background-color: #68D688;
		}
		.ui-left-title{
			font-size: 34rpx;
			color: #353535;
			letter-spacing: 0;
			font-weight: 400;
		}
	}
	.ui-window{
		margin-bottom: 64rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		.ui-content{
			width: 48%;
			margin-left: 56rpx;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			.ui-content-title{
				margin-top: 20rpx;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;
				.ui-content-circle{
					width: 30rpx;
					height: 30rpx;
					margin-bottom: 10rpx;
					border-radius: 50rpx;
				}
				.ui-content-font{
					font-size: 34rpx;
					color: #353535;
					letter-spacing: 0;
					font-weight: 400;
				}
			}
		}
		.myChart{
			width: 90%;
			height: 400rpx;
			margin: 64rpx 32rpx 20rpx;
		}
	}
	
</style>
