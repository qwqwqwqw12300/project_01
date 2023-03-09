<!--
* @Author: white_queen
* @Date: 2023-03-09 10:26:48
* @FilePath: /components/app-map/app-map.vue
* @Description: 地图
-->

<template>
	<view>
		<view id="container" class="container">
		</view>
	</view>
</template>

<script>
	export default {
		name: "app-map",
		data() {
			return {

			};
		}
	}
</script>

<script module="map" lang="renderjs">
	export default {
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
			const script = document.createElement('script')
			script.src =
				'http://webapi.amap.com/maps?v=1.3&key=3a2d950c2fc2774b4b1ee6da9a8d93dc&plugin=AMap.PolyEditor'
			script.onload = this.init.bind(this)
			document.head.appendChild(script)
		},

		methods: {
			init() {
				this.AMap = AMap;
				this.map = new AMap.Map('container', {
					resizeEnable: true,
					center: [119.406043, 26.0185753], //地图中心点
					zoom: 13 //地图显示的缩放级别
				});
				this.clickListener = AMap.event.addListener(this.map, "click", this.mapOnClick.bind(this));
				const str =
					'[{"J":39.91789947393269,"G":116.36744477221691,"lng":116.367445,"lat":39.917899},{"J":39.91184292800211,"G":116.40658356616223,"lng":116.406584,"lat":39.911843},{"J":39.88616249265181,"G":116.37963272998047,"lng":116.379633,"lat":39.886162}]',
					arr = this.json2arr(str);
				this.createPolygon(arr);
			},

			mapOnClick(e) {
				this.beginMarks.push(this.addMarker(e.lnglat));
				this.beginPoints.push(e.lnglat);
				this.beginNum++;
				if (this.beginNum == 3) {
					console.log('三个');
					this.AMap.event.removeListener(this.clickListener); // 移除地图点击事件
					const polygon = this.createPolygon(this.beginPoints);
					const polygonEditor = this.createEditor(polygon);
					this.clearMarks();
				}
			},

			createEditor(polygon) {
				const polygonEditor = new this.AMap.PolyEditor(this.map, polygon);
				polygonEditor.open();
				this.AMap.event.addListener(polygonEditor, 'end', this.polygonEnd);
				return polygonEditor;
			},

			closeEditPolygon() {
				this.polygonEditor.close();
			},

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

			json2arr(json) {
				const arr = JSON.parse(json),
					res = [];
				for (let i = 0; i < arr.length; i++) {
					const line = [];
					line.push(arr[i].lng);
					line.push(arr[i].lat);
					res.push(line);
				};
				return res;
			},

			createPolygon(arr) {
				const polygon = new AMap.Polygon({
					map: this.map,
					path: arr,
					strokeColor: "#007AFF",
					strokeOpacity: 1,
					strokeWeight: 3,
					fillColor: "#f5deb3",
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
		height: 600rpx;
		width: 100%;
	}
</style>
