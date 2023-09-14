<template>
	<view>
		<polygon-map ref="polygonMap" @sendMsg="savepPoints" :record="mapInfo" :clear="clearInfo" @clearChange="saveClearSwitch" @init="handleInit"></polygon-map>
		<!-- 搜索 -->
		<view class="search">
			<u-search shape="square" actionText="" color="#e1805f" searchIconColor="#e1805f" placeholderColor="#e1805f"
				bgColor="#ffffff" @change="searchChange" @focus="searchChange" v-model="search"></u-search>
		</view>
		<!-- 安全距离 -->
		<view class="slider">
			<view class="slider-left">
				<view class="slider-left-location" @tap="mobileLocation">手机</view>
				<!-- <image class="slider-left_img" src="@/static/images/guard_home.png" mode=""></image> -->
			</view>
			<!-- <view class="slider-center">
				<view class="slider-center_circle">
					<u-slider @change="onSlider" max="500" step="10" v-model="mapInfo.sliderValue" activeColor="#eeaa3d"
						blockColor="#eeaa3d" inactiveColor="#6c6c6c" />
				</view>
				<view class="slider-center_text">
					<view class="slider-center_text_name">安全距离</view>
					<view class="slider-center_text_num">{{mapInfo.sliderValue}}米</view>
				</view>
			</view> -->
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
		<!-- <touch-popup :minHeight="0.1" :maxHeight="0.25" :touchHeight="64" radius="30rpx">
			<view class="title">
				电子围栏设置
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
	import {
		GetFenceInfo,
		PostAddFence,
		GetLastPoint
	} from '@/common/http/api.js';
	let mapSearch;
	if (isApp()) mapSearch = weex.requireModule('mapSearch');
	import {
		log
	} from '../../../../common/utils/log';
	export default {
		data() {
			return {
				siteInfo: '',
				currentSelect: '',
				poiList: [], //搜索地址
				nearbyLocation: [],
				search: '',
				fenceType: 'polygon',
				currentCity: '',
				guardName: '',
				loading: true,
				poiShow: false,
				guardNameShow: false,
				locationShow: true,
				clearInfo: {},
				points: [],
				mapInfo: {
					longitude: '',
					latitude: '',
					points: [],
					type: ''
				}
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo,
				urlLocation: state => state.urlLocation
			}, ),
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
			searchChange(val) {
				if (!val) {
					this.poiList = []
					this.poiShow = true
					return
				}
				// this.poiList = []
				// 输入框为空的时候返回附近3000米内的关键字为"小区"的地点
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
			savepPoints(data) {
				this.points = data
			},
			handleInit() {
				
				// 判断是否是添加新的守护区域
				if (this.urlLocation && this.urlLocation.fenceType === 'polygon') {
					this.fenceType = this.urlLocation.fenceType
					this.guardName = this.urlLocation.name
					this.points = this.pointsFormatting(this.urlLocation)
					console.log('初始化有值的情况下进来了看看points' ,this.points)
					// const res = this.urlLocation.points.split(';').map(n => {
					// 	return n.split(',')
					// })
					uni.getLocation({
						geocode: true,
						type: isIos() ? 'wgs84' : 'gcj02',
						success: (res) => {
							this.currentCity = res.address.city
							this.mapInfo = {
								longitude: res.longitude,
								latitude: res.latitude,
								points: this.points
							}
						},
						false: (res) => {
							console.log(res, 'error')
							uni.hideLoading()
						}
					})
					
					return
				}
				this.handleGetLocation()
			},
			pointsFormatting(data) {
				return data && data.points.split(';').map(n => {
					return n.split(',')
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
							this.currentCity = res.address.city
							this.mapInfo = {
								longitude: res.longitude,
								latitude: res.latitude,
								points: []
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
			handleSubmit() {
				console.log(this.points,'111111111111111111111')
				if (!this.guardName) return uni.$u.toast('请输入名称')
				let submitData = this.pointsDataFormat(this.points)
				PostAddFence({
					fenceType: this.fenceType,
					deviceNo: this.deviceInfo.no,
					deviceFenceId: this.urlLocation.deviceFenceId,
					deviceId: this.deviceInfo.deviceId,
					name: this.guardName,
					points: submitData,
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						uni.navigateBack()
					}, 500);
				})
			},
			pointsDataFormat(data) {
				let res
				if(data[0][0]) {
					res = data.map(n => {
						let longitude = n[0]
						let latitude = n[1]
						return `${longitude},${latitude}`
					}).join(';')
				} else {
					res = data.map(n => {
						const {
							longitude,
							latitude
						} = n
						return `${longitude},${latitude}`
					}).join(';')
				}
				return res
			},
			handleSelect(item) {
				let infoData = {
					longitude: item.location.longitude,
					latitude: item.location.latitude,
					sliderValue: item.sliderValue
				}
				this.mapInfo = {
					longitude: item.location.longitude,
					latitude: item.location.latitude,
					points: []
				}
				this.clearInfo = {clear: true}
				this.poiShow = false
			},
			// 下一步
			nextStep() {
				if (!this.points.length) return uni.$u.toast('请先设置多边形点位')
				if (this.urlLocation && this.urlLocation.fenceType === 'circle') {
					uni.showModal({
						title: '提示',
						content: `您确定守护范围选择从圆形切换成多边形吗？`,
						success: async res => {
							if (res.confirm) {
								this.fenceType = 'polygon'
								this.guardNameShow = true
							}
						},
						fail: res => {
							this.fenceType = 'circle'
						}
					})
					return
				}
				this.guardNameShow = true
			},
			// 设备位置
			async getEquipmentLastPoint() {
				uni.showLoading({
					title: '加载中'
				})
				try {
					let res = await GetLastPoint({
						deviceId: this.deviceInfo.deviceId
					})
					const {
						longitude,
						latitude
					} = res.data.location
					this.mapInfo = {
						latitude,
						longitude,
						points: [],
						// type: 'add'
					}
					this.clearInfo = {clear: true}
					uni.hideLoading()
				} catch (e) {
					console.log(e, '抛出异常')
					uni.hideLoading()
					//TODO handle the exception
				}

			},
			saveClearSwitch(val) {
				this.clearInfo = val
			},
			mobileLocation() {
				// this.mapInfo.type = 'add'
				this.handleGetLocation()
				this.clearInfo = {clear: true}
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
		bottom: 190rpx;
		z-index: 99;

		&-left {
			margin-right: 10rpx;
		}

		&-left,
		&-right {
			&-location {
				padding: 13rpx 16rpx;
				display: flex;
				align-items: center;
				justify-content: space-between;
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
		height: auto;
		z-index: 200;

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
				margin-bottom: 60rpx;
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
</style>