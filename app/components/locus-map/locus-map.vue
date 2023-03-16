<!-- 地图轨迹 -->
<template>
	<view>
		<view id="container" class="container"></view>
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
					if (val.length) {
						console.log(val, '0000999=--------')
						this.mapData = uni.$u.deepClone(val)
					}
				},
				immediate: true,
				deep: true,
			}
		},
		data() {
			return {
				mapData: [],
			};
		},
		created() {
			// this.sysHeight = (uni.getSystemInfoSync().windowHeight - 100) || 600
			// console.log(this.sysHeight)
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
				if (!data.length) return
				this.lineArr = data
				console.log(data, 'dddddddddllllll')
				this.loadMap(this.init)
			},
			/**
			 * 初始化
			 */
			init(AMap) {
				this.AMap = AMap;
				this.map = new AMap.Map('container', {
					resizeEnable: true,
					center: [119.39139, 26.03001],
					zoom: 13 //地图显示的缩放级别
				})
				console.log(this.lineArr, 'llllllll')
				let polyline = new AMap.Polyline({
					path: this.lineArr,
					showDir:true,
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

				polyline.setMap(this.map)
				// 缩放地图到合适的视野级别
				this.map.setFitView([polyline])



			},
			loadOperation(newValue, oldValue, ownerInstance, instance) {
				// 数据变更
				console.log(newValue, oldValue, ownerInstance, instance)

			}

		}
	}
</script>
<style lang="scss">
	.container {
		height: calc(100vh - 100px);
		width: 100%;
	}
</style>
