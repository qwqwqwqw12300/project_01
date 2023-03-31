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
				document.head.appendChild(script)
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
						if (option.xAxis && Array.isArray(option.xAxis)) {
							option.xAxis.forEach(ele => {
								const formatter = ele.axisLabel.formatter
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
				switch (type) {
					case 'date':
						return val => {
							const date = new Date(val);
							const hour = date.getHours(),
								minu = date.getMinutes();
							return `${hour > 10 ? hour : 0 + hour}:${minu > 10 ? minu : '0'+minu}`;
						}
						break;
					default:
						return type;
						break;
				}
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
				// if (this.clickData) {
				// 	// 把echarts点击事件相关的值传递到renderjs外
				// 	instance.callMethod('onViewClick', {
				// 		value: this.clickData.data,
				// 		name: this.clickData.name,
				// 		seriesName: this.clickData.seriesName
				// 	})
				// 	// 上次点击数据置空
				// 	this.clickData = null
				// }
			},
			/**
			 * 设置tooltip的位置，防止超出画布
			 */
			tooltipPosition() {
				return (point, params, dom, rect, size) => {
					//其中point为当前鼠标的位置，size中有两个属性：viewSize和contentSize，分别为外层div和tooltip提示框的大小
					let x = point[0]
					let y = point[1]
					let viewWidth = size.viewSize[0]
					let viewHeight = size.viewSize[1]
					let boxWidth = size.contentSize[0]
					let boxHeight = size.contentSize[1]
					let posX = 0 //x坐标位置
					let posY = 0 //y坐标位置
					if (x < boxWidth) { //左边放不开
						posX = 5
					} else { //左边放的下
						posX = x - boxWidth
					}
					if (y < boxHeight) { //上边放不开
						posY = 5
					} else { //上边放得下
						posY = y - boxHeight
					}
					return [posX, posY]
				}
			},
			/**
			 * tooltip格式化
			 * @param {Object} unit 数值后的单位
			 * @param {Object} formatFloat2 是否保留两位小数
			 * @param {Object} formatThousands 是否添加千分位
			 */
			tooltipFormatter(unit, formatFloat2, formatThousands) {
				return params => {
					let result = ''
					unit = unit ? unit : ''
					for (let i in params) {
						if (i == 0) {
							result += params[i].axisValueLabel
						}
						let value = '--'
						if (params[i].data !== null) {
							value = params[i].data
							// 保留两位小数
							if (formatFloat2) {
								value = this.formatFloat2(value)
							}
							// 添加千分位
							if (formatThousands) {
								value = this.formatThousands(value)
							}
						}
						// #ifdef H5
						result += '\n' + params[i].seriesName + '：' + value + ' ' + unit
						// #endif

						// #ifdef APP-PLUS
						result += '<br/>' + params[i].marker + params[i].seriesName + '：' + value + ' ' + unit
						// #endif
					}
					return result
				}
			},
			/**
			 * 保留两位小数
			 * @param {Object} value
			 */
			formatFloat2(value) {
				let temp = Math.round(parseFloat(value) * 100) / 100
				let xsd = temp.toString().split('.')
				if (xsd.length === 1) {
					temp = (isNaN(temp) ? '0' : temp.toString()) + '.00'
					return temp
				}
				if (xsd.length > 1) {
					if (xsd[1].length < 2) {
						temp = temp.toString() + '0'
					}
					return temp
				}
			},
			/**
			 * 添加千分位
			 * @param {Object} value
			 */
			formatThousands(value) {
				if (value === undefined || value === null) {
					value = ''
				}
				if (!isNaN(value)) {
					value = value + ''
				}
				let re = /\d{1,3}(?=(\d{3})+$)/g
				let n1 = value.replace(/^(\d+)((\.\d+)?)$/, function(s, s1, s2) {
					return s1.replace(re, '$&,') + s2
				})
				return n1
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