<!-- 圆形地图  -->

<template>
	<view>
		<view id="container" class="container"></view>
		<view :slider="slider" :change:slider="maps.loadData"></view>
	</view>
</template>

<script>
	export default {
		name: "app-map",
		props: {
			record: {
				type: Object,
				default: () => {},
			},
		},
		watch: {
			record: {
				handler(val) {
					console.log(val, '333333333')
					if (val) {
						this.slider = uni.$u.deepClone(val)
					}
				},
				deep: true,
				immediate: true,
			}
		},
		data() {
			return {
				slider: 0,
			};
		},
		methods: {

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
				mapInfo: {
					sliderValue: 200,
					latitude: '',
					longitude: '',
				},
				circle: null,
				marker: null,
				AMap: null,
				map: null,
			}
		},
		mounted() {
			// this.loadMap(this.init);
		},
		methods: {
			loadData(val) {
				console.log(val, '9-------------------------')
				// 数据变更
				const {
					latitude,
					longitude
				} = val
				this.mapInfo = this.deepClone(val)
				if (this.map) {
					this.map.remove(this.circle)
					this.drawCircle()
					this.map.remove(this.marker)
					this.mapMarker()
				} else {
					if (longitude && latitude) {
						this.loadMap(this.init);
					}
				}
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
			},
			/**
			 * 初始化
			 */
			init(AMap) {
				console.log('098765')
				const {
					latitude,
					longitude
				} = this.mapInfo
				this.AMap = AMap;
				this.map = new AMap.Map('container', {
					resizeEnable: true,
					center: [longitude, latitude],
					zoom: 13 //地图显示的缩放级别
				});
				this.drawCircle()
				this.mapMarker()
			},
			drawCircle() {
				const {
					latitude,
					longitude,
					sliderValue
				} = this.mapInfo
				this.circle = new AMap.Circle({
					center: new AMap.LngLat(longitude, latitude), // 圆心位置
					radius: sliderValue, // 圆半径
					fillColor: 'rgba(100,163,242,0.50)', // 圆形填充颜色
					fillOpacity: 0.4, // 圆形填充透明度
					strokeColor: '#3B7AFF', // 描边颜色
					strokeWeight: 2, // 描边宽度
				});
				this.circle.setMap(this.map)
				// 缩放地图到合适的视野级别
				this.map.setFitView([this.circle])
			},
			mapMarker() {
				const {
					latitude,
					longitude,
				} = this.mapInfo
				const icon = new this.AMap.Icon({
					size: new AMap.Size(40, 40), // 图标尺寸
					image: 'static/images/mapSite.png', // Icon的图像
					// imageOffset: new AMap.Pixel(0, -60), // 图像相对展示区域的偏移量，适于雪碧图等
					imageSize: new AMap.Size(40, 40) // 根据所设置的大小拉伸或压缩图片
				})
				this.marker = new AMap.Marker({
					position: new AMap.LngLat(longitude, latitude),
					offset: new AMap.Pixel(-18, -40),
					icon,
				})
				this.map.add(this.marker)
			}
		}
	}
</script>
<style lang="scss">
	.container {
		height: calc(100vh - 300rpx);
		width: 100%;
	}
</style>
