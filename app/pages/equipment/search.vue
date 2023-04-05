<template>
	<view class="ui-pop-box">
		<!-- mapSearch模块需要页面有地图组件才能正常初始化 -->
		<map v-show="false"></map>
		<view class="ui-u-search">
			<view class="map-icon" @click="cityPicker">
				<u-icon name="map" size="20"></u-icon>
				<text>{{ currentCity }}</text>
			</view>
			<u-search @change="searchChange" placeholder="请输入地址信息" @custom="back" :showAction="true" actionText="取消"
				v-model="search"></u-search>
		</view>
		<view class="ui-site-gloup" v-if="poiList.length">
			<view class="ui-item active" v-for="(item, index) of poiList" :key="index" @tap="handleSelect(item)">
				<u-text prefixIcon="map" iconStyle="font-size: 19px" :text="item.name"></u-text>
				<text>{{getAddress(item)}}</text>
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
				poiList: []
			}
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
				uni.$emit('searchData', val);
				uni.navigateBack();
			},

			back() {
				uni.$off('searchData');
				uni.navigateBack();
			},
			/**
			 * 关键字搜索
			 */
			searchChange($e) {
				uni.$u.debounce(() => {
					mapSearch && mapSearch.poiKeywordsSearch({
						// city 指定搜索所在城市，支持传入格式有：城市名、citycode和adcode  
						key: this.currentCity + $e,
						cityLimit: false,
						sortrule: 0,
						offset: 10
					}, ({
						poiList
					}) => {
						this.poiList = poiList;
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
				uni.showLoading({
					title: '加载中'
				})
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
					}
					uni.hideLoading()
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
		display: flex;
		align-items: center;
		justify-content: space-between;

		.map-icon {
			display: flex;
			align-items: center;
			margin-right: 20rpx;
		}

	}

	.ui-site-gloup {
		margin-top: calc(40rpx + var(--status-bar-height));

		&.empty {
			padding-top: 200rpx;
		}

		.ui-item {
			padding: 30rpx 0;
			border-bottom: 1rpx solid #e2e2e2;
		}

	}
</style>