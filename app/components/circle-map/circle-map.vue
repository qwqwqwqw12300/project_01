<!-- 圆形地图  -->

<template>
	<view>
		<view id="container" class="container"></view>
		<view :sliderData="sliderData" :change:sliderData="maps.loadData"></view>
	</view>
</template>

<script>
	export default {
		name: "app-map",
		props: {
			record: {
				type: Number,
				default: 0,
			},
		},
		watch: {
			record: {
				handler(val) {
					if (val) {
						this.sliderData = uni.$u.deepClone(val)
					}
				},
				immediate: true,
			}
		},
		data() {
			return {
				sliderData: 0,
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
				operation: {
					longitude: 119.39139,
					latitude: 26.03001
				},
				radius: 0,
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
				this.radius = val
				console.log(val, 'radsddsdsd')
				this.init()
			},
			/**
			 * 初始化
			 */
			init(AMap) {
				this.AMap = AMap;
				this.map = new AMap.Map('container', {
					resizeEnable: true,
					center: [this.operation.longitude, this.operation.latitude],
					zoom: 13 //地图显示的缩放级别
				});
				let circle = new AMap.Circle({
					center: new AMap.LngLat(this.operation.longitude, this.operation.latitude), // 圆心位置
					radius: this.radius, // 圆半径
					fillColor: 'pink', // 圆形填充颜色
					fillOpacity: 0.4, // 圆形填充透明度
					strokeColor: '#fff', // 描边颜色
					strokeWeight: 2, // 描边宽度
				});
				this.map.add(circle)
			},


		}
	}
</script>
<style lang="scss">
	.container {
		height: calc(100vh - 300rpx);
		width: 100%;
	}
</style>
