<!--
* @Author: white_queen
* @Date: 2023-03-09 10:26:48
* @FilePath: /components/app-map/app-map.vue
* @Description: 地图
-->

<template>
	<view>
		<view id="container" class="container"></view>
	</view>
</template>

<script>
	export default {
		name: "app-map",
		data() {
			return {

			};
		},
		methods: {
			/**
			 * 接收地图信息
			 */
			onMsg(info) {
				console.log(info, 'Polygon');
			}
		}
	}
</script>

<script module="map" lang="renderjs">
	import {
		mapMixin
	} from '../../common/mixin/map.mixin';
	export default {
		mixins: [mapMixin],
		data() {
			return {
				beginPoints: [],
				beginMarks: [],
				beginNum: 0,
				polygonEditor: '',
				AMap: null,
				map: null,
				clickListener: null
			}
		},
		mounted() {
			this.loadMap(this.init);
		},

		methods: {
			/**
			 * 初始化
			 */
			init(AMap) {
				this.AMap = AMap;
				this.map = new AMap.Map('container', {
					resizeEnable: true,
					zoom: 13 //地图显示的缩放级别
				});
				// 挂载点击事件
				const onclick = ({
					lnglat
				}) => {
					console.log(lnglat, 'lnglat');
					this.beginMarks.push(this.addMarker(lnglat));
					this.beginPoints.push([lnglat.getLng(), lnglat.getLat()]);
					this.beginNum++;
					if (this.beginNum == 3) {
						console.log('三个');
						this.map.off('click', onclick);
						const polygon = this.createPolygon(this.beginPoints); // 创建不规则图形
						const polygonEditor = this.createEditor(polygon);
						polygonEditor.on('end', ($e) => {
							console.log($e, 'end');
						})
						this.clearMarks();
					}
				}
				this.map.on('click', onclick);
			},


			/**
			 * 创建触摸节点
			 * @param {Object} polygon
			 */
			createEditor(polygon) {
				const polygonEditor = new this.AMap.PolygonEditor(this.map, polygon);
				polygonEditor.open();

				return polygonEditor;
			},

			/**
			 * 关闭触摸节点
			 */
			closeEditPolygon() {
				this.polygonEditor.close();
			},

			/**
			 * 操作结束
			 * @param {Object} res
			 */
			polygonEnd(res) {
				this.resPolygon.push(res.target);
				// alert(resPolygon[resNum].contains([116.386328, 39.913818]));
				this.appendHideHtml(this.resNum, res.target.getPath());
				this.resNum++;
				this.init();
			},

			appendHideHtml(index, arr) {
				const strify = JSON.stringify(arr),
					html = '<input type="hidden" id="index' + index + '" name="paths[]" value="' + strify + '">';
				document.body.append(html);
				console.log(html);
			},

			clearMarks() {
				this.map.remove(this.beginMarks);
			},

			createPolygon(arr) {
				this.map.add([arr])
				const polygon = new AMap.Polygon({
					map: this.map,
					path: arr,
					strokeColor: "#007AFF",
					strokeOpacity: 1,
					strokeWeight: 3,
					fillColor: "#f5deb3",
					zIndex: 50,
					fillOpacity: 0.35

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
