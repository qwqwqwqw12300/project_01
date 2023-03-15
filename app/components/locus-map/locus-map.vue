<!--
* @Author: white_queen
* @Date: 2023-03-09 10:26:48
* @FilePath: /components/app-map/app-map.vue
* @Description: 地图
-->

<template>
	<view>
		<view id="container" class="container"></view>
		<!-- <view :operation="operation" :change:operation="maps.loadOperation"></view> -->
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
				map:null,
				AMap:null
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
					center: [119.39139, 26.03001],
					zoom: 13 //地图显示的缩放级别
				});
				var path = [
					 [119.39139,  26.04001],
					 [119.38139,  26.03001],
					 [119.37139,  26.02001],
					 [119.36139,  26.01001]
				 ];
				 
				 var polyline = new AMap.Polyline({
					 path: path,
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
