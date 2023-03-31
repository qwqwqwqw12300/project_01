<template>
	<app-body :hideTitle="true" :bg="pageShow">
		<view v-if="pageShow">
			<view class="ui-banner">
				<swiper class="ui-swiper" circular :indicator-dots="true" :autoplay="false" @change="swiperChange"
					:current="current">
					<swiper-item v-for="item in deviceList" :key="item.deviceId">
						<swiper-device :record="item"></swiper-device>
					</swiper-item>
				</swiper>
			</view>
			<scroll-view class="ui-scroll" scroll-y="true">
				<view class="ui-body">
					<view class="ui-w-h-100">
						<view class="ui-f-center ui-white-bg ui-br-16" style="padding:30rpx 30rpx 30rpx 0"
							@click="jumpUrl('/pages/health/exercise/exercise')">
							<!-- <image class="ui-img-size1" src="@/static/images/caihong.png"></image> -->
							<app-echarts style="height: 250rpx;width:250rpx" id="caiHongChart" :option="caiHongOption">
							</app-echarts>
							<view class="ui-f-center ui-f-wrap ui-w-70">
								<view class="ui-w-52 ui-f-start ui-f-wrap">
									<image class="ui-img-size2" src="@/static/images/xiaohao.png"></image>
									<text class="ui-font-24 ui-mar-l-10">卡路里<千卡></text>
									<view class="ui-text-box ui-mar-t-15">
										<text class="ui-font-1">{{caiHongData[0]?caiHongData[0]:0}}</text>
										<text class="ui-font-2">/{{maxDataArr[0]?maxDataArr[0]:1000}}</text>
									</view>
								</view>
								<view class="ui-w-47 ui-f-start ui-f-wrap">
									<image class="ui-img-size6" src="@/static/images/timeclock.png"></image>
									<text class="ui-font-24 ui-mar-l-10">活动(分)</text>
									<view class="ui-text-box ui-mar-t-15">
										<text class="ui-font-1">{{caiHongData[1]?caiHongData[1]:0}}</text>
										<text class="ui-font-2">/{{maxDataArr[1]?maxDataArr[1]:30}}</text>
									</view>
								</view>
								<view class="ui-w-52 ui-f-start ui-mar-t-45 ui-f-wrap">
									<image class="ui-img-size5" src="@/static/images/walk2.png"></image>
									<text class="ui-font-24 ui-mar-l-10">行走<步></text>
									<view class="ui-text-box ui-mar-t-15">
										<text class="ui-font-1">{{caiHongData[2]?caiHongData[2]:0}}</text>
										<text class="ui-font-2">/{{maxDataArr[2]?maxDataArr[2]:10000}}</text>
									</view>
								</view>
								<view class="ui-w-47 ui-f-start ui-mar-t-45 ui-f-wrap">
									<image class="ui-img-size5" src="@/static/images/stand.png"></image>
									<text class="ui-font-24 ui-mar-l-10">站立<分></text>
									<view class="ui-text-box ui-mar-t-15 ui-f-center">
										<text class="ui-font-2" style="font-size:30rpx">暂无数据</text>
									</view>
								</view>
							</view>
						</view>
						<view class="ui-f-between ui-mar-t-20  ui-f-wrap ui-w-h-100">
							<!-- 睡眠 -->
							<view class="ui-w-43 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20 ui-min-h-121	"
								@click="jumpUrl('/pages/health/sleep/sleep')">
								<view class="ui-f-start ui-f-wrap">
									<image class="ui-img-size3" src="/static/images/yueliang.png"></image>
									<text class="ui-font-32 ui-mar-l-10">睡眠</text>
									<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
										{{date}}
									</view>
									<view class="ui-w-h-100 ui-f-start ui-mar-t-20" v-if="fetchRes.sleepMap">
										<view class="ui-color-block1 ui-color-block1-width"
											v-if="fetchRes.sleepMap > 0 && fetchRes.sleepMap <= 0.25"></view>
										<view class="ui-color-block2 ui-color-block1-width ui-mar-l-10"
											v-if="fetchRes.sleepMap > 0.25 && fetchRes.sleepMap <= 0.5"></view>
										<view class="ui-color-block3 ui-color-block1-width ui-mar-l-10"
											v-if="fetchRes.sleepMap > 0.5 && fetchRes.sleepMap <= 0.75"></view>
										<view class="ui-color-block4 ui-color-block1-width ui-mar-l-10"
											v-if="fetchRes.sleepMap > 0.75 && fetchRes.sleepMap <= 1"></view>
									</view>
									<view class="ui-w-h-100 ui-f-between ui-mar-t-20" v-if="!fetchRes.sleepMap">
										<view class="ui-noData-font">无数据</view>
									</view>
									<view class="ui-f-between ui-w-h-100 ui-mar-t-10" v-if="fetchRes.sleepMap">
										<text class="ui-font-22 ui-font-c-888">差</text>
										<text class="ui-font-22 ui-font-c-888">很好</text>
									</view>
								</view>
							</view>
							<!-- /睡眠 -->
							<!-- 心率 -->
							<view class="ui-w-43 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20 ui-min-h-121"
								@click="jumpUrl('/pages/health/heart-rate/heart-rate')">
								<view class="ui-f-start ui-f-wrap">
									<image class="ui-img-size3" src="/static/images/xinlv.png"></image>
									<text class="ui-font-32 ui-mar-l-10">心率</text>
									<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
										{{date}}
									</view>
									<view class="ui-w-h-100 ui-mar-t-20" v-if="fetchRes.HeartRateList.length">
										<app-echarts class="ui-echarts-size" :option="xinLvOption" id="xinLvChart">
										</app-echarts>
										<!-- <image class="ui-img-size4" src="../../static/images/xinlvLine.png"></image> -->
									</view>
									<view class="ui-w-h-100 ui-mar-t-20" v-if="!fetchRes.HeartRateList.length">
										<view class="ui-noData-font">无数据</view>
									</view>
									<view class="ui-f-between ui-w-h-100 ui-mar-t-10"
										v-if="fetchRes.HeartRateList.length > 1">
										<text class="ui-font-22 ui-font-c-888">00:00</text>
										<text class="ui-font-22 ui-font-c-888">24:00</text>
									</view>
								</view>
							</view>
							<!-- /心率 -->
							<!-- 血压 -->
							<view class="ui-f-between ui-mar-t-20 ">
								<view class="ui-w-43 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20 ui-min-h-121"
									@click="jumpUrl('/pages/health/blood-pressure/blood-pressure')">
									<view class="ui-f-start ui-f-wrap">
										<image class="ui-img-size3" src="/static/images/xueya.png"></image>
										<text class="ui-font-32 ui-mar-l-10">血压</text>
										<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
											{{date}}
										</view>
										<view class="ui-w-h-100 ui-f-between ui-mar-t-20"
											v-if="fetchRes.spMapList.length">
											<app-echarts class="ui-echarts-size" :option="xueYaOption" id="xueYaChart">
											</app-echarts>
											<!-- <image class="ui-img-size4" src="../../static/images/xueyaLine.png"></image> -->
										</view>
										<view class="ui-w-h-100 ui-mar-t-20" v-if="!fetchRes.spMapList.length">
											<view class="ui-noData-font">无数据</view>
										</view>
										<view class="ui-f-between ui-w-h-100 ui-mar-t-10"
											v-if="fetchRes.spMapList.length">
											<text class="ui-font-22 ui-font-c-888">00:00</text>
											<text class="ui-font-22 ui-font-c-888">24:00</text>
										</view>
									</view>
								</view>
								<view
									class="ui-w-43 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20 ui-w-h-100 ui-min-h-121"
									@click="jumpUrl('/pages/health/blood-oxygen/blood-oxygen')">
									<view class="ui-f-start ui-f-wrap">
										<image class="ui-img-size3" src="/static/images/xueyang.png"></image>
										<text class="ui-font-32 ui-mar-l-10">血氧</text>
										<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
											{{date}}
										</view>
										<view class="ui-w-h-100 ui-mar-t-20" v-if="fetchRes.tWatchBloodOxygen.length">
											<app-echarts class="ui-echarts-size" :option="xueYangOption"
												id="xueYangChart">
											</app-echarts>
											<!-- <image class="ui-img-size4" src="../../static/images/xueyangLine.png"></image> -->
										</view>
										<view class="ui-w-h-100 ui-mar-t-20">
											<view class="ui-noData-font" v-if="!fetchRes.tWatchBloodOxygen.length">
												无数据
											</view>
										</view>
										<view class="ui-f-between ui-w-h-100 ui-mar-t-10"
											v-if="fetchRes.tWatchBloodOxygen.length">
											<text class="ui-font-22 ui-font-c-888">00:00</text>
											<text class="ui-font-22 ui-font-c-888">24:00</text>
										</view>
									</view>
								</view>
							</view>
							<!-- /血压 -->
							<!-- 心电 -->
							<view class="ui-f-between ui-mar-t-20 ui-w-h-100">
								<view class="ui-w-43 ui-white-bg ui-br-16 ui-f-wrap ui-padding-20 ui-min-h-121"
									@click="jumpUrl('/pages/health/electrocardiograph/electrocardiograph')">
									<view class="ui-f-start ui-f-wrap">
										<image class="ui-img-size3" src="/static/images/xindian.png"></image>
										<text class="ui-font-32 ui-mar-l-10">心电</text>
										<view class="ui-font-3 ui-w-h-100 ui-padding-l-58 ui-mar-t-15">
											{{date}}
										</view>
										<view class="ui-w-h-100 ui-mar-t-20"
											v-if="fetchRes.electrocardiogramMapList && fetchRes.electrocardiogramMapList.length">
											<app-echarts class="ui-echarts-size" :option="xinDianOption"
												id="xinDianChart">
											</app-echarts>
										</view>
										<view class="ui-w-h-100 ui-mar-t-20">
											<view class="ui-noData-font"
												v-if="!fetchRes.electrocardiogramMapList || !fetchRes.electrocardiogramMapList.length">
												无数据
											</view>
										</view>
										<view class="ui-f-between ui-w-h-100 ui-mar-t-10"
											v-if="fetchRes.electrocardiogramMapList && fetchRes.electrocardiogramMapList.length">
											<text class="ui-font-22 ui-font-c-888">00:00</text>
											<text class="ui-font-22 ui-font-c-888">24:00</text>
										</view>
									</view>
								</view>
							</view>
							<!-- 心电 -->
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
		<u-empty class="ui-p-center" mode="data" v-else>
		</u-empty>
	</app-body>
</template>

<script>
	import * as echarts from '@/static/js/echarts.js';
	import SwiperDevice from '@/pages/watch/watch-detail/components/device-swiper.vue';
	import {
		GetCaiHongData,
		getDeviceListState,
	} from '@/common/http/api.js'
	export default {
		components: {
			SwiperDevice
		},
		data() {
			return {
				fetchRes: {
					spMapList: [],
					tWatchBloodOxygen: [],
					HeartRateList: [],
					electrocardiogramMapList: [],
					sleepMap: ''
				},
				pageShow: true,
				caiHongOption: {},
				caiHongData: [],
				maxDataArr: [],
				xinLvOption: {},
				xueYaOption: {},
				xueYangOption: {},
				xinDianOption: {},
				xinZangOption: {},
				current: 0,
				deviceList: [],
				swiperData: {},
				date: '',
				echartDate: ''
			}
		},
		created() {
			this.getDate();
		},
		onShow() {
			getDeviceListState({
				pageSize: 10000
			}).then(res => {
				this.deviceList = res.rows.filter(n => {
					return n.type === '2'
				})
				if (this.deviceList.length) {
					this.swiperData = this.deviceList[0]
					this.$store.commit('setDeviceInfo', this.swiperData)
					this.fetchData();
				} else {
					this.pageShow = false
				}
			})
		},
		methods: {
			swiperChange(val) {
				this.swiperData = this.deviceList[val.detail.current]
				this.$store.commit('setDeviceInfo', this.swiperData)
				this.fetchData()
			},
			getDate() {
				var date = new Date(); // 创建一个Date对象
				var year = date.getFullYear(); // 获取当前年份
				var month = date.getMonth() + 1; // 获取当前月份，需要加1
				var day = date.getDate(); // 获取当前日期
				this.date = month + '月' + day + '日';

				var monthR = month < 10 ? '0' + month : month;
				var dayR = day < 10 ? '0' + day : day;
				this.echartDate = year + '-' + monthR + '-' + dayR;
			},
			fetchData() {
				const {
					deviceId,
					humanId
				} = this.swiperData
				const params = {
					deviceId,
					dayTime: uni.$u.timeFormat(new Date(), 'yyyy-mm-dd'),
					humanId,
				}
				GetCaiHongData(params).then(res => {
					this.logstatrt(res);
					this.fetchRes = res.data;
					console.log('fetchres', this.fetchRes)
				})
			},
			jumpUrl(url) {
				uni.navigateTo({
					url: url
				})
			},
			logstatrt(res) {
				this.caiHongOptionHandle(res);
				this.xinLvOptionHandle(res);
				this.xueYaOptionHandle(res);
				this.xueYangOptionHandle(res);
				this.xinDianOptionHandle(res);
				// this.xinZangOptionHandle(res);
			},
			caiHongOptionHandle(res) {
				// var data = [{
				// 		"name": "StepNum",
				// 		"value": 30000,
				// 		"maxValue": 1000
				// 	},
				// 	{
				// 		"name": "calorie",
				// 		"value": 2000,
				// 		"maxValue": 10000
				// 	},
				// 	{
				// 		"name": "DurationNum",
				// 		"value": 300,
				// 		"maxValue": 30
				// 	}
				// ];

				let data = res.data.rainbowDiagram;
				let caiHongData = [];
				let maxDataArr = [];

				for (let i = 0; i < data.length; i++) {
					if (data[i]['name'] === 'calorie') { //卡路里
						this.maxDataArr[0] = data[i].maxValue;
						this.caiHongData[0] = data[i].value ? data[i].value : 0;
					}
					if (data[i]['name'] === 'StepNum') { //步数
						this.maxDataArr[2] = data[i].maxValue;
						this.caiHongData[2] = data[i].value ? data[i].value : 0;
					}
					if (data[i]['name'] === 'DurationNum') { //活动时间
						this.maxDataArr[1] = data[i].maxValue;
						this.caiHongData[1] = data[i].value ? data[i].value : 0;
					}
				}
				// data.foreach((item)=>{
				// 	console.log(item)
				// })
				var seriesd = [],
					legend = [];

				for (var j in data) {
					if (legend.indexOf(data[j]["name"] == -1)) {
						legend.push({
							'icon': 'rect',
							"name": data[j]["name"]
						});
					}
					var ra = data.length - 1 - j;
					seriesd.push({
						name: data[j]["name"],
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
						center: ["50%", "60%"],
						data: [{
								value: this.maxDataArr[j] - data[j]["value"] > 0 ? data[j]["value"] : this
									.maxDataArr[j],
								name: data[j]["name"],
								label: {
									normal: {
										postion: "center"
									}
								},
							},
							{
								value: this.maxDataArr[j] - data[j]["value"] > 0 ? this.maxDataArr[j] - data[j]
									["value"] : 0,
								itemStyle: {
									normal: {
										color: 'rgba(203,203,203,0.5)',

									},
									emphasis: {
										color: 'rgba(203,203,203,1)'
									}
								},
								name: 'showtip_' + data[j]["value"]
							},
							{
								value: this.maxDataArr[j],
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
					max: 20,
					splitNumber: 5,
					center: ['50%', '50%'], // 默认全局居中
					radius: '0%',
					endAngle: 0,
					startAngle: 90,
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
					grid: {
						top: 0,
						left: 0
					},
					series: seriesd,
					color: ["rgba(255,97,97,1)", "rgba(142,230,130,1)", "rgba(115,227,255,1)"]
				};
			},
			xinLvOptionHandle(res) {
				// var data = [20, 60, 34, 25, 33, 46, 32, 35, 27, 28];
				var arr = res.data.HeartRateList;
				let resArr = [];
				arr.forEach((item) => {
					resArr.push(item.value)
				})
				this.xinLvOption = {
					grid: {
						top: 0,
						left: 0,
						right: 0,
						bottom: 0
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						show: false
					},
					yAxis: {
						type: 'value',
						show: false,
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
						lineStyle: {
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
						data: resArr
					}]
				};

			},
			xueYaOptionHandle(res) {
				let spMapList = []
				let dpMapList = []

				console.log(res, 'res')
				for (let i = 0; i < res.data.spMapList.length; i++) {
					spMapList.push([
						res.data.spMapList[i].time,
						res.data.spMapList[i].value
					])
				}
				for (let i = 0; i < res.data.dpMapList.length; i++) {
					dpMapList.push([
						res.data.dpMapList[i].time,
						res.data.dpMapList[i].value
					])
				}

				this.xueYaOption = {
					title: {
						text: ''
					},
					backgroundColor: '#fff',
					legend: {
						data: []
					},
					grid: {
						left: '0',
						right: '0',
						bottom: '100%',
						containLabel: true
					},
					toolbox: {},
					xAxis: {
						type: 'time',
						show: false,
						// interval: 6 * 3600 * 1000, // 间隔为6小时
						min: `${this.echartDate + ' 00:00:00'}`, // x轴起始时间
						max: `${this.echartDate + ' 23:59:59'}`, // x轴结束时间
						boundaryGap: false,
						axisTick: {
							show: false
						},
						axisLine: {
							lineStyle: {
								color: 'rgb(238,238,238)',
								width: 1
							}
						},
						axisLabel: {
							textStyle: {
								color: "#666"
							},
							formatter: function(val) {
								return (uni.$u.timeFormat(new Date(val), 'hh:MM'))
							}
						},
						splitLine: {
							show: false
						},
					},
					yAxis: {
						type: 'value',
						show: false,
						min: 0,
						max: 130,
						scale: true,
						splitArea: {
							show: true,
							areaStyle: {
								color: ['#f6f8fc', '#fff']
							}
						},
						axisLine: {
							show: false
						},
						axisTick: {
							show: false
						},
						splitLine: {
							lineStyle: {
								type: "dashed",
								color: "#E9E9E9"
							}
						},
					},
					series: [{
							name: '收缩压',
							// type: 'line',
							// stack: 'Total',
							data: spMapList,
							// showSymbol: false,
							// itemStyle: {
							// 	normal: {
							// 		lineStyle: {
							// 			color: "#FF7E23",
							// 			width: 1
							// 		}
							// 	}
							// },
							type: 'bar',
							showSymbol: false,
							itemStyle: {
								color: '#FF7E23'
							},
							barWidth: '3', //---柱形宽度
							barCategoryGap: '20%', //---柱形间距
						},
						{
							name: '舒张压',
							// type: 'line',
							// stack: 'Total',
							data: dpMapList,
							// showSymbol: false,
							// itemStyle: {
							// 	normal: {
							// 		lineStyle: {
							// 			color: "#63DDBA",
							// 			width: 1
							// 		}
							// 	}
							// },
							type: 'bar',
							showSymbol: false,
							itemStyle: {
								color: '#63DDBA'
							},
							barWidth: '3', //---柱形宽度
							barCategoryGap: '20%', //---柱形间距
						}
					]
				}
			},
			xueYangOptionHandle(res) {
				// var data = [20, 60, 34, 25, 33, 46, 32, 35, 27, 28];
				var arr = res.data.tWatchBloodOxygen;
				let resArr = [];
				arr.forEach((item) => {
					resArr.push([
						item.time,
						item.value
					])
				})
				console.log('resarr', resArr)
				this.xueYangOption = {
					notMerge: true,
					backgroundColor: '#fff',
					grid: {
						left: '0',
						right: '0',
						bottom: '100%',
						containLabel: true
					},
					xAxis: [{
						show: false,
						type: 'time',
						// interval: 6 * 60 * 60 * 1000, // 设置x轴间隔为6小时
						min: `${this.echartDate + ' 00:00:00'}`, // x轴起始时间
						max: `${this.echartDate + ' 23:49:00'}`, // x轴结束时间
						// boundaryGap: false,
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
						splitLine: {
							show: false
						},
						axisLine: {
							lineStyle: {
								color: 'rgb(238,238,238)',
								width: 1
							}
						},
					}, ],
					yAxis: [{
						show: false,
						min: 0,
						max: 100,
						type: "value",
						scale: true,
						splitArea: {
							show: true,
							areaStyle: {
								color: ['#f6f8fc', '#fff']
							}
						},
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
						type: 'bar',
						showSymbol: false,
						itemStyle: {
							color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: '#83bff6'
								},
								{
									offset: 0.5,
									color: '#188df0'
								},
								{
									offset: 1,
									color: '#188df0'
								}
							])
						},
						barWidth: '3', //---柱形宽度
						barCategoryGap: '20%', //---柱形间距
						data: resArr
					}]
				}
			},
			xinDianOptionHandle() {

				var data = [20, 60, 34, 25, 33, 46, 32, 35, 27, 28];
				this.xinDianOption = {
					grid: {
						top: 0,
						left: 0,
						right: 0,
						bottom: 0
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						show: false
					},
					yAxis: {
						type: 'value',
						show: false,
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
						lineStyle: {
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
				var data = [20, 60, 34, 25, 33, 46, 32, 35, 27, 28];
				this.xinZangOption = {
					grid: {
						top: 0,
						left: 0,
						right: 0,
						bottom: 0
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						show: false
					},
					yAxis: {
						type: 'value',
						show: false,
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
						lineStyle: {
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
		padding: 0 20rpx;
		padding-top: 60rpx;
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

		.ui-w-47 {
			width: 47%;
		}

		.ui-w-52 {
			width: 52%;
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

		.ui-w-43 {
			width: 43%;
		}

		.ui-img-size2 {
			width: 36rpx;
			height: 36rpx;
		}

		.ui-img-size5 {
			width: 46rpx;
			height: 46rpx;
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

		.ui-img-size6 {
			width: 40rpx;
			height: 40rpx;
		}

		.ui-echarts-size {
			width: 100%;
			height: 72rpx;
		}

		.ui-padding-20 {
			padding: 20rpx;
		}

		.ui-padding-r-40 {
			padding-right: 40rpx;
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

		.ui-min-h-121 {
			min-height: 242rpx;
		}
	}

	.ui-img {
		width: 100%;
		height: calc(100vh - var(--window-bottom) - var(--status-bar-height));
	}

	.ui-noData-font {
		width: 100%;
		height: 134rpx;
		display: flex;
		align-items: center;
		justify-content: center
	}

	.ui-scroll {
		height: calc(100vh - (var(--window-bottom) + 300rpx + 50px + var(--status-bar-height)))
	}

	.ui-p-center {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
	}
</style>
