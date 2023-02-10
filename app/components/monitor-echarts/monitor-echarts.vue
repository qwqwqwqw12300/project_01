<template>
	<view class="content">
		<view :prop="option" :change:prop="echarts.updateEcharts" id="echarts" class="echarts"></view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		GetStartDevice,
		GetEndDevice,
		GetNowInfo,
		GetRoomZone,
	} from '../../common/http/api';
	export default {
		data() {
			return {
				roomZoneList: [],
				option: {
					// title: {
					// 	text: '实时监控',
					// 	left: "center",
					// },
					xAxis: {
						type: 'value',
						min: -3,
						max: 3,
						interval: 0.5,
						// scale: true,
						axisLine: {
							lineStyle: {
								color: '#ddd',
							},
						},
						axisLabel: {
							color: '#666',
						},
						splitLine: {
							lineStyle: {
								color: '#eee',
							},
						},

					},
					yAxis: {
						type: "value",
						position: 'left',
						min: 0,
						max: 6,
						interval: 0.5,
						scale: true,
						axisLine: {
							lineStyle: {
								color: '#ddd',
							},
						},
						axisLabel: {
							color: '#666',
						},
						splitLine: {
							lineStyle: {
								color: '#eee',
							},
						},

					},
					series: [{
							type: 'scatter',
							name: '实时数据',
							symbolSize: [28, 28],
							// markPoint: {
							// 	label: {
							// 		show: true,
							// 	},
							// 	data: [{name: '某个坐标',
							// 		coord: [100, 120]
							// 	}],
							// },
							data: [],
							markArea: {
								silent: true,
								label: {
									position: ['50%', '50%'],
									align: 'center',
									color: 'rgba(0,0,0,0.3)',
									fontSize: 20
								},
								itemStyle: {
									color: 'rgba(0,0,0,0.1)'
								},
								data: [

								],
							},
						},
						{
							symbolSize: 25,
							data: [-18, 0],
							type: 'scatter',
							symbol: 'image://static/images/echart-device.png',
						}
					]
				}
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			})
		},
		// async onLoad() {
		// 	console.log('455485865')

		// },
		// onUnload() {
		// GetEndDevice({
		// 	deviceId: this.deviceInfo.deviceId
		// })
		// },
		mounted() {
			this.handleInit()
		},
		methods: {
			async handleInit() {
				const data = await GetRoomZone({
					deviceId: this.deviceInfo.deviceId
				})
				const {
					roomLeft,
					roomRight
				} = this.deviceInfo.parameter.deviceLocation
				this.option.xAxis.min = -roomLeft || -3
				this.option.xAxis.max = roomRight || 3
				this.roomZoneList = data.rows.map(n => {
					const {
						x1,
						x2,
						y1,
						y2
					} = n
					return [{
						name: n.name,
						coord: [x2, y1],
						itemStyle: {
							color: 'rgba(215,215,215,0.7)'
						},
					}, {
						coord: [x1, y2],
					}, ]
				})
				console.log(this.roomZoneList, '0999')
				this.option.series[0].markArea.data = this.roomZoneList
				const timer = setInterval(() => {
					this.handleQuery()
				}, 4000);
				this.$once('hook:beforeDestroy', () => {
					clearInterval(timer);
				})
			},
			handleStart() {
				GetStartDevice({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					this.handleQuery()
				})
			},
			handleQuery() {
				GetNowInfo({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					this.option.series[0].data = res.data.map(n => {
						const {
							xPosCm,
							yPosCm,
							state
						} = n
						return {
							value: [((+xPosCm) / 100), ((+yPosCm) / 100)],
							state,
						}
					})
				})
			},
			handleEnd() {
				GetEndDevice({
					deviceId: this.deviceInfo.deviceId
				})
			},
		}
	}
</script>
<script module="echarts" lang="renderjs">
	let myChart
	export default {
		mounted() {
			if (typeof window.echarts === 'function') {
				this.initEcharts()
			} else {
				// 动态引入较大类库避免影响页面展示
				const script = document.createElement('script')
				// view 层的页面运行在 www 根目录，其相对路径相对于 www 计算
				script.src = 'static/js/echarts.js'
				script.onload = this.initEcharts.bind(this)
				document.head.appendChild(script)
			}
		},
		methods: {
			initEcharts() {
				myChart = echarts.init(document.getElementById('echarts'))
				this.option.series[0].symbol = function(value, params, ) {
					const urlObj = {
						0: 'image://static/images/person.png',
						1: 'image://static/images/person-down.png',
					}
					return urlObj[params.data.state]
				}
				// 观测更新的数据在 view 层可以直接访问到
				myChart.setOption(this.option)
			},
			updateEcharts(newValue, oldValue, ownerInstance, instance) {
				// 监听 service 层数据变更
				myChart.setOption(newValue)
			},
			onClick(event, ownerInstance) {
				// 调用 service 层的方法
				ownerInstance.callMethod('onViewClick', {
					test: 'test'
				})
			}
		}
	}
</script>

<style lang="scss">
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 0 50rpx;
		box-sizing: border-box;
		margin-top: 30px;
		// background-color: #fff;
	}

	.echarts {
		// margin-top: 30px;
		width: 100%;
		height: 365px;
		background-color: #fff;
	}
</style>
