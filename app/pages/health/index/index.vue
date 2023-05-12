<template>
	<app-body :hideTitle="true" :bg="true">
		<view v-if="!loading">
			<view class="ui-banner">
				<swiper class="ui-swiper" circular :indicator-dots="true" previous-margin="30rpx" next-margin="30rpx"
					:autoplay="false" @change="swiperChange" :current="current">
					<template v-if="deviceList.length">
						<swiper-item v-for="(item,index) in deviceList" :key="item.deviceId">
							<view :class="[current === index ? 'active' : 'noActive']">
								<swiper-device :record="item"></swiper-device>
							</view>
						</swiper-item>
					</template>
					<template v-else>
						<swiper-item>
							<swiper-device></swiper-device>
						</swiper-item>
					</template>
				</swiper>
			</view>
			<scroll-view class="ui-scroll" :scroll-y="scrollAble" :refresher-enabled="scrollAble"
				:refresher-triggered="isRefresh" @refresherrefresh="pullDownRefresh" refresher-background="transparent"
				lower-threshold="10">
				<view class="ui-body">
					<view class="ui-w-h-100">
						<view class="ui-f-center ui-white-bg ui-br-16" style="padding:30rpx 30rpx 30rpx 0"
							@click="jumpUrl('/pages/health/exercise/exercise')">
							<app-echarts style="height: 300rpx;width:600rpx" id="caiHongChart" :option="caiHongOption">
							</app-echarts>
							<view class="ui-f-center ui-f-wrap ui-w-60">
								<view class="ui-f-start ui-f-wrap">
									<image class="ui-img-size2" src="@/static/images/xiaohao.png"></image>
									<text class="ui-font-24 ui-mar-l-10">卡路里<千卡></text>
									<view class="ui-text-box ui-mar-t-15">
										<text class="ui-font-1">{{caiHongData[0]?caiHongData[0]:0}}</text>
										<text class="ui-font-2">/{{maxDataArr[0]?maxDataArr[0]:0}}</text>
									</view>
								</view>
								<!-- 								<view class="ui-w-47 ui-f-start ui-f-wrap">
									<image class="ui-img-size6" src="@/static/images/timeclock.png"></image>
									<text class="ui-font-24 ui-mar-l-10">活动(分)</text>
									<view class="ui-text-box ui-mar-t-15">
										<text class="ui-font-1">{{caiHongData[1]?caiHongData[1]:0}}</text>
										<text class="ui-font-2">/{{maxDataArr[1]?maxDataArr[1]:0}}</text>
									</view>
								</view> -->
								<view class="ui-f-start ui-mar-t-45 ui-f-wrap">
									<image class="ui-img-size5" src="@/static/images/walk2.png"></image>
									<text class="ui-font-24 ui-mar-l-10">行走<步></text>
									<view class="ui-text-box ui-mar-t-15">
										<text class="ui-font-1">{{caiHongData[2]?caiHongData[2]:0}}</text>
										<text class="ui-font-2">/{{maxDataArr[2]?maxDataArr[2]:0}}</text>
									</view>
								</view>
								<!-- 							<view class="ui-w-47 ui-f-start ui-mar-t-45 ui-f-wrap">
									<image class="ui-img-size6" src="@/static/images/stand.png"></image>
									<text class="ui-font-24 ui-mar-l-10">站立<分></text>
									<view class="ui-text-box ui-mar-t-15 ui-f-center">
										<text class="ui-font-2" style="font-size:30rpx">暂无数据</text>
									</view>
								</view> -->
							</view>
						</view>
						<movable-area class="ui-f-between ui-mar-t-10  ui-f-wrap ui-w-h-100" ref="areaBox" id="areaBox"
							style="background-color: rgba(255,255,255,0);">
							<!-- 这块只是循环出固定内容，监听其元素touch事件获取坐标 -->
							<view class="ui-w-48 ui-white-bg ui-br-16 ui-f-wrap ui-mar-t-20 "
								v-for="(item,index) in listData_c" :key="index" :id="'appLi' + index"
								:class="(hoverClass==='appLi'+index)?'select':''"
								@touchstart="AppLi_touchstart(index,$event)" @touchmove="AppLi_touchmove"
								@touchend="AppLi_touchend(index)" v-if="item.isShow || isEdit">
								<SleepCard v-if="item.type === '1'" :date="date" :sleepMap="sleepMap" :item="item"
									:isEdit="isEdit" @iconClick="editCard"></SleepCard>
								<XinLvCard v-if="item.type === '2' "
									:date="fetchRes.HeartRateTime.substr(5,6) && fetchRes.HeartRateList.length !== 0? fetchRes.HeartRateTime.substr(5,6) : '暂无数据'"
									:fetchRes="fetchRes" :option="xinLvOption" :item="item" :isEdit="isEdit"
									@iconClick="editCard"></XinLvCard>
								<XueYaCard v-if="item.type === '3'"
									:date="fetchRes.BloodPressureTime.substr(5,6) && fetchRes.spMapList.length !== 0? fetchRes.BloodPressureTime.substr(5,6) : '暂无数据'"
									:fetchRes="fetchRes" :option="xueYaOption" :item="item" :isEdit="isEdit"
									@iconClick="editCard"></XueYaCard>
								<XueYangCard v-if="item.type === '4'"
									:date="fetchRes.BloodOxygenTime.substr(5,6) && fetchRes.tWatchBloodOxygen.length !== 0 ? fetchRes.BloodOxygenTime.substr(5,6) : '暂无数据'"
									:fetchRes="fetchRes" :option="xueYangOption" :item="item" :isEdit="isEdit"
									@iconClick="editCard"></XueYangCard>
								<XinDianCard v-if="item.type === '5'"
									:date="fetchRes.ElectrocardiogramTime.substr(5,6) && fetchRes.electrocardiogramMapList.length ? fetchRes.ElectrocardiogramTime.substr(5,6): '暂无数据'"
									:fetchRes="fetchRes" :option="xinDianOption" :item="item" :isEdit="isEdit"
									@iconClick="editCard"></XinDianCard>
							</view>
							<!-- 滑块 -->
							<movable-view v-if="moviewShow" :animation="false" class="ui-mov-view ui-br-16" :x="moveX"
								:y="moveY" direction="all"
								:style="{ width: moveViewSize + 'rpx', height: 270 + 'rpx' }">
								<image class="ui-w-h-100" :src="touchItem.img"></image>
							</movable-view>
						</movable-area>
						<view class="ui-f-center ui-mar-t-45">
							<text class="ui-block-edit ui-font-center ui-br-16"
								@click="tabEdit()">{{ isEdit ? "完成" : "编辑" }}</text>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
		<view v-else>
			<view class="ui-loading">
				<u-loading-icon mode="semicircle" :vertical="true" text="加载中" textSize="18"></u-loading-icon>
			</view>
		</view>
		<!-- 		<touch-popup :minHeight="0.03" :maxHeight="0.15" :touchHeight="64" radius="30rpx">
			<view class="ui-list">
				<view v-for="item in moveList" :key="item.key" class="list-item">
					<u-icon :name="item.icon" size="50"></u-icon>
					<text>{{ item.title }}</text>
				</view>
			</view>
		</touch-popup> -->
	</app-body>
</template>

<script>
	import IndexDarg from "./components/index-darg.vue"
	import * as echarts from '@/static/js/echarts.js';
	import SwiperDevice from '@/pages/equipment/device-detail/components/watch-swiper.vue';
	import SleepCard from "./components/SleepCard.vue";
	import XinLvCard from "./components/XinLvCard.vue";
	import XueYaCard from "./components/XueYaCard.vue";
	import XueYangCard from "./components/XueYangCard.vue";
	import XinDianCard from "./components/XinDianCard.vue"
	import {
		GetCaiHongData,
		getDeviceListState,
		GetDaySleepQuality
	} from '@/common/http/api.js'
	import {
		mapState,
	} from 'vuex';
	export default {
		components: {
			SwiperDevice,
			IndexDarg,
			SleepCard,
			XinLvCard,
			XueYaCard,
			XueYangCard,
			XinDianCard
		},
		data() {
			return {
				sleepTime: '',
				sleepMap: {},
				fetchRes: {
					spMapList: [],
					dpMapList: [],
					tWatchBloodOxygen: [],
					HeartRateList: [],
					electrocardiogramMapList: [],
					sleepMap: 1,
					HeartRateTime: '',
					BloodPressureTime: '',
					BloodOxygenTime: '',
					ElectrocardiogramTime: ''
				},
				pageShow: false,
				isEdit: false,
				scrollAble: true,
				isRefresh: false,
				loading: true,
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
				echartDate: '',
				listData_c: [{
					type: '1',
					name: 'SleepCard',
					img: '../../../static/images/sleep_drag.svg',
					isShow: true
				}, {
					type: '2',
					name: 'XinLvCard',
					img: '../../../static/images/xinlv_drag.svg',
					isShow: true
				}, {
					type: '3',
					name: 'XueYaCard',
					img: '../../../static/images/xueya_drag.svg',
					isShow: true
				}, {
					type: '4',
					name: 'XueYangCard',
					img: '../../../static/images/xueyang_drag.svg',
					isShow: true
				}, {
					type: '5',
					name: 'XinDianCard',
					img: '../../../static/images/xindian_drag.svg',
					isShow: true
				}],
				// CheckAppId: null,
				deleteAppID: null, //触发删除的itemID
				showDelete: false, //删除按钮状态
				IsDeleteAfter: false, //是否为删除后
				IsCancelDelete: false, //是否为取消后
				moviewShow: false, //滑块状态
				areaBoxInfo: null, //保存滑动区域盒子dom信息
				inBoxXY: {}, //鼠标在item中的坐标
				touchIndex: 0, //被移动index
				touchItem: '', //备份被移动item数据
				moveX: 0, //相对滑动盒子的坐标
				moveY: 0, //相对滑动盒子的坐标
				hoverClass: '',
				hoverClassIndex: null, //最终index
				moveList: [{
					key: 'walk',
					icon: '/static/images/walk@3x.png',
					title: '行走'
				}, {
					key: 'run',
					icon: '/static/images/run@3x.png',
					title: '跑步'
				}, {
					key: 'circling',
					icon: '/static/images/cycling.png',
					title: '骑行'
				}]
			}
		},
		created() {
			this.sleepTime = uni.$u.timeFormat(new Date(), 'yyyy-mm-dd')
			this.getDate();
			const _this = this;
			uni.getStorage({
				key: 'healthCardData',
				success(res) {
					_this.listData_c = res.data;
				}
			})
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				familyList: state => state.familyList,
			}, ),
			moveViewSize() {
				if (this.areaBoxInfo && this.areaBoxInfo.width) {
					return this.areaBoxInfo.width / 1.2
				} else {
					return 0
				}
			}
		},
		onShow() {
			this.init();
		},
		methods: {
			/**
			 *下拉刷新
			 **/
			pullDownRefresh() {
				this.isRefresh = true;
				this.init();
			},
			init() {
				// this.pageShow = false;
				// this.loading = true;
				getDeviceListState({
					pageSize: 10000
				}).then(res => {
					this.deviceList = res.rows.filter(n => {
						return n.type === '2'
					})
					if (this.deviceList.length) {
						this.current = 0
						this.swiperData = this.deviceList[0]
						this.$store.commit('setDeviceInfo', this.swiperData)
						this.pageShow = true;
						this.fetchData();
					} else { // 没有数据
						this.pageShow = false;
						this.loading = false;
						this.logstatrt({});
					}
				})
			},
			tabEdit() {
				this.isEdit = !this.isEdit;
				if (this.isEdit) {
					setTimeout(() => {
						this.resetListDom()
					}, 300)
				}
			},
			editCard(type) {
				console.log('type', type)
				this.listData_c.forEach(item => {
					if (item.type === type) {
						item.isShow = !item.isShow;
					}
				})
				this.setCardData();
			},
			swiperChange(val) {
				this.current = val.detail.current
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
					// dayTime: uni.$u.timeFormat(new Date(), 'yyyy-mm-dd'),
					dayTime: '',
					humanId,
				}
				GetCaiHongData(params).then(res => {
					res.data.sleepMap = res.data.sleepMap ? res.data.sleepMap : 1; // 无数据默认显示1
					this.logstatrt(res);
					// var tt = {
					// 	data:this.fetchRes
					// }
					// this.logstatrt(tt);
					this.fetchRes = res.data;
					this.loading = false;
					this.isRefresh = false;
					console.log('fetchres', this.fetchRes)
				})
				GetDaySleepQuality({
					deviceId,
					humanId,
					// dayTime: '2023-5-06'
					dayTime: uni.$u.timeFormat(new Date(), 'yyyy-mm-dd')
				}).then(res => {
					const {
						sleepQuality,
						sleepScore
					} = res.data
					this.sleepMap = {
						sleepQuality,
						score: sleepScore || 0
					}
					console.log(res, '-----------------------')
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
			/**
			 * 活动图表
			 */
			caiHongOptionHandle(res) {
				// var data = [{
				// 		"name": "StepNum",
				// 		"value": 3000,
				// 		"maxValue": 10000
				// 	},
				// 	{
				// 		"name": "calorie",
				// 		"value": 200,
				// 		"maxValue": 1000
				// 	},
				// 	{
				// 		"name": "DurationNum",
				// 		"value": 10,
				// 		"maxValue": 30
				// 	}
				// ];
				let data;
				if (res.data) {
					data = res.data.rainbowDiagram;
				} else {
					data = [{
							"name": "StepNum",
							"value": 0,
							"maxValue": 10000
						},
						{
							"name": "calorie",
							"value": 0,
							"maxValue": 1000
						},
						{
							"name": "DurationNum",
							"value": 0,
							"maxValue": 30
						}
					];
				}
				let caiHongData = [];
				let maxDataArr = [];
				let resArr = [];
				for (let i = 0; i < data.length; i++) {
					if (data[i]['name'] === 'calorie') { //卡路里
						this.maxDataArr[0] = data[i].maxValue;
						this.caiHongData[0] = data[i].value ? data[i].value : 0;
						resArr[0] = data[i];
					}
					if (data[i]['name'] === 'StepNum') { //步数
						this.maxDataArr[2] = data[i].maxValue;
						this.caiHongData[2] = data[i].value ? data[i].value : 0;
						resArr[2] = data[i];
					}
					if (data[i]['name'] === 'DurationNum') { //活动时间
						this.maxDataArr[1] = data[i].maxValue;
						this.caiHongData[1] = data[i].value ? data[i].value : 0;
						resArr[1] = data[i];
					}
				}
				console.log(resArr, 'resarr')
				// data.foreach((item)=>{
				// 	console.log(item)
				// })
				var seriesd = [],
					legend = [];

				for (var j in resArr) {
					if (legend.indexOf(resArr[j]["name"] == -1)) {
						legend.push({
							'icon': 'rect',
							"name": resArr[j]["name"]
						});
					}
					var ra = resArr.length - 1 - j;
					seriesd.push({
						name: resArr[j]["name"],
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
						center: ["50%", "70%"],
						data: [{
								value: this.maxDataArr[j] - resArr[j]["value"] > 0 ? resArr[j]["value"] : this
									.maxDataArr[j],
								name: resArr[j]["name"],
								label: {
									normal: {
										postion: "center"
									}
								},
							},
							{
								value: this.maxDataArr[j] - resArr[j]["value"] > 0 ? this.maxDataArr[j] -
									resArr[j]
									["value"] : 0,
								itemStyle: {
									normal: {
										color: 'rgba(203,203,203,0.5)',

									},
									emphasis: {
										color: 'rgba(203,203,203,1)'
									}
								},
								name: 'showtip_' + resArr[j]["value"]
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
				console.log('seriesd', seriesd)
				seriesd.push({
					type: 'gauge',
					z: 3,
					min: 0,
					max: 200,
					splitNumber: 5,
					center: ['100%', '100%'], // 默认全局居中
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
				let colorArr = [];
				seriesd.forEach(item => {
					if (item.name) {
						switch (item.name) {
							case 'calorie':
								colorArr.push("rgba(255,97,97,1)");
								break;
							case 'DurationNum':
								colorArr.push("rgba(142,230,130,1)");
								break;
							case 'StepNum':
								colorArr.push("rgba(115,227,255,1)");
								break;
						}
					}
				})
				console.log('qq', colorArr)

				this.caiHongOption = {
					grid: {
						top: 0,
						left: 0,
						bottom: 0,
						right: 0
					},
					series: seriesd,
					color: colorArr
				};
			},
			/**
			 * 心率图表
			 * @param {Object} res
			 */
			xinLvOptionHandle(res) {
				// var data = [20, 60, 34, 25, 33, 46, 32, 35, 27, 28];
				let arr = [];
				if (res.data && res.data.HeartRateList.length !== 0) {
					arr = res.data.HeartRateList;
				} else {
					arr = [{
							time: this.date + " 00:51:00",
							value: "20"
						},
						{
							time: this.date + " 06:51:00",
							value: "33"
						},
						{
							time: this.date + " 10:51:00",
							value: "34"
						},
						{
							time: this.date + " 13:51:00",
							value: "45"
						},
						{
							time: this.date + " 15:51:00",
							value: "25"
						},
						{
							time: this.date + " 18:51:00",
							value: "60"
						},
						{
							time: this.date + " 21:51:00",
							value: "35"
						},
						{
							time: this.date + " 23:51:00",
							value: "27"
						},
					]
				}
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
			/**
			 * 血氧图表
			 * @param {Object} res
			 */
			xueYaOptionHandle(res) {
				let dateRes = res.data && res.data.BloodPressureTime.slice(0, 4) + '-' + res.data.BloodPressureTime.slice(
						5, 7) +
					'-' + res.data.BloodPressureTime.slice(8, 10);
				let spMapList = []
				let dpMapList = []
				let spArr = [];
				let dpArr = [];
				if (res.data && res.data.spMapList.length !== 0) {
					spArr = res.data.spMapList;
				} else {
					console.log('血氧无数据');
					spArr = [{
							time: (dateRes ? dateRes : this.echartDate) + " 00:00:01",
							value: "90"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 02:00:00",
							value: "129"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 04:00:00",
							value: "90"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 06:00:00",
							value: "129"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 08:00:00",
							value: "90"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 10:00:00",
							value: "129"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 12:00:00",
							value: "90"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 14:00:00",
							value: "129"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 16:00:00",
							value: "90"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 18:00:00",
							value: "129"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 20:00:00",
							value: "90"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 22:00:00",
							value: "129"
						}
					]
				}
				if (res.data && res.data.dpMapList.length !== 0) {
					dpArr = res.data.dpMapList;
				} else {
					dpArr = [{
							time: (dateRes ? dateRes : this.echartDate) + " 00:00:01",
							value: "60"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 02:00:00",
							value: "75"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 04:00:00",
							value: "60"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 06:00:00",
							value: "75"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 08:00:00",
							value: "60"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 10:00:00",
							value: "75"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 12:00:00",
							value: "60"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 14:00:00",
							value: "75"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 16:00:00",
							value: "60"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 18:00:00",
							value: "75"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 20:00:00",
							value: "60"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 22:00:00",
							value: "75"
						}
					]
				}

				for (let i = 0; i < spArr.length; i++) {
					spMapList.push([
						spArr[i].time,
						spArr[i].value
					])
				}
				for (let i = 0; i < dpArr.length; i++) {
					dpMapList.push([
						dpArr[i].time,
						dpArr[i].value
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
						left: '-15%',
						right: '0',
						bottom: '100%',
						containLabel: true
					},
					toolbox: {},
					xAxis: {
						type: 'time',
						show: false,
						// interval: 6 * 3600 * 1000, // 间隔为6小时
						min: `${dateRes + ' 00:00:00'}`, // x轴起始时间
						max: `${dateRes + ' 23:59:59'}`, // x轴结束时间
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
						name: '舒张压',
						// type: 'line',
						// stack: 'Total',
						data: dpMapList,
						// showSymbol: false,
						itemStyle: {
							normal: {
								lineStyle: {
									color: "#63DDBA",
									width: 1
								}
							},
							barBorderRadius: [15, 15, 0, 0]
						},
						type: 'bar',
						showSymbol: false,
						itemStyle: {
							color: '#63DDBA'
						},
						barWidth: '3', //---柱形宽度
						barCategoryGap: '10%', //---柱形间距
					}, {
						name: '收缩压',
						// type: 'line',
						// stack: 'Total',
						data: spMapList,
						// showSymbol: false,
						itemStyle: {
							normal: {
								lineStyle: {
									color: "#FF7E23",
									width: 1
								}
							},
							barBorderRadius: [15, 15, 0, 0]
						},
						type: 'bar',
						showSymbol: false,
						itemStyle: {
							color: '#FF7E23'
						},
						barWidth: '3', //---柱形宽度
						barCategoryGap: '10%', //---柱形间距
					}]
				}
			},
			/**
			 * 血压图表
			 * @param {Object} res
			 */
			xueYangOptionHandle(res) {
				const dateRes = res.data && res.data.BloodOxygenTime.slice(0, 4) + '-' + res.data.BloodOxygenTime.slice(5,
						7) + '-' +
					res.data.BloodOxygenTime.slice(8, 10);
				let arr = [];
				if (res.data && res.data.tWatchBloodOxygen.length !== 0) {
					arr = res.data.tWatchBloodOxygen;
				} else {
					arr = [{
							time: (dateRes ? dateRes : this.echartDate) + " 00:00:01",
							value: "75"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 02:00:00",
							value: "109"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 04:00:00",
							value: "75"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 06:00:00",
							value: "109"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 08:00:00",
							value: "75"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 10:00:00",
							value: "109"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 12:00:00",
							value: "75"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 14:00:00",
							value: "109"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 16:00:00",
							value: "75"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 18:00:00",
							value: "109"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 20:00:00",
							value: "75"
						},
						{
							time: (dateRes ? dateRes : this.echartDate) + " 22:00:00",
							value: "109"
						}
					]
				}
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
						left: '-15%',
						right: '0',
						bottom: '100%',
						containLabel: true
					},
					xAxis: [{
						show: false,
						type: 'time',
						// interval: 6 * 60 * 60 * 1000, // 设置x轴间隔为6小时
						min: `${dateRes + ' 00:00:00'}`, // x轴起始时间
						max: `${dateRes + ' 23:49:00'}`, // x轴结束时间
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
							]),
							barBorderRadius: [15, 15, 0, 0]
						},
						barWidth: '3', //---柱形宽度
						barCategoryGap: '20%', //---柱形间距
						data: resArr
					}]
				}
			},
			/**
			 * 心电图表
			 */
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
			listChange(option) {
				console.log("listChange", option)
			},
			getDomInfo(id, callBack) {
				const query = uni.createSelectorQuery().in(this);
				query.select('#' + id)
					.boundingClientRect()
					.exec(function(res) {
						callBack(res[0]);
					});
			},
			AppLi_touchstart(index, event) {
				if (!this.isEdit) {
					return; //编辑状态下允许拖动
				}
				this.scrollAble = false; //阻止页面滚动
				this.touchItem = this.listData_c[index];
				// 行为判断
				if (this.showDelete) {
					// 取消删除
					if (this.touchItem.appId != this.deleteAppID) {
						this.deleteAppID = null;
						this.showDelete = false;
						this.IsCancelDelete = true;
					}
					// 删除
					// if(this.touchItem.appId==this.deleteAppID){
					// 	this.deleteAppItem(index)
					// }
				}
				// 过时触发（touchEnd中清除此定时器）
				this.Loop = setTimeout(
					() => {
						// 触感反馈（安卓上是150毫秒，ios无短触控反馈）
						uni.vibrateShort();
						this.showDelete = true;
						this.deleteAppID = this.touchItem.appId;
						// 拖动逻辑
						//显示可移动方块
						this.moviewShow = true
						//保存当前所选择的索引
						this.touchIndex = index;
						// 设置可移动方块的初始位置为当前所选中图片的位置坐标
						this.moveX = this.listData_c[index].x;
						this.moveY = this.listData_c[index].y;
						var x = event.changedTouches[0].clientX - this.areaBoxInfo.left;
						var y = event.changedTouches[0].clientY - this.areaBoxInfo.top;
						// 保存鼠标在图片内的坐标
						this.inBoxXY = {
							x: x - this.listData_c[index].x,
							y: y - this.listData_c[index].y,
						}
					},
					200);
			},
			AppLi_touchmove(event) {
				if (!this.isEdit) {
					return; //编辑状态下允许拖动
				}
				// 每次endTouch清除startTouch删除按钮定时器
				if (this.Loop) {
					clearTimeout(this.Loop);
					this.Loop = null;
				}
				if (this.showDelete) {
					let areaBoxTop = this.areaBoxInfo.top;
					let areaBoxLeft = this.areaBoxInfo.left;
					//重置为以拖拽盒子左上角为坐标原点
					var x = event.changedTouches[0].clientX - areaBoxLeft;
					var y = event.changedTouches[0].clientY - areaBoxTop;
					this.moveX = x - this.inBoxXY.x;
					this.moveY = y - this.inBoxXY.y;
					let setIng = false;
					this.listData_c.forEach((item, idx) => {
						if (this.moveX > item.x - 50 && this.moveX < item.x + 100 && this.moveY > item.y && this
							.moveY < item.y + 160) {
							this.hoverClass = 'appLi' + idx
							this.hoverClassIndex = idx;
							setIng = true
						}
					});
					// 都不存在代表脱离
					if (!setIng) {
						this.hoverClass = ""
						this.hoverClassIndex = null;
					}
				}
			},
			AppLi_touchend(index) {
				if (!this.isEdit) {
					return; //编辑状态下允许拖动
				}
				this.scrollAble = true; //阻止页面滚动
				console.log(this.listData_c)
				if (!this.showDelete && !this.IsDeleteAfter && !this.IsCancelDelete) {
					// 点击事件进入
				} else {
					// 为下次getInto清除状态
					this.IsDeleteAfter = false;
					this.IsCancelDelete = false;
					// 移动结束隐藏可移动方块
					if (this.hoverClassIndex != null && this.touchIndex != this.hoverClassIndex) {
						console.log('ffff', this.hoverClassIndex, 'touchitem', this.touchItem)
						this.$set(this.listData_c, this.touchIndex, this.listData_c[this.hoverClassIndex]);
						this.$set(this.listData_c, this.hoverClassIndex, this.touchItem);
						this.setCardData()
						this.showDelete = false;
						this.resetListDom()
					}
					this.touchItem = ""
					this.moviewShow = false
					this.hoverClass = ""
					this.hoverClassIndex = null;
				}

				// 每次endTouch清除startTouch删除按钮定时器
				if (this.Loop) {
					clearTimeout(this.Loop);
					this.Loop = null;
				}
			},
			resetListDom() {
				let _this = this;
				this.getDomInfo('areaBox', info => {
					_this.areaBoxInfo = info;
					// 设置区域内所有图片的左上角坐标
					_this.listData_c.forEach((item, idx) => {
						_this.getDomInfo('appLi' + idx, res => {
							console.log(res, info, 'resinfo')
							item.x = idx % 2 === 1 ? 200 : 0;
							item.y = res.top - info.top;
						});
					});
				});
			},
			boxClick() {
				this.deleteAppID = null;
				this.showDelete = false;
			},
			setCardData() {
				uni.setStorage({
					key: 'healthCardData',
					data: this.listData_c
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	@font-face {
		font-family: 'health-index-font';
		src: url('@/static/ttf/DIN_Alternate_Bold.ttf');
	}

	.active {
		transition: all 0.1s;
		transform: scale(1);
	}

	.noActive {
		transition: all 0.1s;
		transform: scale(0.92);
	}

	movable-view {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 150rpx;
		width: 150rpx;
		background-color: #007AFF;
		color: #fff;
	}

	movable-area {
		height: 300rpx;
		width: 100%;
		background-color: #D8D8D8;
		overflow: hidden;
	}

	.max {
		width: 500rpx;
		height: 500rpx;
	}

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

		.ui-w-48 {
			width: 48%;
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
			padding-left: 50rpx;

			.ui-font-1 {
				font-size: 44rpx;
				color: #353535;
				letter-spacing: 0;
				line-height: 36rpx;
				font-weight: 700;
				font-family: health-index-font;
			}

			.ui-font-2 {
				font-size: 44rpx;
				color: #D4D4D4;
				letter-spacing: 0;
				line-height: 36rpx;
				font-weight: 700;
				font-family: health-index-font;
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

		.ui-block-edit {
			font-size: 30rpx;
			background-color: #FFF;
			border-radius: 30rpx;
			color: #353535;
			width: 156rpx;
			/* border: 1px solid #999999; */
			height: 56rpx;
			line-height: 56rpx;
			box-shadow: #eeeeee 4rpx 4rpx 16rpx 4rpx;
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
		transform: translate(-50%, -50%);
		position: absolute;
		top: 50%;
		left: 50%;
	}

	.ui-mov-view {
		background-color: #FFF;
		width: 100%;
		height: auto;
		box-shadow: #999999 2rpx 2rpx 10rpx 0rpx;
	}

	.select {
		// transform: scale(1.3);
		border-radius: 16rpx;
		border: 1px dashed #C0C0C0;
		color: #C0C0C0;
	}

	.ui-font-center {
		text-align: center;
	}

	.ui-add-watch {
		position: fixed;
		bottom: 268rpx;
		display: flex;
		justify-content: center;
		width: 100%;
	}

	.ui-add-watch-img1 {
		width: 330rpx;
		height: 330rpx;
	}

	.ui-add-watch-img2 {
		box-shadow: #dddddd 4rpx 4rpx 16rpx 4rpx;
		width: 100rpx;
		height: 100rpx;
		border-radius: 50%;
	}

	.ui-add-watch-text {
		position: fixed;
		bottom: 202rpx;
		display: flex;
		justify-content: center;
		width: 100%;
		color: #888888;
	}

	.ui-add-watch-font {
		font-size: 40rpx;
		color: #888888;
		font-weight: 500;
	}

	.ui-list {
		margin-top: 16rpx;
		display: flex;
		justify-content: space-around;
		align-items: center;

		.list-item {
			display: flex;
			flex-direction: column;
			align-items: center;

			text {
				font-size: 32rpx;
				color: #353535;
			}
		}
	}

	.ui-loading {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 500px;
		width: 100%;
	}
</style>