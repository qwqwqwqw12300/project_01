<!--多边形-->
<template>
	<view>
		<template v-if="show">
			<view class="ui-loading">
				<u-loading-icon mode="semicircle" :vertical="true" text="加载中" textSize="18"></u-loading-icon>
			</view>
		</template>
		<view v-show="!show" id="container" class="container"></view>
		<view :mapData="mapData" :change:mapData="maps.loadData"></view>
		<view class="ui-float">
			<view class="float-item" id="claear_action">
				<u-icon name="/static/images/clear.png" size="32px"></u-icon>
				<!-- <text>更新位置</text> -->
			</view>
			<!-- 	<view class="float-line"></view> -->
		</view>
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
				show: false,
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
			},
			onLoadMsg(val) {
				this.show = val
			}
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
			document.getElementById('claear_action').addEventListener('click', e => {
				this.handleClear()
			})
		},

		methods: {
			loadData(val) {
				this.mapInfo = this.deepClone(val)

				const {
					latitude,
					longitude,
					points
				} = val
				if (latitude && longitude) {
					this.$ownerInstance.callMethod('onLoadMsg', true)
					this.loadMap(this.init)
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
				this.AMap = AMap
				setTimeout(() => {
					this.map = new AMap.Map('container', {
						resizeEnable: true,
						center: [longitude, latitude],
						zoom: 13 //地图显示的缩放级别
					});
					if (points.length) {
						this.polygon = this.createPolygon(points);
						this.polygonEditor = this.createEditor(this.polygon);
						this.map.setFitView()
						this.sendMsg(this.beginPoints)
					} else {
						// 挂载点击事件
						this.clickListener = this.map.on('click', this.mapOnClick);
						// AMap.event.addListener(this.map, "click", this.mapOnClick.bind(this));
					}
				}, 100)
				this.$ownerInstance.callMethod('onLoadMsg', false)
			},

			/**
			 * @param {Object}地图点击，生成坐标点
			 */
			mapOnClick(e) {
				this.beginMarks.push(this.addMarker(e.lnglat));
				this.beginPoints.push(e.lnglat);
				this.beginNum++;
				console.log(this.beginNum, this.beginPoints, '000000000--------------')
				if (this.beginNum === 3) {
					console.log(this.beginPoints, 'u---------------')
					this.map.off('click'); // 移除地图点击事件
					this.polygon = this.createPolygon(this.beginPoints);
					this.polygonEditor = this.createEditor(this.polygon);
					this.sendMsg(this.beginPoints)
					// this.$ownerInstance.callMethod('onMsg', data);
					this.clearMarks();
				}
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
			},

			/**
			 * 创建触摸节点
			 * @param {Object} polygon
			 */
			createEditor(polygon) {
				const polygonEditor = new this.AMap.PolygonEditor(this.map, polygon);
				const events = ['end', 'adjust', 'addnode', 'removenode', 'add'];
				events.forEach(ele => { // 所有变化事件都需要监听
					polygonEditor.on(ele, (e) => {
						this.polygonJust(e);
					});
				})
				polygonEditor.open();
				return polygonEditor;
			},

			polygonJust(res) {
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
				this.map.add([arr])
				const polygon = new AMap.Polygon({
					map: this.map,
					path: arr,
					strokeColor: " rgba(59,122,255,1)",
					strokeOpacity: 1,
					strokeWeight: 3,
					fillColor: "rgba(100,163,242,0.50)",
					fillOpacity: 0.35,
					zIndex: 50

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

			handleClear() {
				this.clearMarks()
				this.beginPoints = []
				this.beginNum = 0
				this.beginMarks = []
				if (this.beginNum && this.beginNum < 3) {
					return
				}
				this.polygonEditor && this.polygonEditor.close();
				this.polygon && this.map.remove(this.polygon)
				this.clickListener = this.map.on('click', this.mapOnClick);
				this.polygon = this.createPolygon(this.beginPoints);
				this.polygonEditor = this.createEditor(this.polygon);
				this.sendMsg([])
				// this.map.off('click', this.clickListener); // 移除地图点击事件
			}

		}
	}
</script>
<style lang="scss">
	.ui-loading {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 800rpx;
		width: 100%;
	}

	.container {
		height: calc(100vh - 400rpx);
		width: 100%;
	}

	.ui-float {
		position: fixed;
		right: 20rpx;
		bottom: 400rpx;
		padding: 20rpx 20rpx;
		background-color: #FFFFFF;
		box-sizing: border-box;
		border-radius: 15rpx;
		z-index: 999;

		.float-item {
			display: flex;
			flex-direction: column;
			align-items: center;

			text {
				margin-top: 4rpx;
				font-size: 26rpx;
			}
		}

		.float-line {
			width: 100%;
			border-bottom: 2rpx solid #353535;
			margin: 20rpx 0;
		}

	}
</style>