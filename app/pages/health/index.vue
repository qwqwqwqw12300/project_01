<template>
	<app-body :hideTitle="true">
		<view class="ui-banner">
			<swiper class="ui-swiper" circular :indicator-dots="true" :autoplay="false" @change="swiperChange"
				:current="current">
				<swiper-item>
					<swiper-device></swiper-device>
				</swiper-item>
			</swiper>
		</view>
		<view class="ui-body">
			<view class="ui-w-h-100">
				<view class="ui-f-center ui-box ui-white-bg ui-br-16">
					<image class="ui-img-size1" src="@/static/images/caihong.png"></image>
					<view class="ui-f-center ui-f-wrap ui-w-70 ui-mar-l-32">
						<view class="ui-w-60 ui-f-start ui-f-wrap">
							<image class="ui-img-size2" src="@/static/images/xiaohao.png"></image>
							<text class="ui-font-24 ui-mar-l-10">卡路里<千卡></text>
							<view class="ui-text-box ui-mar-t-15">
								<text class="ui-font-1">300</text>
								<text class="ui-font-2">/1000</text>
							</view>
						</view>
						<view class="ui-w-40 ui-f-start ui-f-wrap">
							<image class="ui-img-size2" src="@/static/images/timeclock.png"></image>
							<text class="ui-font-24 ui-mar-l-10">活动(分)</text>
							<view class="ui-text-box ui-mar-t-15">
								<text class="ui-font-1">16</text>
								<text class="ui-font-2">/30</text>
							</view>
						</view>
						<view class="ui-w-60 ui-f-start ui-mar-t-45 ui-f-wrap">
							<image class="ui-img-size2" src="@/static/images/walk.png"></image>
							<text class="ui-font-24 ui-mar-l-10">行走<步></text>
							<view class="ui-text-box ui-mar-t-15">
								<text class="ui-font-1">2000</text>
								<text class="ui-font-2">/10000</text>
							</view>
						</view>
						<view class="ui-w-40 ui-f-start ui-mar-t-45 ui-f-wrap">
							<image class="ui-img-size2" src="@/static/images/stand.png"></image>
							<text class="ui-font-24 ui-mar-l-10">站立<分></text>
							<view class="ui-text-box ui-mar-t-15">
								<text class="ui-font-1">20</text>
								<text class="ui-font-2">/30</text>
							</view>
						</view>
					</view>
				</view>
				<view class="ui-f-between ui-mar-t-20  ui-f-wrap">
					<view class="ui-w-42 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20"
						@click="jumpUrl('/pages/health/sleep/sleep')">
						<view class="ui-f-start ui-f-wrap">
							<image class="ui-img-size3" src="../../static/images/yueliang.png"></image>
							<text class="ui-font-32 ui-mar-l-10">睡眠</text>
							<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
								2月16日
							</view>
							<view class="ui-w-h-100 ui-f-between ui-mar-t-20">
								<view class="ui-color-block1 ui-color-block1-width"></view>
								<view class="ui-color-block2 ui-color-block1-width"></view>
								<view class="ui-color-block3 ui-color-block1-width"></view>
								<view class="ui-color-block4 ui-color-block1-width"></view>
							</view>
							<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
								<text class="ui-font-22 ui-font-c-888">差</text>
								<text class="ui-font-22 ui-font-c-888">很好</text>
							</view>
						</view>
						
					</view>
					<view class="ui-w-42 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20">
						<view class="ui-f-start ui-f-wrap">
							<image class="ui-img-size3" src="../../static/images/xinlv.png"></image>
							<text class="ui-font-32 ui-mar-l-10">心率76次/分</text>
							<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
								2月16日
							</view>
							<view class="ui-w-h-100 ui-mar-t-20">
								<app-echarts class="ui-echarts-size" :option="xinLvOption" id="xinLvChart">
								</app-echarts>
								<!-- <image class="ui-img-size4" src="../../static/images/xinlvLine.png"></image> -->
							</view>
							<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
								<text class="ui-font-22 ui-font-c-888">00:00</text>
								<text class="ui-font-22 ui-font-c-888">24:00</text>
							</view>
						</view>
					</view>
					<view class="ui-f-between ui-mar-t-20 ">
						<view class="ui-w-42 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20" @click="jumpUrl('/pages/health/blood-pressure/blood-pressure')">
							<view class="ui-f-start ui-f-wrap">
								<image class="ui-img-size3" src="../../static/images/xueya.png"></image>
								<text class="ui-font-32 ui-mar-l-10">血压</text>
								<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
									2月16日
								</view>
								<view class="ui-w-h-100 ui-f-between ui-mar-t-20">
									<app-echarts class="ui-echarts-size" :option="xueYaOption" id="xueYaChart">
									</app-echarts>
									<!-- <image class="ui-img-size4" src="../../static/images/xueyaLine.png"></image> -->
								</view>
								<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
									<text class="ui-font-22 ui-font-c-888">00:00</text>
									<text class="ui-font-22 ui-font-c-888">24:00</text>
								</view>
							</view>
						</view>
						<view class="ui-w-42 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20">
							<view class="ui-f-start ui-f-wrap">
								<image class="ui-img-size3" src="../../static/images/xueyang.png"></image>
								<text class="ui-font-32 ui-mar-l-10">血氧</text>
								<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
									2月16日
								</view>
								<view class="ui-w-h-100 ui-mar-t-20">
									<app-echarts class="ui-echarts-size" :option="xueYangOption" id="xueYangChart">
									</app-echarts>
									<!-- <image class="ui-img-size4" src="../../static/images/xueyangLine.png"></image> -->
								</view>
								<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
									<text class="ui-font-22 ui-font-c-888">00:00</text>
									<text class="ui-font-22 ui-font-c-888">24:00</text>
								</view>
							</view>
						</view>
					</view>
					<view class="ui-f-between ui-mar-t-20">
						<view class="ui-w-42 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20">
							<view class="ui-f-start ui-f-wrap">
								<image class="ui-img-size3" src="../../static/images/xindian.png"></image>
								<text class="ui-font-32 ui-mar-l-10">心电</text>
								<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
									2月16日
								</view>
								<view class="ui-w-h-100 ui-f-between ui-mar-t-20">
									<app-echarts class="ui-echarts-size" :option="xinDianOption" id="xueYangChart">
									</app-echarts>
									<!-- <image class="ui-img-size4" src="../../static/images/xindianLine.png"></image> -->
								</view>
								<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
									<text class="ui-font-22 ui-font-c-888">00:00</text>
									<text class="ui-font-22 ui-font-c-888">24:00</text>
								</view>
							</view>
						</view>
						<view class="ui-w-42 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20">
							<view class="ui-f-start ui-f-wrap">
								<image class="ui-img-size3" src="../../static/images/xinzang.png"></image>
								<text class="ui-font-32 ui-mar-l-10">心脏</text>
								<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
									2月16日
								</view>
								<view class="ui-w-h-100 ui-mar-t-20">
									<app-echarts class="ui-echarts-size" :option="xinZangOption" id="xueYangChart">
									</app-echarts>
									<!-- <image class="ui-img-size4" src="../../static/images/xinzangLine.png"></image> -->
								</view>
								<view class="ui-f-between ui-w-h-100 ui-mar-t-10">
									<text class="ui-font-22 ui-font-c-888">00:00</text>
									<text class="ui-font-22 ui-font-c-888">24:00</text>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</app-body>
</template>

<script>
	import  * as echarts from '@/static/js/echarts.js';
	import SwiperDevice from './components/swiper-device.vue';
	export default {
		// components: {
		// 	echarts
		// },
		data() {
			return {
				caiHongOption: {},
				xinLvOption: {},
				xueYaOption: {},
				xueYangOption: {},
				xinDianOption: {},
				xinZangOption: {},
			}
		},
		created() {
			window.onload=function(){
				console.log(this)
			}
			this.logstatrt();
		},
		mounted() {
			
		},
		methods: {
			jumpUrl(url) {
				uni.navigateTo({
					url: url
				})
			},
			swiperChange(val) {

			},
			logstatrt() {
				this.caiHongOptionHandle();
				this.xinLvOptionHandle();
				this.xueYaOptionHandle();
				this.xueYangOptionHandle();
				this.xinDianOptionHandle();
				this.xinZangOptionHandle();
			},
			caiHongOptionHandle() {
					var test_data = [{
							"name": "22",
							"value": 1,
							"maxValue":20
						},
						{
							"name": "33",
							"value": 5,
							"maxValue":20
						},
						{
							"name": "25",
							"value": 3,
							"maxValue":20
						},
						{
							"name": "26",
							"value": 4,
							"maxValue":20
						}
				];
				var maxData = 5,
					seriesd = [],
					legend = [];

				for (var j in test_data) {
					if (legend.indexOf(test_data[j]["name"] == -1)) {
						legend.push({
							'icon': 'rect',
							"name": test_data[j]["name"]
						});
					}
					var ra = test_data.length - 1 - j;
					seriesd.push({
						name: test_data[j]["name"],
						type: 'pie',
						radius: [(ra * 20 + 15) + "%", (30 + ra * 20) + "%"],
						itemStyle: {
							normal: {
								label: {
									show: false
								}
							}
				  },
						hoverAnimation: false,
						startAngle: 180,
						center: ["45%", "75%"],
						data: [{
								value: test_data[j]["value"],
								name: test_data[j]["name"],
								label: {
									normal: {
										postion: "center"
									}
								},
							},
							{
								value: maxData - test_data[j]["value"],
								itemStyle: {
									normal: {
										color: 'rgba(203,203,203,0.5)',

									},
									emphasis: {
										color: 'rgba(203,203,203,1)'
									}
								},
								name: 'showtip_' + test_data[j]["value"]
							},
							{
								value: maxData,
								itemStyle: {
									normal: {
										color: 'rgba(0,0,0,0)',
										label: {
											show: true
										},
										labelLine: {
											show: true
										}
									},
									emphasis: {
										color: 'rgba(0,0,0,0)'
									}
								},
								name: 'hide'
							}
						]
					})
				}
				seriesd.push({
					type: 'gauge',
					z: 3,
					min: 0,
					max: 100,
					splitNumber: 5,
					center: ['45%', '75%'], // 默认全局居中
					radius: '0%',
					endAngle: 0,
					startAngle: 180,
					axisLabel: {
						show: false,
						formatter: "{value}%"
					},
					axisLine: { // 坐标轴线 
						show: false,
						lineStyle: {
							color: [
								[1, "rgba(203,203,203,1)"]
							], //仪表盘颜色
							width: 2, //仪表盘宽度
						}
					},
					axisTick: { // 坐标轴小标记
						show: false,
						length: 5, // 属性length控制线长
						lineStyle: { // 属性lineStyle控制线条样式
							color: 'auto'
						}
					},
					splitLabel: {
						show: false
					},
					pointer: {
						show: false
					},
					splitLine: { // 分隔线
						show: false,

					},
					title: {
						show: false
					},
					detail: { //显示数据
						show: false,
					},
					itemStyle: {
						normal: {
							color: "#676767", //仪表盘颜色
						}
					}

				})
				this.caiHongOption = {
					tooltip: {
						show: true,
						formatter: function(params) {
							if (params.name == "hide") {
								return null
							} else {
				   	if (params.name.indexOf("showtip_") != -1) {
									var num = Number(params.name.split("_")[1]);
								} else {
									var num = params.value;
								}
								if (Number(num) == 0) return params.seriesName + ":" + Number(num) + "";
								return params.seriesName + ":" + parseFloat(num * 100 / maxData).toFixed(2) + "%";
				 		}
						}
					},
					grid: {
						top: 0,
						height: 0,
						left: "0%",
						right: '0%',
					},
					series: seriesd,
					color: ["rgba(255,97,97,1)", "rgba(142,230,130,1)", "rgba(115,227,255,1)", "rgba(112,150,255,1)"]
				};
			},
			xinLvOptionHandle() {
				// var data = [Math.random() * 30];
				// for (var i = 1; i < 20; i++) {
				// 	data.push(Math.round((Math.random() - 0.5) * 200 + data[i - 1]));
				// }

				var data = [20, 60, 34, 25, 33, 46, 32, 35, 27,28];
				this.xinLvOption = {
					tooltip: {
						trigger: 'axis',
						position: function(pt) {
							return [pt[0], '10%'];
						}
					},
					legend: {
						top: 'bottom',
						data: ['意向']
					},
					grid: {
						top:-20,
						left:0,
						right:0,
						bottom:0
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						show:false
					},
					yAxis: {
						type: 'value',
						show:false,
						boundaryGap: [0, '100%']
					},

					series: [{
						name: '折线数据',
						type: 'line',
						smooth: false,
						// symbolSize:1,
						symbol: 'none',
						sampling: 'average',
						itemStyle: {
							normal: {
								color: 'rgb(255,92,92)' //折线颜色
							}
						},
						lineStyle:{
							width: 1
						},
						areaStyle: {
							normal: {
							    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
							        offset: 0,
							        color: 'rgba(255,92,92,0.5)' //渐变起始颜色
				 		    }, {
							        offset: 1,
							        color: 'rgba(255,92,92,0.1)' //渐变结束颜色
							    }])
							}
						},
						data: data
					}]
				};

			},
			xueYaOptionHandle() {
				var data = [20, 60, 34, 25, 33, 46, 32, 35, 27,28];
				this.xueYaOption = {
					tooltip: {
						trigger: 'axis',
						position: function(pt) {
							return [pt[0], '10%'];
						}
					},
					legend: {
						top: 'bottom',
						data: ['意向']
					},
					grid: {
						top:-20,
						left:0,
						right:0,
						bottom:0
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						show:false
					},
					yAxis: {
						type: 'value',
						show:false,
						boundaryGap: [0, '100%']
					},
				
					series: [{
						name: '折线数据',
						type: 'line',
						smooth: false,
						// symbolSize:1,
						symbol: 'none',
						sampling: 'average',
						itemStyle: {
							normal: {
								color: 'rgb(255,148,72)'
							}
						},
						lineStyle:{
							width: 1
						},
						areaStyle: {
							normal: {
							    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
							        offset: 0,
							        color: 'rgba(255,148,72,0.5)'
				 		    }, {
							        offset: 1,
							        color: 'rgba(255,148,72,0.1)'
							    }])
							}
						},
						data: data
					}]
				};
			},
			xueYangOptionHandle() {
				var data = [20, 60, 34, 25, 33, 46, 32, 35, 27,28];
				this.xueYangOption = {
					tooltip: {
						trigger: 'axis',
						position: function(pt) {
							return [pt[0], '10%'];
						}
					},
					legend: {
						top: 'bottom',
						data: ['意向']
					},
					grid: {
						top:-20,
						left:0,
						right:0,
						bottom:0
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						show:false
					},
					yAxis: {
						type: 'value',
						show:false,
						boundaryGap: [0, '100%']
					},
				
					series: [{
						name: '折线数据',
						type: 'line',
						smooth: false,
						// symbolSize:1,
						symbol: 'none',
						sampling: 'average',
						itemStyle: {
							normal: {
								color: 'rgb(54,191,255)'
							}
						},
						lineStyle:{
							width: 1
						},
						areaStyle: {
							normal: {
							    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
							        offset: 0,
							        color: 'rgba(54,191,255,0.5)'
				 		    }, {
							        offset: 1,
							        color: 'rgba(54,191,255,0.1)'
							    }])
							}
						},
						data: data
					}]
				};
			},
			xinDianOptionHandle() {
				var data = [20, 60, 34, 25, 33, 46, 32, 35, 27,28];
				this.xinDianOption = {
					tooltip: {
						trigger: 'axis',
						position: function(pt) {
							return [pt[0], '10%'];
						}
					},
					legend: {
						top: 'bottom',
						data: ['意向']
					},
					grid: {
						top:-20,
						left:0,
						right:0,
						bottom:0
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						show:false
					},
					yAxis: {
						type: 'value',
						show:false,
						boundaryGap: [0, '100%']
					},
				
					series: [{
						name: '折线数据',
						type: 'line',
						smooth: false,
						// symbolSize:1,
						symbol: 'none',
						sampling: 'average',
						itemStyle: {
							normal: {
								color: 'rgb(112,203,136)'
							}
						},
						lineStyle:{
							width: 1
						},
						areaStyle: {
							normal: {
							    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
							        offset: 0,
							        color: 'rgba(112,203,136,0.5)'
				 		    }, {
							        offset: 1,
							        color: 'rgba(112,203,136,0.1)'
							    }])
							}
						},
						data: data
					}]
				};
			},
			xinZangOptionHandle() {
				var data = [20, 60, 34, 25, 33, 46, 32, 35, 27,28];
				this.xinZangOption = {
					tooltip: {
						trigger: 'axis',
						position: function(pt) {
							return [pt[0], '10%'];
						}
					},
					legend: {
						top: 'bottom',
						data: ['意向']
					},
					grid: {
						top:-20,
						left:0,
						right:0,
						bottom:0
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						show:false
					},
					yAxis: {
						type: 'value',
						show:false,
						boundaryGap: [0, '100%']
					},
				
					series: [{
						name: '折线数据',
						type: 'line',
						smooth: false,
						// symbolSize:1,
						symbol: 'none',
						sampling: 'average',
						itemStyle: {
							normal: {
								color: 'rgb(255,58,58)'
							}
						},
						lineStyle:{
							width: 1
						},
						areaStyle: {
							normal: {
							    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
							        offset: 0,
							        color: 'rgba(255,58,58,0.5)'
				 		    }, {
							        offset: 1,
							        color: 'rgba(255,58,58,0.1)'
							    }])
							}
						},
						data: data
					}]
				};
			},
		}
	}
</script>

<style lang="scss" scoped>
	.ui-banner {
		margin-top: 60rpx;
		padding: 0 20rpx;
	}

	.ui-swiper {
		height: 320rpx;
	}

	.ui-body {
		padding: 32rpx;

		.ui-mar-l-20 {
			margin-left: 20rpx;
		}

		.ui-mar-t-10 {
			margin-top: 10rpx;
		}

		.ui-mar-t-15 {
			margin-top: 15rpx;
		}

		.ui-mar-t-20 {
			margin-top: 20rpx;
		}

		.ui-mar-t-45 {
			margin-top: 45rpx;
		}

		.ui-white-bg {
			background-color: #FFF;
		}

		.ui-w-h-100 {
			width: 100%;
			height: 100%;
		}

		.ui-br-16 {
			border-radius: 16rpx;
		}

		.ui-f-center {
			display: flex;
			justify-content: center;
			align-items: center;
		}

		.ui-f-between {
			display: flex;
			justify-content: space-between;
			align-items: center;
		}

		.ui-f-start {
			display: flex;
			justify-content: flex-start;
			align-items: center;
		}

		.ui-t-box {
			width: 50%;
		}

		.ui-f-wrap {
			flex-wrap: wrap;
		}

		.ui-img-size1 {
			width: 200rpx;
			height: 100rpx;
		}

		.ui-w-40 {
			width: 40%;
		}

		.ui-w-60 {
			width: 60%;
		}

		.ui-w-70 {
			width: 70%;
		}

		.ui-mar-l-10 {
			margin-left: 10rpx;
		}

		.ui-mar-l-32 {
			margin-left: 32rpx;
		}

		.ui-box {
			padding: 47rpx 32rpx 42rpx 32rpx;
		}

		.ui-w-45 {
			width: 45%;
		}

		.ui-w-42 {
			width: 42%;
		}

		.ui-img-size2 {
			width: 36rpx;
			height: 36rpx;
		}

		.ui-font-24 {
			font-size: 24rpx;
		}

		.ui-font-22 {
			font-size: 22rpx;
		}

		.ui-text-box {
			padding-left: 36rpx;

			.ui-font-1 {
				font-size: 36rpx;
				color: #353535;
				letter-spacing: 0;
				line-height: 36rpx;
				font-weight: 700;
			}

			.ui-font-2 {
				font-size: 36rpx;
				color: #D4D4D4;
				letter-spacing: 0;
				line-height: 36rpx;
				font-weight: 700;
			}
		}

		.ui-img-size3 {
			width: 48rpx;
			height: 48rpx;
		}

		.ui-img-size4 {
			width: 100%;
			height: 64rpx;
		}
		.ui-echarts-size{
			width:100%;
			height:72rpx;
		}
		.ui-padding-20 {
			padding: 20rpx;
		}

		.ui-font-32 {
			font-size: 32rpx;
		}

		.ui-font-3 {
			font-size: 26rpx;
			color: #353535;
			line-height: 26rpx;
			font-weight: 400;
		}

		.ui-padding-l-58 {
			padding-left: 58rpx;
		}

		.ui-color-block1 {
			background: rgba(82, 136, 245, 0.25);
			height: 72rpx;
		}

		.ui-color-block2 {
			background: rgba(82, 136, 245, 0.50);
			height: 72rpx;
		}

		.ui-color-block3 {
			background: rgba(82, 136, 245, 0.75);
			height: 72rpx;
		}

		.ui-color-block4 {
			background: rgba(82, 136, 245, 1);
			height: 72rpx;
		}

		.ui-color-block1-width {
			width: 23%;
		}

		.ui-font-c-888 {
			color: #888888;
		}
	}

	.ui-img {
		width: 100%;
		height: calc(100vh - var(--window-bottom) - var(--status-bar-height));

	}
</style>
