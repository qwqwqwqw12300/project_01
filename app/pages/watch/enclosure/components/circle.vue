<template>
	<view>
		<circle-map @change="mapChange" :record="mapInfo" ref="mapRef"></circle-map>
		<!-- 搜索 -->
		<view class="search">
			<u-search shape="square" actionText="" color="#e1805f" searchIconColor="#e1805f" placeholderColor="#e1805f"
				bgColor="#ffffff" @change="searchChange" @focus="searchChange" v-model="search"></u-search>
		</view>
		<!-- 安全距离 -->
		<view class="slider">
			<view class="slider-left">
				<view class="slider-left-location" @tap="handleGetLocation">手机</view>
				<!-- <image class="slider-left_img" src="@/static/images/guard_home.png" mode=""></image> -->
			</view>
			<view class="slider-center">
				<view class="slider-center_circle">
					<u-slider @change="onSlider" max="500" min="50" step="10" v-model="mapInfo.sliderValue"
						activeColor="#eeaa3d" blockColor="#eeaa3d" inactiveColor="#6c6c6c" />
				</view>
				<view class="slider-center_text">
					<!-- <view class="slider-center_text_name">安全距离</view> -->
					<view class="slider-center_text_num">{{mapInfo.sliderValue}}米</view>
				</view>
			</view>
			<view class="slider-right">
				<view class="slider-right-location" @tap="getEquipmentLastPoint">设备</view>
				<!-- <image class="slider-right_img" src="@/static/images/guard_home.png" mode=""></image> -->
			</view>
		</view>
		<view class="result" v-if="poiList.length && poiShow">
			<!-- <view class="result-list" v-for="(item, index) in poiList">{{ item.name }}</view> -->
			<view class="result-list" v-for="(item, index) of poiList" :key="index" @tap="handleSelect(item)">
				<u-text iconStyle="font-size: 19px" :text="item.name"></u-text>
				<text>{{ addressFormmatting(item) }}</text>
			</view>
		</view>
		<view class="location" v-show="locationShow">
			<!-- 列表 -->
			<view class="location-list" style="position: relative;">
				<view class="location-list-oneself">
					<u-text class="location-list-oneself-name" :lines="2" :text="siteInfo" color="#eeaa3d"></u-text>
					<u-icon name="map" size="24" color="#eeaa3d"></u-icon>
					<!-- <image class="location-list-oneself-icon" src="@/static/images/add-home.png" mode=""></image> -->
				</view>
				<view class="location-list-item" v-for="(item, index) in nearbyLocation" :key="index"
					@tap="handleSelect(item, 'locationList')">
					<u-text class="location-list-item-name" :lines="2" :text="item.name"></u-text>
					<view class="location-list-item-distance">{{ item.distance }}米</view>
				</view>
			</view>
			<!-- 下一步 -->
			<view class="location-bottom">
				<button class="location-bottom-btn" @tap="nextStep">下一步</button>
			</view>
		</view>
		<u-popup :show="guardNameShow" @close="guardNameShow = false" mode="center" :zIndex="999999">
			<view class="popup">
				<view class="popup-title">请输入名称</view>
				<u--input placeholder="请输入名称" border="surround" clearable v-model="guardName" :cursorSpacing="700"
					:adjustPosition="true" maxlength="8"></u--input>
				<view class="popup-btn">
					<button class="popup-btn-cancel" @tap="guardNameShow = false">取消</button>
					<button class="popup-btn-confirm" @tap="handleSubmit">确定</button>
				</view>
			</view>
		</u-popup>
		<!-- <touch-popup :minHeight="0.1" :maxHeight="0.75" :touchHeight="64" radius="30rpx">
			<view class="title">
				电子围栏设置
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
		</touch-popup> -->
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
		GetFenceInfo,
		GetLastPoint,
		GetWatchTrack
	} from '@/common/http/api.js';
	import loadingIcon from '../../../../uni_modules/uview-ui/libs/config/props/loadingIcon';
	export default {
		data() {
			return {
				siteInfo: '',
				currentSelect: '',
				poiList: [], //搜索地址
				nearbyLocation: [],
				search: '',
				currentCity: '',
				guardName: '',
				loading: true,
				poiShow: false,
				guardNameShow: false,
				locationShow: true,
				fenceType: 'circle',
				mapInfo: {
					sliderValue: 200,
					latitude: '',
					longitude: '',
				}
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo,
				urlLocation: state => state.urlLocation
			}, ),
			getMapIcon() {
				return function(item) {
					return this.currentSelect === item.index ? '/static/images/map-select.png' :
						'/static/images/position.png'
				}
			},
			addressFormmatting() {
				return function(item) {
					let addressStr = item.address.replace(/\\/g, "").replace(/./g, "");
					// let addressStr = item.address.replace(/./g, "");
					return item.city + item.district + addressStr
				}
			}
		},
		mounted() {
			this.handleInit()
		},
		methods: {
			// 获取定位
			getLocationSearch() {
				return new Promise(resolve => {
					uni.getLocation({
						geocode: true,
						type: isIos() ? 'wgs84' : 'gcj02',
						success: resolve
					});
				});
			},
			searchChange(val) {
				if (!val) {
					this.poiList = []
					this.poiShow = false
					return
				}
				uni.$u.debounce(() => {
					this.loading = true;
					mapSearch && mapSearch.poiKeywordsSearch({
						// city 指定搜索所在城市，支持传入格式有：城市名、citycode和adcode  
						key: this.currentCity + val,
						cityLimit: false,
						sortrule: 0,
						offset: 10
					}, ({
						poiList
					}) => {
						this.loading = false;
						this.poiShow = true
						this.poiList = poiList;
						// if (poiList && poiList.length) {
						// 	this.poiList = poiList;
						// }
					})
				}, 500)
			},
			async handleInit() {
				uni.getLocation({
					geocode: true,
					type: isIos() ? 'wgs84' : 'gcj02',
					success: (res) => {
						this.currentCity = res.address.city
					},
					false: (res) => {
						console.log(res, 'error')
						uni.hideLoading()
					}
				})
				// 判断是否是添加新的守护区域
				if (this.urlLocation && this.urlLocation.fenceType === 'circle') {
					this.fenceType = this.urlLocation.fenceType
					this.guardName = this.urlLocation.name
					const {
						longitude,
						latitude
					} = this.urlLocation.location

					this.getSiteInfo({
						longitude,
						latitude
					})
					this.mapInfo = {
						sliderValue: this.urlLocation.radius,
						latitude,
						longitude
					}
					this.siteInfo = await this.getLocation({
						longitude,
						latitude
					})
					return
				}
				this.handleGetLocation()
			},
			// 设置地图定位
			mapMarker(data, name) {
				name = name ? name : ''
				const {
					province,
					city,
					district,
					address,
					// index,
					location: {
						latitude,
						longitude
					}
				} = data
				// this.currentSelect = index
				// this.mapInfo = {
				// 	sliderValue: 200,
				// 	latitude,
				// 	longitude,
				// }
				this.mapInfo.longitude = longitude
				this.mapInfo.latitude = latitude
				this.siteInfo = !name ? province + city + district + address : name
			},
			onSlider() {

			},
			// 地图位置（经纬度）发生变化时触发
			async mapChange(obj) {
				// 位置发生变化后重新搜索附近的小区
				const {
					sliderValue,
					latitude,
					longitude
				} = obj
				this.getSiteInfo(obj)
				const res = await this.getLocation(obj)
				this.siteInfo = res
				this.mapInfo = {
					sliderValue,
					latitude,
					longitude
				}
			},
			getLocation(obj) {
				const {
					latitude,
					longitude
				} = obj
				return new Promise((resolve, reject) => {
					mapSearch && mapSearch.reverseGeocode({
							point: {
								latitude,
								longitude
							},
						},
						e => {
							console.log(e, 'reverseGeocodereverseGeocodereverseGeocode')
							if (e.type === 'fail') return reject()
							resolve(e.address)
						})
				})
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
					uni.hideLoading()
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
				}, async res => {
					const {
						poiList
					} = res;
					if (poiList && poiList.length) {
						await poiList.map((item, index) => {
							if (item.location.longitude === Number(longitude)) {
								poiList.splice(index, 1)
							}
						})
						this.nearbyLocation = poiList
						// this.poiList = (poiList.length > 4 ? poiList.slice(0, 4) : poiList).map((n, i) => {
						// 	n.index = i
						// 	return n
						// })
					}
				})
			},
			/**
			 * 保存
			 */
			handleSubmit() {
				if (!this.guardName) return uni.$u.toast('请输入名称')
				const {
					longitude,
					latitude,
					sliderValue
				} = this.$refs.mapRef.mapInfo
				const {
					siteInfo: address,
				} = this
				if (sliderValue === 0) {
					return uni.$u.toast('半径长度大于0')
				}
				var newobj = {
					fenceType: this.fenceType,
					deviceNo: this.deviceInfo.no,
					deviceId: this.deviceInfo.deviceId,
					address,
					longitude,
					latitude,
					radius: sliderValue + '',
					name: this.guardName,
					deviceFenceId: this.urlLocation.deviceFenceId ? this.urlLocation.deviceFenceId : ''
				}
				console.log('newobjnewobjnewobj',newobj)
				PostAddFence({
					fenceType: this.fenceType,
					deviceNo: this.deviceInfo.no,
					deviceId: this.deviceInfo.deviceId,
					address,
					longitude,
					latitude,
					radius: sliderValue + '',
					name: this.guardName,
					deviceFenceId: this.urlLocation.deviceFenceId ? this.urlLocation.deviceFenceId : ''
				}).then(res => {
					console.log('PostAddFence',res)
					uni.navigateBack()
					uni.$u.toast(res.msg)
				})
			},
			handleSelect(item, type) {
				let name = !type ? '' : item.name
				this.mapMarker(item, name)
				let infoData = {
					longitude: item.location.longitude,
					latitude: item.location.latitude,
					sliderValue: item.sliderValue
				}
				this.getSiteInfo(infoData)
				this.poiShow = false
			},
			// 下一步
			nextStep() {
				if (this.urlLocation && this.urlLocation.fenceType === 'polygon') {
					uni.showModal({
						title: '提示',
						content: `您确定守护范围选择从多边形切换成圆形吗？`,
						success: async res => {
							if (res.confirm) {
								this.fenceType = 'circle'
								this.guardNameShow = true
							}
						},
						fail: res => {
							this.fenceType = 'polygon'
						}
					})
					return
				}
				this.guardNameShow = true
			},
			async getEquipmentLastPoint() {
				uni.showLoading({
					title: '加载中'
				})
				try {
					let res = await GetWatchTrack({
						deviceId: this.deviceInfo.deviceId
					})
					const {
						longitude,
						latitude
					} = res.data.location

					this.getSiteInfo({
						longitude,
						latitude
					})
					this.mapInfo = {
						sliderValue: 200,
						latitude,
						longitude
					}
					this.siteInfo = await this.getLocation({
						longitude,
						latitude
					})
					uni.hideLoading()
				} catch (e) {
					console.log(e, '抛出异常')
					uni.hideLoading()
					//TODO handle the exception
				}

			}
		}
	}
</script>

<style lang="scss" scoped>
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
				color: #333333;
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

	.ui-btn {
		margin-top: 20rpx;
	}

	// 搜索
	.search {
		position: absolute;
		width: calc(100vw - 0rpx);
		left: 40rpx;
		top: 220rpx;
		z-index: 99;
	}

	// 安全距离
	.slider {
		position: absolute;
		width: calc(100vw - 80rpx);
		display: flex;
		align-items: center;
		left: 40rpx;
		bottom: 570rpx;
		z-index: 99;

		&-left,
		&-right {
			&-location {
				padding: 13rpx 16rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				white-space: nowrap;
				background: rgba(255, 255, 255, 0.70);
				box-shadow: 0px 2px 10px 0px rgba(0, 0, 0, 0.1);
				border-radius: 28px;
				font-size: 24rpx;

				&:before {
					display: block;
					content: "";
					min-width: 18rpx;
					min-height: 18rpx;
					background-color: #2FCE7D;
					border-radius: 50%;
					margin-right: 7rpx;
				}
			}
		}

		&-center {
			width: 100%;
			padding: 4rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			border: 1px solid #b7b6ba;
			border-radius: 60rpx;
			margin: 0 16rpx;
			background-color: rgba(255, 255, 255, .5);

			&_circle {
				width: 100%;
			}

			&_text {
				width: 140rpx;
				// display: flex;
				align-items: center;
				font-size: 24rpx;
				text-align: center;
				color: #eeaa3d;

				&_name {}

				&_num {}
			}
		}

		&-right {
			&-location {
				&:before {
					background-color: #FEAE43;
				}
			}
		}
	}

	// 搜索记录
	.result {
		width: 100%;
		height: calc(100% - 296rpx);
		position: absolute;
		top: 296rpx;
		left: 0;
		background-color: #ffffff;
		z-index: 99999;
		overflow: scroll;

		&-list {
			padding: 10rpx;
		}
	}

	// 附近位置
	.location {
		position: absolute;
		left: 0;
		bottom: 0;
		background-color: #ffffff;
		width: 100%;
		height: 550rpx;
		z-index: 90;

		&-list {
			height: 400rpx;
			overflow: scroll;
			font-size: 24rpx;

			&-oneself {
				width: 100%;
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding: 20rpx 32rpx;
				box-sizing: border-box;

				&-name {
					color: #eeaa3d;
				}

				&-icon {
					width: 34rpx;
					height: 34rpx;
				}
			}

			&-item {
				width: 100%;
				padding: 20rpx 32rpx;
				display: flex;
				justify-content: space-between;
				align-items: center;
				border-top: 1px solid #f2f2f2;
				box-sizing: border-box;

				&-name {}

				&-distance {}
			}
		}

		&-bottom {
			border-top: 1px solid #f2f2f2;

			&-btn {
				margin-top: 26rpx;
				width: 686rpx;
				background-color: #ffffff;
				border: 1px solid #f2f2f2;
			}
		}
	}

	// 下一步后的输入名称框
	/deep/ .u-popup__content {
		width: 100%;
	}

	.popup {
		padding: 32rpx;
		box-sizing: border-box;
		width: 100%;
		background: #ffffff;

		&-title {
			text-align: center;
			padding-bottom: 50rpx;
		}

		&-btn {
			padding-top: 30rpx;
			display: flex;
			font-size: 24rpx;

			&-cancel,
			&-confirm {
				width: 50%;
				display: flex;
				align-items: center;
				justify-content: center;
				border: 1px solid #f1f1f1;
				background-color: #ffffff;
				border-radius: 10rpx;
				font-size: 28rpx;
				padding: 8rpx;
				box-sizing: border-box;
			}

			&-cancel {
				border-right: none;
			}

			&-confirm {
				color: #eeaa3d;
			}
		}
	}

	.title {
		height: 120rpx;
		display: flex;
		align-items: center;
		border-bottom: solid 2px #f7f7f7;
		font-size: 34rpx;
		color: #353535;
		font-weight: 550;
	}

	/deep/ uni-slider {
		// margin: 20rpx 16rpx;
	}
</style>