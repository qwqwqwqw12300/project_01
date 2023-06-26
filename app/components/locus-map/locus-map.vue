<!-- 地图轨迹 -->
<template>
	<view>
		<template v-if="show">
			<view class="ui-loading">
				<u-loading-icon mode="semicircle" :vertical="true" text="加载中" textSize="18"></u-loading-icon>
			</view>
		</template>
		<view v-show="!show" id="container" class="container"></view>
		<view :mapData="mapData" :change:mapData="maps.loadData"></view>
	</view>
</template>

<script>
	export default {
		name: "locus-map",
		props: {
			record: {
				type: Array,
				default: () => [],

			},
		},
		watch: {
			record: {
				handler(val) {
					// if (val.length) {
					// this.mapData.info = uni.$u.deepClone(val)
					if (val && val.length) {
						let list = val.map((item, index) => {
							return [item.location[0], item.location[1]]
						})
						this.mapData = {
							info: uni.$u.deepClone(list),
							cur: uni.$u.deepClone(this.$store.getters.positionInfo) || {},
							markersList: uni.$u.deepClone(val)
						}
					}

					// }
				},
				immediate: true,
				deep: true,
			}
		},
		data() {
			return {
				show: false,
				mapData: {
					info: [],
					cur: {}
				},
			};
		},
		created() {
			// this.sysHeight = (uni.getSystemInfoSync().windowHeight - 100) || 600
			// console.log(this.sysHeight)
		},
		methods: {
			onMsg(val) {
				this.show = val
			}
		}
	}
</script>

<script module="maps" lang="renderjs">
	import {
		mapMixin
	} from '../../common/mixin/map.mixin';
	export default {
		mixins: [mapMixin],
		data() {
			return {
				polyline: null,
				Marker: null,
				AMap: null,
				map: null,
				lineArr: [],
			}
		},
		mounted() {
			// this.loadMap(this.init);
		},

		methods: {
			loadData(data) {
				// if (!data.length) return
				const {
					info,
					cur
				} = data
				if (info && info.length && cur) {
					this.lineArr = this.deepClone(data)
					// this.loadMap(this.init)
					if (this.map) {
						this.polyline && this.map.remove(this.polyline)
						if (info.length) {
							this.markLine()
							// this.markers()
						} else {
							this.map.setCenter([cur.longitude, cur.latitude])
						}
					} else {
						this.$ownerInstance.callMethod('onMsg', true)
						this.loadMap(this.init);
					}
				}

			},
			/**
			 * 初始化
			 */
			init(AMap) {
				const {
					info,
					cur: {
						longitude,
						latitude
					}
				} = this.lineArr
				const center = info.length ? [info[0][0], info[0][1]] : [longitude, latitude]
				this.AMap = AMap;
				setTimeout(() => {
					this.map = new AMap.Map('container', {
						resizeEnable: true,
						center: center,
						zoom: 13 //地图显示的缩放级别
					})
					if(info.length) {
						this.markLine()
						// this.markers()
					}
				}, 100)
				this.$ownerInstance.callMethod('onMsg', false)
				// this.markLine()
			},
			markLine() {
				this.polyline = new this.AMap.Polyline({
					path: this.lineArr.info,
					showDir: true,
					isOutline: true,
					outlineColor: '#ffeeff',
					borderWeight: 3,
					strokeColor: "#3366FF",
					strokeOpacity: 1,
					strokeWeight: 6,
					// 折线样式还支持 'dashed'
					strokeStyle: "solid",
					// strokeStyle是dashed时有效
					strokeDasharray: [10, 5],
					lineJoin: 'round',
					lineCap: 'round',
					zIndex: 50,
				})

				this.polyline.setMap(this.map)
				// 缩放地图到合适的视野级别
				this.map.setFitView([this.polyline])
			},
			markers() {
				this.mapData.markersList.map((item, index) => {
					this.Marker = new this.AMap.Marker({
						map: this.map,
						icon: new this.AMap.Icon({
							// image: './static/images/starting_point.png',
							image: item.icon,
							size: new this.AMap.Size(32, 32), //图标大小
							imageSize: new this.AMap.Size(32, 32)
						}),
						position: [item.location[0], item.location[1]],
						offset: new AMap.Pixel(-13, -30)
					})
				})
			},
			deepClone(target) {
				let copy_obj = [];

				function _deepCopy(target) {
					if ((typeof target !== 'object') || !target) {
						return target;
					}
					for (let i = 0; i < copy_obj.length; i++) {
						if (copy_obj[i].target === target) {
							return copy_obj[i].copyTarget;
						}
					}
					let obj = {};
					if (Array.isArray(target)) {
						obj = []; //处理target是数组的情况
					}
					copy_obj.push({
						target: target,
						copyTarget: obj
					})
					Object.keys(target).forEach(key => {
						if (obj[key]) {
							return;
						}
						obj[key] = _deepCopy(target[key]);
					});
					return obj;
				}
				return _deepCopy(target);
			}
		}
	}
</script>
<style lang="scss">
	.ui-loading {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 800rpx;
		width: 100%;
	}

	.container {
		height: calc(100vh - 100px);
		width: 100%;
	}
</style>	