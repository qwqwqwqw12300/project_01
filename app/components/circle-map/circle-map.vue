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
				let circle = new AMap.Circle({
				    center: new AMap.LngLat(this.operation.longitude,this.operation.latitude),  // 圆心位置
				    radius: 1000, // 圆半径
				    fillColor: 'pink',   // 圆形填充颜色
					fillOpacity: 0.4,// 圆形填充透明度
				    strokeColor: '#fff', // 描边颜色
				    strokeWeight: 2, // 描边宽度
				});
				this.map.add(circle)
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
