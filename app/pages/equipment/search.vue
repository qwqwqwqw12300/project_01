<template>
	<view class="ui-pop-box">
		<view class="ui-u-search">
			<u-search @change="searchChange" placeholder="请输入地址信息" :showAction="false" v-model="search"></u-search>
		</view>
		<view class="ui-site-gloup">
			<view class="ui-item active" v-for="(item, index) of poiList" :key="index">
				<u-text prefixIcon="map" iconStyle="font-size: 19px" :text="getAddress(item)"></u-text>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		isApp
	} from '../../common/utils/util';
	let mapSearch;
	if (isApp()) mapSearch = weex.requireModule('mapSearch');

	export default {
		data() {
			return {
				search: '',
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
			searchChange($e) {
				uni.$u.debounce(() => {
					mapSearch && mapSearch.poiKeywordsSearch({
						// city 指定搜索所在城市，支持传入格式有：城市名、citycode和adcode  
						key: $e,
						cityLimit: false,
						sortrule: 0,
						offset: 10
					}, ({
						poiList
					}) => {
						console.log(poiList);
						if (poiList && poiList.length) {
							console.log(poiList);
							this.poiList = poiList;
						}
					})
				}, 500)

			}
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
		top: calc(var(--status-bar-height) + 20rpx);
		width: calc(100% - 60rpx);

	}

	.ui-site-gloup {
		margin-top: calc(40rpx + var(--status-bar-height));

		.ui-item {
			padding: 30rpx 0;
			border-bottom: 1rpx solid #e2e2e2;
		}

	}
</style>
