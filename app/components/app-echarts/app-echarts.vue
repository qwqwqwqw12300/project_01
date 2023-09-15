<template>
	<view>
		<view class="echarts" :prop="option" :change:prop="echarts.update"></view>
	</view>
</template>

<script>
	export default {
		name: 'echarts',
		props: {
			option: {
				type: Object,
				required: true
			}
		},
		methods: {
			/**
			 * renderjs内的点击事件，回调到父组件
			 * @param {Object} params
			 */
			onViewClick(params) {
				// console.log('paramsparamsparams', params)
				this.$emit('Cclick', params)
				// this.$emit('clickechart', params.data[0])
			}
		},

	}
</script>

<script module="echarts" lang="renderjs">
	export default {
		data() {
			return {
				chart: null,
				initOption: {
					title: {
						text: ''
					},
					tooltip: {
						positionStatus: true,
						trigger: 'axis',
					},
					backgroundColor: '#fff',
					legend: {
						data: []
					},
					grid: {
						left: '20',
						right: '20',
						bottom: '5',
						top: '20',
						containLabel: true
					},
					toolbox: {},
					xAxis: {
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
							formatter: 'week'
						},
						splitLine: {
							show: false
						},
					},
					yAxis: {
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
					},
					series: [{
						showSymbol: false
					}]
				},
			}
		},
		// onShow() {
		// 	// this.update(this.option)
		// },
		mounted() {
			if (typeof window.echarts === 'object') {
				this.init()
			} else {
				// 动态引入类库
				window.wx = {}
				const script = document.createElement('script')
				script.src = 'static/js/echarts.js'
				// script.src = "https://cdn.jsdelivr.net/npm/echarts/dist/echarts.min.js";
				script.onload = this.init
				document.head.appendChild(script);
			}
		},
		methods: {
			/**
			 * 初始化echarts
			 */
			init() {
				console.log('initinitinit');
				this.chart = echarts.init(this.$el)
				this.chart.on('click', params => {
					console.log(params.data[0], 'params');
					this.onClick(params)
					// // 把点击事件的数据缓存
					// this.clickData = params
				})
				// this.chart.on('mousemove', (params) => {
				// 	console.log(params, '滑动获取到底');
				// })

				this.update(this.option)
			},
			/**
			 * 监测数据更新
			 * @param {Object} obj
			 */
			update(options) {
				const option = this.assignDeep({}, options)
				if (option.isRender) {
					option.series.forEach(n => {
						n.renderItem = this.seriesRender()
					})
				}

				if (this.chart) {
					// this.chart.clear();
					// 因App端，回调函数无法从renderjs外传递，故在此自定义设置相关回调函数
					if (option) {
						// const option = {};
						// this.assignDeep(option, this.initOption, obj);
						// console.log(option, '--------option');
						if (option.xAxis) {
							if (!Array.isArray(option.xAxis)) option.xAxis = [option.xAxis];
							option.xAxis.forEach(ele => {
								const formatter = ele.axisLabel && ele.axisLabel.formatter;
								if (formatter == 'week') {
									console.log(formatter, 'formatter');
									ele.axisLabel.formatter = this.axisLabelFormatter(formatter)
								} else {
									// console.log('else');
									ele.axisLabel.formatter = (val, index) => {
										if (index == 0) {
											val = 1694534400000
										}
										if (index == 1) {
											val = 1694556000000
										}
										if (index == 2) {
											val = 1694577600000
										}
										if (index == 3) {
											val = 1694599200000
										}
										if (index == 4) {
											val = 1694620799000
										}
										if (index == 5) {
											return
										}


										const date = new Date(val);
										const hour = date.getHours()
										const minu = date.getMinutes();
										return `${hour > 10 ? hour : 0 + hour}:${minu > 10 ? minu : '0'+minu}`;
									}
								}
							})
						}

						// 处理单条折线数据无法展示的问题
						if (option.series) {
							if (!Array.isArray(option.series)) option.series = [option.series];
							for (let i = 0; i < option.series.length; i++) {
								if (option.series[i].data && option.series[i].data.length === 1) {
									if (option.xAxis[0].type === 'time') {
										const arr = option.series[i].data[0];
										option.series[i].data.push([arr[0], 0]);
									} else {
										option.series[i].data.push(0);
									}
								}

							}

						}
						// console.log(option, 'option.series[i]------');


						// 设置新的option
						setTimeout(() => {
							this.chart.setOption(option, option.notMerge)
						}, 500)
					}
				}
			},
			/**
			 * 设置renderItem
			 */
			seriesRender() {
				return function(params, api) {
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
			},
			/**
			 * 设置Formatter
			 */
			axisLabelFormatter(type) {
				// console.log(type);
				let fn;
				switch (type) {
					case 'date':
						fn = val => {
							const date = new Date(val);
							const hour = date.getHours(),
								minu = date.getMinutes();
							return `${hour > 10 ? hour : 0 + hour}:${minu > 10 ? minu : '0'+minu}`;
						}
						break;
					case 'week':
						fn = (val, index) => {
							const weekArr = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
							return index == 7 ? '' : weekArr[new Date(val).getDay()];
						}
						break;
					default:
						fn = type;
						break;
				}
				return fn;
			},

			/**
			 * 点击事件，可传递到外部
			 * @param {Object} event
			 * @param {Object} instance
			 */
			onClick(options) {
				this.$ownerInstance.callMethod('onViewClick', {
					value: options.data[0]
				})
			},

			assignDeep(target, ...sources) {
				// 1. 参数校验
				if (target == null) {
					throw new TypeError('Cannot convert undefined or null to object');
				}

				// 2. 如果是基本类型，则转换包装对象
				const result = Object(target);
				// 3. 缓存已拷贝过的对象
				const hash = new WeakMap();

				// 4. 目标属性是否可直接覆盖赋值判断
				function canPropertyCover(node) {
					if (!node.target[node.key]) {
						return true;
					}
					if (node.target[node.key] == null) {
						return true;
					}
					if (!(typeof node.target[node.key] === 'object')) {
						return true;
					}
					if (Array.isArray(node.target[node.key]) !== Array.isArray(node.data)) {
						return true;
					}
					return false;
				}

				sources.forEach(v => {
					const source = Object(v);

					const stack = [{
						data: source,
						key: undefined,
						target: result
					}];

					while (stack.length > 0) {
						const node = stack.pop();
						if (typeof node.data === 'object' && node.data !== null) {
							let isPropertyDone = false;
							if (hash.get(node.data) && node.key !== undefined) {
								if (canPropertyCover(node)) {
									node.target[node.key] = hash.get(node.data);
									isPropertyDone = true;
								}
							}

							if (!isPropertyDone) {
								// tslint:disable-next-line:no-shadowed-variable
								let target;
								if (node.key !== undefined) {
									if (canPropertyCover(node)) {
										target = Array.isArray(node.data) ? [] : {};
										hash.set(node.data, target);
										node.target[node.key] = target;
									} else {
										target = node.target[node.key];
									}
								} else {
									target = node.target;
								}

								Reflect.ownKeys(node.data).forEach(key => {
									// 过滤不可枚举属性
									const desc = Object.getOwnPropertyDescriptor(node.data, key);
									if (desc && !desc.enumerable) {
										return;
									}
									stack.push({
										data: node.data[key],
										key: key,
										target: target
									});
								});
							}
						} else {
							Object.assign(node.target, {
								[node.key]: node.data
							});
						}
					}

				});
				return result;
			},

		}
	}
</script>

<style lang="scss" scoped>
	.echarts {
		width: 100%;
		height: 100%;
	}
</style>