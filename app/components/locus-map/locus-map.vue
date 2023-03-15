<!--
* @Author: white_queen
* @Date: 2023-03-09 10:26:48
* @FilePath: /components/app-map/app-map.vue
* @Description: 地图
-->

<template>
	<view>
		<view id="container" class="container"></view>
		<view :operation="operation" :change:operation="maps.loadOperation"></view>
	</view>
</template>

<script>
	export default {
		name: "app-map",
		props:{
			
		},
		data() {
			return {
				operation: {
					longitude : 119.39139,
					latitude : 26.03001
				},
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
					center:[this.operation.longitude,this.operation.latitude],
					zoom: 13 //地图显示的缩放级别
				});
				var marker, lineArr = [[119.39139,26.03001],[119.39138,26.03001],[119.39139,26.03001]];
				marker = new AMap.Marker({
					map: map,
					position: [this.operation.longitude,this.operation.latitude],
					icon: "https://webapi.amap.com/images/car.png",
					offset: new AMap.Pixel(-26, -13),
					autoRotation: true,
					angle:-90,
				});
				 // 绘制轨迹
				let polyline = new AMap.Polyline({
					map: map,
					path: lineArr,
					showDir:true,
					strokeColor: "#28F",  //线颜色
					// strokeOpacity: 1,     //线透明度
					strokeWeight: 6,      //线宽
					// strokeStyle: "solid"  //线样式
				});
				let passedPolyline = new AMap.Polyline({
					map: map,
					// path: lineArr,
					strokeColor: "#AF5",  //线颜色
					// strokeOpacity: 1,     //线透明度
					strokeWeight: 6,      //线宽
					// strokeStyle: "solid"  //线样式
				});
				
				
				marker.on('moving', function (e) {
					console.log(e,'e')
					passedPolyline.setPath(e.passedPath);
				});
				
				map.setFitView(); 
			
				
				
			},
			loadOperation(newValue, oldValue, ownerInstance, instance) {
				// 数据变更
				console.log(newValue,oldValue,ownerInstance,instance)
				
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
