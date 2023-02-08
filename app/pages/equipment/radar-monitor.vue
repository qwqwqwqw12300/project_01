<!-- <template>
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
					title: {
						text: '实时监控',
						left: "center",
					},
					xAxis: {
						type: 'value',
						min: -3,
						max: 3,
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
							name: '销量',
							symbol(params) {
								const urlObj = {
									0: '../../static/images/person.png',
									1: '../../static/images/person-down.png',
								}
								return `image://${urlObj[params[2]]}`

							},
							symbolSize: [30, 30],
							// markPoint: {
							// 	label: {
							// 		show: true,
							// 	},
							// 	data: [{name: '某个坐标',
							// 		coord: [100, 120]
							// 	}],
							// },
							data: [
								[1, 1, '0'],
								[1, 2, '1'],
								[2, 2, '0']
							],
							markArea: {
								silent: true,
								label: {
									position: ['50%', '50%'],
									align: 'center',
									color: 'rgba(0,0,0,0.3)',
									fontSize: 30
								},
								itemStyle: {
									color: 'rgba(0,0,0,0.1)'
								},
								data: [

								],
							},
						},
						{
							symbolSize: 20,
							data: [-10, 0],
							type: 'scatter',
							symbol: 'image://../../static/images/echart-device.png'
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
		async onLoad() {
			this.handleStart()
			const data = await GetRoomZone({
				deviceId: this.deviceInfo.deviceId
			})
			console.log(data.rows, 'ii')
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
						color: 'rgba(0,255,0,0.2)'
					},
				}, {
					coord: [x1, y2],
				}, ]
			})
			this.option.series[0].markArea.data = this.roomZoneList
		},
		mounted() {
			const timer = setInterval(() => {
				this.handleQuery()
			}, 4000);
			this.$once('hook:beforeDestroy', () => {
				clearInterval(timer);
			})
		},
		methods: {
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
					this.option.series[0].data = [
						[1, 1, '1'],
						[1, 2, '1'],
						[2, 2, '0']
					]
					console.log(res, '999')
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
				script.src = 'static/echarts.js'
				script.onload = this.initEcharts.bind(this)
				document.head.appendChild(script)
			}
		},
		methods: {
			initEcharts() {
				myChart = echarts.init(document.getElementById('echarts'))
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

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.echarts {
		margin-top: 30px;
		width: 100%;
		height: 400px;
	}

	.ui-tip {
		display: flex;
		align-items: center;
		justify-content: space-around;
		padding: 0rpx 70rpx;
		margin-top: 20rpx;
	}

	.ui-item {
		display: flex;
		flex-direction: row;
		align-items: center;

		text {
			font-size: 24rpx;
		}

		.span {
			width: 50rpx;
			height: 50rpx;
			background-color: gray;
			margin-right: 10rpx;
		}

		.span1 {
			width: 50rpx;
			height: 50rpx;
			background-color: #008fff;
			margin-right: 14rpx;
		}
	}
</style>
 -->