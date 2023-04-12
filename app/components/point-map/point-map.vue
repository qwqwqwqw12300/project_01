<template>
	<view>
		<view :mapInfo="mapInfo" :change:mapInfo="maps.loadData"></view>
		<template v-if="show">
			<view class="ui-loading">
				<u-loading-icon mode="semicircle" :vertical="true" text="加载中" textSize="18"></u-loading-icon>
			</view>
		</template>
		<view v-show="!show" id="container" class="container"></view>
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
				async handler(val) {
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
						console.log(677777777)
						const {
							latitude,
							longitude
						} = await this.$store.dispatch('setLocation')
						console.log(latitude, longitude, 'lllllpppppppppp--------')
						this.mapInfo = {
							type: 'cur',
							latitude,
							longitude
						}
					}
				},
				// immediate: true,
				deep: true,

			}
		},
		mounted() {},
		data() {
			return {
				show: false,
				mapInfo: {
					type: 'add', //cur: 当前定位,add: '地图定位'
					latitude: '',
					longitude: '',
				}
			}
		},
		methods: {
			onMsg(val) {
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
				AMap: null,
				map: null,
				mapData: {},
				marker: null,
			}
		},
		mounted() {},
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
					this.marker && this.map.remove(this.marker)
					if (type === 'add') {
						this.mapMarker()
					} else {
						this.map.setCenter([longitude, latitude]); //设置地图中心点
					}
				} else {
					this.$ownerInstance.callMethod('onMsg', true);
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
				})
				type === 'add' && this.mapMarker()
				this.$ownerInstance.callMethod('onMsg', false);
			},
			mapMarker() {
				const {
					latitude,
					longitude
				} = this.mapData
				const icon = new this.AMap.Icon({
					size: new AMap.Size(40, 40), // 图标尺寸
					image: 'static/images/mapSite.png', // Icon的图像
					// imageOffset: new AMap.Pixel(-10, -10), // 图像相对展示区域的偏移量，适于雪碧图等
					imageSize: new AMap.Size(40, 40) // 根据所设置的大小拉伸或压缩图片
				})
				this.marker = new AMap.Marker({
					position: new AMap.LngLat(longitude, latitude),
					offset: new AMap.Pixel(-20, -20),
					icon,
				})
				this.map.add(this.marker)
				this.map.setCenter([longitude, latitude]) //设置地图中心点
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
		height: calc(100vh - 540rpx - var(--status-bar-height));
		width: 100%;
	}

	.ui-loading {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 700rpx;
		width: 100%;
	}
</style>