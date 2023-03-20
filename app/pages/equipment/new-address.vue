<template>
	<view>
		<circle-map :record="mapInfo"></circle-map>
		<touch-popup :minHeight="0.1" :maxHeight="0.75" :touchHeight="64" radius="30rpx">
			<view class="title">
				获取位置详情
			</view>
			<view class="address">
				<text class="label">
					地址
				</text>
				<view class="input">
					<view class="input-text" @click="goSearch">{{siteInfo || '请输入地址'}}
						<u-icon name="arrow-right" color="#D4D4D4" size="32rpx"></u-icon>
					</view>
				</view>
			</view>
			<view class="radius">
				<text class="label">
					半径
				</text>
				<view class="slider">
					<u-slider @change="onSlider" max="500" v-model="mapInfo.sliderValue" activeColor="#eeaa3d"
						blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
				</view>
				<text class="value" style="text-align: center;">
					{{mapInfo.sliderValue}}米
				</text>
			</view>
			<view class="address-list">
				<view class="address-list-title">
					<text class="title-icon"></text>
					地址列表
				</view>
				<view class="list-item" v-for="(n, index) in poiList" :key="index" @click="mapMarker(n)">
					<text class="list-item-label">
						{{ n.name }}
					</text>
					<u-icon :name="getMapIcon(n)" size="44rpx" style="margin-right: 6rpx;" />
				</view>
			</view>
			<view class="ui-btn">
				<button class="default" @click="handleSubmit">提交</button>
			</view>
		</touch-popup>
	</view>
</template>

<script>
	import {
		mapState,
	} from 'vuex';
	import {
		isIos,
		isApp
	} from '@/common/utils/util';
	let mapSearch;
	if (isApp()) mapSearch = weex.requireModule('mapSearch');
	import {
		PostAddFence,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				siteInfo: '',
				currentSelect: '',
				poiList: [], //搜索地址
				mapInfo: {
					sliderValue: 200,
					latitude: '',
					longitude: '',
				}
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, ),
			getMapIcon() {
				return function(item) {
					return this.currentSelect === item.index ? '/static/images/map-select.png' :
						'/static/images/position.png'
				}
			},
		},
		created() {
			uni.$on('searchData', data => {
				console.log(data, 'dddddd----------')
				this.mapMarker(data)
			})
			
		},
		methods: {
			mapMarker(data) {
				const {
					province,
					city,
					district,
					address,
					index,
					location: {
						latitude,
						longitude
					}
				} = data
				this.currentSelect = index
				this.siteInfo = province + city + district + address
				this.mapInfo = {
					sliderValue: 200,
					latitude,
					longitude,
				}
			},
			/**
			 * 跳转搜索
			 */
			goSearch() {
				uni.navigateTo({
					url: '/pages/equipment/search'
				})
			},
			onSlider() {

			},
			/**
			 * 获取当前定位
			 */
			handleGetLocation() {
				uni.showLoading({
					title: '加载中'
				})
				try {
					uni.getLocation({
						geocode: true,
						type: isIos() ? 'wgs84' : 'gcj02',
						success: (res) => {
							this.getSiteInfo(res)
							const {
								latitude,
								longitude,
								address: {
									province,
									city,
									district,
									street
								},
							} = res
							this.latitude = latitude
							this.longitude = longitude
							this.siteInfo = province + city + district + street
							this.mapInfo = {
								sliderValue: 200,
								latitude,
								longitude,
							}
							uni.hideLoading()
						},
						false: (res) => {
							console.log(res, 'error')
							uni.hideLoading()
						}
					})
				} catch (e) {
					console.log(e, '代码报错--------');
					//TODO handle the exception
				}

			},
			/**
			 * 获取周围位置信息
			 */
			getSiteInfo(data) {
				const {
					latitude,
					longitude
				} = data
				mapSearch && mapSearch.poiSearchNearBy({
					point: {
						latitude,
						longitude
					},
					key: '小区'
				}, res => {
					const {
						poiList
					} = res;
					if (poiList && poiList.length) {
						this.poiList = (poiList.length > 4 ? poiList.slice(0, 4) : poiList).map((n, i) => {
							n.index = i
							return n
						})
					}
				})
			},
			/**
			 * 保存
			 */
			handleSubmit() {
				const {
					// deviceId,
					siteInfo: address,
					mapInfo: {
						longitude,
						latitude,
						sliderValue,
					}
				} = this
				const obj = {
					address,
					longitude,
					latitude,
					sliderValue
				}
				if (sliderValue === 0) {
					return uni.$u.toast('半径长度大于0')
				}
				uni.$emit('getMapData',obj)
				uni.navigateBack()
			},
		},
		onLoad(option){
			const obj = JSON.parse(option.obj)
			console.log(obj,'obj')
			const {
				siteInfo,
				longitude,
				latitude,
				sliderValue
			} = obj
			if( longitude && latitude && siteInfo){
				this.mapInfo = obj
				this.siteInfo = siteInfo
			}else{
				this.handleGetLocation()
			}
		}
	}
</script>

<style lang="scss" scoped>
	.title {
		height: 120rpx;
		display: flex;
		align-items: center;
		border-bottom: solid 2px #f7f7f7;
		font-size: 34rpx;
		color: #353535;
		font-weight: 550;
	}

	.address {
		height: 128rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: solid 2px #f7f7f7;

		.label {
			font-size: 34rpx;
			width: 150rpx;
			color: #353535;
		}

		.input {
			flex: 1;

			// width: 220rpx;
			.input-text {
				margin-right: 10rpx;
				font-size: 30rpx;
				color: #D4D4D4;
				display: flex;
				align-items: flex-end;
				justify-content: flex-end;
			}
		}
	}

	.radius {
		height: 128rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: solid 2px #f7f7f7;

		.label,
		.value {
			font-size: 34rpx;
			width: 120rpx;
			color: #353535;
		}

		.slider {
			flex: 1;
		}
	}

	.address-list {
		margin-top: 20rpx;
		background-color: #fff;

		.address-list-title {
			height: 100rpx;
			display: flex;
			flex-direction: row;
			align-items: center;
			font-size: 36rpx;
			color: #FEAE43;
			font-weight: 600;
			border-bottom: solid 2px #f7f7f7;

			.title-icon {
				height: 50rpx;
				border-left: solid 10rpx #FEAE43;
				margin-right: 20rpx;
			}
		}

		.list-item {
			// padding: 48rpx 32rpx;
			height: 128rpx;
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;
			border-bottom: solid 2px #f7f7f7;

			.list-item-label {
				font-size: 34rpx;
				color: #353535;
			}
		}
	}
	.ui-btn{
		margin-top: 20rpx;
	}
</style>
