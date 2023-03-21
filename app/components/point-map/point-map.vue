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
						const {
							latitude,
							longitude
						} = uni.$u.deepClone(val)
						this.mapInfo = {
							type: 'add',
							latitude,
							longitude
						}
					} else {
						const {
							latitude,
							longitude
						} = uni.$u.deepClone(this.$store.getters.positionInfo || {})
						this.mapInfo = {
							type: 'cur',
							latitude,
							longitude
						}
					}
				},
				immediate: true,
				deep: true,

			}
		},
		mounted() {
			// console.log(this.$store.getters.positionInfo, 'ccccc------------------------------')
		},
		data() {
			return {
				mapInfo: {
					type: 'add', //cur: 当前定位,add: '地图定位'
					latitude: '',
					longitude: '',
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
			// console.log(store, '33999999999--------------------------------')
			// this.loadMap(this.init);
		},
		methods: {
			loadData(data) {
				const {
					type,
					latitude,
					longitude
				} = data
				if (!latitude || !longitude) return
				this.mapData = this.deepClone(data)
				if (this.map) {
					this.map.remove(this.marker)
					if (type === 'add') {
						this.mapMarker()
					} else {
						this.map.setCenter([latitude, longitude]); //设置地图中心点
					}
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
					longitude,
					type
				} = this.mapData
				this.map = new AMap.Map('container', {
					resizeEnable: true,
					center: [longitude, latitude],
					zoom: 13 //地图显示的缩放级别
				});
				type === 'add' && this.mapMarker()
			},
			mapMarker() {
				const {
					latitude,
					longitude
				} = this.mapData
				console.log(this.AMap, '00000000--------------')
				this.marker = new AMap.Marker({
					position: new AMap.LngLat(longitude, latitude),
					offset: new AMap.Pixel(-13, -30),
					icon: 'http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png'
				})
				this.map.add(this.marker)
				this.map.setFitView()
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

		},
	}
</script>
<style lang="scss" scoped>
	.container {
		height: 100vh;
		width: 100%;
	}
</style>
