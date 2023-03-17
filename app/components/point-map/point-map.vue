<template>
	<view>
		<view :mapInfo="mapInfo" :change:mapInfo="maps.loadData"></view>
		<view id="container" class="container"></view>
	</view>
</template>

<script>
	export default {
		name: "point-map",
		props: {
			record: {
				type: Object,
				default: () => {},

			},
		},
		watch: {
			record: {
				handler(val) {
					if (val.latitude && val.longitude) {
						console.log(val, '0000999=--------')
						this.mapInfo = uni.$u.deepClone(val)
					}
				},
				immediate: true,
				deep: true,
			}
		},
		data() {
			return {
				mapInfo: {

				}
			}
		},
		methods: {}
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
				AMap: null,
				map: null,
				mapData: {},
				marker: null,
			}
		},
		mounted() {
			// this.loadMap(this.init);
		},
		methods: {
			loadData(data) {
				if (!data.latitude || !data.longitude) return
				console.log(data, '4444')
				this.mapData = data
				const {
					latitude,
					longitude
				} = data
				if (this.map) {
					this.map.remove(this.marker)
					this.mapMarker(longitude, latitude)
				} else {
					this.loadMap(this.init);
				}
			},
			/**
			 * 初始化
			 */
			init(AMap) {
				this.AMap = AMap;
				const {
					latitude,
					longitude
				} = this.mapData

				console.log(latitude, longitude, '----------------------------')
				this.map = new AMap.Map('container', {
					resizeEnable: true,
					center: [longitude, latitude],
					zoom: 13 //地图显示的缩放级别
				});
				this.mapMarker(longitude, latitude)
				// let marker = new AMap.Marker({
				// 	position: new AMap.LngLat(longitude, latitude),
				// 	offset: new AMap.Pixel(-13, -30),
				// 	icon: 'http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png'
				// })
				// this.map.add(marker)
			},
			mapMarker(longitude, latitude) {
				this.marker = new AMap.Marker({
					position: new AMap.LngLat(longitude, latitude),
					offset: new AMap.Pixel(-13, -30),
					icon: 'http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png'
				})
				this.map.add(this.marker)
			}
		},
	}
</script>
<style lang="scss" scoped>
	.container {
		height: 100vh;
		width: 100%;
	}
</style>
