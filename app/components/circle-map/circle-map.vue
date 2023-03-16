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
				// 数据变更
				const {
					latitude,
					longitude
				} = val
				this.mapInfo = val
				if (this.map) {
					this.map.remove(this.circle)
					this.drawCircle()
					this.map.remove(this.marker)
					this.mapMarker()
				} else {
					if (longitude && longitude) {
						this.loadMap(this.init);
					}
				}
			},
			/**
			 * 初始化
			 */
			init(AMap) {
				// const {
				// 	latitude,
				// 	longitude
				// } = this.mapInfo
				this.AMap = AMap;
				this.map = new AMap.Map('container', {
					resizeEnable: true,
					center: [119.39139, 26.03001],
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
				const icon  = new this.AMap.Icon({
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
