<!--多边形-->
<template>
	<view>
		<view id="container" class="container"></view>
		<view :mapData="mapData" :change:mapData="maps.loadData"></view>
	</view>
</template>

<script>
	export default {
		name: "polygon-map",
		props: {
			record: {
				type: Object,
				default: () => {},
			},
		},
		data() {
			return {
				mapData: {

				},
			};
		},
		watch: {
			record: {
				handler(val) {
					if (val) {
						this.mapData = uni.$u.deepClone(val)
					}
				},
				deep: true,
				immediate: true,
			}
		},
		methods: {
			/**
			 * 接收地图信息
			 */
			onMsg(info) {
				this.$emit('sendMsg', info)
			}
		}
	}
</script>

<script module="maps" lang="renderjs">
	import {
		mapMixin
	} from '../../common/mixin/map.mixin';
	import {
		deepClone,
	} from '@/common/utils/util';
	export default {
		mixins: [mapMixin],
		data() {
			return {
				beginPoints: [],
				beginMarks: [],
				beginNum: 0,
				polygon: null,
				polygonEditor: '',
				AMap: null,
				map: null,
				clickListener: null,
				mapInfo: {},
			}
		},
		mounted() {
			// this.loadMap(this.init);
		},

		methods: {
			loadData(val) {
				console.log(val, '0000000000000000000000-------')
				this.mapInfo = deepClone(val)
				const {
					latitude,
					longitude,
					points
				} = val
				if (latitude && longitude) {
					this.loadMap(this.init);
				}
			},
			/**
			 * 初始化
			 */
			init(AMap) {
				const {
					latitude,
					longitude,
					points,
				} = this.mapInfo
				this.AMap = AMap;
				this.map = new AMap.Map('container', {
					resizeEnable: true,
					center: [longitude, latitude],
					zoom: 13 //地图显示的缩放级别
				});
				if (points.length) {
					this.polygon = this.createPolygon(points);
					this.polygonEditor = this.createEditor(this.polygon);
					this.sendMsg(this.beginPoints)
				} else {
					// 挂载点击事件
					this.clickListener = AMap.event.addListener(this.map, "click", this.mapOnClick.bind(this));
				}
				// this.$ownerInstance.callMethod('onMsg', {
				// 	AMap,
				// 	map: this.map
				// });
			},

			/**
			 * @param {Object}地图点击，生成坐标点
			 */
			mapOnClick(e) {
				this.beginMarks.push(this.addMarker(e.lnglat));
				this.beginPoints.push(e.lnglat);
				this.beginNum++;
				if (this.beginNum === 3) {
					this.AMap.event.removeListener(this.clickListener); // 移除地图点击事件
					this.polygon = this.createPolygon(this.beginPoints);
					this.polygonEditor = this.createEditor(this.polygon);
					console.log(this.beginPoints, '99988')
					this.sendMsg(this.beginPoints)
					// this.$ownerInstance.callMethod('onMsg', data);
					this.clearMarks();
				}
			},

			/**
			 * 创建触摸节点
			 * @param {Object} polygon
			 */
			createEditor(polygon) {
				const polygonEditor = new this.AMap.PolyEditor(this.map, polygon);
				// this.AMap.event.addListener(polygonEditor, 'addnode', this.polygonOpen);
				this.AMap.event.addListener(polygonEditor, 'end', this.polygonEnd);
				this.AMap.event.addListener(polygonEditor, 'adjust', this.polygonJust);
				polygonEditor.open();
				return polygonEditor;
			},

			polygonJust(res) {
				console.log(res.target.getPath(), 'res')
				this.sendMsg(res.target.getPath())
			},
			sendMsg(data) {
				const res = data.map(n => {
					const {
						lat,
						lng
					} = n
					return {
						longitude: lng,
						latitude: lat
					}
				})
				this.$ownerInstance.callMethod('onMsg', res);
			},
			/**
			 * 操作结束
			 * @param {Object} res
			 */
			polygonEnd(res) {


			},

			clearMarks() {
				this.map.remove(this.beginMarks);
			},

			/**
			 * 创建多边形实例
			 * @param {Object} res
			 */
			createPolygon(arr) {
				const polygon = new AMap.Polygon({
					map: this.map,
					path: arr,
					strokeColor: " rgba(59,122,255,1)",
					strokeOpacity: 1,
					strokeWeight: 2,
					fillColor: "rgba(100,163,242,0.50)",
					fillOpacity: 0.35,

				});
				return polygon;
			},

			// 实例化点标记
			addMarker(lnglat) {
				const marker = new AMap.Marker({
					icon: "http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",
					position: lnglat
				});
				marker.setMap(this.map);
				return marker;
			},
			
		}
	}
</script>
<style lang="scss">
	.container {
		height: calc(100vh - 400rpx);
		width: 100%;
	}
</style>
