!
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
				this.$emit('click', params)
			}
		}
	}
</script>

<script module="echarts" lang="renderjs">
	export default {
		data() {
			return {
				chart: null,
			}
		},
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
				this.chart = echarts.init(this.$el)
				this.chart.on('click', params => {
					this.onClick(params)
					// // 把点击事件的数据缓存
					// this.clickData = params

				})
				this.update(this.option)
			},
			/**
			 * 监测数据更新
			 * @param {Object} option
			 */
			update(option) {
				if (this.chart) {
					// 因App端，回调函数无法从renderjs外传递，故在此自定义设置相关回调函数
					if (option) {
						if (option.xAxis) {
							if (!Array.isArray(option.xAxis)) option.xAxis = [option.xAxis];
							option.xAxis.forEach(ele => {
								const formatter = ele.axisLabel.formatter;
								if (formatter) {
									ele.axisLabel.formatter = this.axisLabelFormatter(formatter)
								}
							})
						}
						// 设置新的option
						this.chart.setOption(option, option.notMerge)
					}
				}
			},

			/**
			 * 设置Formatter
			 */
			axisLabelFormatter(type) {
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
							const weekArr = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
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
					value: options.data,
					name: options.name,
					seriesName: options.seriesName
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.echarts {
		width: 100%;
		height: 100%;
	}
</style>
