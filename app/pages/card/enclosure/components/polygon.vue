<template>
	<view>
		<polygon-map @sendMsg="savepPoints" :record="mapInfo"></polygon-map>
		<!-- 搜索 -->
		<view class="search">
			<u-search shape="square" actionText="" color="#e1805f" searchIconColor="#e1805f" placeholderColor="#e1805f"
				bgColor="#ffffff" @change="searchChange" @focus="searchChange" v-model="search"></u-search>
		</view>
		<!-- 安全距离 -->
		<view class="slider">
			<view class="slider-left">
				<view class="slider-left-location" @tap="handleGetLocation">位置</view>
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
		<u-popup :show="guardNameShow" @close="guardClose" mode="center" :zIndex="999999">
			<view class="popup">
				<view class="popup-title">请输入名称</view>
				<u--input placeholder="请输入名称" border="surround" clearable v-model="guardName" :cursorSpacing="700"
					:adjustPosition="true"></u--input>
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
	} from '@/common/utils/util';
	import {
		GetFenceInfo,
		PostAddFence,
		GetLastPoint
	} from '@/common/http/api.js';
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
				points: [],
				mapInfo: {
					longitude: '',
					latitude: '',
					points: []
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
				console.log(val, ';;;;;;')
				if (!val) {
					this.poiList = []
					this.poiShow = false
					return
				}
				// this.poiList = []
				// 输入框为空的时候返回附近3000米内的关键字为"小区"的地点
				// if(!val) {
				// 	this.getLocationSearch().then(location => {
				// 		this.currentCity = location.address.city
				// 		console.log(location, 'location')
				// 		let locations = {
				// 			latitude: location.latitude,
				// 			longitude: location.longitude
				// 		}
				// 			console.log(locations, '我是locations我是locations我是locations我是locations我是locations我是locations')
				// 		mapSearch && mapSearch.poiSearchNearBy({
				// 			point: locations,
				// 			key: '小区'
				// 		}, res => {
				// 			console.log(res, '我是res,我是res,我是res,我是res,我是res,我是res,我是res,我是res,我是res,')
				// 			const {
				// 				poiList
				// 			} = res;
				// 			if (poiList && poiList.length) {
				// 				this.poiList = poiList;
				// 			}
				// 			this.loading = false;
				// 			// uni.hideLoading()
				// 		})
				// 	});
				// 	return
				// }
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
						console.log(this.poiList, 'aaaaaaaaa;;;;;sss;')
						// if (poiList && poiList.length) {
						// 	this.poiList = poiList;
						// }
					})
				}, 500)
			},
			savepPoints(data) {
				console.log(data, 'adddddddddddddddddddddddddddd')
				this.points = data
			},
			handleInit() {
				// 判断是否是添加新的守护区域
				if (this.urlLocation && Object.keys(this.urlLocation).length) {
					console.log(this.urlLocation, 'urlLocaiton')
					this.points = this.pointsFormatting(this.urlLocation)
					// const res = this.urlLocation.points.split(';').map(n => {
					// 	return n.split(',')
					// })
					console.log('我是初始化呀', res)
					this.mapInfo = {
						longitude: res[0][0],
						latitude: res[0][1],
						points: this.points
					}
					return
				}
				this.handleGetLocation()
			}, 
			pointsFormatting(data) {
				console.log('我是data', data)
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
							const {
								latitude,
								longitude,
							} = res
							this.mapInfo = {
								latitude,
								longitude,
								points: this.points ? this.points : ''
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
				if (!this.guardName) return uni.$u.toast('请输入名称')
				console.log(this.points, '--------')
				const res = this.points.map(n => {
					const {
						longitude,
						latitude
					} = n
					return `${longitude},${latitude}`
				}).join(';')
				PostAddFence({
					fenceType: 'polygon',
					deviceNo: this.deviceInfo.no,
					deviceId: this.deviceInfo.deviceId,
					points: res,
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						uni.navigateBack()
					}, 500);
				})
			},
			handleSelect(item, type) {
				console.log(item, 'item嘿嘿俄黑')
				console.log(type, 'type嘿嘿')
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
				this.guardNameShow = true
				// this.locationShow = false
			},
			guardClose() {
				// this.locationShow = true
				this.guardNameShow = false
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
					console.log(res)
					const {
						longitude,
						latitude
					} = res.data.location
					this.mapInfo = {
						latitude,
						longitude,
						points: this.points ? this.points : ''
					}
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