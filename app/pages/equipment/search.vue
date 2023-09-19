<template>
	<view class="ui-pop-box">
		<!-- mapSearch模块需要页面有地图组件才能正常初始化 -->
		<view class="ui-u-search">
			<view class="searchbox">
				<view class="map-icon" @click="cityPicker">
					<u-icon name="map" size="20"></u-icon>
					<text>{{ currentCity }}</text>
				</view>
				<u-search @change="searchChange" placeholder="请输入地址信息" @custom="back" :showAction="true" actionText="取消"
					v-model="search"></u-search>
			</view>
			<view class="mapbox">
				<map id="popMap" @tap='mapClick' :longitude="longitude" :latitude="latitude" scale="16"
					:markers="covers"></map>
			</view>
		</view>

		<view class="ui-site-gloup ui-loading" v-if="loading">
			<u-loading-icon text="查询中..." :vertical="true" mode="semicircle"></u-loading-icon>
		</view>
		<view class="ui-site-gloup" v-else-if="poiList.length">
			<view class="ui-item active" v-for="(item, index) of poiList" :key="index" @tap="handleSelect(item)">
				<view class="" style="display: flex;align-items: center;justify-content: left;">
					<u-icon name="map" size="15"></u-icon>
					<text>{{item.name}}</text>
				</view>
				<view class="">
					<text style="font-size: 26rpx;">{{getAddress(item)}}</text>
				</view>
			</view>
		</view>
		<view class="ui-site-gloup empty" v-else>
			<u-empty mode="search" />
		</view>
	</view>
</template>

<script>
	import {
		isApp,
		isIos
	} from '../../common/utils/util';
	let mapSearch;
	if (isApp()) mapSearch = weex.requireModule('mapSearch');

	export default {
		data() {
			return {
				search: '',
				currentCity: '',
				poiList: [],
				loading: true,
				longitude: 0,
				latitude: 0,
				covers: [{
					latitude: 0,
					longitude: 0,
					iconPath: '../../static/images/mapSite2x.png',
					fontSize: 80, //一般要定义大小要不出不来图片
				}]
			}
		},
		mounted() {
			this.mapClick()
		},
		computed: {
			getAddress: function() {
				return function(item) {
					const {
						province,
						city,
						district,
						address
					} = item;
					return province + city + district + address;
				}
			}
		},
		methods: {
			/**
			 * 选择搜索结果
			 */
			handleSelect(val) {
				this.longitude = val.location.longitude
				this.latitude = val.location.latitude
				this.covers[0].latitude = val.location.latitude
				this.covers[0].longitude = val.location.longitude
				console.log('searchData', val)
				uni.$emit('searchData', val);
				setTimeout(() => {
					uni.navigateBack();
				}, 1500)
			},
			// 拖动地图
			mapClick() {
				let that = this;
				var maps = uni.createMapContext("popMap", this).$getAppMap();
				maps.onclick = function(point) {
					that.longitude = point.longitude
					that.latitude = point.latitude
					that.covers = [];
					that.covers = [{
						latitude: point.latitude,
						longitude: point.longitude,
						iconPath: '../../static/images/mapSite2x.png',
						fontSize: 80, //一般要定义大小要不出不来图片
					}]
					mapSearch && mapSearch.poiSearchNearBy({
						point: point,
						key: '小区'
					}, res => {
						const {
							poiList
						} = res;
						if (poiList && poiList.length) {
							that.poiList = poiList;
						}
					})
					// let pages = getCurrentPages()
					// let prevPage = pages[pages.length - 2]
					// let currentLocat = that.longitude + ',' + that.latitude
					// prevPage.$vm.getMapValue(currentLocat)
					// setTimeout(res => {
					// 	uni.navigateBack({
					// 		delta: 1 // 返回的页面数
					// 	})
					// }, 1000)
				}
				// this.covers = [{
				// 	latitude: e.target.latitude,
				// 	longitude: e.target.longitude,
				// 	iconPath: '../../static/images/mapSite2x.png'
				// }]
			},
			back() {
				uni.$off('searchData');
				uni.navigateBack();
			},
			/**
			 * 关键字搜索
			 */
			searchChange($e) {
				// this.poiList = []
				uni.$u.debounce(() => {
					this.loading = true;
					mapSearch && mapSearch.poiKeywordsSearch({
						// city 指定搜索所在城市，支持传入格式有：城市名、citycode和adcode  
						key: this.currentCity + $e,
						cityLimit: false,
						sortrule: 0,
						offset: 10
					}, ({
						poiList
					}) => {
						this.loading = false;
						this.poiList = poiList;
						this.longitude = poiList[0].location.longitude
						this.latitude = poiList[0].location.latitude
						this.covers[0].latitude = poiList[0].location.latitude
						this.covers[0].longitude = poiList[0].location.longitude
						// if (poiList && poiList.length) {
						// 	this.poiList = poiList;
						// }
					})
				}, 500)
			},

			/**
			 * 获取定位
			 */
			getLocation() {
				return new Promise(resolve => {
					uni.getLocation({
						geocode: true,
						type: isIos() ? 'wgs84' : 'gcj02',
						success: resolve
					});
				});

			},

			/**
			 * 选择城市
			 */
			cityPicker() {
				uni.$on('citySelect', data => {
					this.currentCity = data.cityName;
					this.searchChange(this.search)
				})
				this.$setCache('position', this.currentCity);
				uni.navigateTo({
					url: '/pages/equipment/citySelect/citySelect'
				});
			}
		},
		created() {
			this.getLocation().then(location => {
				// uni.showLoading({
				// 	title: '加载中'
				// })
				console.log('locationlocation', location)
				this.longitude = location.longitude
				this.latitude = location.latitude
				this.covers[0].latitude = location.latitude
				this.covers[0].longitude = location.longitude
				this.currentCity = location.address.city
				mapSearch && mapSearch.poiSearchNearBy({
					point: location,
					key: '小区'
				}, res => {
					const {
						poiList
					} = res;
					if (poiList && poiList.length) {
						this.poiList = poiList;
						console.log(this.poiList)
					}
					this.loading = false;
					// uni.hideLoading()
				})
			});
		},

		onBackPress(event) {
			uni.$off('detailsScreenResult');
		}
	}
</script>

<style lang="scss">
	.ui-pop-box {
		box-sizing: border-box;
		width: 100%;
		height: 100%;
		padding: 53rpx 30rpx;
	}

	.ui-u-search {
		position: fixed;
		top: 0;
		z-index: 9999;
		padding-top: calc(var(--status-bar-height) + 20rpx);
		width: calc(100% - 60rpx);
		background-color: #fff;

		.searchbox {
			display: flex;
			align-items: center;
			justify-content: space-between;
		}

		.map-icon {
			display: flex;
			align-items: center;
			margin-right: 20rpx;
			font-size: 28rpx;
		}

	}

	.ui-site-gloup {
		margin-top: calc(40rpx + 400rpx);

		&.empty {
			padding-top: 200rpx;
		}

		.ui-item {
			padding: 30rpx 0;
			border-bottom: 1rpx solid #e2e2e2;
		}

	}

	.ui-loading {
		margin-top: 200rpx;
	}

	.mapbox {
		margin-top: 30rpx;
	}

	uni-map {
		width: 100%;
		height: 150px;
	}
</style>